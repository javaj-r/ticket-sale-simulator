package com.javid.repository.jdbc;

import com.javid.config.Configurable;
import com.javid.domain.Cinema;
import com.javid.repository.CinemaRepository;
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
public class CinemaRepositoryImpl implements CinemaRepository, Configurable {

    private static class Singleton {
        private static final CinemaRepository INSTANCE = new CinemaRepositoryImpl();
    }

    public static CinemaRepository getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Cinema find(Cinema entity) {
        Connection connection = CONFIG.getConnection();
        Cinema cinema = new Cinema();
        cinema.setUsername(entity.getUsername())
                .setPassword(entity.getPassword());
        String query = """
                SELECT u.id, u.username, u.password, u.email, u.enabled, c.name, c.balance 
                FROM USERS u
                    JOIN CINEMA c ON u.id = c.user_id
                WHERE username = ?
                AND password = ?
                ;
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getUsername());
            statement.setString(2, entity.getPassword());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                cinema.setId(result.getInt(1));
                cinema.setEmail(result.getString(4))
                        .setEnabled(result.getBoolean(5));
                cinema.setName(result.getString(6))
                        .setBalance(result.getLong(7));
            }

            return cinema;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinema;
    }

    @Override
    public Set<Cinema> findAll() {
        Connection connection = CONFIG.getConnection();
        Set<Cinema> cinemas = new HashSet<>();
        String query = """
                SELECT u.id, u.username, u.password, u.email, u.enabled, c.name, c.balance 
                FROM USERS u
                JOIN CINEMA c ON u.id = c.user_id
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Cinema cinema = new Cinema();
                cinema.setId(result.getInt(1));
                cinema.setUsername(result.getString(2))
                        .setPassword(result.getString(3))
                        .setEmail(result.getString(4))
                        .setEnabled(result.getBoolean(5));
                cinema.setName(result.getString(6))
                        .setBalance(result.getLong(7));

                cinemas.add(cinema);
            }
            return cinemas;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cinemas;
    }

    @Override
    public Cinema findById(Integer id) {
        return null;
    }

    @Override
    public Cinema save(Cinema entity) {
        if (entity == null || entity.isNew())
            return null;

        Connection connection = CONFIG.getConnection();

        String query = """
                INSERT INTO CINEMA(name, balance, user_id)
                VALUES(?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setLong(2, entity.getBalance());
            statement.setInt(3, entity.getId());

            statement.execute();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                return entity;
            }

            return null;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    public void deleteById(Integer id) {
    }
}
