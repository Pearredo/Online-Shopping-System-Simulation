package OnlineShoppingSystem.UI;

import OnlineShoppingSystem.Data.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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
    //loads the customer login screne
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
        //back button
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
    //creates new account
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
        //creates accounts when registering
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
    //loads customer menu
    public static void loadCustomerMenu() {
        Label welcome = new Label("Welcome " + Main.customer.getName());
        Button Select_Items = new Button("Select Items");
        Button View_Order = new Button("View Order");
        Button View_Invoice = new Button("View Invoice");
        Button Make_order = new Button("Make Order");
        Button logout = new Button("Logout");
        HBox customer_menu_interface1 = new HBox(5f,Select_Items, Make_order);
        HBox customer_menu_interface2 = new HBox(5f,View_Order, View_Invoice);
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerLogin();
            }
        });
        Select_Items.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSelectItemsMenu();
            }
        });
        View_Order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadViewOrderScreen();
            }
        });
        View_Invoice.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadViewInvoiceScreen();
            }
        });
        Make_order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadMakeOrderScreen();
            }
        });
        Main.scene.setRoot(new VBox(5f,welcome, customer_menu_interface1, customer_menu_interface2, logout));
    }

    public static void loadSelectItemsMenu(){
        Label Select_items_label = new Label("Please Select a store item below:");
        Button back_button = new Button("Back");
        Label Items_Temp = new Label("");
        ArrayList<SupplierAccount> ItemSuppliers = new ArrayList<>();
        try {
            ItemSuppliers = SupplierAccount.getSuppliers();
        }catch(Exception e){
            e.printStackTrace();
        }
        int tmpID=0;
        Item tmpItem = new Item();
        ArrayList<Item> listOfItems = new ArrayList<>();
        int Supplier_Counter=ItemSuppliers.size();
        for(int i = 0; i < Supplier_Counter; i++){
            tmpID = ItemSuppliers.get(i).id();

            try{
                listOfItems = Item.getItems(tmpID);
            }catch(Exception e){
                e.printStackTrace();
            }
            for(int e =0; e < listOfItems.size(); e++){
                Items_Temp.setText(Items_Temp.getText()+"\n"+listOfItems.get(e).stringify());
            }
            //Items_Temp.setText(Items_Temp.getText()+"\n"+ItemSuppliers.get(i));
        }
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerMenu();
            }
        });
        Main.scene.setRoot(new VBox(Select_items_label,Items_Temp, back_button));
    }
    public static void loadViewOrderScreen(){
        Label View_Order_label = new Label ("Here are your current order details.");
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerMenu();
            }
        });
        Main.scene.setRoot(new VBox(View_Order_label, back_button));
    }
    public static void loadViewInvoiceScreen(){
        Label view_invoice_label = new Label("Here are your past orders:");
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerMenu();
            }
        });
        Main.scene.setRoot(new VBox(view_invoice_label, back_button));
    }
    public static void loadMakeOrderScreen(){
        Label make_order_label = new Label("Here is the order you are willing to make:");
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerMenu();
            }
        });
        Main.scene.setRoot(new VBox(make_order_label, back_button));
    }
}