package OnlineShoppingSystem.UI;

import OnlineShoppingSystem.Data.*;
import Core.Buffer;
import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Application {
    // These are used in the Customer and Supplier classes
    static Stage stage;
    public static Scene scene;
    public static VBox menu_loading;
    public static CustomerAccount customer;
    public static SupplierAccount supplier;
    public static HashMap<Integer, OrderItem> cart;
    public static void loadWelcomeMenu() {
        customer = null;
        supplier = null;
        cart = null;
        Label title1 = new Label("Are you a customer or a supplier?");
        Button supplier_button = new Button("Supplier");
        supplier_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Supplier.loadSupplierLogin();
            }
        });
        Button customer_button = new Button("Customer");

        customer_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Customer.loadCustomerLogin();
            }
        });
        HBox header = new HBox (title1);
        HBox button_holder = new HBox(5f,customer_button, supplier_button);
        Button exit = new Button("Quit");
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        VBox menu_welcome = new VBox(5f,header, button_holder, exit);
        menu_welcome.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
        Main.scene.setRoot(menu_welcome);
    }
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        menu_loading = new VBox(new Label("Loading..."));
        scene = new Scene(menu_loading, 800, 600);
        primaryStage.setTitle("Online Shopping System");
        primaryStage.setScene(scene);
        primaryStage.show();
        loadWelcomeMenu();
    }
    public static void main(HashMap<String, String> config, Buffer buffer) {
        // Do stuff with config...
        OnlineShoppingSystem.Data.DataManager.initialize(config, buffer);
        launch();
    }
}