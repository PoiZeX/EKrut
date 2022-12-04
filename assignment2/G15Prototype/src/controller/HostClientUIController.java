package controller;

import javafx.event.ActionEvent;
import utils.ChangeScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.nio.file.Path;

import javax.tools.StandardJavaFileManager.PathFactory;

import client.ChatClient;
import client.ClientController;
import common.CommonFunctions;
import utils.*;

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

	@FXML
    void SendPort(ActionEvent event) {
    	String port = txtConnectToServerArea.getText();
		FXMLLoader loader = new FXMLLoader();
		
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
    	chat.accept("Connection success");
    	chat.accept("Switching view from Configuration to Editor");
		chat.accept("Connect");  // change later to Message OBJECT 
//    		Thread.sleep(4000);
//    		chat.accept("Disconnect");
		
    	// Go to next screen (controller creates the screen)
		Stage primaryStage = new Stage();
		ChangeScreen screenChanger = new ChangeScreen();
		screenChanger.changeScreen(primaryStage, "/boundary/EditUsersBoundary.fxml", event);
//    	try {
//    		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
//    		Stage primaryStage = new Stage();
//    		Pane root = loader.load(getClass().getResource("/boundary/EditUsersBoundary.fxml"));
//    		//EditUsersController editUsersController = loader.getController();		
//    		
//    		Scene scene = new Scene(root);			
//    		//scene.getStylesheets().add(getClass().getResource("/gui/StudentForm.css").toExternalForm());
//    		primaryStage.setTitle("Edit EKrut Users");
//
//    		primaryStage.setScene(scene);		
//    		primaryStage.show();
//
//        	
//    	}
//    	catch(Exception ex) {
//    		ex.printStackTrace();
//    	}
    	
    }
	 
    public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/boundary/HostClientUI.fxml"));
				
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/styles/HostClientUI.css").toExternalForm());
		primaryStage.setTitle("EKrut connect");
		primaryStage.setScene(scene);
		
		primaryStage.show();	 	   
	}
    
   


}
