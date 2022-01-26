package com.javid.repository.jdbc;

import com.javid.config.Configurable;
import com.javid.domain.Cinema;
import com.javid.domain.Customer;
import com.javid.domain.Ticket;
import com.javid.repository.TicketRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author javid
 * Created on 1/1/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TicketRepositoryImpl implements TicketRepository, Configurable {

    private static class Singleton {
        private static final TicketRepository INSTANCE = new TicketRepositoryImpl();
    }

    public static TicketRepository getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Ticket> findUnexpired(Cinema cinema) {
        Connection connection = CONFIG.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        String query = """
                SELECT id, movie_name, show_date, show_time, number, sold, price, user_id
                FROM ticket
                WHERE user_id = ?
                  AND (
                            show_date > CURRENT_DATE
                        OR (show_date = CURRENT_DATE AND show_time > CURRENT_TIME)
                    );
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cinema.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tickets.add(parseTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public List<Ticket> findAllUnexpired() {
        Connection connection = CONFIG.getConnection();
        List<Ticket> tickets = new ArrayList<>();
        String query = """
                SELECT id, movie_name, show_date, show_time, number, sold, price, user_id
                FROM ticket
                WHERE show_date > CURRENT_DATE OR
                (show_date = CURRENT_DATE AND show_time > CURRENT_TIME);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tickets.add(parseTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public boolean updateTicket(Customer customer, Ticket ticket, int number) {
        Connection connection = CONFIG.getConnection();

        String query = """
                Update ticket SET  sold=sold + ?
                WHERE id = ?;
                                
                INSERT INTO USERS_TICKET (user_id, ticket_id)
                values (?,?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, number);
            statement.setInt(2, ticket.getId());
            statement.setInt(3, customer.getId());
            statement.setInt(4, ticket.getId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    @Override
    public Set<Ticket> findAll() {
        Connection connection = CONFIG.getConnection();
        Set<Ticket> tickets = new HashSet<>();
        String query = """
                SELECT id, movie_name, show_date, show_time, number, sold, price, user_id
                FROM ticket;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tickets.add(parseTicket(resultSet));
            }
            return tickets;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public Ticket findById(Integer id) {
        Connection connection = CONFIG.getConnection();
        String query = """
                SELECT id, movie_name, show_date, show_time, number, sold, price, user_id
                FROM ticket
                WHERE id = ?;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return parseTicket(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Ticket save(Ticket entity) {
        if (entity == null || entity.getCinema() == null || entity.getCinema().isNew())
            return entity;
        Connection connection = CONFIG.getConnection();
        Ticket ticket = new Ticket()
                .setCinema(entity.getCinema())
                .setMovieName(entity.getMovieName())
                .setDate(entity.getDate())
                .setTime(entity.getTime())
                .setNumber(entity.getNumber())
                .setSold(entity.getSold())
                .setPrice(entity.getPrice());
        String query = """
                INSERT INTO ticket(movie_name, show_date, show_time, number, sold, price, user_id)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getMovieName());
            statement.setDate(2, Date.valueOf(entity.getDate()));
            statement.setTime(3, Time.valueOf(entity.getTime()));
            statement.setInt(4, entity.getNumber());
            statement.setLong(5, entity.getSold());
            statement.setLong(6, entity.getPrice());
            statement.setInt(7, entity.getCinema().getId());
            statement.execute();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                ticket.setId(result.getInt("id"));
            }
            return ticket;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ticket;
    }

    @Override
    public void deleteById(Integer id) {
        Connection connection = CONFIG.getConnection();
        String query = """
                DELETE FROM USERS_TICKET
                WHERE ticket_id = ?
                ;
                DELETE FROM TICKT
                WHERE id = ?
                ;
                """;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Ticket parseTicket(ResultSet resultSet) throws SQLException {
        return new Ticket()
                .setId(resultSet.getInt("id"))
                .setMovieName(resultSet.getString("movie_name"))
                .setDate(resultSet.getDate("show_date").toString())
                .setTime(resultSet.getTime("show_time").toString())
                .setNumber(resultSet.getInt("number"))
                .setSold(resultSet.getInt("sold"))
                .setPrice(resultSet.getLong("price"));
    }
}
