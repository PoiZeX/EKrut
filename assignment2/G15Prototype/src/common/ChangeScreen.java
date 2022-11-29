package common;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChangeScreen {
	  public void changeScreen(Stage primaryStage, String path) {
		  try {
			  Parent root = FXMLLoader.load(getClass().getResource(path));
		      Scene scene = new Scene(root);
		      primaryStage.setScene(scene);
		      primaryStage.show();
		    } catch (Exception e) {
		      e.printStackTrace();
		    } 
	  }
}
