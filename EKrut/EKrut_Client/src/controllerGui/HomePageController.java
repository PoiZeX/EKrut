/**
 * Sample Skeleton for 'HomePageBoundary.fxml' Controller Class
 */

package controllerGui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Store.DataStore;
import Store.NavigationStoreController;
import common.CommonFunctions;
import common.IScreen;
import common.Message;
import common.PopupTypeEnum;
import common.RolesEnum;
import common.ScreensNamesEnum;
import common.TaskType;
import controller.ItemsController;
import controller.OrderController;
import entity.PersonalMessageEntity;
import entity.UserEntity;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import utils.AppConfig;
import utils.TooltipSetter;

public class HomePageController implements IScreen {
	private ArrayList<RolesEnum> rolesViableForMessages;
	private TooltipSetter tooltip;
	private UserEntity currentUser = NavigationStoreController.connectedUser;
	private Image image;

	@FXML
	private VBox vbox;

    @FXML
    private Label homePageTopLabel;

	@FXML
	private Label lastMessageDateTimeLabel;

	@FXML
	private GridPane memberEmployeeGridBox;

	@FXML
	private Button memberMenuBtn;

	@FXML
	private Button employeeMenuBtn;

	@FXML
	private Button bottomBtn;

	@FXML
	private Button logOutBtn;

	@FXML
	private Button middleBtn;

	@FXML
	private Label lastMsgLabel;

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
	
	/**
	
	This method is used to initialize the main menu screen for the user.
	It sets the visibility of the buttons and labels according to the role of the current connected user.
	It also sets up the view for the personal messages and initializes the common data from the database.
	It also activate the timeout.
	*/
	@Override
	public void initialize() { 
		rolesViableForMessages = new ArrayList<>();
		rolesViableForMessages.add(RolesEnum.CEO);
		rolesViableForMessages.add(RolesEnum.regionManager);
		rolesViableForMessages.add(RolesEnum.registered);
		rolesViableForMessages.add(RolesEnum.member);
		
		// Get the role type of the current connected user
		RolesEnum currentRole = currentUser.getRole_type();

		// set hidden as default
		initializeBtnAndLbl();

		// display the main menu
		displayUserMenuByRoleType(currentRole);

		// Set up last message view
		initializePersonalMessages(currentRole);

		// initialize all common data's from DB.
		DataStore.initData();

		// activate timeout
		NavigationStoreController.transition.play();
	}

	/**
	 * Initialize the buttons visibility default to false. Initialize the header
	 * also
	 */
	private void initializeBtnAndLbl() {
		memberEmployeeGridBox.setVisible(false);
		toggleBtnsVisible(new Button[] { mailBtn, topBtn, middleBtn, bottomBtn }, false);
		// Set up welcome label
		welcomeLabel.setText("Welcome " + currentUser.fullName() + "!");
		// Build the role label string
		String[] splitString = currentUser.getRole_type().toString()
				.split("(?<=[^A-Z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][^A-Z])");
		String role = "";
		splitString[0] = splitString[0].substring(0, 1).toUpperCase() + splitString[0].substring(1);
		if (splitString.length > 1)
			for (String s : splitString)
				role += s + " ";
		else
			role = splitString[0];
		roleLabel.setText(role); // Set up the role label
	}

	/**
	 * Initialize the personal messages if needed
	 */
	private void initializePersonalMessages(RolesEnum currentRole) {
		if (rolesViableForMessages.contains(currentRole)) {
			PersonalMessagesController.setPersonalMessages(
					new Message(TaskType.RequestPersonalMessages, NavigationStoreController.connectedUser));
			if (!PersonalMessagesController.getMsgList().isEmpty()) {
				// Get the last message as a whole
				String lastMessageLong = ((PersonalMessageEntity) (PersonalMessagesController.getMsgList()
						.toArray()[PersonalMessagesController.getMsgList().toArray().length - 1])).getMessage();
				tooltip = new TooltipSetter(lastMessageLong); // Set a tooltip for the label inorder to view the whole
																// message
				// Get the date of the last message
				String dateTime = ((PersonalMessageEntity) (PersonalMessagesController.getMsgList()
						.toArray()[PersonalMessagesController.getMsgList().toArray().length - 1])).getDate();
				lastMessageDateTimeLabel.setText(dateTime); // Set date label
				// Get the first short part of the last message
				String lastMessageShort = ((PersonalMessageEntity) (PersonalMessagesController.getMsgList()
						.toArray()[PersonalMessagesController.getMsgList().toArray().length - 1])).getMessage()
						.split("\n")[0];
				lastMsgLabel.setText(lastMessageShort); // Set up short message label
				lastMsgLabel.setTooltip(tooltip.getTooltip()); // Set up the whole message tooltip
			}  
			else {
				// No messages to display
				lastMsgLabel.setText("No Messages");
				lastMessageDateTimeLabel.setText("");
			}
		}
		else {
			// If the user doesn't have the option for messages, nicely display the current time and date
			AnimationTimer timer = new AnimationTimer() {
			    @Override
			    public void handle(long now) {
			    	lastMessageDateTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
			    }
			};
			timer.start();
			lastMsgLabel.setText("");
			homePageTopLabel.setText("Welcome back!");
		}

	}

	/**
	 * Manager for menu displaying
	 * 
	 * @param currentRole
	 */
	private void displayUserMenuByRoleType(RolesEnum currentRole) {
		// switch case by role
		switch (currentRole) {
		case registered:
		case member:
			displayMemberMenu(currentRole);
			break;
		case CEO: // CEO case will use the regionalManager case, i.e. no break;
		case regionManager:
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				// if OL and Region manager -> show the
				if (currentRole.equals(RolesEnum.regionManager)) {
					setBtn(topBtn, "Approve Users", "View, Manage and Approve users", ScreensNamesEnum.UsersManagement);
					setBtn(bottomBtn, "Supply Management", "Manage the available supply",
							ScreensNamesEnum.SupplyManagement);
				}
				setBtn(middleBtn, "View Reports", "View the current monthly reports", ScreensNamesEnum.ReportSelection);
				if (currentUser.getRole_type() == RolesEnum.regionManager)
					setBtn(mailBtn, "", "See messages", ScreensNamesEnum.PersonalMessages);
				image = new Image(getClass().getResourceAsStream("/styles/images/manager.png"));
				checkEmployeeMemberStatus(currentUser, currentRole);
			} else {
				// is on 'EK'
				if (!checkEmployeeMemberStatus(currentUser, currentRole))
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\n"
									+ "Or login in 'OL' configuration");
			}
			break;

		case customerServiceWorker:
			toggleBtnsVisible(new Button[] { mailBtn, bottomBtn, middleBtn }, false);
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				setBtn(topBtn, "Open New Account", "Open new registered / subscribed account",
						ScreensNamesEnum.RegistrationForm);
				image = new Image(getClass().getResourceAsStream("/styles/images/salesworker.png"));
				checkEmployeeMemberStatus(currentUser, currentRole);
			} else {
				// is on 'EK'
				if (!checkEmployeeMemberStatus(currentUser, currentRole))
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\n"
									+ "Or login in 'OL' configuration");
			}
			break;

		case deliveryOperator:
			toggleBtnsVisible(new Button[] { mailBtn, bottomBtn, middleBtn }, false);
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				checkEmployeeMemberStatus(currentUser, currentRole);
				setBtn(topBtn, "Handle Delivery", "See details and change status of current delivery",
						ScreensNamesEnum.DeliveryManagement);
				image = new Image(getClass().getResourceAsStream("/styles/images/deliveryguy.png"));
			} else {
				// is on 'EK'
				if (!checkEmployeeMemberStatus(currentUser, currentRole))
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\n"
									+ "Or login in 'OL' configuration");
			}
			break;

		case marketingWorker:
			toggleBtnsVisible(new Button[] { mailBtn, bottomBtn, middleBtn }, false);
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				checkEmployeeMemberStatus(currentUser, currentRole);
				setBtn(topBtn, "Activate New Sale", "Activate sale for region", ScreensNamesEnum.SalesManagement);
				image = new Image(getClass().getResourceAsStream("/styles/images/salesworker.png"));
			} else {
				// is on 'EK'
				if (!checkEmployeeMemberStatus(currentUser, currentRole))
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\n"
									+ "Or login in 'OL' configuration");
			}
			break;
		case marketingManager:
			toggleBtnsVisible(new Button[] { mailBtn, bottomBtn }, false);
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				image = new Image(getClass().getResourceAsStream("/styles/images/marketingManager.png"));
				checkEmployeeMemberStatus(currentUser, currentRole);
				setBtn(topBtn, "Create New Sale", "Activate region sale by pattern", ScreensNamesEnum.CreateNewSale);
				setBtn(middleBtn, "Watch sales", "Watch sales by region", ScreensNamesEnum.SalesManagement);
			} 
			else {
				// is on 'EK'
				if (!checkEmployeeMemberStatus(currentUser, currentRole))
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here!\nIf you want to order please register in Customer Service\n"
									+ "Or login in 'OL' configuration");
			}
			break;

		case supplyWorker:
			toggleBtnsVisible(new Button[] { mailBtn, bottomBtn, middleBtn }, false);
			if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
				checkEmployeeMemberStatus(currentUser, currentRole);
				setBtn(topBtn, "Update supply", "Update supplies for item(s)", ScreensNamesEnum.SupplyUpdate);
				image = new Image(getClass().getResourceAsStream("/styles/images/deliveryguy.png"));} 
			else {
				// is on 'EK'
				if (!checkEmployeeMemberStatus(currentUser, currentRole))
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here!\nIf you want to order please register in Customer Service\n"
									+ "Or login in 'OL' Configuration");
			}
			break;

		default:
			CommonFunctions.createPopup(PopupTypeEnum.Warning,
					"No role detected!\nPlease contact Customer Service to register\nPhone: 04-8109839\nEmail: service@ekrut.com");
			break;
		}
		ImageView roleImg = new ImageView();
		if (image != null) {
			rigthVbox.getChildren().clear();
			roleImg.setImage(image);
			roleImg.setFitHeight(350.0);
			roleImg.setFitWidth(350.0);
			if (currentRole.equals(RolesEnum.supplyWorker) || currentRole.equals(RolesEnum.deliveryOperator))
				roleImg.setFitWidth(175.0);
			rigthVbox.getChildren().addAll(roleImg);
		}
	}

	/**
	 * Change all buttons visibility at once
	 * 
	 * @param btns
	 * @param setToVisible
	 */
	private void toggleBtnsVisible(Button[] btns, boolean setToVisible) {
		for (Button btn : btns)
			btn.setVisible(setToVisible);
	}

	/**
	 * Manager for employee which is a member
	 * 
	 * @param currentUser
	 * @param currentRole
	 * @return
	 */
	private boolean checkEmployeeMemberStatus(UserEntity currentUser, RolesEnum currentRole) {
		if (!CommonFunctions.isNullOrEmpty(currentUser.getCc_num())) {
			memberEmployeeGridBox.setVisible(true);

			employeeMenuBtn.setTooltip(new TooltipSetter("Display the employee menu").getTooltip());
			memberMenuBtn.setTooltip(new TooltipSetter("Display the member menu").getTooltip());

			employeeMenuBtn.setDisable(true);
			memberMenuBtn.setDisable(false);

			// member action setter
			memberMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					memberMenuBtn.setDisable(true);
					employeeMenuBtn.setDisable(false);
					displayMemberMenu(currentRole);
				}
			});

			// employee action setter
			employeeMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					memberMenuBtn.setDisable(false);
					employeeMenuBtn.setDisable(true);
					displayUserMenuByRoleType(currentRole);
				}
			});
			return true;
		}
		return false;
	}

	/**
	 * Member menu manager
	 * 
	 * @param image
	 * @param currentRole
	 */
	private void displayMemberMenu(RolesEnum currentRole) {
		setBtn(topBtn, "Create New Order", "View the catalog and create a new order", ScreensNamesEnum.ViewCatalog);

		if (AppConfig.SYSTEM_CONFIGURATION.equals("EK"))
			setBtn(topBtn, "Collect An Order", "Collect any orders that are ready",
					ScreensNamesEnum.ConfirmOnlineOrder); // need
		else if (AppConfig.SYSTEM_CONFIGURATION.equals("OL"))
			setBtn(middleBtn, "Confirm delivery", "Confirm recived delivery", ScreensNamesEnum.ConfirmOnlineOrder);
		
		setBtn(mailBtn, "", "See messages", ScreensNamesEnum.PersonalMessages);
		

		image = new Image(getClass().getResourceAsStream("/styles/images/vending-machineNOBG.png"));
		ItemsController.requestItemsFromServer();
		OrderController.getActiveSalesFromDB();
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
						CommonFunctions.createSelectPopup("/boundary/ShipmentMethodPopupBoundary.fxml",
								"Shipment Method");
					else
						NavigationStoreController.getInstance().setCurrentScreen(scName);
					break;
				case SalesManagement:
					if (currentUser.getRole_type().equals(RolesEnum.marketingManager)) {
						CommonFunctions.createSelectPopup("/boundary/ChooseRegionPopUpBoundary.fxml", "Select region");
						break;
					}

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
		logOutBtn.setMouseTransparent(true);
	}

}
