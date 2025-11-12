package com.damian.marketgrid.user.service;

import com.damian.marketgrid.exception.OAuth2UserVerificationException;
import com.damian.marketgrid.user.data.entity.UserIdentity;
import com.damian.marketgrid.user.data.entity.Role;
import com.damian.marketgrid.user.data.entity.User;
import com.damian.marketgrid.user.data.repository.RoleRepository;
import com.damian.marketgrid.user.data.repository.UserRepository;
import com.damian.marketgrid.user.data.entity.RoleName;
import com.damian.marketgrid.user.dto.UserAuthDetails;
import com.damian.marketgrid.user.dto.UserSession;
import com.damian.marketgrid.user.mapper.UserAuthMapper;
import com.damian.marketgrid.user.messaging.event.UserChangedEvent;
import com.damian.marketgrid.user.messaging.producer.UserChangedProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserAuthMapper userAuthMapper;
    private final UserChangedProducer userChangedProducer;

    @Override
    @Transactional
    public void createIfNotExists(UserSession userSession) {
        final UserIdentity identity = new UserIdentity(userSession.provider(), userSession.externalId());
        if (!userRepository.existsByIdentity(identity)) {
            Role role = roleRepository.findByName(RoleName.USER).orElseGet(() -> new Role(RoleName.USER));
            User user = userAuthMapper.toEntity(userSession, role, identity);
            User newUser = userRepository.save(user);

            UserChangedEvent event = userAuthMapper.toUserChangedEvent(newUser);
            userChangedProducer.produce(event);
        }
    }

    @Override
    public UserAuthDetails getUserAuthDetails(String provider, String externalId) {
        final UserIdentity identity = new UserIdentity(provider, externalId);
        var userAuth = userRepository.findByIdentity(identity).orElseThrow(OAuth2UserVerificationException::new);
        return userAuthMapper.toUserAuthDetails(userAuth);
    }
}
