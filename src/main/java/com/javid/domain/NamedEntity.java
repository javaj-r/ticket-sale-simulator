package com.javid.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author javid
 * Created on 1/2/2022
 */

@Getter
@Setter
@Accessors(chain = true)
public class NamedEntity extends BaseEntity {

    private String name;
}
