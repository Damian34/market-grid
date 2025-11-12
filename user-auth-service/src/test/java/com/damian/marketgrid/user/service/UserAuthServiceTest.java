package com.damian.marketgrid.user.service;

import com.damian.marketgrid.exception.OAuth2UserVerificationException;
import com.damian.marketgrid.user.data.entity.UserIdentity;
import com.damian.marketgrid.user.data.repository.RoleRepository;
import com.damian.marketgrid.user.data.repository.UserRepository;
import com.damian.marketgrid.user.dto.UserAuthDetails;
import com.damian.marketgrid.user.dto.UserSession;
import com.damian.marketgrid.user.messaging.producer.UserChangedProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.never;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAuthServiceTest {

    @Autowired
    private UserAuthService userAuthService;

    @MockitoBean
    private UserChangedProducer userChangedProducer;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCreateUserWhenNotExists() {
        // given
        UserSession userSession = createUserSession();
        UserIdentity identity = new UserIdentity(userSession.provider(), userSession.externalId());
        
        // when
        userAuthService.createIfNotExists(userSession);
        
        // then
        assertThat(userRepository.existsByIdentity(identity));
        verify(userChangedProducer).produce(any());
    }

    @Test
    void shouldNotCreateUserWhenAlreadyExists() {
        // given
        UserSession userSession = createUserSession();
        userAuthService.createIfNotExists(userSession);
        reset(userChangedProducer);
        
        // when
        userAuthService.createIfNotExists(userSession);
        
        // then
        assertThat(userRepository.count() == 1);
        verify(userChangedProducer, never()).produce(any());
    }

    @Test
    void shouldReturnUserAuthDetails() {
        // given
        UserSession userSession = createUserSession();
        userAuthService.createIfNotExists(userSession);
        
        // when
        UserAuthDetails result = userAuthService.getUserAuthDetails(userSession.provider(), userSession.externalId());
        
        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenUserNotExists() {
        // given
        UserSession userSession = createUserSession();

        // when & then
        assertThatThrownBy(() -> userAuthService.getUserAuthDetails(userSession.provider(), userSession.externalId()))
                .isInstanceOf(OAuth2UserVerificationException.class);
    }

    private UserSession createUserSession() {
        return new UserSession(
                "google",
                "external-123",
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com"
        );
    }
}

