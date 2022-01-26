package com.javid.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author javid
 * Created on 1/3/2022
 */

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class Customer extends Person {

    private List<Ticket> tickets = new ArrayList<>();
}
