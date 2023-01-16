package controllerGui;

import java.util.Timer;
import java.util.TimerTask;

import Store.NavigationStoreController;
import common.CommonFunctions;
import enums.ScreensNamesEnum;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.AppConfig;

public class EKTPopupController extends LoginController {

	@FXML
	private Label headlineLabel;

	@FXML
	private ImageView loadingImage;

	protected static Timer timerSuccess;
	protected static Timer timerTimeLimit;

	// define username and password to login with
	private String[] usernamePasswordStub = new String[] { "mbrN", "123456" }; // mbr + [N/S/U]

	/**
	 * Initialize screen
	 */
	@Override
	public void initialize() {
		headlineLabel.setText("Waiting for EKT connection");
		timerSuccess = new Timer();
		timerTimeLimit = new Timer();
		setBackgroundTask();
		setTimeLimitBackgroundTask();
	}

	/**
	 * Sets a background task; Waiting to subscriber connection via EKT, to validate
	 * information (works like NFC)
	 */
	private void setBackgroundTask() {
		timerSuccess.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					// simulate: after <APPCONFIG> seconds:

					if (!loginProccess(usernamePasswordStub))
						cancelOperation();
					else {
						timerTimeLimit.cancel(); // time limit is irrelevant now

						// login success
						headlineLabel.setText("Login with EKT success!");
						Image image = new Image(
								getClass().getResourceAsStream("/styles/icons/EKTloading_success.gif"));
						loadingImage.setImage(image); // get information gif

						// wait for 2 seconds
						CommonFunctions.SleepFor(AppConfig.WAIT_AFTER_VALIDATION_SUCCESS, () -> {
							Platform.runLater(() -> {
								((Stage) headlineLabel.getScene().getWindow()).close(); // close the popup window
								// NavigationStoreController.getInstance().getPrimaryStage().show();
								NavigationStoreController.getInstance().setCurrentScreen(ScreensNamesEnum.HomePage);
							});
						});
					}
				});
			}

		}, AppConfig.WAIT_BEFORE_SIMULATE_LOGIN);

	}

	/**
	 * Sets a background task; Limit the time for waiting to connection from the
	 * user via EKT
	 */
	private void setTimeLimitBackgroundTask() {

		timerTimeLimit.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {

					// login success
					headlineLabel.setText("Login time limit exceed, window will close in few seconds");
					loadingImage.setVisible(false);
					timerSuccess.cancel(); // cancel the other timer

					// wait for 5 seconds
					(new Timer()).schedule(new TimerTask() {
						@Override
						public void run() {
							Platform.runLater(() -> {
								cancelOperation();
							});
						}
					}, AppConfig.WAIT_AFTER_MSG);

				});
			}
		}, AppConfig.WAIT_BEFORE_MSG);
	}

	/**
	 * When cancel the operation for any reason, also close the current popup and
	 * enable the regular login btn
	 */
	protected void cancelOperation() {
		timerSuccess.cancel();
		timerTimeLimit.cancel();
		LoginController s = (LoginController) NavigationStoreController.getInstance().getController();
		s.setLoginBtnDisable(false);
		((Stage) headlineLabel.getScene().getWindow()).close(); // close the popup window

		s.showErrorMsg();

	}

}