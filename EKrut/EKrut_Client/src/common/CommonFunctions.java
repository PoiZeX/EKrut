package common;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import controllerGui.PopupController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CommonFunctions {
	private static Timer timerTimeLimit;
	private static Scene latestScene;

	/**
	 * check if the string is null or empty (spaces is empty)
	 * 
	 * @param txt
	 * @return
	 */
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.trim().isEmpty());
	}

	/**
	 * general function to execute lambda <callback> after <num> time
	 * 
	 * @param num
	 * @param callback
	 */
	public static void SleepFor(long num, Runnable callback) {
		timerTimeLimit = new Timer();
		timerTimeLimit.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					try {
						callback.run();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}

		}, num);
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
	/**

	This method takes in a String and splits it by uppercase letters, returning a new String with spaces between each of the split parts.

	@param str the input String to be split

	@return a new String with spaces between each of the split parts
	*/
	public static String splitByUpperCase(String str) {
		String goodName = "";

		String[] splitString = str.toString().split("(?<=[^A-Z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][^A-Z])");
		if (splitString.length > 1) {
			for (String s : splitString)
				goodName += s + " ";
		} else
			goodName = splitString[0];
		return goodName;
	}
	/**

	This method sets up a given GridPane to display a loading animation and message.
	It clears any existing children of the GridPane, sets its max size, clears any column or row constraints,
	adds a "Loading Catalog, Please Wait..." label and a gif image to the GridPane,
	and sets the alignment of these components to be centered both horizontally and vertically.
	@param gridPane the GridPane to be set up for the loading animation and message
	*/
	public static void setupLoadingGridPane(GridPane gridPane) {
		gridPane.getChildren().clear();
		gridPane.setMaxSize(150, 150);
		gridPane.getColumnConstraints().clear();
		gridPane.getRowConstraints().clear();
		Label loadingLabel = new Label("Loading Catalog, Please Wait...");
		ImageView img = new ImageView(new Image("/styles/images/loadingGif.gif"));
		loadingLabel.getStyleClass().add("LabelRoleTitle");
		img.setFitHeight(200);
		img.setFitWidth(200);
		gridPane.add(loadingLabel, 1, 1);
		gridPane.add(img, 1, 2);
		loadingLabel.setPadding(new Insets(10, 0, 0, 0));
		GridPane.setHalignment(loadingLabel, HPos.CENTER);
		GridPane.setHalignment(img, HPos.CENTER);
		GridPane.setValignment(loadingLabel, VPos.TOP);
	}
	/**

	This method takes in a String representing a month and returns a String of the corresponding numerical month (e.g. "january" -> "01").
	If the given month string is not a valid month, the method returns "Invalid month"
	@param month the input month string to be converted to numerical form
	@return a String of the numerical month representation, or "Invalid month" if the given month string is not valid
	*/
	public static String getNumericMonth(String month) {
		int index, numOfMonths = 12;
		String[] months = { "january", "february", "march", "april", "may", "june", "july", "august", "september",
				"october", "november", "december" };
		for (index = 1; index <= numOfMonths; index++) {
			if (month.toLowerCase().equals(months[index - 1]))
				if (index < 10)
					return ("0" + index);
				else if (index >= 10)
					return ("" + index);
		}
		return "Invalid month";
	}

}
