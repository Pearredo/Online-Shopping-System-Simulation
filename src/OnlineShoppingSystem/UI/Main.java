package OnlineShoppingSystem.UI;

import Core.Buffer;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.HashMap;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Label text1 = new Label("hi this is a UI page");
        Button button1 = new Button("Click me!");
        Button button2 = new Button("Click me too!");
        Label text2 = new Label("hi this is a different UI page");
        VBox vbox1 = new VBox(new Label("Header"), text1, button1, new Label("Footer"));
        VBox vbox2 = new VBox(new Label("Header"), text2, button2, new Label("Footer"));
        Scene scene = new Scene(vbox1, 300, 275);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(vbox2);
            }
        });
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene.setRoot(vbox1);
            }
        });
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(HashMap<String, String> config, Buffer buffer) {
        // Do stuff with config...
        OnlineShoppingSystem.Data.DataManager.initialize(config, buffer);
        launch();
    }
}
