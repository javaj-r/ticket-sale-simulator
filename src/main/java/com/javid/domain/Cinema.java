package com.javid.domain;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * @author javid
 * Created on 1/1/2022
 */

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Cinema extends User {

    private String name;
    private Long balance;
    private Set<Ticket> tickets = new HashSet<>();
}
