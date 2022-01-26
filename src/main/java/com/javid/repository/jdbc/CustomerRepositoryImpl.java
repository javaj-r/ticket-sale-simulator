package com.javid.repository.jdbc;

import com.javid.domain.Customer;
import com.javid.repository.CustomerRepository;
import com.javid.config.Configurable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author javid
 * Created on 1/1/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerRepositoryImpl implements CustomerRepository, Configurable {

    @Override
    public Customer find(Customer entity) {
        Connection connection = CONFIG.getConnection();
        Customer customer = new Customer();
        String query = """
                SELECT u.id, u.username, u.password, u.email, 
                    p.firstname, p.lastname, p.national_code, p.mobile_number  
                FROM USERS u
                    JOIN CUSTOMER p ON u.id = p.user_id
                WHERE username = ?
                AND password = ?
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                customer.setId(result.getInt(1));
                customer.setUsername(result.getString(2))
                        .setPassword(result.getString(3))
                        .setEmail(result.getString(4));
                customer.setFirstname(result.getString(5))
                        .setFirstname(result.getString(6))
                        .setNationalCode(result.getLong(7))
                        .setMobileNumber(result.getLong(8));
            }
            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    private static class Singleton {
        private static final CustomerRepository INSTANCE = new CustomerRepositoryImpl();
    }

    public static CustomerRepository getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Set<Customer> findAll() {
        Connection connection = CONFIG.getConnection();
        Set<Customer> customerSet = new HashSet<>();
        String query = """
                SELECT u.id, u.username, u.password, u.email, 
                    p.firstname, p.lastname, p.national_code, p.mobile_number  
                FROM USERS u
                JOIN CUSTOMER p ON u.id = p.user_id
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Customer customer = new Customer();
                customer.setId(result.getInt(1));
                customer.setUsername(result.getString(2))
                        .setPassword(result.getString(3))
                        .setEmail(result.getString(4));
                customer.setFirstname(result.getString(5))
                        .setFirstname(result.getString(6))
                        .setNationalCode(result.getLong(7))
                        .setMobileNumber(result.getLong(8));

                customerSet.add(customer);
            }
            return customerSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerSet;
    }

    @Override
    public Customer findById(Integer id) {
        return null;
    }

    @Override
    public Customer save(Customer entity) {
        if (entity.isNew())
            return entity;

        Connection connection = CONFIG.getConnection();
        String query = """
                INSERT INTO  CUSTOMER(firstname, lastname, national_code, mobile_number, user_id)
                VALUES (?, ?, ?, ?, ?)
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getFirstname());
            statement.setString(2, entity.getLastname());
            statement.setLong(3, entity.getNationalCode());
            statement.setLong(4, entity.getMobileNumber());
            statement.setInt(5, entity.getId());
            statement.execute();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                return entity;
            }

            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;

    }

    @Override
    public void deleteById(Integer id) {
        Connection connection = CONFIG.getConnection();
        String query = """
                DELETE FROM CUSTOMER
                WHERE user_id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
