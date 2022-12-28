package common;

import java.util.Timer;
import java.util.TimerTask;

import controllerGui.PopupController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CommonFunctions {
	private static Timer timerTimeLimit;

	/**
	 * check if the string is null or empty (spaces is empty)
	 * @param txt
	 * @return
	 */
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.trim().isEmpty());
	}

	/**
	 * general function to execute lambda <callback> after <num> time
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
			stage.setScene(new Scene(root));
			
			// set properties 
			stage.setTitle(type.toString());
			stage.setResizable(false);
			stage.setWidth(400);
			stage.setHeight(350);
			stage.show();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
