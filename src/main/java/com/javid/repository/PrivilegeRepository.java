package com.javid.repository;

import com.javid.domain.Privilege;

import java.util.Set;

/**
 * @author javid
 * Created on 1/3/2022
 */
public interface PrivilegeRepository extends CrudRepository<Privilege, Integer>{

    Set<Privilege> findAllByUserId(Integer id);

}
