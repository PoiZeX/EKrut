package controllerGui;

import javafx.fxml.FXML;
import common.CommonFunctions;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PaymentPopupController {

	@FXML
	private Label headlineLabel;

	@FXML
	private ImageView paymentLoading;

	/**
	 * Initialize screen
	 */
	public void initialize() {
		headlineLabel.setText("External payment");
		simulatePayment();
	}

	/**
	 * Simulate the external payment process
	 */
	private void simulatePayment() {
		while (threadToWake == null)
			;
		CommonFunctions.SleepFor(2500, () -> {
			CommonFunctions.SleepFor(1500, () -> {
				headlineLabel.setText("Payment success!");
				Platform.runLater(() -> {
					((Stage) headlineLabel.getScene().getWindow()).close(); // close the popup window
					threadToWake.interrupt();
					threadToWake.notify();
				});
			});
		});
	}

	private static Thread threadToWake = null;

	public void setThread(Thread thread) {
		threadToWake = thread;
	}
}