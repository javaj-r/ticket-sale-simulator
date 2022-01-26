package com.javid.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/2/2022
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Privilege extends NamedEntity {

    public static enum Values {
        ADMIN_LOGIN, CONFIRM_CINEMA, CINEMA_LOGIN, CREATE_TICKET, DELETE_TICKET, CUSTOMER_LOGIN, RESERVE_TICKET
    }

    private List<User> users = new ArrayList<>();

    public void setName(Privilege.Values name) {
        super.setName(name.name());
    }
}
