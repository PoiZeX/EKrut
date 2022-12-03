package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import client.ClientController;
import common.CommonFunctions;
public class HostClientUIController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField txtConnectToServerArea;

    @FXML
    private Button connectBtnclient;

    @FXML
    private Label gridLabel;

    @FXML
    private Label headLine;
	public static ClientController chat; // only one instance

  
    public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/boundary/HostClientUI.fxml"));
				
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/styles/HostClientUI.css").toExternalForm());
		primaryStage.setTitle("EKrut connect");
		primaryStage.setScene(scene);
		
		primaryStage.show();	 	   
	}
    
    @FXML
    void SendPort(ActionEvent event) {
    	String port = txtConnectToServerArea.getText();

    	// Validate
    	if(CommonFunctions.isNullOrEmpty(port)) { System.out.println("Please insert text"); return; }
    	try {
    		Integer.parseInt(port);
    	}
    	catch(Exception ex) {
    		System.out.println("Please insert digits only"); return;
    	}
    	
    	// Establish connection
    	chat = new ClientController("localhost", Integer.parseInt(port));
    	chat.accept("Try this");
    	
    	// Go to next screen (controller creates the screen)
    	
    }

}
