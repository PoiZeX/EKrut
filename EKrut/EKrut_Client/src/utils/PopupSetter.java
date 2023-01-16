package utils;

import java.io.IOException;

import common.CommonFunctions;
import controllerGui.PopupController;
import enums.PopupTypeEnum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupSetter {
	private static Scene latestScene;
	public PopupSetter() {
		// TODO Auto-generated constructor stub
	}
	/**

	Creates a pop-up window with the specified type and message.

	@param type the type of pop-up to create.

	@param message the message to display in the pop-up window.
	*/
	public static void createPopup(PopupTypeEnum type, String message) {
		FXMLLoader loader;

		try {
			// load the boundary
			loader = new FXMLLoader(CommonFunctions.class.getResource("/boundary/PopupBoundary.fxml"));
			Parent root = loader.load();

			// get controller and use it
			PopupController popupController = loader.getController();
			popupController.setupPopup(type, message);

			// create the stage & scene
			Stage stage = new Stage();
			Scene sc = new Scene(root);
			sc.setUserData(popupController);
			stage.setScene(sc);
			setLatestPopup(sc);
			// set properties
			stage.setTitle(type.toString());
			stage.setResizable(false);

			// freeze current screen until got popup close
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**

	Sets the latest pop-up scene.
	@param sc the scene to be set as the latest pop-up scene.
	*/
	private static void setLatestPopup(Scene sc) {
		latestScene = sc;
	}
	/**

	Returns the latest pop-up scene.
	@return the latest pop-up scene.
	*/
	public static Scene getLatestPopup() {
		return latestScene;
	}
	/**

	This method creates a select popup window using an FXML file specified by the given path and sets the title of the window to the given title.
	The method also sets the window's resizability to false and makes the current screen unresponsive until the popup is closed.
	@param path the path to the FXML file to use for the popup window
	@param title the title to be set for the popup window
	*/
	public static void createSelectPopup(String path, String title) {
		FXMLLoader loader;
		loader = new FXMLLoader(CommonFunctions.class.getResource(path));
		Parent root;
		try {
			root = loader.load();
			// get controller and use it
			Stage stage = new Stage();
			stage.setScene(new Scene(root));

			// set properties
			stage.setTitle(title);
			stage.setResizable(false);

			// freeze current screen until got popup close
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
