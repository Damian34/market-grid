package com.damian.marketgrid.user.data.projection;

import com.damian.marketgrid.user.data.entity.Role;

import java.util.Set;

public interface UserAuthDetailsProjection {
    Long getId();
    Set<Role> getRoles();
}
