package com.javid.repository;

import com.javid.domain.Cinema;

/**
 * @author javid
 * Created on 1/1/2022
 */
public interface CinemaRepository extends CrudRepository<Cinema, Integer> {

    Cinema find(Cinema entity);
}
