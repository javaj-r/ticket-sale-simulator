package com.javid.control.controllers;

import com.javid.config.Configurable;
import com.javid.control.UserController;
import com.javid.domain.NamedEntity;
import com.javid.domain.Privilege;
import com.javid.domain.User;
import com.javid.repository.PrivilegeRepository;
import com.javid.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author javid
 * Created on 1/1/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserControllerImpl implements UserController, Configurable {

    UserRepository userRepository = CONFIG.getUserRepository();
    PrivilegeRepository privilegeRepository = CONFIG.getPrivilegeRepository();

    private static class Singleton {
        private static final UserController INSTANCE = new UserControllerImpl();
    }

    public static UserController getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public User getAdminUser(User user) {
        User fetched = userRepository.find(user);
        if (fetched.isNew())
            return fetched;

        boolean hasPrivilege = privilegeRepository.findAllByUserId(fetched.getId())
                .stream()
                .map(NamedEntity::getName)
                .anyMatch(s -> s.equalsIgnoreCase(Privilege.Values.ADMIN_LOGIN.name()));

        return hasPrivilege && fetched.isEnabled() ? fetched : user;
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        return userRepository.isUsernameAvailable(username);
    }

    @Override
    public int enableUser(User user) {
        user.setEnabled(true);
        return userRepository.update(user);
    }


}
