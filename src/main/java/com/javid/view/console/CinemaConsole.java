package com.javid.view.console;

import com.javid.config.Configurable;
import com.javid.domain.Cinema;
import com.javid.domain.Ticket;
import com.javid.router.Router;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/4/2022
 */
public class CinemaConsole implements Configurable {

    private Router router = CONFIG.getRouter();
    private Cinema cinema;

    public void login() {
        cinema = new Cinema();
        boolean loopFlag = true;

        while (loopFlag) {
            String username = Screen.getString("Enter username: ");
            String password = Screen.getPassword("Enter password: ");

            cinema.setUsername(username);
            cinema.setPassword(password);
            cinema = router.loginAsCinema(cinema);

            if (cinema == null || cinema.isNew()) {
                int choice = Screen.showMenu("username or password is incorrect.", "",
                        "Please select suitable choice from menu: ",
                        "Invalid choice.", "Exit to main.", "Try again.");

                loopFlag = choice != 0;

            } else {
                System.out.println("Login success.");
                cinemaMenu();
                loopFlag = false;
            }
        }
    }

    public void signUp() {

        boolean loopFlag = true;

        while (loopFlag) {
            String username = "";
            while (true) {
                username = Screen.getString("Username: ");
                if (router.isUsernameAvailable(username))
                    break;
                System.out.println(username + " already exists! Try another.");
            }

            if (username.isEmpty()) {
                System.out.println("Failed to save username!");
                continue;
            }

            String password = Screen.getPassword("Password: ");
            String email = Screen.getString("Email: ");
            String name = Screen.getString("Cinema name: ");

            cinema = new Cinema();
            cinema.setUsername(username);
            cinema.setPassword(password);
            cinema.setEmail(email);
            cinema.setName(name);

            cinema = router.saveCinema(cinema);
            if (cinema == null) {
                int choice = Screen.showMenu("An error occurred!.", "",
                        "Please select suitable choice from menu: ",
                        "Invalid choice.", "Exit to main.", "Try again.");

                loopFlag = choice != 0;
            } else {
                System.out.println("You registered successfully.");
                cinemaMenu();
                loopFlag = false;
            }
        }
    }

    private void cinemaMenu() {
        boolean loopFlag = true;

        while (loopFlag) {
            if (cinema.isEnabled()) {
                int choice = Screen.showMenu("", "",
                        "Please select suitable choice from menu: ",
                        "Invalid choice.", "Logout to main menu."
                        , "Add new ticket."
                        , "Show unexpired tickets.");
                loopFlag = choice != 0;

                switch (choice) {
                    case 1 -> addTicketMenu();
                    case 2 -> showUnexpiredTicketsMenu();
                }
            } else {
                System.out.println("Your account is not enabled yet.");
                System.out.println("Please wait for admin to enable your account.");
                Screen.getString("Press ENTER to get back to main menu");
                break;
            }
        }
    }

    private void addTicketMenu() {
        String movieName = Screen.getString("Movie name: ");
        Integer number = Screen.getInt("Available tickets count: ", "Invalid number");
        String date = Screen.getStingDate("Showing date: ", "Invalid date");
        String time = Screen.getStringTime("Showing time: ", "Invalid time");
        Long price = Screen.getLong("Ticket price: ", "Invalid price");

        Ticket ticket = new Ticket()
                .setCinema(cinema)
                .setMovieName(movieName)
                .setDate(date)
                .setTime(time)
                .setNumber(number)
                .setSold(0)
                .setPrice(price);

        Ticket savedTicket = router.addNewTicket(ticket);

        if (savedTicket.isNew())
            System.out.println("Failed to save ticket.");

        System.out.println("Ticket added successfully");
    }

    private void showUnexpiredTicketsMenu() {

        boolean loopFlag = true;

        while (loopFlag) {
            List<Ticket> tickets = router.getUnexpiredTickets(cinema);
            String[] menuItems = tickets.stream()
                    .map(Ticket::toString)
                    .toList()
                    .toArray(new String[0]);
            int choice = Screen.showMenu("Select ticket number to delete.", "",
                    "Please select suitable choice from menu: ",
                    "Invalid choice.", "Back", menuItems);
            loopFlag = choice != 0;

            if (choice > 0) {
                int index = choice - 1;
                router.deleteTicket(tickets.get(index));
            }

        }

    }
}
