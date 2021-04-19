package OnlineShoppingSystem.UI;

import OnlineShoppingSystem.Data.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Supplier {
    public static void loadSupplierIntro() {
        Main.supplier = null;
        Label supplier_welcome_label = new Label("Welcome Supplier, would you like to log in or register?");
        Button sup_login = new Button("log in");
        sup_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierLogin();
            }
        });
        Button sup_reg = new Button("Register");
        sup_reg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierRegister();
            }
        });
        HBox sup_wel_buttons = new HBox(sup_login, sup_reg);
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                Main.loadWelcomeMenu();
            }
        });
        Main.scene.setRoot(new VBox(supplier_welcome_label,sup_wel_buttons,back_button));
    }
    public static void loadSupplierLogin() {
        Main.supplier = null;
        Label login_label = new Label("Please enter Supplier Log in information: ");
        Label error_label = new Label(Main.resubmit ? "That username is already claimed." : "");
        error_label.setTextFill(Color.color(.85f, 0, 0));
        Label user_textbox_label = new Label("Username: ");
        TextField user_textbox = new TextField();
        Label password_textbox_label = new Label("Password: ");
        TextField password_textbox = new TextField();
        HBox user_login_interface = new HBox(user_textbox_label, user_textbox, password_textbox_label, password_textbox);
        Button supplier_login_button = new Button ("Log in");
        supplier_login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Main.supplier = SupplierAccount.Login(user_textbox.getText(), password_textbox.getText());
                    if (!(Main.resubmit = Main.supplier == null)) {
                        loadSupplierMenu();
                    } else {
                        loadSupplierLogin();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.resubmit = true;
                    loadSupplierLogin();
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierIntro();
            }
        });
        Main.scene.setRoot(new VBox(login_label, error_label, user_login_interface, supplier_login_button, back_button));
    }
    public static void loadSupplierRegister() {
        Main.supplier = null;
        Label supplier_register_label = new Label("Please enter your intended Username, Password, And Supplier Name: ");
        Label error_label = new Label(Main.resubmit ? "That username is already claimed." : "");
        error_label.setTextFill(Color.color(.85f, 0, 0));
        Label sup_reg_textbox_label = new Label("Username: ");
        TextField sup_reg_textbox = new TextField();
        Label sup_pass_textbox_label = new Label("Password: ");
        TextField sup_pass_textbox = new TextField();
        Label supplier_ven_textbox_label = new Label("Name: ");
        TextField supplier_ven_textbox = new TextField();
        HBox sup_reg_interface = new HBox(
            sup_reg_textbox_label,
            sup_reg_textbox,
            sup_pass_textbox_label,
            sup_pass_textbox,
            supplier_ven_textbox_label,
            supplier_ven_textbox);
        Button sup_reg_button = new Button ("Register");
        sup_reg_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.supplier = new SupplierAccount(
                    sup_reg_textbox.getText(),
                    sup_pass_textbox.getText(),
                    supplier_ven_textbox.getText());
                try {
                    if (!(Main.resubmit = !Main.supplier.create())) {
                        loadSupplierMenu();
                    } else {
                        loadSupplierRegister();
                    }
                } catch (Exception e){
                    System.out.println("Failed to create supplier account: " + e.toString());
                    Main.resubmit = true;
                    loadSupplierRegister();
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierIntro();
            }
        });
        Main.scene.setRoot(new VBox(supplier_register_label, error_label, sup_reg_interface, sup_reg_button, back_button));
    }
    public static void loadSupplierMenu() {
        Label welcome = new Label("Welcome " + Main.supplier.getName());
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierLogin();
            }
        });
        Main.scene.setRoot(new VBox(welcome, logout));
    }
}
