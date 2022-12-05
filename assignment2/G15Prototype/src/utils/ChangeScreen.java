package utils;

import client.ClientController;
import common.MessageType;
import controller.HostClientUIController;
import controller.ServerConfigurationUIController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class ChangeScreen {
	public void changeScreen(Stage primaryStage, String path, ActionEvent event) {
		try {
			if (event != null)
				((Node) event.getSource()).getScene().getWindow().hide();
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					HostClientUIController.chat.acceptObj(MessageType.ClientDisconnect);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
