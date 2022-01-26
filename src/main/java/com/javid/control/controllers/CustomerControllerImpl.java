package com.javid.control.controllers;

import com.javid.config.Configurable;
import com.javid.control.CustomerController;
import com.javid.domain.Customer;
import com.javid.domain.Privilege;
import com.javid.domain.User;
import com.javid.repository.CustomerRepository;
import com.javid.repository.PrivilegeRepository;
import com.javid.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author javid
 * Created on 1/5/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerControllerImpl implements CustomerController, Configurable {

    UserRepository userRepository = CONFIG.getUserRepository();
    PrivilegeRepository privilegeRepository = CONFIG.getPrivilegeRepository();
    CustomerRepository customerRepository = CONFIG.getCustomerRepository();

    private static class Singleton {
        private static final CustomerController INSTANCE = new CustomerControllerImpl();
    }

    public static CustomerController getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Customer register(Customer customer) {
        customer.setEnabled(true);
        setPrivileges(customer);
        User user = userRepository.save(customer);

        if (user.isNew())
            return customer;

        customer.setId(user.getId());

        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomer(Customer customer) {
        Customer fetched = customerRepository.find(customer);
        if (fetched.isNew())
            return fetched;

        List<Privilege> list = privilegeRepository.findAllByUserId(fetched.getId())
                .stream().toList();

        fetched.setPrivileges(list);

        return fetched;
    }

    private void setPrivileges(Customer customer) {
        Privilege logIn = new Privilege();
        logIn.setName(Privilege.Values.CUSTOMER_LOGIN);
        Privilege reserveTicket = new Privilege();
        reserveTicket.setName(Privilege.Values.RESERVE_TICKET);

        customer.getPrivileges().add(logIn);
        customer.getPrivileges().add(reserveTicket);
    }
}
