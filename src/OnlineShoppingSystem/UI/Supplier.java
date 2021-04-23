package OnlineShoppingSystem.UI;

import OnlineShoppingSystem.Data.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;

public class Supplier {
    public static void loadSupplierLogin() {
        Main.supplier = null;
        Label login_label = new Label("Welcome to the Supplier Portal!");
        Label login_error = new Label(),
            user_error = new Label(),
            pass_error = new Label();
        login_error.setTextFill(Color.color(.85f, 0, 0));
        user_error.setTextFill(Color.color(.85f, 0, 0));
        pass_error.setTextFill(Color.color(.85f, 0, 0));
        Label user_textbox_label = new Label("Username: ");
        TextField user_textbox = new TextField();
        Label password_textbox_label = new Label("Password: ");
        TextField password_textbox = new TextField();
        VBox user_login_interface = new VBox(
            new HBox(user_textbox_label, user_textbox, user_error),
            new HBox(password_textbox_label, password_textbox, pass_error));
        Button supplier_login_button = new Button ("Log in");
        supplier_login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean error = false;
                    login_error.setText("");
                    user_error.setText("");
                    pass_error.setText("");
                    if (user_textbox.getText().length() < 1) {
                        error = true;
                        user_error.setText("A username must be provided");
                    }
                    if (password_textbox.getText().length() < 1) {
                        error = true;
                        pass_error.setText("A password must be provided");
                    }
                    if (!error) {
                        if ((Main.supplier = SupplierAccount.Login(user_textbox.getText(), password_textbox.getText())) != null) {
                            loadSupplierMenu();
                        } else {
                            login_error.setText("An invalid username or password were provided");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    login_error.setText("An unexpected error occurred");
                }
            }
        });
        Button sup_reg = new Button("Register New Account");
        sup_reg.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierRegister();
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.loadWelcomeMenu();
            }
        });
        Main.scene.setRoot(new VBox(login_label, user_login_interface, supplier_login_button, login_error, sup_reg, back_button));
    }
    public static void loadSupplierRegister() {
        Main.supplier = null;
        Label supplier_register_label = new Label("Please enter your intended Username, Password, And Supplier Name: ");
        Label reg_error = new Label(),
            user_error = new Label(),
            pass_error = new Label(),
            name_error = new Label();
        reg_error.setTextFill(Color.color(.85f, 0, 0));
        user_error.setTextFill(Color.color(.85f, 0, 0));
        pass_error.setTextFill(Color.color(.85f, 0, 0));
        name_error.setTextFill(Color.color(.85f, 0, 0));
        Label sup_reg_textbox_label = new Label("Username: ");
        TextField sup_reg_textbox = new TextField();
        Label sup_pass_textbox_label = new Label("Password: ");
        TextField sup_pass_textbox = new TextField();
        Label supplier_ven_textbox_label = new Label("Name: ");
        TextField supplier_ven_textbox = new TextField();
        VBox sup_reg_interface = new VBox(
            new HBox(sup_reg_textbox_label, sup_reg_textbox, user_error),
            new HBox(sup_pass_textbox_label, sup_pass_textbox, pass_error),
            new HBox(supplier_ven_textbox_label, supplier_ven_textbox, name_error));
        Button sup_reg_button = new Button ("Register");
        sup_reg_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean error = false;
                    reg_error.setText("");
                    user_error.setText("");
                    pass_error.setText("");
                    name_error.setText("");
                    if (sup_reg_textbox.getText().length() < 1) {
                        error = true;
                        user_error.setText("A username must be provided");
                    }
                    if (sup_pass_textbox.getText().length() < 1) {
                        error = true;
                        pass_error.setText("A password must be provided");
                    }
                    if (supplier_ven_textbox.getText().length() < 1) {
                        error = true;
                        name_error.setText("A name must be provided");
                    }
                    if (!error) {
                        Main.supplier = new SupplierAccount(
                            sup_reg_textbox.getText(),
                            sup_pass_textbox.getText(),
                            supplier_ven_textbox.getText());
                        if (Main.supplier.create()) {
                            loadSupplierMenu();
                        } else {
                            Main.supplier = null;
                            reg_error.setText("That username is already taken");
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    Main.supplier = null;
                    reg_error.setText("An unexpected error occurred");
                }
            }
        });
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierLogin();
            }
        });
        Main.scene.setRoot(new VBox(supplier_register_label, sup_reg_interface, sup_reg_button, reg_error, back_button));
    }
    public static void loadSupplierMenu() {
        Label welcome = new Label("Welcome " + Main.supplier.getName());
        Button updateInfo = new Button("Update Information");
        updateInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierInfo();
            }
        });
        Button stockManager = new Button("Manage Stock");
        stockManager.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierStockMenu();
            }
        });
        Button orderManager = new Button("Fill Open Orders");
        orderManager.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierOrders();
            }
        });
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierLogin();
            }
        });
        Main.scene.setRoot(new VBox(welcome, updateInfo, stockManager, orderManager, logout));
    }
    public static void loadSupplierInfo() {
        Label header = new Label("Updating Personal Information:");
        Label update_error = new Label(),
            pass_error = new Label(),
            name_error = new Label();
        update_error.setTextFill(Color.color(.85f, 0, 0));
        pass_error.setTextFill(Color.color(.85f, 0, 0));
        name_error.setTextFill(Color.color(.85f, 0, 0));
        Label passwordLabel = new Label("Change Password (Leave blank to not change): ");
        TextField password = new TextField();
        Label nameLabel = new Label("Full Name: ");
        TextField name = new TextField(Main.supplier.getName());
        VBox update_interface = new VBox(
                new HBox(passwordLabel, password, pass_error),
                new HBox(nameLabel, name, name_error)
        );
        Button update_button = new Button("Update Account");
        update_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean error = false;
                    update_button.setText("");
                    pass_error.setText("");
                    name_error.setText("");
                    if (name.getText().length() < 1) {
                        error = true;
                        name_error.setText("A name must be provided");
                    }
                    if (!error) {
                        if (!password.getText().isEmpty()) {
                            Main.supplier.setPassword(password.getText());
                        }
                        Main.supplier.setName(name.getText());
                        if (!Main.supplier.update()) {
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
                loadSupplierMenu();
            }
        });
        Main.scene.setRoot(new VBox(
                header,
                update_interface,
                update_button,
                update_error,
                back_button));
    }
    public static void loadSupplierStockMenu() {
        ArrayList<Item> items = new ArrayList<>();
        boolean itemsLoaded = false;
        try {
            items = Item.getItems(Main.supplier.id());
            itemsLoaded = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Label header = new Label("Stock Management");
        Button newItem = new Button("Add New Item");
        newItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierItemCreate();
            }
        });
        Label curItems = new Label("Current Items");
        Label notice = new Label();
        if (itemsLoaded && items.size() < 1) {
            notice.setText("You are not currently providing any items. Please add new items.");
        } else if (!itemsLoaded) {
            notice.setText("Your items were unable to be loaded. Please refresh this page.");
            notice.setTextFill(Color.color(.85f, 0, 0));
        }
        Node[] itemOptions = new Node[items.size()];
        for (int i = 0; i < items.size(); i++) {
            int id = items.get(i).id();
            Item item = items.get(i);
            Label iLabel = new Label(item.getItemName()),
                iStock = new Label("Stock: " + item.getItemQty()),
                iRsrv = new Label("Reserved: " + item.getReservedQty()),
                iAvail = new Label("Available: " + (item.getItemQty() - item.getReservedQty())),
                stockLabel = new Label("Add "),
                stockLabel_end = new Label(" to Available Stock: ");
            TextField stockAdj = new TextField("1");
            stockAdj.setMaxWidth(35);
            Button decStock = new Button("-");
            decStock.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!(stockAdj.getText().isEmpty() || stockAdj.getText().matches("[^0-9]")) && Integer.parseInt(stockAdj.getText()) > 0) {
                        stockAdj.setText(String.valueOf(Integer.parseInt(stockAdj.getText()) - 1));
                    }
                }
            });
            Button incStock = new Button("+");
            incStock.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!(stockAdj.getText().isEmpty() || stockAdj.getText().matches("[^0-9]"))) {
                        stockAdj.setText(String.valueOf(Integer.parseInt(stockAdj.getText()) + 1));
                    }
                }
            });
            Button addStock = new Button("Add");
            addStock.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (!(stockAdj.getText().isEmpty() || stockAdj.getText().matches("[^0-9]")) && Integer.parseInt(stockAdj.getText()) > 0) {
                        item.setItemQty(item.getItemQty() + Integer.parseInt(stockAdj.getText()));
                        try {
                            item.update();
                            iStock.setText("Stock: " + item.getItemQty());
                            iRsrv.setText("Reserved: " + item.getReservedQty());
                            iAvail.setText("Available: " + (item.getItemQty() - item.getReservedQty()));
                        } catch (Exception ex) {
                            // maybe do something...
                        }
                    }
                }
            });
            Button iButton = new Button("Edit");
            iButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadSupplierItemEdit(id);
                }
            });
            itemOptions[i] = new VBox(
                new HBox(15f, iLabel, iButton),
                new HBox(10f, iStock, iRsrv, iAvail),
                new HBox(stockLabel, decStock, stockAdj, incStock, stockLabel_end, addStock));
        }
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierMenu();
            }
        });
        VBox allItems = new VBox(10f, itemOptions);
        ScrollPane itemPane = new ScrollPane(allItems);
        itemPane.setPrefViewportHeight(Main.scene.getHeight());
        itemPane.setPrefViewportWidth(Math.min(allItems.getWidth() * 2, Main.scene.getWidth()));
        itemPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        itemPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        Main.scene.setRoot(new VBox(
            header,
            newItem,
            notice,
            curItems,
            itemPane,
            back_button));
    }
    public static void loadSupplierItemCreate() {
        Label header = new Label("Adding a New Item"),
            name_label = new Label("Item Name: "),
            desc_label = new Label("Item Description: "),
            regCost_label = new Label("Item Regular Cost: "),
            premCost_label = new Label("Item Premium Cost: "),
            qty_label = new Label("Item Quantity: "),
            item_error = new Label(),
            name_error = new Label(),
            desc_error = new Label(),
            regCost_error = new Label(),
            premCost_error = new Label(),
            qty_error = new Label();
        item_error.setTextFill(Color.color(0.85f, 0, 0));
        name_error.setTextFill(Color.color(0.85f, 0, 0));
        desc_error.setTextFill(Color.color(0.85f, 0, 0));
        regCost_error.setTextFill(Color.color(0.85f, 0, 0));
        premCost_error.setTextFill(Color.color(0.85f, 0, 0));
        qty_error.setTextFill(Color.color(0.85f, 0, 0));
        TextField name = new TextField(),
            desc = new TextField(),
            regCost = new TextField(),
            premCost = new TextField(),
            qty = new TextField();
        Button post = new Button("Add Item");
        post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    boolean error = false;
                    item_error.setText("");
                    item_error.setTextFill(Color.color(0.85f, 0, 0));
                    name_error.setText("");
                    desc_error.setText("");
                    regCost_error.setText("");
                    premCost_error.setText("");
                    qty_error.setText("");
                    if (name.getText().length() < 1) {
                        error = true;
                        name_error.setText("An item name must be provided");
                    }
                    if (desc.getText().length() < 1) {
                        error = true;
                        desc_error.setText("An item description must be provided");
                    }
                    if (regCost.getText().length() < 1 || !regCost.getText().matches("^(\\$)?[0-9]+(\\.[0-9][0-9]?)?$")) {
                        error = true;
                        regCost_error.setText("A valid regular cost in the form # or #.##, with or without $ must be provided");
                    }
                    if (premCost.getText().length() < 1 || !premCost.getText().matches("^(\\$)?[0-9]+(\\.[0-9][0-9]?)?$")) {
                        error = true;
                        premCost_error.setText("A valid premium cost in the form # or #.##, with or without $ must be provided");
                    }
                    if (qty.getText().length() < 1 || !qty.getText().matches("^[0-9]*$")) {
                        error = true;
                        qty_error.setText("A valid, whole-number quantity must be provided");
                    }
                    if (!error) {
                        Item item = new Item(
                            Main.supplier.id(),
                            name.getText(),
                            desc.getText(),
                            Float.parseFloat(regCost.getText()),
                            Float.parseFloat(premCost.getText()),
                            Integer.parseInt(qty.getText()));
                        if (item.create()) {
                            item_error.setText(name.getText() + " successfully added to your catalogue");
                            item_error.setTextFill(Color.color(0, 0.85f, 0));
                            name.setText("");
                            desc.setText("");
                            regCost.setText("");
                            premCost.setText("");
                            qty.setText("");
                        } else {
                            item_error.setText("An unexpected error occurred");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    item_error.setText("An unexpected error occurred");
                }
            }
        });
        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierStockMenu();
            }
        });
        Main.scene.setRoot(new VBox(
            header,
            new HBox(name_label, name, name_error),
            new HBox(desc_label, desc, desc_error),
            new HBox(regCost_label, regCost, regCost_error),
            new HBox(premCost_label, premCost, premCost_error),
            new HBox(qty_label, qty, qty_error),
            post,
            item_error,
            back));
    }
    public static void loadSupplierItemEdit(int id) {
        Item item = null;
        try {
            item = Item.getItem(id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (item == null) {
            loadSupplierMenu();
        } else {
            final Item curItem = item;
            Label header = new Label("Creating a New Item"),
                name_label = new Label("Item Name: " + item.getItemName()),
                desc_label = new Label("Item Description: "),
                regCost_label = new Label("Item Regular Cost: "),
                premCost_label = new Label("Item Premium Cost: "),
                qty_label = new Label("Item Stock: "),
                item_error = new Label(),
                desc_error = new Label(),
                regCost_error = new Label(),
                premCost_error = new Label(),
                qty_error = new Label();
            item_error.setTextFill(Color.color(0.85f, 0, 0));
            desc_error.setTextFill(Color.color(0.85f, 0, 0));
            regCost_error.setTextFill(Color.color(0.85f, 0, 0));
            premCost_error.setTextFill(Color.color(0.85f, 0, 0));
            qty_error.setTextFill(Color.color(0.85f, 0, 0));
            TextField desc = new TextField(curItem.getItemDesc()),
                regCost = new TextField(String.valueOf(curItem.getItemRegCost())),
                premCost = new TextField(String.valueOf(curItem.getItemPremCost())),
                qty = new TextField(String.valueOf(curItem.getItemQty()));
            Button post = new Button("Update Item");
            post.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        boolean error = false;
                        item_error.setText("");
                        item_error.setTextFill(Color.color(0.85f, 0, 0));
                        desc_error.setText("");
                        regCost_error.setText("");
                        premCost_error.setText("");
                        qty_error.setText("");
                        if (desc.getText().length() < 1) {
                            error = true;
                            desc_error.setText("An item description must be provided");
                        }
                        if (regCost.getText().length() < 1 || !regCost.getText().matches("^(\\$)?[0-9]+(\\.[0-9][0-9]?)?$")) {
                            error = true;
                            regCost_error.setText("A valid regular cost in the form # or #.##, with or without $ must be provided");
                        }
                        if (premCost.getText().length() < 1 || !premCost.getText().matches("^(\\$)?[0-9]+(\\.[0-9][0-9]?)?$")) {
                            error = true;
                            premCost_error.setText("A valid premium cost in the form # or #.##, with or without $ must be provided");
                        }
                        if (qty.getText().length() < 1 || !qty.getText().matches("^[0-9]*$")) {
                            error = true;
                            qty_error.setText("A valid, whole-number quantity must be provided");
                        }
                        if (!error) {
                            curItem.setItemDesc(desc.getText());
                            curItem.setItemRegCost(Float.parseFloat(regCost.getText()));
                            curItem.setItemPremCost(Float.parseFloat(premCost.getText()));
                            curItem.setItemQty(Integer.parseInt(qty.getText()));
                            if (curItem.update()) {
                                item_error.setText("Item successfully updated");
                                item_error.setTextFill(Color.color(0, 0.85f, 0));
                            } else {
                                item_error.setText("An unexpected error occurred");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        item_error.setText("An unexpected error occurred");
                    }
                }
            });
            Button back = new Button("Back");
            back.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadSupplierStockMenu();
                }
            });
            Main.scene.setRoot(new VBox(
                header,
                name_label,
                new HBox(desc_label, desc, desc_error),
                new HBox(regCost_label, regCost, regCost_error),
                new HBox(premCost_label, premCost, premCost_error),
                new HBox(qty_label, qty, qty_error),
                post,
                item_error,
                back));
        }
    }
    public static void loadSupplierOrders() {
        ArrayList<Item> items = new ArrayList<>();
        HashMap<Integer, Item> itemRef = new HashMap<>();
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        try {
            items = Item.getItems(Main.supplier.id());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        for (Item item : items) {
            itemRef.put(item.id(), item);
            ArrayList<OrderItem> itemOrders = new ArrayList<>();
            try {
                itemOrders = OrderItem.getOrderItems(item.id(), 'i');
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            orderItems.addAll(itemOrders);
        }
        Node[] oiMenus = new Node[orderItems.size()];
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            Item curItem = itemRef.get(orderItem.id());
            Label oiName = new Label(itemRef.get(orderItem.id()).getItemName()),
                oiQty = new Label("Available: " + (itemRef.get(orderItem.getItemID()).getItemQty() - itemRef.get(orderItem.getItemID()).getReservedQty())),
                oiReserved = new Label("Reserved: " + orderItem.getItemQty());
            Button fill = new Button("Fill");
            fill.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    curItem.setReservedQty(curItem.getReservedQty() + orderItem.getItemQty());
                    orderItem.setOrderItemStatus((byte)2);
                    try {
                        if (Order.syncOrderStatus(orderItem.getOrderItemStatus())) {
                            curItem.update();
                            orderItem.update();
                            loadSupplierOrders();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // do something here...
                    }
                }
            });
            Button ship = new Button("Ship");
            fill.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    curItem.setItemQty(curItem.getItemQty() - orderItem.getItemQty());
                    curItem.setReservedQty(curItem.getReservedQty() - orderItem.getItemQty());
                    orderItem.setOrderItemStatus((byte)3);
                    try {
                        if (Order.syncOrderStatus(orderItem.getOrderItemStatus())) {
                            curItem.update();
                            orderItem.update();
                            loadSupplierOrders();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        // do something here...
                    }
                }
            });
            oiMenus[i] = new VBox(oiName, new HBox(
                5f,
                orderItem.getOrderItemStatus() == 1 ? oiQty : oiReserved,
                orderItem.getOrderItemStatus() == 1 ? fill : ship));
        }
        Label header = new Label("Managing Pending Item Orders");
        Label notice = new Label(orderItems.size() < 1 ? "There are no pending orders for your catalogue." : "");
        Button back  = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadSupplierMenu();
            }
        });
        VBox allOIs = new VBox(oiMenus);
        ScrollPane oiPane = new ScrollPane(allOIs);
        oiPane.setPrefViewportHeight(Main.scene.getHeight());
        oiPane.setPrefViewportWidth(Math.min(allOIs.getWidth() * 2, Main.scene.getWidth()));
        oiPane.setPrefSize(Main.scene.getWidth(), Main.scene.getHeight());
        Main.scene.setRoot(new VBox(
            header,
            notice,
            oiPane,
            back
        ));
    }
}
