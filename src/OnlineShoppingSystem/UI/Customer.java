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
import java.util.ArrayList;
import java.util.HashMap;

public class Customer {
    public static void loadCustomerIntro() {
        Main.customer = null;
        Main.cart = null;
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
        Main.cart = null;
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
        Main.cart = null;
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
        Main.cart = Main.cart == null ? new HashMap<>() : Main.cart;
        Label welcome = new Label("Welcome " + Main.customer.getName());
        Button Select_Items = new Button("Select Items");
        Button View_Order = new Button("View Order");
        Button View_Invoice = new Button("View Invoice");
        Button logout = new Button("Logout");
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
        Main.scene.setRoot(new VBox(5f,welcome, Select_Items, customer_menu_interface2, logout));
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
            HBox Catalog_UI = new HBox(Catalog_Button);
            supplier_array[i]=Catalog_UI;
            SupplierAccount sup = ItemSuppliers.get(i);
            Catalog_Button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.resubmit = false;
                    loadAddToOrder(sup);
                }
            });
        }

        ScrollPane oiPane = new ScrollPane(new VBox(supplier_array));
        oiPane.setPrefViewportHeight(Main.scene.getHeight());
        oiPane.setPrefViewportWidth(Math.min(Main.scene.getWidth() * 2, Main.scene.getWidth()));
        oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerMenu();
            }
        });
        Main.scene.setRoot(new VBox(Select_items_label, oiPane, back_button));
    }
    public static void loadAddToOrder(SupplierAccount sup){
        Label welcomeToStore = new Label("Welcome to the catalogue for " + sup.getName());
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
        for(int i = 0; i < itemCount; i++){
            Item item = itemList.get(i);
            Label itemName = new Label(item.getItemName());
            Label itemInfo = new Label(item.getItemDesc());
            Label itemPrc = new Label();
            if(Main.customer.isPremium()){
                itemPrc.setText("For the price of :\t" + item.getItemPremCost());
            }else{
                itemPrc.setText("For the price of :\t" + item.getItemRegCost());
            }
            Button reduce = new Button("<"),
                increase = new Button(">");
            TextField qty = new TextField("0");
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
                }
            });
            HBox buyUI = new HBox(5f, reduce, qty, increase);
            Label curQty = new Label(Main.cart.containsKey(item.id()) ? Main.cart.get(item.id()).getItemQty() + " on current order.\n" : "");
            VBox storeItem = new VBox(itemName,itemInfo,itemPrc,buyUI,curQty);
            listOfItems[i] = storeItem;
        }
        ScrollPane oiPane = new ScrollPane(new VBox(listOfItems));
        oiPane.setPrefViewportHeight(Main.scene.getHeight());
        oiPane.setPrefViewportWidth(Main.scene.getWidth());
        oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
        Button buyButton = new Button("Add items to cart");
        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
                            try {
                                Main.cart.put(id, new OrderItem(0, item.id(), value, 0, (byte)1));
                            } catch (Exception ex) {
                                // maybe do something?
                            }
                        }
                    }
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSelectItemsMenu();
            }
        });
        Main.scene.setRoot(new VBox(welcomeToStore, oiPane, buyButton, back_button));
    }
    public static void loadViewOrderScreen() {
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
                orderItems[i] = new VBox(
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
        ScrollPane oiPane = new ScrollPane(new VBox(orderItems));
        oiPane.setPrefViewportHeight(Main.scene.getHeight());
        oiPane.setPrefViewportWidth(Main.scene.getWidth());
        oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
        Label total = new Label(String.format("Total Cost: $%,.2f", totalItemCost + premCost));
        ComboBox<String> deliveryType = new ComboBox<>();
        deliveryType.getItems().addAll(
            "Mail Delivery ($3.00 Surcharge)",
            "In-Store Pickup (Free)");
        deliveryType.setValue("In-Store Pickup (Free)");
        deliveryType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue observable, String oldValue, String newValue) {
                float deliveryCost = newValue.equalsIgnoreCase("In-Store Pickup (Free)") ? 0 : 3;
                total.setText(String.format("Total Cost: $%,.2f", totalItemCost + premCost + deliveryCost));
            }
        });
        Label premPurchase = new Label(premCost > 0 ? String.format("Premium Cost: %,.2f", premCost) : "");
        TextField creditCard = new TextField(Main.customer.getCreditCard());
        Label invalidCC = new Label("");
        invalidCC.setTextFill(Color.color(0.85f, 0, 0));
        HBox ccInfo = new HBox(creditCard, invalidCC);
        Button buyButton = new Button("Purchase Order");
        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                byte deliveryMethod = deliveryType.getValue().equalsIgnoreCase("In-Store Pickup (Free)") ? 0 : (byte)1;
                float deliveryCost = (float)deliveryMethod * 3;
                int date = java.time.LocalDate.now().getYear() * 10000 + java.time.LocalDate.now().getMonthValue() * 100 + java.time.LocalDate.now().getDayOfYear();
                Order order = new Order(Main.customer.id(), (byte)0, date, totalItemCost + premCost + deliveryCost, premCost, deliveryMethod, deliveryCost);
                try {
                    order.create();
                    if (order.purchase(creditCard.getText())) {
                        invalidCC.setText("");
                        for (OrderItem oi : Main.cart.values()) {
                            Item item = Item.getItem(oi.getItemID());
                            float lineCost = oi.getItemQty() * (Main.customer.isPremium() ? item.getItemPremCost() : item.getItemRegCost());
                            OrderItem newOI = new OrderItem(order.id(), oi.getItemID(), oi.getItemQty(), lineCost, oi.getOrderItemStatus());
                            newOI.create();
                        }
                        order.update();
                        Main.cart = null;
                        loadViewInvoicesScreen(order.id());
                        // go to view invoice screen using order id...
                    } else {
                        order.delete();
                        invalidCC.setText("Invalid credit card or insufficient funds");
                    }
                } catch (Exception ex) {
                    Main.resubmit = true;
                    loadViewOrderScreen();
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadCustomerMenu();
            }
        });
        if (Main.cart.size() > 0) {
            Main.scene.setRoot(new VBox(
                5f,
                View_Order_label,
                oiPane,
                deliveryType,
                premPurchase,
                total,
                ccInfo,
                buyButton,
                new Label(),
                back_button));
        } else {
            Main.scene.setRoot(new VBox(
                5f,
                View_Order_label,
                empty,
                back_button));
        }
    }
    public static void loadViewInvoiceScreen() {
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
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
                String date = String.format("%d/%d/%d", invoice.getOrderDate() % 10000 / 100, invoice.getOrderDate() % 100, invoice.getOrderDate() / 1000);
                Button invoiceMenu = new Button(invoice.getInvoiceID() + ", " + date);
                invoiceMenu.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        loadViewInvoicesScreen(invoice.id());
                    }
                });
                invoiceItems[i] = invoiceMenu;
            }
            ScrollPane iPane = new ScrollPane(new VBox(invoiceItems));
            iPane.setPrefViewportHeight(Main.scene.getHeight());
            iPane.setPrefViewportWidth(Main.scene.getWidth());
            iPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
            Label view_invoice_label = new Label("Here are your past orders:");
            Main.scene.setRoot(new VBox(view_invoice_label, iPane, back_button));
        } else {
            Main.scene.setRoot(new VBox(
                new Label("You do not have any historical orders."),
                back_button));
        }
    }
    public static void loadViewInvoicesScreen(int orderID) {
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
                Main.resubmit = false;
                loadCustomerMenu();
            }
        });
        if (order == null) {
            Label view_invoice_label = new Label("This order does not exist");
            Main.scene.setRoot(new VBox(view_invoice_label, back_button));
        } else {
            String date = String.format("%d/%d/%d", order.getOrderDate() % 10000 / 100, order.getOrderDate() % 100, order.getOrderDate() / 1000);
            Label view_invoice_label = new Label("Viewing invoice " + order.getInvoiceID() + " from " + date);
            ArrayList<OrderItem> orderItems = new ArrayList<>();
            try {
                orderItems = OrderItem.getOrderItems(orderID, '0');
            } catch (Exception ex) {
                // do something here
            }
            Node[] oiList = new Node[orderItems.size()];
            for (int i = 0; i < orderItems.size(); i++) {
                OrderItem oi = orderItems.get(i);
                try {
                    Item item = Item.getItem(oi.getItemID());
                    oiList[i] = new Label(String.format("%s, %d unit(s): %,.2f", item.getItemName(), oi.getItemQty(), oi.getLineCost()));
                } catch (Exception ex) {
                    // do something here
                }
            }
            Label authNumber = new Label("Authorization Number: " + order.getPurchaseAuth()),
                total = new Label(String.format("Total Cost: %,.2f", order.getOrderCost()));
            ScrollPane oiPane = new ScrollPane(new VBox(oiList));
            oiPane.setPrefViewportHeight(Main.scene.getHeight());
            oiPane.setPrefViewportWidth(Main.scene.getWidth());
            oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
            Main.scene.setRoot(new VBox(
                view_invoice_label,
                oiPane,
                authNumber,
                total,
                back_button));
        }
    }
}