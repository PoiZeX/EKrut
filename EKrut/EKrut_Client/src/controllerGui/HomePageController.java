/**
 * Sample Skeleton for 'HomePageBoundary.fxml' Controller Class
 */

package controllerGui;

import Store.NavigationStoreController;
import common.CommonData;
import common.CommonFunctions;
import common.RolesEnum;
import common.ScreensNames;
import controller.ItemsController;
import controller.OrderController;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utils.AppConfig;
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
	private Button mailBtn;

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
		mailBtn.setVisible(false);
		Image image = null;
		RolesEnum currentRole = currentUser.getRole_type();
		// switch case by role
		switch (currentRole) {
		case registered:
		case member:
			mailBtn.setVisible(true);
			setBtn(topBtn, "Create New Order", "View the catalog and create a new order", ScreensNames.ViewCatalog);
			setBtn(middleBtn, "Collect An Order", "Collect any orders that are ready", ScreensNames.ConfirmOnlineOrder); // need
																															// to
																															// change
																															// later
			setBtn(bottomBtn, "Confirm delivery", "Confirm recived delivery", ScreensNames.ConfirmOnlineOrder); // need
																												// to //
																												// later
			setBtn(mailBtn, "", "See messages", ScreensNames.PersonalMessages);
			image = new Image(getClass().getResourceAsStream("/styles/images/vending-machineNOBG.png"));
			ItemsController.requestItemsFromServer();
			if (currentRole == RolesEnum.member)
				OrderController.getActiveSalesFromDB();
			break;

		case CEO:
		case regionManager:
			// CEO has 3 buttons.
			setBtn(topBtn, "Approve Users", "View, manage and approve users", ScreensNames.UsersManagement);
			setBtn(middleBtn, "View Reports", "View the current monthly reports", ScreensNames.ReportSelection);
			setBtn(bottomBtn, "Supply Management", "Manage the available supply", ScreensNames.SupplyManagement);
			if (currentUser.getRole_type() == RolesEnum.regionManager)
				setBtn(mailBtn, "", "See messages", ScreensNames.PersonalMessages);
			image = new Image(getClass().getResourceAsStream("../styles/images/manager.png"));
			break;

		case customerServiceWorker:
			setBtn(topBtn, "Open New Account", "Open new registered / subscribed account",
					ScreensNames.RegistrationForm);
			break;

		case deliveryOperator:
			setBtn(topBtn, "Handle Delivery", "See details and change status of current delivery",
					ScreensNames.DeliveryManagement);
			break;

		case marketingWorker:
			setBtn(topBtn, "Activate New Sale", "Activate sale for region", ScreensNames.MarketingWorker); // just if
																											// the
																											// manager
																											// activated
																											// it
			// already
			break;

		case marketingManager:
			setBtn(topBtn, "Activate New Sale", "Activate global sale by pattern", ScreensNames.MarketingManager);
			image = new Image(getClass().getResourceAsStream("../styles/images/marketingManager.png"));
			break;

		case supplyWorker:
			setBtn(topBtn, "Update supply", "Update supplies for item(s)", ScreensNames.SupplyUpdate);
			break;

		default:
			// TODO: add label to inform the user he needs to contact customer support
			System.out.println("No role detected!"); // show the screen anyway because the login succeed
			break;
		}
		CommonData.initData(); // initialize all common data's from DB.

		welcomeLabel.setText("Welcome " + currentUser.fullName() + "!");
		String[] splitString = currentUser.getRole_type().toString()
				.split("(?<=[^A-Z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][^A-Z])");
		String role = "";
		splitString[0] = splitString[0].substring(0, 1).toUpperCase() + splitString[0].substring(1);
		if (splitString.length > 1)
			for (String s : splitString)
				role += s + " ";
		else
			role = splitString[0];

		roleLabel.setText(role);

		ImageView roleImg = new ImageView();
		if (image != null) {
			roleImg.setImage(image);
			roleImg.setFitHeight(350.0);
			roleImg.setFitWidth(350.0);
			rigthVbox.getChildren().addAll(roleImg);
		}

		// activate timeout
		NavigationStoreController.transition.play();

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
				switch (scName) {
				case ViewCatalog:
					if (AppConfig.SYSTEM_CONFIGURATION.equals("OL"))
						CommonFunctions.createShipmentPopup();
					else
						NavigationStoreController.getInstance().setCurrentScreen(scName);
					break;
				default:
					NavigationStoreController.getInstance().setCurrentScreen(scName);
					break;
				}
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
		OrderController.clearAll();
		NavigationStoreController.ExitHandler(false);
	}

}
