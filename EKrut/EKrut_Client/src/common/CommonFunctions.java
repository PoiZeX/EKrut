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
			stage.setWidth(400);
			stage.setHeight(350);
			
			// freeze current screen until got popup close
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			//stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void setLatestPopup(Scene sc) {
		latestScene = sc;	
	}
	public static Scene getLatestPopup() {
		return latestScene;
	}
	
	public static void createShipmentPopup()  {
		FXMLLoader loader;
		loader = new FXMLLoader(CommonFunctions.class.getResource("/boundary/ShipmentMethodPopupBoundary.fxml"));
		Parent root;
		try {
			root = loader.load();
			// get controller and use it
			Stage stage = new Stage();
			stage.setScene(new Scene(root));

			// set properties
			stage.setTitle("Shipment Method");
			stage.setResizable(false);
			
			// freeze current screen until got popup close
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();		
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
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

}
