package controllerGui;

import javafx.fxml.FXML;
import java.util.Timer;
import java.util.TimerTask;
import Store.NavigationStoreController;
import common.ScreensNames;
import entity.UserEntity;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.AppConfig;

public class EKTPopupController {

	@FXML
	private Label headlineLabel;

	@FXML
	private ImageView loadingImage;

	private Timer timerSuccess;
	private Timer timerTimeLimit;
	/**
	 * Initialize screen
	 */
	public void initialize() {
		headlineLabel.setText("Waiting for EKT connection");
		timerSuccess = new Timer();
		timerTimeLimit = new Timer();
		setBackgroundTask();
		setTimeLimitBackgroundTask();
	}

	/**
	 * Sets a background task; Waiting to subscriber connection via EKT, to validate information
	 */
	private void setBackgroundTask() {

		timerSuccess.schedule(new TimerTask() {
			@Override
			public void run() {
				// simulate: after X seconds:
				// validate proccess

				// ------------- enter validation here!!! -------------

				Platform.runLater(() -> {
					// login success
					headlineLabel.setText("Login with EKT success!");
					Image image = new Image(getClass().getResourceAsStream("../styles/icons/EKTloading_success.gif"));
					loadingImage.setImage(image); // get information gif

					// wait for 2 seconds
					(new Timer()).schedule(new TimerTask() {
						@Override
						public void run() {
							// set current user
							Platform.runLater(() -> {
								NavigationStoreController.connectedUser = new UserEntity("", "", "Lidi", "ankava", "", "", "",
										"subscribed", null, false, false);
								NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.HomePage);
								((Stage) headlineLabel.getScene().getWindow()).close(); // close the popup window

							});
						}
					}, AppConfig.WAIT_AFTER_VALIDATION_SUCCESS);

				});
			}
		}, AppConfig.WAIT_BEFORE_SIMULATE_LOGIN);
	}
	
	private void setTimeLimitBackgroundTask() {

		timerTimeLimit.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					// login success
					headlineLabel.setText("Login time limit exceed, window will close in few seconds");
					loadingImage.setVisible(false);
					timerSuccess.cancel();  // cancel the other timer
					
					// wait for 5 seconds
					(new Timer()).schedule(new TimerTask() {
						@Override
						public void run() {
							Platform.runLater(() -> {
								((Stage) headlineLabel.getScene().getWindow()).close(); // close the popup window
							});
						}
					}, AppConfig.WAIT_AFTER_MSG);

				});
			}
		}, AppConfig.WAIT_BEFORE_MSG);
	}
}