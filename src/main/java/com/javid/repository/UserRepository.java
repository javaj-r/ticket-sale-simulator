package com.javid.repository;

import com.javid.domain.User;

/**
 * @author javid
 * Created on 1/1/2022
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    User find(User entity);

    void grantPrivileges(User user);

    boolean isUsernameAvailable(String username);

    int update(User user);
}
