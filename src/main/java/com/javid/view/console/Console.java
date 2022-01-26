package com.javid.view.console;

import com.javid.util.Screen;

/**
 * @author javid
 * Created on 1/3/2022
 */
public class Console {

    public void mainMenu() {
        while (true) {
            try {
                int choice = Screen.showMenu("", "", "Please select suitable choice from menu: "
                        , "Invalid choice.", "Exit"
                        , "Login as administrator."
                        , "Login as customer.", "Sign Up customer."
                        , "Login as cinema.", "Sign up cinema.");

                if (choice == 0) {
                    break;
                }

                switch (choice) {
                    case 1 -> new AdminConsole().login();
                    case 2 -> new CustomerConsole().login();
                    case 3 -> new CustomerConsole().signUp();
                    case 4 -> new CinemaConsole().login();
                    case 5 -> new CinemaConsole().signUp();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
