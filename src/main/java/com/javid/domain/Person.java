package com.javid.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author javid
 * Created on 1/1/2022
 */

@Getter
@Setter
@Accessors(chain = true)
public class Person extends User {

    private String firstname;
    private String lastname;
    private Long nationalCode;
    private Long mobileNumber;
}
