package OnlineShoppingSystem.UI;

import OnlineShoppingSystem.Data.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Customer {
    public static void loadCustomerIntro() {
        Main.customer = null;
        Button customer_login = new Button("Log in");
        customer_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerLogin();
            }
        });
        Button customer_register = new Button("Register");
        customer_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerRegister();
            }
        });
        HBox customer_button_holder = new HBox(customer_login, customer_register);
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                Main.loadWelcomeMenu();
            }
        });
        Main.scene.setRoot(new VBox(
            new Label("Welcome Customer"),
            new Label("Would you like to Log in or register for a customer account?"),
            customer_button_holder,
            back_button));
    }
    public static void loadCustomerLogin() {
        Main.customer = null;
        Label login_label = new Label("Please enter your customer log in information:");
        Label error_label = new Label(Main.resubmit ? "An invalid username and password were provided." : "");
        error_label.setTextFill(Color.color(.85f, 0, 0));
        Label user_textbox_label = new Label("Username:");
        TextField user_textbox = new TextField();
        Label password_label = new Label("Password:");
        TextField password_textbox = new TextField();
        HBox user_login_interface = new HBox(user_textbox_label, user_textbox, password_label, password_textbox);
        Button customer_login_button = new Button("Log in");
        customer_login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Main.customer = CustomerAccount.Login(user_textbox.getText(), password_textbox.getText());
                    if (!(Main.resubmit = Main.customer == null)) {
                        loadCustomerMenu();
                    } else {
                        loadCustomerLogin();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.resubmit = true;
                    loadCustomerLogin();
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerIntro();
            }
        });
        Main.scene.setRoot(new VBox(login_label, error_label, user_login_interface, customer_login_button, back_button));
    }
    public static void loadCustomerRegister() {
        Main.customer = null;
        Label customer_register_label = new Label("Please enter your intended Username, Password, and general information:");
        Label error_label = new Label(Main.resubmit ? "That username is already claimed." : "");
        error_label.setTextFill(Color.color(.85f, 0, 0));
        Label reg_user_textbox_label = new Label("Username:\t");
        TextField reg_user_textbox = new TextField();
        Label reg_pass_textbox_label = new Label("Password:\t");
        TextField reg_pass_textbox = new TextField();
        HBox user_reg_interface = new HBox(reg_user_textbox_label, reg_user_textbox, reg_pass_textbox_label, reg_pass_textbox);
        Label reg_customer_name_label = new Label("Full Name:\t");
        TextField reg_name = new TextField();
        Label reg_customer_addr_label = new Label("Address:\t");
        TextField reg_customer_addr = new TextField();
        HBox user_reg_interface1 = new HBox(reg_customer_name_label,reg_name,reg_customer_addr_label,reg_customer_addr);
        Label reg_customer_CC_Label = new Label("Credit Card Number:\t");
        TextField reg_customer_cc = new TextField();
        HBox reg_interface2 = new HBox(reg_customer_CC_Label, reg_customer_cc);
        Button customer_reg_button = new Button("Register");
        customer_reg_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.customer = new CustomerAccount(
                    reg_user_textbox.getText(),
                    reg_pass_textbox.getText(),
                    reg_name.getText(),
                    reg_customer_addr.getText(),
                    reg_customer_cc.getText(),
                    false,
                    false);
                try {
                    if (!(Main.resubmit = !Main.customer.create())) {
                        loadCustomerMenu();
                    } else {
                        loadCustomerRegister();
                    }
                } catch (Exception e) {
                    System.out.println("Failed to create customer account: " + e.toString());
                    Main.resubmit = true;
                    loadCustomerRegister();
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerIntro();
            }
        });
        Main.scene.setRoot(new VBox(
            customer_register_label,
            error_label,
            user_reg_interface,
            user_reg_interface1,
            reg_interface2,
            customer_reg_button,
            back_button));
    }
    public static void loadCustomerMenu() {
        Label welcome = new Label("Welcome " + Main.customer.getName());
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerLogin();
            }
        });
        Main.scene.setRoot(new VBox(welcome, logout));
    }
}