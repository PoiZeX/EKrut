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

    @FXML
    private Label ccnumAndSum;
    
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
		CommonFunctions.SleepFor(2500, () -> {
			CommonFunctions.SleepFor(1500, () -> {
				headlineLabel.setText("Payment success!");
				Platform.runLater(() -> {
					((Stage) headlineLabel.getScene().getWindow()).close(); // close the popup window
				});
			});
		});

	}

	public void setLabel(String ccNumber, double totalSum) {
		ccnumAndSum.setText(String.format("Credit card number: %s, Total sum: %.2f₪", ccNumber, totalSum));
	}
}