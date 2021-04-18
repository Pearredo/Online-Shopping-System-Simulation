package OnlineShoppingSystem.UI;

import Core.Buffer;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import javax.xml.soap.Text;
import java.util.HashMap;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //first scene
        Label Title1 = new Label("Are you a Customer or a Supplier");
        Button supplier_button = new Button("Supplier");
        Button customer_button = new Button("Customer");
        HBox header = new HBox (Title1);
        HBox button_holder = new HBox(customer_button, supplier_button);
        VBox vbox1 = new VBox(header, button_holder);
        vbox1.setAlignment(Pos.BASELINE_CENTER);

        //scene
        Scene scene = new Scene(vbox1, 1280, 720);



        //Customer Scene
        Button customer_login = new Button("Log in");
        Button customer_register = new Button("Register");
        HBox customer_button_holder = new HBox(customer_login, customer_register);
        VBox ConsumerMenu = new VBox(new Label("Welcome Customer"),new Label("Would you like to Log in or register for a customer account?:"),customer_button_holder);
        customer_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(ConsumerMenu);
            }
        });
        //Customer Log in Screen
        Label login_label = new Label("Please enter your customer log in information:");
        Label user_textbox_label = new Label("Username:");
        TextField user_textbox = new TextField();
        Label password_label = new Label("Password:");
        TextField password_textbox = new TextField();
        HBox user_login_interface = new HBox(user_textbox_label, user_textbox, password_label, password_textbox);
        Button customer_login_button = new Button("Log in");
        VBox Customer_login_scene = new VBox(login_label, user_login_interface, customer_login_button);
        customer_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(Customer_login_scene);
            }
        });
        //Customer Register Account Screen
        Label customer_register_label = new Label("Please enter your intended Username and Password:");
        Label reg_user_textbox_label = new Label("Username:");
        TextField reg_user_textbox = new TextField();
        Label reg_pass_textbox_label = new Label("Password:");
        TextField reg_pass_textbox = new TextField();
        HBox user_reg_interface = new HBox(reg_user_textbox_label, reg_user_textbox, reg_pass_textbox_label, reg_pass_textbox);
        Button customer_reg_button = new Button("Register");
        VBox customer_register_scene = new VBox(customer_register_label, user_reg_interface,customer_reg_button);
        customer_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(customer_register_scene);
            }
        });

        //Supplier Scene
        Label supplier_welcome_label = new Label("Welcome Supplier, would you like to log in or register?");
        Button sup_login = new Button("log in");
        Button sup_reg = new Button("Register");
        HBox sup_wel_buttons = new HBox(sup_login, sup_reg);
        VBox SupplierMenu = new VBox(supplier_welcome_label,sup_wel_buttons);
        supplier_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(SupplierMenu);
            }
        });

        //Supplier Login scene
        Label supplier_login_label = new Label("Please enter your supplier log in information");
        Label sup_user_label = new Label("Username:");
        TextField sup_user = new TextField();
        Label sup_pass_label = new Label("Password:");
        TextField sup_pass = new TextField();
        HBox sup_login_interface = new HBox(sup_user_label, sup_user, sup_pass_label, sup_pass);
        Button sup_login_button = new Button("Login");
        VBox sup_login_scene = new VBox(supplier_login_label, sup_login_interface, sup_login_button);
        sup_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(sup_login_scene);
            }
        });
        //Supplier registration scene
        Label supplier_registration_label = new Label("Please enter your intended log in information:");
        Label sup_reg_user_label = new Label("Username:");
        TextField reg_sup_user = new TextField();
        Label sup_reg_pass_label = new Label("Password:");
        TextField sup_reg_pass = new TextField();
        HBox sup_reg_interface = new HBox(sup_reg_user_label, reg_sup_user, sup_reg_pass_label, sup_reg_pass);
        Button sup_reg_button = new Button("Register");
        VBox sup_reg_scene = new VBox(supplier_registration_label, sup_reg_interface, sup_reg_button);
        sup_reg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(sup_reg_scene);
            }
        });

        //runs the scene
        primaryStage.setTitle("Online Shopping System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(HashMap<String, String> config, Buffer buffer) {
        // Do stuff with config...
        OnlineShoppingSystem.Data.DataManager.initialize(config, buffer);
        launch();
    }
}
