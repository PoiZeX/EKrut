/**
 * Sample Skeleton for 'HomePageBoundary.fxml' Controller Class
 */

package controllerGui;

import Store.DataStore;
import Store.NavigationStoreController;
import common.CommonFunctions;
import common.PopupTypeEnum;
import common.RolesEnum;
import common.ScreensNamesEnum;
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
			setBtn(topBtn, "Create New Order", "View the catalog and create a new order", ScreensNamesEnum.ViewCatalog);
			if (AppConfig.SYSTEM_CONFIGURATION.equals("EK"))
				setBtn(middleBtn, "Collect An Order", "Collect any orders that are ready",
						ScreensNamesEnum.ConfirmOnlineOrder); // need
			else if (AppConfig.SYSTEM_CONFIGURATION.equals("OL"))
				setBtn(middleBtn, "Confirm delivery", "Confirm recived delivery", ScreensNamesEnum.ConfirmOnlineOrder);

			mailBtn.setVisible(true);
			setBtn(mailBtn, "", "See messages", ScreensNamesEnum.PersonalMessages);
			image = new Image(getClass().getResourceAsStream("/styles/images/vending-machineNOBG.png"));
			ItemsController.requestItemsFromServer();
			if (currentRole == RolesEnum.member)
				OrderController.getActiveSalesFromDB();
			break;

		case CEO:
		case regionManager:
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				if (currentRole.equals(RolesEnum.regionManager)) {
					setBtn(topBtn, "Approve Users", "View, manage and approve users", ScreensNamesEnum.UsersManagement);
					setBtn(bottomBtn, "Supply Management", "Manage the available supply",
							ScreensNamesEnum.SupplyManagement);
				}
				setBtn(middleBtn, "View Reports", "View the current monthly reports", ScreensNamesEnum.ReportSelection);
				if (currentUser.getRole_type() == RolesEnum.regionManager)
					setBtn(mailBtn, "", "See messages", ScreensNamesEnum.PersonalMessages);
				image = new Image(getClass().getResourceAsStream("../styles/images/manager.png"));
			} else
				CommonFunctions.createPopup(PopupTypeEnum.Warning,
						"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");

			break;

		case customerServiceWorker:
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				setBtn(middleBtn, "Open New Account", "Open new registered / subscribed account",
						ScreensNamesEnum.RegistrationForm);
				image = new Image(getClass().getResourceAsStream("../styles/images/salesworker.png"));
			}

			else
				CommonFunctions.createPopup(PopupTypeEnum.Warning,
						"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
			break;

		case deliveryOperator:
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				setBtn(middleBtn, "Handle Delivery", "See details and change status of current delivery",
						ScreensNamesEnum.DeliveryManagement);
				image = new Image(getClass().getResourceAsStream("../styles/images/deliveryguy.png"));
			} else
				CommonFunctions.createPopup(PopupTypeEnum.Warning,
						"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");

			break;

		case marketingWorker:
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				setBtn(middleBtn, "Activate New Sale", "Activate sale for region", ScreensNamesEnum.SalesManagement);
				image = new Image(getClass().getResourceAsStream("../styles/images/salesworker.png"));
			} else
				CommonFunctions.createPopup(PopupTypeEnum.Warning,
						"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
			break;

		case marketingManager:
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {

				setBtn(topBtn, "Create New Sale", "Activate region sale by pattern",
						ScreensNamesEnum.CreateNewSale);
				setBtn(middleBtn, "Watch sales", "Watch sales by region", ScreensNamesEnum.SalesManagement);

				image = new Image(getClass().getResourceAsStream("../styles/images/marketingManager.png"));
			} else
				CommonFunctions.createPopup(PopupTypeEnum.Warning,
						"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");

			break;

		case supplyWorker:
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				setBtn(middleBtn, "Update supply", "Update supplies for item(s)", ScreensNamesEnum.SupplyUpdate);
				image = new Image(getClass().getResourceAsStream("../styles/images/deliveryguy.png"));
			} else
				CommonFunctions.createPopup(PopupTypeEnum.Warning,
						"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
			break;

		default:
			// TODO: add label to inform the user he needs to contact customer support
			// show the screen anyway because the login succeed
			CommonFunctions.createPopup(PopupTypeEnum.Warning,
					"No role detected!\nPlease Contact customer service to register\nPhone: 04-8109839\nEmail: service@ekrut.com");
			break;
		}
		DataStore.initData(); // initialize all common data's from DB.

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
		if (currentRole.equals(RolesEnum.supplyWorker))
			roleImg.setFitWidth(175.0);
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
	private <T extends Button> void setBtn(T btn, String btnText, String tooltiptext, ScreensNamesEnum scName) {
		btn.setText(btnText);
		tooltip = new TooltipSetter(tooltiptext);
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				switch (scName) {
				case ViewCatalog:
					if (AppConfig.SYSTEM_CONFIGURATION.equals("OL"))
						CommonFunctions.createSelectPopup("/boundary/ShipmentMethodPopupBoundary.fxml","Shipment Method");
					else
						NavigationStoreController.getInstance().setCurrentScreen(scName);
					break;
				case SalesManagement:
					if(currentUser.getRole_type().equals(RolesEnum.marketingManager))
						CommonFunctions.createSelectPopup("/boundary/ChooseRegionPopUpBoundary.fxml","Select region");
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
