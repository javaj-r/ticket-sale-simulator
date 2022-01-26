package com.javid.control;

import com.javid.domain.Customer;

/**
 * @author javid
 * Created on 1/5/2022
 */
public interface CustomerController {

    Customer register(Customer customer);

    Customer getCustomer(Customer customer);
}
