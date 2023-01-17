package controllerGui;

import common.CommonFunctions;
import interfaces.IScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
/**
 * Payment GUI controller, implements Screen interface
 * Simulate the external popup. In future it will replaced with real one
 * @author Lidor
 *
 */
public class PaymentPopupController  implements IScreen {

	@FXML
	private Label headlineLabel;

	@FXML
	private ImageView paymentLoading;

    @FXML
    private Label ccnumAndSum;
    
	/**
	 * Initialize screen
	 */
    @Override
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
	/**
	Method to set the text of the ccnumAndSum label with the provided credit card number and total sum.
	@param ccNumber the credit card number to be displayed on the label
	@param totalSum the total sum to be displayed on the label
	*/
	public void setLabel(String ccNumber, double totalSum) {
		ccnumAndSum.setText(String.format("Credit card number: %s, Total sum: %.2f₪", ccNumber, totalSum));
	}
}