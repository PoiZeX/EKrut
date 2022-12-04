package utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChangeScreen {
	  public void changeScreen(Stage primaryStage, String path, ActionEvent event) {
		  try {
			  if (event != null)
				  ((Node)event.getSource()).getScene().getWindow().hide();
			  Parent root = FXMLLoader.load(getClass().getResource(path));
		      Scene scene = new Scene(root);
		      primaryStage.setScene(scene);
		      primaryStage.show();
		      
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
	  }
}
