package com.javid.repository.jdbc;

import com.javid.config.Configurable;
import com.javid.domain.Privilege;
import com.javid.domain.User;
import com.javid.repository.UserRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author javid
 * Created on 1/1/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRepositoryImpl implements UserRepository, Configurable {

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "email";
    private static final String ENABLED = "enabled";

    private static class Singleton {
        private static final UserRepository INSTANCE = new UserRepositoryImpl();
    }

    public static UserRepository getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public User find(User entity) {
        Connection connection = CONFIG.getConnection();

        String query = """
                SELECT * FROM USERS
                WHERE username = ?
                AND password = ?
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }

    @Override
    public Set<User> findAll() {
        Connection connection = CONFIG.getConnection();
        Set<User> userSet = new HashSet<>();
        String query = "SELECT * FROM USERS;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userSet.add(parseUser(resultSet));
            }
            return userSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userSet;
    }

    @Override
    public User findById(Integer id) {
        Connection connection = CONFIG.getConnection();
        String query = """
                SELECT * FROM USERS
                WHERE id = ?
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseUser(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public synchronized User save(User entity) {
        Connection connection = CONFIG.getConnection();
        User user = new User();
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        user.setEmail(entity.getEmail());
        user.setEnabled(entity.isEnabled());
        user.setPrivileges(entity.getPrivileges());
        boolean setEmail = entity.getEmail() != null;
        String query = getInsertQuery(setEmail);

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            statement.setBoolean(3, entity.isEnabled());
            if (setEmail) {
                statement.setString(4, entity.getEmail());
            }
            statement.setString(setEmail ? 5 : 4, entity.getUsername());

            statement.execute();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                grantPrivileges(user);
                user.setId(result.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    private String getInsertQuery(boolean setEmail) {
        String query = """
                INSERT INTO USERS(username, password, enabled %s)
                SELECT ?, ?, ? %s
                WHERE NOT EXISTS(SELECT * FROM USERS WHERE username = ?); 
                """;
        query = setEmail ? String.format(query, ", email", ", ?")
                : String.format(query, "", "");
        return query;
    }

    @Override
    public void deleteById(Integer id) {
        Connection connection = CONFIG.getConnection();
        String query = """
                DELETE FROM USERS
                WHERE id = ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void grantPrivileges(User user) {
        if (user == null || user.isNew() ||
            user.getPrivileges() == null || user.getPrivileges().isEmpty())
            return;
        Connection connection = CONFIG.getConnection();
        List<Privilege> list = user.getPrivileges();
        String query = """
                INSERT INTO USERS_PRIVILEGE (user_id, privilege_id)
                SELECT U.id, P.id
                FROM (SELECT * FROM USERS WHERE id = ?) U
                         CROSS JOIN (SELECT * FROM PRIVILEGE WHERE name IN ('non' %s)) P
                WHERE (U.id, P.id) NOT IN (SELECT user_id, privilege_id FROM USERS_PRIVILEGE WHERE user_id = ?)
                """;
        StringBuilder builder = new StringBuilder();
        list.stream().map(Privilege::getName)
                .forEach(s -> builder.append(", '").append(s).append("'"));
        query = String.format(query, builder);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            statement.setInt(2, user.getId());
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean isUsernameAvailable(String username) {
        Connection connection = CONFIG.getConnection();
        String query = """
                select (case count(*)
                        when  0 then true
                        else false
                        end) enabled
                       from users where username = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getBoolean(1);
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int update(User user) {
        if (user == null || user.isNew())
            return -1;

        Connection connection = CONFIG.getConnection();
        String query = """
                UPDATE USERS SET enabled = true
                where id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, user.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private User parseUser(ResultSet resultSet) throws SQLException {
        User user = new User()
                .setUsername(resultSet.getString(USERNAME))
                .setPassword(resultSet.getString(PASSWORD))
                .setEmail(resultSet.getString(EMAIL))
                .setEnabled(resultSet.getBoolean(ENABLED));
        user.setId(resultSet.getInt(ID));
        return user;
    }
}
