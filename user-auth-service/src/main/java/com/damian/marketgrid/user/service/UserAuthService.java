package com.damian.marketgrid.user.service;

import com.damian.marketgrid.user.dto.UserAuthDetails;
import com.damian.marketgrid.user.dto.UserSession;

public interface UserAuthService {
    void createIfNotExists(UserSession userSession);

    UserAuthDetails getUserAuthDetails(String provider, String externalId);
}
