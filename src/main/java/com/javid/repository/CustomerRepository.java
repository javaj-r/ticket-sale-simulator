package com.javid.repository;

import com.javid.domain.Customer;

/**
 * @author javid
 * Created on 1/1/2022
 */
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    Customer find(Customer entity);
}
