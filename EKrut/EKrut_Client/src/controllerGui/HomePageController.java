/**
 * Sample Skeleton for 'HomePageBoundary.fxml' Controller Class
 */

package controllerGui;

import Store.NavigationStoreController;
import common.ScreensNames;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utils.TooltipSetter;

public class HomePageController {

////--------------------------------

	private TooltipSetter tooltip;
	private UserEntity currentUser = NavigationStoreController.connectedUser;

	//// --------------------------------
	@FXML
	private VBox vbox;

	@FXML
	private Button bottomBtn;

	@FXML
	private Button logOutBtn;

	@FXML
	private Button middleBtn;

	@FXML
	private Button topBtn;

	@FXML
	private Label welcomeLabel;

	@FXML
	private Label roleLabel;

	@FXML
	private VBox rigthVbox;

	public void initialize() {
		// set hidden as default
		topBtn.setVisible(false);
		middleBtn.setVisible(false);
		bottomBtn.setVisible(false);
		Image image = null;
		// switch case by role
		switch (currentUser.getRole_type()) {
		case "registered":
		case "subscribed":
			setBtn(topBtn, "Create New Order", "View the catalog and create a new order", ScreensNames.ViewCatalog);
			setBtn(middleBtn, "Collect An Order", "Collect any orders that are ready", null); // need to change later
			image = new Image(getClass().getResourceAsStream("/styles/images/vending-machineNOBG.png"));
			break;

		case "ceo":
		case "regionManager":
			// CEO has 3 buttons.
			setBtn(topBtn, "Approve Users", "View, manage and approve users", ScreensNames.UsersManagement);
			setBtn(middleBtn, "View Reports", "View the current monthly reports", ScreensNames.ReportSelection);
			setBtn(bottomBtn, "Supply Management", "Manage the available supply", ScreensNames.SupplyReport);
			image = new Image(getClass().getResourceAsStream("../styles/images/manager.png"));
			break;

		default:
			System.out.println("No role detected!"); // show the screen anyway because the login succeed
			break;
		}

		welcomeLabel.setText("Welcome " + currentUser.fullName() + "!");
		roleLabel.setText(currentUser.getRole_type());

		ImageView roleImg = new ImageView();
		if (image != null) {
			roleImg.setImage(image);
			roleImg.setFitHeight(350.0);
			roleImg.setFitWidth(350.0);
			rigthVbox.getChildren().addAll(roleImg);
		}

		// updateButtonsSize();
	}

	/**
	 * Generic method to handle buttons setup according to button text, tooltip and
	 * the screen to go to
	 * 
	 * @param <T>
	 * @param btn
	 * @param btnText
	 * @param tooltiptext
	 * @param scName
	 */
	private <T extends Button> void setBtn(T btn, String btnText, String tooltiptext, ScreensNames scName) {
		btn.setText(btnText);
		tooltip = new TooltipSetter(tooltiptext);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (scName != null)
					NavigationStoreController.getInstance().setCurrentScreen(scName);
			}
		});
		btn.setTooltip(tooltip.getTooltip());
		btn.setVisible(true);
	}

	/**
	 * Log out from the system. sets the current user to null and changes the view
	 * 
	 * @param event
	 */
	@FXML
	private void logOutAction(ActionEvent event) {
		currentUser = null;
		NavigationStoreController.getInstance().refreshStage(ScreensNames.Login);

	}

}
