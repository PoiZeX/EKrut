/**
 * Sample Skeleton for 'HomePageBoundary.fxml' Controller Class
 */

package controllerGui;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Store.DataStore;
import Store.NavigationStoreController;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.RolesEnum;
import common.ScreensNamesEnum;
import common.TaskType;
import controller.ItemsController;
import controller.OrderController;
import entity.PersonalMessageEntity;
import entity.UserEntity;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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

////--------------------------------

	private TooltipSetter tooltip;
	private UserEntity currentUser = NavigationStoreController.connectedUser;

	//// --------------------------------
	@FXML
	private VBox vbox;

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

	@Override
	public void initialize() {
		// Set up last message view
		Message message = new Message(TaskType.RequestPersonalMessages, NavigationStoreController.connectedUser);
		PersonalMessagesController.setPersonalMessages(message);
		if (!PersonalMessagesController.getMsgList().isEmpty()) {
			// Get the last message as a whole
			String lastMessageLong = ((PersonalMessageEntity) (PersonalMessagesController.getMsgList().toArray()[PersonalMessagesController.getMsgList().toArray().length - 1])).getMessage();
			tooltip = new TooltipSetter(lastMessageLong); // Set a tooltip for the label inorder to view the whole message
			// Get the date of the last message
			String dateTime = ((PersonalMessageEntity) (PersonalMessagesController.getMsgList().toArray()[PersonalMessagesController.getMsgList().toArray().length - 1])).getDate();
			lastMessageDateTimeLabel.setText(dateTime); // Set date label
			// Get the first short part of the last message
			String lastMessageShort = ((PersonalMessageEntity) (PersonalMessagesController.getMsgList().toArray()[PersonalMessagesController.getMsgList().toArray().length - 1])).getMessage().split("\n")[0];
			lastMsgLabel.setText(lastMessageShort); // Set up short message label
			lastMsgLabel.setTooltip(tooltip.getTooltip()); // Set up the whole message tooltip
		}
		else {
			// No messages to display
			lastMsgLabel.setText("No Messages");
			lastMessageDateTimeLabel.setText("");
		}
			
		
//		// Set date and time in the menu
//		AnimationTimer timer = new AnimationTimer() {
//		    @Override
//		    public void handle(long now) {
//		    	lastMessageDateTimeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
//		    }
//		};
//		timer.start();
		
		// set hidden as default
		memberEmployeeGridBox.setVisible(false);
		topBtn.setVisible(false); 
		middleBtn.setVisible(false);
		bottomBtn.setVisible(false);
		mailBtn.setVisible(false);
		RolesEnum currentRole = currentUser.getRole_type(); // Get the role type of the current connected user
		displayUserMenuByRoleType(currentRole);
		DataStore.initData(); // initialize all common data's from DB.
		welcomeLabel.setText("Welcome " + currentUser.fullName() + "!"); // Set up welcome label
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
		// activate timeout
		NavigationStoreController.transition.play();

	}
	
	private void displayUserMenuByRoleType(RolesEnum currentRole) {
		Image image = null;
		// switch case by role
		switch (currentRole) {
			case registered:
			case member:
				 displayMemberMenu(image, currentRole); // Display the member menu
			case CEO: // CEO case will use the regionalManager case, i.e. no break;
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
					checkEmployeeMemberStatus(currentUser, currentRole);
				} else
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
				break;
			case customerServiceWorker:
				mailBtn.setVisible(false);
				bottomBtn.setVisible(false);
				middleBtn.setVisible(false);
				if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
					setBtn(topBtn, "Open New Account", "Open new registered / subscribed account",
							ScreensNamesEnum.RegistrationForm);
					image = new Image(getClass().getResourceAsStream("../styles/images/salesworker.png"));
					checkEmployeeMemberStatus(currentUser, currentRole);
				}
				else
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
				break;
			case deliveryOperator:
				mailBtn.setVisible(false);
				bottomBtn.setVisible(false);
				middleBtn.setVisible(false);
				if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
					setBtn(topBtn, "Handle Delivery", "See details and change status of current delivery",
							ScreensNamesEnum.DeliveryManagement);
					image = new Image(getClass().getResourceAsStream("../styles/images/deliveryguy.png"));
					checkEmployeeMemberStatus(currentUser, currentRole);
				} else
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
				break;
			case marketingWorker:
				mailBtn.setVisible(false);
				bottomBtn.setVisible(false);
				middleBtn.setVisible(false);
				if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
					setBtn(topBtn, "Activate New Sale", "Activate sale for region", ScreensNamesEnum.SalesManagement);
					image = new Image(getClass().getResourceAsStream("../styles/images/salesworker.png"));
					checkEmployeeMemberStatus(currentUser, currentRole);
				} else
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
				break;
			case marketingManager:
				mailBtn.setVisible(false);
				bottomBtn.setVisible(false);
				if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
					setBtn(topBtn, "Create New Sale", "Activate region sale by pattern",
							ScreensNamesEnum.CreateNewSale);
					setBtn(middleBtn, "Watch sales", "Watch sales by region", ScreensNamesEnum.SalesManagement);

					image = new Image(getClass().getResourceAsStream("../styles/images/marketingManager.png"));
					checkEmployeeMemberStatus(currentUser, currentRole);
				} else
					CommonFunctions.createPopup(PopupTypeEnum.Warning,
							"You have nothing to see here\nIf you want to order please register in customer service\nOr login in 'OL' configuration");
				break;
			case supplyWorker:
				mailBtn.setVisible(false);
				bottomBtn.setVisible(false);
				middleBtn.setVisible(false);
				if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
					setBtn(topBtn, "Update supply", "Update supplies for item(s)", ScreensNamesEnum.SupplyUpdate);
					image = new Image(getClass().getResourceAsStream("../styles/images/deliveryguy.png"));
					checkEmployeeMemberStatus(currentUser, currentRole);
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
		ImageView roleImg = new ImageView();
		if (image != null) {
			rigthVbox.getChildren().clear();
			roleImg.setImage(image);
			roleImg.setFitHeight(350.0);
			roleImg.setFitWidth(350.0);
		if (currentRole.equals(RolesEnum.supplyWorker)||currentRole.equals(RolesEnum.deliveryOperator))
			roleImg.setFitWidth(175.0);
			rigthVbox.getChildren().addAll(roleImg);
		}
	}
	
	private boolean checkEmployeeMemberStatus(UserEntity currentUser, RolesEnum currentRole) {
		Image image = null;
		if (!CommonFunctions.isNullOrEmpty(currentUser.getCc_num())) {
			memberEmployeeGridBox.setVisible(true);
			tooltip = new TooltipSetter("Display the employee menu");
			employeeMenuBtn.setDisable(true);
			employeeMenuBtn.setTooltip(tooltip.getTooltip());
			tooltip = new TooltipSetter("Display the member menu");
			memberMenuBtn.setDisable(false);
			memberMenuBtn.setTooltip(tooltip.getTooltip());
			memberMenuBtn.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	memberMenuBtn.setDisable(true);
	            	employeeMenuBtn.setDisable(false);
	            	displayMemberMenu(image, currentRole);
	            }
		    });
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
	
	private void displayMemberMenu(Image image, RolesEnum currentRole) {
		setBtn(topBtn, "Create New Order", "View the catalog and create a new order", ScreensNamesEnum.ViewCatalog);
		if (AppConfig.SYSTEM_CONFIGURATION.equals("EK"))
			setBtn(topBtn, "Collect An Order", "Collect any orders that are ready",
					ScreensNamesEnum.ConfirmOnlineOrder); // need
		else if (AppConfig.SYSTEM_CONFIGURATION.equals("OL"))
			setBtn(middleBtn, "Confirm delivery", "Confirm recived delivery", ScreensNamesEnum.ConfirmOnlineOrder);
		if (currentUser.getRole_type() == RolesEnum.member) {
			mailBtn.setVisible(true);
			setBtn(mailBtn, "", "See messages", ScreensNamesEnum.PersonalMessages);
		}

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
						CommonFunctions.createSelectPopup("/boundary/ShipmentMethodPopupBoundary.fxml","Shipment Method");
					else
						NavigationStoreController.getInstance().setCurrentScreen(scName);
					break;
				case SalesManagement:
					if(currentUser.getRole_type().equals(RolesEnum.marketingManager)) {
						CommonFunctions.createSelectPopup("/boundary/ChooseRegionPopUpBoundary.fxml","Select region");
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
