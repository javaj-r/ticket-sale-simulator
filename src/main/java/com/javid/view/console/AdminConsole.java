package com.javid.view.console;

import com.javid.config.Configurable;
import com.javid.domain.Cinema;
import com.javid.domain.User;
import com.javid.router.Router;
import com.javid.util.Screen;

import java.util.List;

/**
 * @author javid
 * Created on 1/3/2022
 */
public class AdminConsole implements Configurable {

    private Router router = CONFIG.getRouter();
    private User admin;

    public void login() {
        admin = new User();
        boolean loopFlag = true;

        while (loopFlag) {
            String username = Screen.getString("Enter username: ");
            String password = Screen.getPassword("Enter password: ");

            admin.setUsername(username);
            admin.setPassword(password);
            admin = router.loginAsAdmin(admin);

            if (admin == null || admin.isNew()) {
                int choice = Screen.showMenu("username or password is incorrect.", "",
                        "Please select suitable choice from menu: ",
                        "Invalid choice.", "Exit to main.", "Try again.");

                loopFlag = choice != 0;

            } else {
                System.out.println("Login success.");
                adminMenu();
                loopFlag = false;
            }
        }
    }

    private void adminMenu() {
        boolean loopFlag = true;

        while (loopFlag) {
            int choice = Screen.showMenu("", "",
                    "Please select suitable choice from menu: ",
                    "Invalid choice.", "Logout to main menu.", "Show new registered cinemas.");
            loopFlag = choice != 0;

            if (choice == 1) {
                showCinemaList();
                loopFlag = false;
            }
        }
    }

    private void showCinemaList() {
        boolean loopFlag = true;

        while (loopFlag) {
            List<Cinema> cinemas = router.getUnconfirmedCinemas();
            String[] menuItems = cinemas.stream()
                    .map(cinema -> "Name:%s  Email:%s".formatted(cinema.getName(), cinema.getEmail()))
                    .toList()
                    .toArray(new String[0]);

            int choice = Screen.showMenu("Select cinema number to confirm cinema registration.", "",
                    "Please select suitable choice from menu: ",
                    "Invalid choice.", "Logout to main menu.", menuItems);
            loopFlag = choice != 0;

            if (choice > 0) {
                int index = choice - 1;
                int result = router.confirmCinema(cinemas.get(index));
                if (result == -1)
                    System.out.println("An error occurred!");
            }
        }
    }
}