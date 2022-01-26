package com.javid.control;

import com.javid.domain.User;

/**
 * @author javid
 * Created on 1/4/2022
 */
public interface UserController {

    User getAdminUser(User user);

    boolean isUsernameAvailable(String username);

    int enableUser(User user);
}
