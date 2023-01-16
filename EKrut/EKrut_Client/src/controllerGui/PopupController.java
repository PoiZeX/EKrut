package controllerGui;

import enums.PopupTypeEnum;
import interfaces.IScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PopupController  implements IScreen {

	@FXML
	private Button NoCancelBtn;

	@FXML
	private ImageView iconImageView;

	@FXML
	private Label messageLabel;

	@FXML
	private Label titleLabel;

	@FXML
	private Button yesOkBtn;
    @FXML
    private ImageView onePlusOneImg;

	// define colors
	private static final String ERROR_COLOR = "#ff0303";
	private static final String SUCCESS_COLOR = "#078d0f";
	private static final String WARNING_COLOR = "#ffb22d";
	private static final String INFORMATION_COLOR = "#2701ff";
	private static final String DECISION_COLOR = INFORMATION_COLOR;

	/**
	 * initialize the screen
	 */
	@Override
	public void initialize() { 
		// set hidden as default
		yesOkBtn.setVisible(false);
		NoCancelBtn.setVisible(false);
		yesOkBtn.setText("OK");
		messageLabel.setWrapText(true);
	}

	/**
	 * Setup the popup with headline, message, buttons and image
	 * 
	 * @param type
	 * @param message
	 */
	public void setupPopup(PopupTypeEnum type, String message) {
		Image image = null;

		switch (type) {
		case Error:
			image = new Image(getClass().getResourceAsStream("/styles/icons/cancel.png"));
			titleLabel.setStyle("-fx-text-fill: " + ERROR_COLOR);
			break;

		case Success:
			image = new Image(getClass().getResourceAsStream("/styles/icons/success.png"));
			titleLabel.setStyle("-fx-text-fill: " + SUCCESS_COLOR);
			break;

		case Warning:
			image = new Image(getClass().getResourceAsStream("/styles/icons/warning.png"));
			titleLabel.setStyle("-fx-text-fill: " + WARNING_COLOR);
			break;

		case Information:
		case Simulation:
		case Sale:
			image = new Image(getClass().getResourceAsStream("/styles/icons/info.png"));
			titleLabel.setStyle("-fx-text-fill: " + INFORMATION_COLOR);
			if(type.equals(PopupTypeEnum.Sale))
				onePlusOneImg.setVisible(true);
			break;

		case Decision:
			image = new Image(getClass().getResourceAsStream("/styles/icons/decision.png"));
			titleLabel.setStyle("-fx-text-fill: " + DECISION_COLOR);
			yesOkBtn.setText("YES");
			NoCancelBtn.setText("NO");
			NoCancelBtn.setVisible(true); // uncomment this & set when 'decision'
			break;

		default:
			titleLabel.setVisible(false);
			messageLabel.setVisible(false);
			return;
		}
		titleLabel.setText(type.toString() + "!");
		messageLabel.setText(message);
		yesOkBtn.setVisible(true);
		if (image != null) {
			iconImageView.setImage(image);
			iconImageView.resize(52, 49);
		}

	}

	public static Boolean isOkPressed = null;
	/**

	This method is called when the 'Cancel' button is pressed on the window. It sets the 'isOkPressed' variable to false and closes the current window.
	@param event The event that triggers this method.
	*/
	@FXML
	void cancelAction(ActionEvent event) {
		isOkPressed = false;
		((Stage) yesOkBtn.getScene().getWindow()).close(); // close the popup window
	}
	/**

	Handles the action of pressing the OK button in the popup window.
	Sets the value of the isOkPressed variable to true and closes the popup window.
	@param event - the event of clicking the OK button
	*/
	@FXML
	void okAction(ActionEvent event) {
		isOkPressed = true;
		((Stage) yesOkBtn.getScene().getWindow()).close(); // close the popup window
	}
	/**

	This method returns the message label of the popup window
	@return messageLabel - The label containing the message to be displayed on the popup window
	*/
	public Label getMsgLabel() {
		return messageLabel;
	}
}
