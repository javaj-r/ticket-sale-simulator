package com.javid.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author javid
 * Created on 1/1/2022
 */

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class BaseEntity {

    private Integer id;

    public boolean isNew() {
        return id == null;
    }
}
