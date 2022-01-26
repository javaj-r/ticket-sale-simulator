package com.javid.view.console;

import com.javid.config.Configurable;
import com.javid.domain.Customer;
import com.javid.domain.Ticket;
import com.javid.router.Router;
import com.javid.util.Screen;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author javid
 * Created on 1/4/2022
 */
public class CustomerConsole implements Configurable {

    private Router router = CONFIG.getRouter();
    Customer customer;

    public void login() {
        customer = new Customer();
        boolean loopFlag = true;

        while (loopFlag) {
            String username = Screen.getString("Enter username: ");
            String password = Screen.getPassword("Enter password: ");

            customer.setUsername(username);
            customer.setPassword(password);
            customer = router.loginAsCustomer(customer);

            if (customer == null || customer.isNew()) {
                int choice = Screen.showMenu("username or password is incorrect.", "",
                        "Please select suitable choice from menu: ",
                        "Invalid choice.", "Exit to main.", "Try again.");

                loopFlag = choice != 0;

            } else {
                System.out.println("Login success.");
                customerMenu();
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
            String firstname = Screen.getString("Firstname: ");
            String lastname = Screen.getString("Lastname: ");
            long nationalCode = Screen.getLong("National code: ", "Invalid number");
            long mobileNumber = Screen.getLong("Mobile number: ", "Invalid number");


            customer = new Customer();
            customer.setUsername(username)
                    .setPassword(password)
                    .setEmail(email);
            customer.setFirstname(firstname)
                    .setLastname(lastname)
                    .setNationalCode(nationalCode)
                    .setMobileNumber(mobileNumber);

            customer = router.saveCustomer(customer);

            if (customer == null) {
                int choice = Screen.showMenu("An error occurred!.", "",
                        "Please select suitable choice from menu: ",
                        "Invalid choice.", "Exit to main.", "Try again.");

                loopFlag = choice != 0;
            } else {
                System.out.println("You registered successfully.");
                customerMenu();
                loopFlag = false;
            }
        }
    }

    private void customerMenu() {
        boolean loopFlag = true;

        while (loopFlag) {
            int choice = Screen.showMenu("", "",
                    "Please select suitable choice from menu: ",
                    "Invalid choice.", "Logout to main menu."
                    , "Show all tickets."
                    , "Filter tickets.");
            loopFlag = choice != 0;

            switch (choice) {
                case 1 -> showAllTickets();
                case 2 -> filterTickets();
            }
        }
    }

    private void showAllTickets() {
        reserveTicket(router.getAllTickets());
    }

    private void filterTickets() {
        String name = Screen.getString("Enter name if you want to filter by name else just press ENTER.");
        String date = Screen.getStingDate("Enter date if you want to filter by show date else just press ENTER."
                , "Invalid date.");

        List<Ticket> tickets = router.getAllTickets();
        Stream<Ticket> stream = tickets.stream();

        if (!name.trim().isEmpty()) {
            stream = stream.filter(ticket -> ticket.getMovieName().equalsIgnoreCase(name));
        }

        if (date != null) {
            stream = stream.filter(ticket -> ticket.getDate().equals(date.trim()));
        }

        reserveTicket(stream.toList());
    }

    private void reserveTicket(List<Ticket> tickets) {
        while (true) {
            String[] menuItems = tickets.stream()
                    .map(Ticket::toString)
                    .toList()
                    .toArray(new String[0]);

            int choice = Screen.showMenu("Select ticket number to reserve.", "",
                    "Please select suitable choice from menu: ",
                    "Invalid choice.", "Back", menuItems);

            if (choice == 0)
                break;

            if (choice > 0) {
                int index = choice - 1;
                Ticket ticket = tickets.get(index);

                if (ticket.getNumber().equals(ticket.getSold())) {
                    System.out.println("Ticket is all sold.");
                    continue;
                } else {
                    boolean reserved = router.reserveTicket(customer, ticket, 1);

                    if (reserved) {
                        ticket.setSold(router.findTicket(ticket).getSold());
                        System.out.println("Ticket added to your account");
                    }
                }
            }
        }
    }
}
