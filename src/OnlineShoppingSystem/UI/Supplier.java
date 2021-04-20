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
        Button stockManager = new Button("Manage Stock");
        stockManager.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierStockMenu();
            }
        });
        Button orderManager = new Button("Fill Open Orders");
        orderManager.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierOrders();
            }
        });
        Button logout = new Button("Logout");
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierLogin();
            }
        });
        Main.scene.setRoot(new VBox(welcome, stockManager, orderManager, logout));
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
                Main.resubmit = false;
                loadSupplierItemCreate();
            }
        });
        Label curItems = new Label("Current Items");
        Label notice = new Label();
        if (itemsLoaded && items.size() < 1) {
            notice.setText("You are not currently providing any items. Please add new items.");
        } else if (Main.resubmit) {
            notice.setText("Your items were unable to be loaded. Please refresh this page.");
            notice.setTextFill(Color.color(.85f, 0, 0));
        }
        Node[] itemOptions = new Node[items.size()];
        for (int i = 0; i < items.size(); i++) {
            int id = items.get(i).id();
            Label iLabel = new Label(items.get(i).getItemName());
            Button iButton = new Button("Edit");
            iButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.resubmit = false;
                    loadSupplierItemEdit(id);
                }
            });
            itemOptions[i] = new HBox(
                5f,
                iLabel,
                iButton
            );
        }
        Button back_button = new Button("Back");
        back_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierMenu();
            }
        });
        Main.scene.setRoot(new VBox(
            header,
            newItem,
            notice,
            curItems,
            new VBox(itemOptions),
            back_button));
    }
    public static void loadSupplierItemCreate() {
        Label header = new Label("Creating a New Item"),
            name_label = new Label("Item Name: "),
            desc_label = new Label("Item Description: "),
            regCost_label = new Label("Item Regular Cost: "),
            premCost_label = new Label("Item Premium Cost: "),
            qty_label = new Label("Item Quantity: ");
        TextField name = new TextField(),
            desc = new TextField(),
            regCost = new TextField(),
            premCost = new TextField(),
            qty = new TextField();
        Button post = new Button("Add Item");
        post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Item item = new Item(
                    Main.supplier.id(),
                    name.getText(),
                    desc.getText(),
                    Float.parseFloat(regCost.getText()),
                    Float.parseFloat(premCost.getText()),
                    Integer.parseInt(qty.getText()));
                Main.resubmit = true;
                try {
                    Main.resubmit = !item.create();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                loadSupplierStockMenu();
            }
        });
        Button back = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierStockMenu();
            }
        });
        Main.scene.setRoot(new VBox(
            header,
            new HBox(5f, name_label, name),
            new HBox(5f, desc_label, desc),
            new HBox(5f, regCost_label, regCost),
            new HBox(5f, premCost_label, premCost),
            new HBox(5f, qty_label, qty),
            post,
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
            Main.resubmit = true;
            loadSupplierMenu();
        } else {
            final Item curItem = item;
            Label header = new Label("Creating a New Item"),
                name_label = new Label("Item Name: " + item.getItemName()),
                desc_label = new Label("Item Description: "),
                regCost_label = new Label("Item Regular Cost: "),
                premCost_label = new Label("Item Premium Cost: "),
                qty_label = new Label("Item Quantity: ");
            TextField desc = new TextField(curItem.getItemDesc()),
                regCost = new TextField(String.valueOf(curItem.getItemRegCost())),
                premCost = new TextField(String.valueOf(curItem.getItemPremCost())),
                qty = new TextField(String.valueOf(curItem.getItemQty()));
            Button post = new Button("Update Item");
            post.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    curItem.setItemDesc(desc.getText());
                    curItem.setItemRegCost(Float.parseFloat(regCost.getText()));
                    curItem.setItemPremCost(Float.parseFloat(premCost.getText()));
                    curItem.setItemQty(Integer.parseInt(qty.getText()));
                    Main.resubmit = true;
                    try {
                        Main.resubmit = !curItem.update();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    loadSupplierStockMenu();
                }
            });
            Button back = new Button("Back");
            back.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    Main.resubmit = false;
                    loadSupplierStockMenu();
                }
            });
            Main.scene.setRoot(new VBox(
                header,
                name_label,
                new HBox(5f, desc_label, desc),
                new HBox(5f, regCost_label, regCost),
                new HBox(5f, premCost_label, premCost),
                new HBox(5f, qty_label, qty),
                post,
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
                oiQty = new Label("Stock: " + itemRef.get(orderItem.id()).getItemQty()),
                oiReserved = new Label("Reserved: " + itemRef.get(orderItem.id()).getReservedQty());
            Button fill = new Button("Fill and Ship");
            fill.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    curItem.setReservedQty(curItem.getItemQty() - orderItem.getItemQty());
                    curItem.setReservedQty(curItem.getReservedQty() - orderItem.getItemQty());
                    try {
                        Main.resubmit = !curItem.update();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Main.resubmit = true;
                    }
                    loadSupplierOrders();
                }
            });
            oiMenus[i] = new VBox(oiName, new HBox(5f, new VBox(oiQty, oiReserved), fill));
        }
        Label header = new Label("Managing Pending Item Orders");
        Label notice = new Label();
        if (orderItems.size() < 1) {
            notice.setText("There are no pending orders for your inventory.");
        } else if (Main.resubmit) {
            notice.setText("An error has occurred while allocating stock. Please try again.");
            notice.setTextFill(Color.color(.85f, 0, 0));
        }
        Button back  = new Button("Back");
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.resubmit = false;
                loadSupplierMenu();
            }
        });
        Main.scene.setRoot(new VBox(
            header,
            notice,
            new VBox(oiMenus),
            back
        ));
    }
}
