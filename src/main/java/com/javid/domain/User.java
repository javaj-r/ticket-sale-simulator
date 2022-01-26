package com.javid.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/1/2022
 */

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class User extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private List<Privilege> privileges = new ArrayList<>();

    public User(String username, String password, String email, boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
    }
}
