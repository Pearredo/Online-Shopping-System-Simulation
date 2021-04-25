package OnlineShoppingSystem.UI;

import OnlineShoppingSystem.Data.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class Customer {
    //loads the customer login scene
    public static Background customerBackground(){
        Background cus_background = new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY));
        return cus_background;
    }

    public static void loadCustomerLogin() {
        Main.customer = null;
        Main.cart = null;
        Label login_label = new Label("Welcome to the Customer Portal!"),
            login_error = new Label(),
            user_error = new Label(),
            password_error = new Label();
        login_error.setTextFill(Color.color(.85f, 0, 0));
        user_error.setTextFill(Color.color(.85f, 0, 0));
        password_error.setTextFill(Color.color(.85f, 0, 0));
        Label user_textbox_label = new Label("Username:\t");
        TextField user_textbox = new TextField();
        Label password_label = new Label("Password:\t\t");
        PasswordField password_textbox = new PasswordField();
        VBox user_login_interface = new VBox(10f,
            new HBox(5f,user_textbox_label, user_textbox, user_error),
            new HBox(5f,password_label, password_textbox, password_error));
        Button customer_login_button = new Button("Log in");

        customer_login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean error = false;
                    login_error.setText("");
                    user_error.setText("");
                    password_error.setText("");
                    if (user_textbox.getText().length() < 1) {
                        error = true;
                        user_error.setText("A username must be provided");
                    }
                    if (password_textbox.getText().length() < 1) {
                        error = true;
                        password_error.setText("A password must be provided");
                    }
                    if (!error) {
                        if ((Main.customer = CustomerAccount.Login(user_textbox.getText(), password_textbox.getText())) != null) {
                            loadCustomerMenu();
                        } else {
                            login_error.setText("An invalid username and password were provided");
                        }
                    }
                } catch (Exception e) {
                    login_error.setText("An unexpected error occurred");
                }
            }
        });
        Button customer_register = new Button("Register New Account");
        customer_register.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerRegister();
            }
        });
        //back button
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.loadWelcomeMenu();
            }
        });
        VBox Cus_login_interface = new VBox(10f,login_label, user_login_interface, customer_login_button, login_error, customer_register, back_button);
        Cus_login_interface.setBackground(customerBackground());
        Main.scene.setRoot(Cus_login_interface);
    }
    //creates new account
    public static void loadCustomerRegister() {
        Main.customer = null;
        Main.cart = null;
        Label customer_register_label = new Label("Please enter your account information:"),
            register_error = new Label(),
            user_error = new Label(),
            pass_error = new Label(),
            name_error = new Label(),
            addr_error = new Label(),
            phone_error = new Label(),
            cc_error = new Label();
        register_error.setTextFill(Color.color(.85f, 0, 0));
        user_error.setTextFill(Color.color(.85f, 0, 0));
        pass_error.setTextFill(Color.color(.85f, 0, 0));
        name_error.setTextFill(Color.color(.85f, 0, 0));
        addr_error.setTextFill(Color.color(.85f, 0, 0));
        phone_error.setTextFill(Color.color(.85f, 0, 0));
        cc_error.setTextFill(Color.color(.85f, 0, 0));
        Label reg_user_textbox_label = new Label("Username: ");
        TextField reg_user_textbox = new TextField();
        Label reg_pass_textbox_label = new Label("Password: ");
        PasswordField reg_pass_textbox = new PasswordField();
        Label reg_customer_name_label = new Label("Full Name: ");
        TextField reg_name = new TextField();
        Label reg_customer_addr_label = new Label("Address: ");
        TextField reg_customer_addr = new TextField();
        reg_customer_addr.setPrefWidth(300);
        Label reg_customer_phone_label = new Label("Phone Number: ");
        TextField reg_customer_phone = new TextField();
        Label reg_customer_CC_Label = new Label("Credit Card Number: ");
        TextField reg_customer_cc = new TextField();
        Label reg_customer_prem_label = new Label("Account Type: ");
        ComboBox<String> accountType = new ComboBox<>();
        accountType.getItems().addAll(
            "Standard (Free)",
            "Premium ($40.00 w/ First Annual Purchase)");
        accountType.setValue("Standard (Free)");
        VBox reg_interface = new VBox(10f,
            new HBox(5f,reg_user_textbox_label, reg_user_textbox, user_error),
                new HBox(5f,reg_pass_textbox_label, reg_pass_textbox, pass_error),
                new HBox(5f,reg_customer_name_label, reg_name, name_error),
                new HBox(5f,reg_customer_addr_label, reg_customer_addr, addr_error),
                new HBox(5f,reg_customer_phone_label, reg_customer_phone, phone_error),
                new HBox(5f,reg_customer_CC_Label, reg_customer_cc, cc_error),
                new HBox(5f,reg_customer_prem_label, accountType)
        );
        Button customer_reg_button = new Button("Register");
        //creates accounts when registering
        customer_reg_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean error = false;
                    register_error.setText("");
                    user_error.setText("");
                    pass_error.setText("");
                    name_error.setText("");
                    addr_error.setText("");
                    cc_error.setText("");
                    if (reg_user_textbox.getText().length() < 1) {
                        error = true;
                        user_error.setText("A username must be provided");
                    }
                    if (reg_pass_textbox.getText().length() < 1) {
                        error = true;
                        pass_error.setText("A password must be provided");
                    }
                    if (reg_name.getText().length() < 1) {
                        error = true;
                        name_error.setText("A name must be provided");
                    }
                    if (reg_customer_addr.getText().length() < 1) {
                        error = true;
                        addr_error.setText("An address must be provided");
                    }
                    if (reg_customer_phone.getText().length() < 10 || !reg_customer_phone.getText().matches("^(\\(?)(\\d{3})(\\)?)(-?)(\\d{3})(-?)(\\d{4})$")) {
                        error = true;
                        phone_error.setText("A 10-digit phone number must be provided");
                    }
                    if (reg_customer_cc.getText().length() != 16 || reg_customer_cc.getText().matches("[^0-9]")) {
                        error = true;
                        cc_error.setText("A valid, 16-digit credit card must be provided");
                    }
                    if (!error) {
                        Main.customer = new CustomerAccount(
                            reg_user_textbox.getText(),
                            reg_pass_textbox.getText(),
                            reg_name.getText(),
                            reg_customer_addr.getText(),
                            reg_customer_phone.getText(),
                            reg_customer_cc.getText(),
                            accountType.getValue().equalsIgnoreCase("Premium ($40.00 w/ First Annual Purchase)"),
                            false);
                        if (Main.customer.create()) {
                            loadCustomerMenu();
                        } else {
                            Main.customer = null;
                            register_error.setText("That username is already taken");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Main.customer = null;
                    register_error.setText("An unexpected error occurred");
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerLogin();
            }
        });
        VBox reg_scene = new VBox(10f,customer_register_label, reg_interface, customer_reg_button, register_error, back_button);
        reg_scene.setBackground(customerBackground());
        Main.scene.setRoot(reg_scene);
    }
    //loads customer menu
    public static void loadCustomerMenu() {
        Main.cart = Main.cart == null ? new HashMap<>() : Main.cart;
        Label welcome = new Label("Welcome " + Main.customer.getName());
        Button updateInfo = new Button("Update Information");
        updateInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerInfo();
            }
        });
        Button Select_Items = new Button("Select Items");
        Button View_Cart = new Button("View Cart");
        Button View_Orders = new Button("View Orders");
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerLogin();
            }
        });
        Select_Items.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSelectItemsMenu();
            }
        });
        View_Cart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadViewCartScreen();
            }
        });
        View_Orders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadViewOrdersScreen();
            }
        });
        VBox cus_menu_interface = new VBox(10f,welcome, updateInfo, Select_Items, View_Cart, View_Orders, logout);
        cus_menu_interface.setBackground(customerBackground());
        Main.scene.setRoot(cus_menu_interface);
    }
    public static void loadCustomerInfo() {
        Label header = new Label("Updating Personal Information:");
        Label update_error = new Label(),
            pass_error = new Label(),
            name_error = new Label(),
            addr_error = new Label(),
            phone_error = new Label(),
            cc_error = new Label();
        update_error.setTextFill(Color.color(.85f, 0, 0));
        pass_error.setTextFill(Color.color(.85f, 0, 0));
        name_error.setTextFill(Color.color(.85f, 0, 0));
        addr_error.setTextFill(Color.color(.85f, 0, 0));
        phone_error.setTextFill(Color.color(.85f, 0, 0));
        cc_error.setTextFill(Color.color(.85f, 0, 0));
        Label passwordLabel = new Label("Change Password (Leave blank to not change): ");
        PasswordField password = new PasswordField();
        Label nameLabel = new Label("Full Name: ");
        TextField name = new TextField(Main.customer.getName());
        Label addressLabel = new Label("Address: ");
        TextField address = new TextField(Main.customer.getAddress());
        address.setPrefWidth(300);
        Label phoneLabel = new Label("Phone Number: ");
        TextField phone = new TextField(Main.customer.getPhoneNumber());
        Label creditCardLabel = new Label("Credit Card Number: ");
        TextField creditCard = new TextField(Main.customer.getCreditCard());
        Label premiumLabel = new Label("Account Type: ");
        ComboBox<String> accountType = new ComboBox<>();
        // We don't have a yearly reset feature, so we don't allow people to unsubscribe
        if (Main.customer.isPremium()) {
            accountType.getItems().addAll("Premium ($40.00 w/ First Annual Purchase)");
        } else {
            accountType.getItems().addAll(
                "Standard (Free)",
                "Premium ($40.00 w/ First Annual Purchase)");
        }
        accountType.setValue(Main.customer.isPremium() ? "Premium ($40.00 w/ First Annual Purchase)" : "Standard (Free)");
        VBox update_interface = new VBox(10f,
            new HBox(5f,passwordLabel, password, pass_error),
            new HBox(5f,nameLabel, name, name_error),
            new HBox(5f,addressLabel, address, addr_error),
            new HBox(5f,phoneLabel, phone, phone_error),
            new HBox(5f,creditCardLabel, creditCard, cc_error),
            new HBox(5f,premiumLabel, accountType)
        );
        update_interface.setBackground(customerBackground());
        Button update_button = new Button("Update Account");
        update_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean error = false;
                    update_button.setText("");
                    pass_error.setText("");
                    name_error.setText("");
                    addr_error.setText("");
                    cc_error.setText("");
                    if (name.getText().length() < 1) {
                        error = true;
                        name_error.setText("A name must be provided");
                    }
                    if (address.getText().length() < 1) {
                        error = true;
                        addr_error.setText("An address must be provided");
                    }
                    if (phone.getText().length() < 10 || !phone.getText().matches("^(\\(?)(\\d{3})(\\)?)(-?)(\\d{3})(-?)(\\d{4})$")) {
                        error = true;
                        phone_error.setText("A 10-digit phone number must be provided");
                    }
                    if (creditCard.getText().length() != 16 || creditCard.getText().matches("[^0-9]")) {
                        error = true;
                        cc_error.setText("A valid, 16-digit credit card must be provided");
                    }
                    if (!error) {
                        if (!password.getText().isEmpty()) {
                            Main.customer.setPassword(password.getText());
                        }
                        Main.customer.setName(name.getText());
                        Main.customer.setAddress(address.getText());
                        Main.customer.setPhoneNumber(phone.getText());
                        Main.customer.setCreditCard(creditCard.getText());
                        Main.customer.setPremPaid(Main.customer.premPaid());
                        Main.customer.setPremium(accountType.getValue().equalsIgnoreCase("Premium ($40.00 w/ First Annual Purchase)"));
                        if (!Main.customer.update()) {
                            update_error.setText("An unexpected error occurred");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    update_error.setText("An unexpected error occurred");
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerMenu();
            }
        });
        VBox cus_update_interface =new VBox(10f,header, update_interface, update_button, update_error, back_button);
        cus_update_interface.setBackground(customerBackground());
        Main.scene.setRoot(cus_update_interface);
    }
    public static void loadSelectItemsMenu(){
        Label Select_items_label = new Label("Please select a store catalogue below:");
        Button back_button = new Button("Back");
        ArrayList<SupplierAccount> ItemSuppliers = new ArrayList<>();
        try {
            ItemSuppliers = SupplierAccount.getSuppliers();
        }catch(Exception e){
            e.printStackTrace();
        }
        int Supplier_Counter = ItemSuppliers.size();
        Node[] supplier_array = new Node[Supplier_Counter];
        for(int i = 0; i < Supplier_Counter; i++){
            Button Catalog_Button = new Button(ItemSuppliers.get(i).getName());
            HBox Catalog_UI = new HBox(5f,Catalog_Button);
            supplier_array[i]=Catalog_UI;
            SupplierAccount sup = ItemSuppliers.get(i);
            Catalog_Button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadAddToCart(sup);
                }
            });
        }
        ScrollPane oiPane = new ScrollPane(new VBox(10f,supplier_array));
        oiPane.setPrefViewportHeight(Main.scene.getHeight());
        oiPane.setPrefViewportWidth(Main.scene.getWidth());
        oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerMenu();
            }
        });
        VBox select_items_interface = new VBox(10f,Select_items_label, oiPane, back_button);
        select_items_interface.setBackground(customerBackground());
        Main.scene.setRoot(select_items_interface);
    }
    public static void loadAddToCart(SupplierAccount sup){
        Label welcomeToStore = new Label("Welcome to the catalogue for " + sup.getName() + "!");
        int supID = sup.id();
        int itemCount = 0;
        ArrayList<Item> temp = new ArrayList<>();
        try {
            temp = Item.getItems(supID);
            itemCount = temp.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Item> itemList = temp;
        Node[] listOfItems = new Node[itemCount];
        TextField[] buyAmounts = new TextField[itemCount];
        Label add_error = new Label();
        add_error.setBackground(new Background(new BackgroundFill(Color.color(0.85f, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
        for(int i = 0; i < itemCount; i++){
            Item item = itemList.get(i);
            Label itemName = new Label(item.getItemName());
            Label itemInfo = new Label(item.getItemDesc());
            HBox itemPrc = new HBox(
                5f,
                new Label(String.format("Regular Price: %,.2f", item.getItemRegCost())),
                new Label(String.format("Premium Price: %,.2f", item.getItemPremCost())));
            Button reduce = new Button("-"),
                increase = new Button("+");
            TextField qty = new TextField("0");
            qty.setMaxWidth(35);
            buyAmounts[i] = qty;
            reduce.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int value;
                    try {
                        value = Integer.parseInt(qty.getText());
                    } catch (Exception ex) {
                        value = 0;
                    }
                    qty.setText(String.valueOf(value > 0 ? --value : 0));
                    add_error.setText("");
                    add_error.setBackground(new Background(new BackgroundFill(Color.color(0.85f, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });
            increase.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    int value;
                    try {
                        value = Integer.parseInt(qty.getText());
                    } catch (Exception ex) {
                        value = 0;
                    }
                    qty.setText(String.valueOf(++value));
                    add_error.setText("");
                    add_error.setBackground(new Background(new BackgroundFill(Color.color(0.85f, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            });
            HBox buyUI = new HBox(5f, reduce, qty, increase);
            Label curQty = new Label(Main.cart.containsKey(item.id()) ? Main.cart.get(item.id()).getItemQty() + " on current order.\n" : "");
            VBox storeItem = new VBox(5f,itemName,itemInfo,itemPrc,buyUI,curQty);
            listOfItems[i] = storeItem;
        }
        VBox listOfItems_VBox = new VBox(listOfItems);

        ScrollPane oiPane = new ScrollPane(listOfItems_VBox);
        oiPane.setPrefViewportHeight(Main.scene.getHeight());
        oiPane.setPrefViewportWidth(Main.scene.getWidth());
        oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
        Button addButton = new Button("Add items to cart");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean added = false;
                    add_error.setText("");
                    add_error.setBackground(new Background(new BackgroundFill(Color.color(0.85f, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
                    for (int i = 0; i < buyAmounts.length; i++) {
                        Item item = itemList.get(i);
                        int value,
                            id = item.id();
                        try {
                            value = Integer.parseInt(buyAmounts[i].getText());
                            buyAmounts[i].setText("0");
                        } catch (Exception ex) {
                            value = 0;
                        }
                        if (value > 0) {
                            if (Main.cart.containsKey(id)) {
                                Main.cart.get(id).setItemQty(Main.cart.get(id).getItemQty() + value);
                            } else {
                                Main.cart.put(id, new OrderItem(0, item.id(), value, 0, (byte)1));
                            }
                            added = true;
                        }
                    }
                    if (added) {
                        add_error.setText("All items were added to your cart");
                        add_error.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                } catch (Exception ex) {
                    add_error.setText("An unexpected error occurred");
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSelectItemsMenu();
            }
        });

        VBox add_cart_interface = new VBox(10f,welcomeToStore, oiPane, addButton, add_error, back_button);
        add_cart_interface.setBackground(customerBackground());
        Main.scene.setRoot(add_cart_interface);
    }
    public static void loadViewCartScreen() {
        float runningTotal = 0;
        Label View_Order_label = new Label("Current order details:"),
                empty = new Label("Your cart is currently empty");
        Node[] orderItems = new Node[Main.cart.size()];
        int i = 0;
        for (int id : Main.cart.keySet()) {
            Item item;
            try {
                item = Item.getItem(id);
                float cost = Main.customer.isPremium() ? item.getItemPremCost() : item.getItemRegCost();
                runningTotal += Main.cart.get(id).getItemQty() * cost;
                orderItems[i] = new VBox(10f,
                        new Label(item.getItemName() + ", " + item.getItemDesc()),
                        new Label("Ordered: " + Main.cart.get(id).getItemQty()),
                        new Label(String.format("Cost: $%,.2f", Main.cart.get(id).getItemQty() * cost)),
                        new Label());
            } catch (Exception ex) {
                // maybe do something...
            }
            i++;
        }
        float totalItemCost = runningTotal,
            premCost = Main.customer.isPremium() && !Main.customer.premPaid() ? 40 : 0;
        ScrollPane oiPane = new ScrollPane(new VBox(10f,orderItems));
        oiPane.setPrefViewportHeight(Main.scene.getHeight());
        oiPane.setPrefViewportWidth(Main.scene.getWidth());
        oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
        Label total = new Label(String.format("Total Cost: $%,.2f", totalItemCost + premCost));
        ComboBox<String> deliveryType = new ComboBox<>();
        deliveryType.getItems().addAll(
            "In-Store Pickup (Free)",
            "Mail Delivery ($3.00 Surcharge)");
        deliveryType.setValue("In-Store Pickup (Free)");
        deliveryType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                float deliveryCost = newValue.equalsIgnoreCase("In-Store Pickup (Free)") ? 0 : 3;
                total.setText(String.format("Total Cost: $%,.2f", totalItemCost + premCost + deliveryCost));
            }
        });
        TextField creditCard = new TextField(Main.customer.getCreditCard());
        Label invalidCC = new Label();
        invalidCC.setTextFill(Color.color(0.85f, 0, 0));
        HBox ccInfo = new HBox(5f,creditCard, invalidCC);
        Node premInfo;
        if (Main.customer.isPremium() && !Main.customer.premPaid()) {
            Button switchAndLose = new Button("Switch and Lose");
            switchAndLose.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Main.customer.setPremium(false);
                        Main.customer.update();
                        loadViewCartScreen();
                    } catch (Exception ex) {
                        // maybe do something
                    }
                }
            });
            premInfo = new VBox(10f,
                new Label(String.format("Premium Subscription Fee: $%,.2f", premCost)),
                new HBox(5f,new Label("Switch to a standard account to not pay a subscription fee, but pay more on items: "), switchAndLose));
        } else if (Main.customer.isPremium()) {
            premInfo = new HBox(5f,new Label("Thank you for being a premium customer!"));
        } else {
            Button switchAndSave = new Button("Switch and Save!");
            switchAndSave.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Main.customer.setPremium(true);
                        Main.customer.update();
                        loadViewCartScreen();
                    } catch (Exception ex) {
                        // maybe do something
                    }
                }
            });
            premInfo = new VBox(10f,
                    new HBox(5f,new Label("Switch to a premium account to save on all items: "), switchAndSave),
                    new Label("Premium subscriptions cost $40.00 and are billed to your first purchase each year, starting with this purchase"));
        }
        Label buy_error = new Label();
        buy_error.setTextFill(Color.color(0.85f, 0, 0));
        Button buyButton = new Button("Purchase Order");
        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    buy_error.setText("");
                    invalidCC.setText("");
                    if (creditCard.getText().length() == 16 && !creditCard.getText().matches("[^0-9]")) {
                        byte deliveryMethod = deliveryType.getValue().equalsIgnoreCase("In-Store Pickup (Free)") ? 0 : (byte) 1;
                        float deliveryCost = (float) deliveryMethod * 3;
                        int date = java.time.LocalDate.now().getYear() * 10000 + java.time.LocalDate.now().getMonthValue() * 100 + java.time.LocalDate.now().getDayOfMonth();
                        Order order = new Order(Main.customer.id(), (byte)1, date, totalItemCost + premCost + deliveryCost, premCost, deliveryMethod, deliveryCost);
                        order.create();
                        if (order.purchase(creditCard.getText())) {
                            invalidCC.setText("");
                            for (OrderItem oi : Main.cart.values()) {
                                Item item = Item.getItem(oi.getItemID());
                                float lineCost = oi.getItemQty() * (Main.customer.isPremium() ? item.getItemPremCost() : item.getItemRegCost());
                                OrderItem newOI = new OrderItem(order.id(), oi.getItemID(), oi.getItemQty(), lineCost, (byte)1);
                                newOI.create();
                            }
                            order.update();
                            Main.cart = null;
                            Main.customer.setPremPaid(Main.customer.premPaid() || order.getPremCost() > 0);
                            Main.customer.setCreditCard(creditCard.getText());
                            Main.customer.update();
                            loadViewInvoiceScreen(order.id());
                        } else {
                            order.delete();
                            invalidCC.setText("Invalid credit card or insufficient funds");
                        }
                    } else {
                        invalidCC.setText("Invalid credit card or insufficient funds");
                    }
                } catch (Exception ex) {
                    buy_error.setText("An unexpected error occurred");
                }
            }
        });
        Button cancelButton = new Button("Cancel Order");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.cart = new HashMap<>();
                loadViewCartScreen();
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerMenu();
            }
        });
        if (Main.cart.size() > 0) {
            VBox view_cart_with_items_interface = new VBox(
                10f,
                View_Order_label,
                oiPane,
                deliveryType,
                premInfo,
                total,
                ccInfo,
                buyButton,
                buy_error,
                cancelButton,
                back_button);
            view_cart_with_items_interface.setBackground(customerBackground());
            Main.scene.setRoot(view_cart_with_items_interface);
        } else {
            VBox view_cart_without_items_interface = new VBox(10f, View_Order_label, empty, back_button);
            view_cart_without_items_interface.setBackground(customerBackground());
            Main.scene.setRoot(view_cart_without_items_interface);
        }
    }
    public static void loadViewOrdersScreen() {
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerMenu();
            }
        });
        ArrayList<Order> invoices = new ArrayList<>();
        try {
            invoices = Order.getOrders(Main.customer.id());
        } catch (Exception ex) {
            // do something here
        }
        if (invoices.size() > 0) {
            Node[] invoiceItems = new Node[invoices.size()];
            for (int i = 0; i < invoices.size(); i++) {
                Order invoice = invoices.get(i);
                String date = String.format("%d/%d/%d", invoice.getOrderDate() % 10000 / 100, invoice.getOrderDate() % 100, invoice.getOrderDate() / 10000),
                    deliveryType = invoice.getOrderDelivery() == 0 ? "In-Store Pickup" : "Mail Delivery",
                    orderStatus = "";
                switch (invoice.getOrderStatus()) {
                    case 0: orderStatus = "Not Purchased"; break;
                    case 1: orderStatus = "Purchased, not Started"; break;
                    case 2: orderStatus = "Partially Filled"; break;
                    case 3: orderStatus = "Filled"; break;
                    case 4: orderStatus = "Partially Shipped"; break;
                    case 5: orderStatus = "Shipped"; break;
                }
                Button invoiceMenu = new Button("View Invoice");
                invoiceMenu.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        loadViewInvoiceScreen(invoice.id());
                    }
                });
                VBox orderMenu = new VBox(10f,
                    new Label("Order #" + invoice.id()),
                    new Label("Order Date: " + date),
                    new Label("Delivery Method: " + deliveryType),
                    new Label("Order Status: " + orderStatus),
                    new HBox(5f,invoiceMenu)
                );
                invoiceItems[i] = orderMenu;
            }
            ScrollPane iPane = new ScrollPane(new VBox(10f, invoiceItems));
            iPane.setPrefViewportHeight(Main.scene.getHeight());
            iPane.setPrefViewportWidth(Main.scene.getWidth());
            iPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
            Label view_invoice_label = new Label("Here are your past orders:");

            VBox view_orders_with_orders =new VBox(10f,view_invoice_label, iPane, back_button);
            view_orders_with_orders.setBackground(customerBackground());
            Main.scene.setRoot(view_orders_with_orders);
        } else {
            VBox view_order_without_orders = new VBox(10f,new Label("You do not have any historical orders."), back_button);
            view_order_without_orders.setBackground(customerBackground());
            Main.scene.setRoot(view_order_without_orders);
        }
    }
    public static void loadViewInvoiceScreen(int orderID) {
        Order order = null;
        try {
            order = Order.getOrder(orderID);
        } catch (Exception ex) {
            // maybe do something...
        }
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadCustomerMenu();
            }
        });
        if (order == null) {
            Label view_invoice_label = new Label("This order does not exist");
            VBox no_invoice_interface = new VBox(10f,view_invoice_label, back_button);
            no_invoice_interface.setBackground(customerBackground());
            Main.scene.setRoot(no_invoice_interface);
        } else {
            String date = String.format("%d/%d/%d", order.getOrderDate() % 10000 / 100, order.getOrderDate() % 100, order.getOrderDate() / 10000);
            Label view_invoice_label = new Label("Viewing invoice " + order.getInvoiceID() + " from " + date);
            ArrayList<OrderItem> orderItems = new ArrayList<>();
            try {
                orderItems = OrderItem.getOrderItems(orderID, 'o');
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Node[] oiList = new Node[orderItems.size()];
            for (int i = 0; i < orderItems.size(); i++) {
                OrderItem oi = orderItems.get(i);
                try {
                    Item item = Item.getItem(oi.getItemID());
                    oiList[i] = new Label(String.format("%s, %d unit(s): %,.2f", item.getItemName(), oi.getItemQty(), oi.getLineCost()));
                } catch (Exception ex) {
                   ex.printStackTrace();
                }
            }
            Label deliveryCost = new Label(order.getOrderDelivery() == 1 ? "Mail Delivery: $3.00" : "In-Store Pickup: $0.00"),
                premCost = new Label(order.getPremCost() > 0 ? String.format("Premium Subscription Fee: $%,.2f", order.getPremCost()) : ""),
                total = new Label(String.format("Total Cost: %,.2f", order.getOrderCost())),
                authNumber = new Label("Authorization Number: " + order.getPurchaseAuth());
            ScrollPane oiPane = new ScrollPane(new VBox(10f,oiList));
            oiPane.setPrefViewportHeight(Main.scene.getHeight());
            oiPane.setPrefViewportWidth(Main.scene.getWidth());
            oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
            VBox view_invoice_with_invoice = new VBox(10f,view_invoice_label, oiPane, deliveryCost, premCost, total, authNumber, back_button);
            view_invoice_with_invoice.setBackground(customerBackground());
            Main.scene.setRoot(view_invoice_with_invoice);
        }
    }
}