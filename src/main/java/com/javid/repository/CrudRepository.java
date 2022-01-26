package com.javid.repository;

import java.util.Set;

/**
 * @author javid
 * Created on 1/1/2022
 */
public interface CrudRepository<T, I> {

    Set<T> findAll();

    T findById(I id);

    T save(T entity);

    void deleteById(I id);
}
