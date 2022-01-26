package com.javid.repository.jdbc;

import com.javid.repository.PrivilegeRepository;
import com.javid.config.Configurable;
import com.javid.domain.Privilege;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author javid
 * Created on 1/3/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrivilegeRepositoryImpl implements PrivilegeRepository, Configurable {

    private static class Singleton {
        private static final PrivilegeRepository INSTANCE = new PrivilegeRepositoryImpl();
    }

    public static PrivilegeRepository getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Set<Privilege> findAll() {
        Connection connection = CONFIG.getConnection();
        String query = "SELECT * FROM PRIVILEGE";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            return getPrivileges(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }

    @Override
    public Privilege findById(Integer id) {
        return null;
    }

    @Override
    public Privilege save(Privilege entity) {
        Connection connection = CONFIG.getConnection();
        Privilege privilege = new Privilege();
        privilege.setName(entity.getName());
        String query = """
                INSERT INTO PRIVILEGE (name)
                SELECT ?
                WHERE NOT EXISTS(SELECT * FROM PRIVILEGE WHERE name = ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getName());
            statement.execute();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                privilege.setId(result.getInt(1));
            }
            return privilege;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return privilege;
    }

    @Override
    public void deleteById(Integer id) {
    }

    @Override
    public Set<Privilege> findAllByUserId(Integer id) {
        Connection connection = CONFIG.getConnection();
        String query = """
                SELECT P.id, P.name
                FROM USERS_PRIVILEGE UP
                         JOIN PRIVILEGE P on P.id = UP.privilege_id
                WHERE UP.user_id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            return getPrivileges(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }

    private Set<Privilege> getPrivileges(ResultSet result) throws SQLException {
        Set<Privilege> privilegeSet = new HashSet<>();
        while (result.next()) {
            Privilege privilege = new Privilege();
            privilege.setId(result.getInt("id"));
            privilege.setName(result.getString("name"));
            privilegeSet.add(privilege);
        }
        return privilegeSet;
    }
}
