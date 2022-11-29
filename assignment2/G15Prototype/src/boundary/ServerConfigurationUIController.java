package boundary;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerConfigurationUIController {

    @FXML
    private TextField txtIP;

    @FXML
    private TextField txtPort;

    @FXML
    private TextField txtDBName;

    @FXML
    private PasswordField txtDBPassword;

    @FXML
    private TextField txtDBUsername;

    @FXML
    private Button connectBtn;

    @FXML
    private Button disconnectBtn;

    @FXML
    void connectToDB(ActionEvent event) {

    }

    @FXML
    void disconnectFromDB(ActionEvent event) {

    }

	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/boundary/ServerConfigurationUI.fxml"));
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		primaryStage.setTitle("Client");
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}

}
