package utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.Serializable;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;

/***
 * The class PopupWindow is a javafx application that demonstrates how to use a Popup control in javafx.
 * The application creates a popup window that shows a circle shape.
 * The user can show and hide the popup window by clicking on the 'Show' and 'Hide' button respectively.
 * @author User
 *
 */
public class PopupWindow extends Application implements Serializable{
/***
 * The main method, it launches the application.
 * @param args command line arguments.
 */
    public static void main(String[] args) {
        launch(args);
    }
/***
 * This method is called when the application is launched. It sets the title of the primary stage and creates a Popup object.
 * It also creates two buttons 'show' and 'hide' to show and hide the Popup respectively.
 * @param primaryStage the primary stage of the application.
 */
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Popup Example");
        final Popup popup = new Popup();
        popup.setX(300);
        popup.setY(200);
        popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));

        Button show = new Button("Show");
        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.show(primaryStage);
            }
        });

        Button hide = new Button("Hide");
        hide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                popup.hide();
            }
        });

        HBox layout = new HBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().addAll(show, hide);
        primaryStage.setScene(new Scene(layout));
        primaryStage.show();
    }

}