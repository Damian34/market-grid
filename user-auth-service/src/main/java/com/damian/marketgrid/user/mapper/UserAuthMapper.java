package com.damian.marketgrid.user.mapper;

import com.damian.marketgrid.user.data.entity.Role;
import com.damian.marketgrid.user.data.entity.User;
import com.damian.marketgrid.user.data.entity.UserIdentity;
import com.damian.marketgrid.user.data.projection.UserAuthDetailsProjection;
import com.damian.marketgrid.user.dto.UserAuthDetails;
import com.damian.marketgrid.user.dto.UserSession;
import com.damian.marketgrid.user.messaging.event.UserChangedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserAuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identity", source = "identity")
    @Mapping(target = "roles", source = "role", qualifiedByName = "mapRoleToSet")
    @Mapping(target = "name", source = "userSession.name")
    User toEntity(UserSession userSession, Role role, UserIdentity identity);

    @Named("mapRoleToSet")
    default Set<Role> mapRoleToSet(Role role) {
        return Set.of(role);
    }

    UserAuthDetails toUserAuthDetails(UserAuthDetailsProjection projection);

    default String roleToString(Role role) {
        return role == null ? null : role.getName().name();
    }

    UserChangedEvent toUserChangedEvent(User user);
}
