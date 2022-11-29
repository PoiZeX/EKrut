package server;

import javafx.application.Application;
import javafx.stage.Stage;
import server.EKrutServer;
import java.util.Vector;
import boundary.ServerConfigurationUIController;
import common.ChangeScreen;


public class EKrutServerUI extends Application {
	  public static final int DEFAULT_PORT = 5555;
	  
	  static EKrutServer EKrutServer;
	  
	  public static void main(String[] args) throws Exception {
	    launch(args);
	  }
	  
	  public void start(Stage primaryStage) throws Exception {
	    ChangeScreen screen = new ChangeScreen();
	    screen.changeScreen(primaryStage, "/boundary/ServerConfigurationUI.fxml");
	  }
	  
	  public static void runServer(String port, String DBAddress, String username, String password) {

	  }
	  
	  public static void disconnect() {

	  }
}
