package controllerGui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.Map;

import java.util.concurrent.Semaphore;
import java.util.regex.Pattern;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.RolesEnum;
import common.ScreensNames;
import common.TaskType;
import controller.OrderController;
import controller.SMSMailHandlerController;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.OrderEntity;
import entity.PickupEntity;
import entity.UserEntity;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.AppConfig;
import utils.TooltipSetter;

public class ReviewOrderController {
	private static ClientController chat = HostClientController.chat;
	private TooltipSetter tooltip;

	@FXML
	private Label ReviewOrderTitleLbl;

	@FXML
	private TextField aptTxtField;

	@FXML
	private TextField cityTxtField;

	@FXML
	private TextField streetTxtField;

	@FXML
	private GridPane addressDetalisGridPane;

	@FXML
	private Label contactDetailsLbl;

	@FXML
	private GridPane contactDetalisGridPane;

	@FXML
	private Label deliveryPriceLbl;

	@FXML
	private TextField firstNameTxtField;

	@FXML
	private TextField lastNameTxtField;

	@FXML
	private Button paymentBtn;

	@FXML
	private TextField phoneNumTxtField;

	@FXML
	private ScrollPane reviewOrderScrollPane;

	@FXML
	private GridPane rightGridPane;

	@FXML
	private Label totalSumLbl;

	@FXML
	private Label totulDiscountSumLbl;

	@FXML
	private Label totulProductsSumLbl;

	@FXML
	private GridPane productsGrid;
	private static Object data;
	private static boolean isDataRecived = false;

	private static LinkedHashMap<ItemInMachineEntity, Integer> cart;
	private static OrderEntity orderEntity = OrderController.getCurrentOrder();
	private UserEntity user = NavigationStoreController.connectedUser;
	private StringBuilder address = new StringBuilder();

	public void initialize() {
		try {

			// cancelOrderBtn --<<< add ???

			// set current cart (replace with order entity?)
			cart = OrderController.getCart();

			// build graphical side
			buildReviewOrder();

			// initialize fields
			setTextFields();

			// apply discounts if exists
			checkAndApplyDiscounts();

			// check if OL/EK (for delivery)
			rightGridHandle();

			/*
			 * TODO: 1. Future payment for member 2. check if item is under minimum 3.
			 * Cancel order button
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Set text / labels according to relevant data
	 */
	private void setTextFields() {
		totulProductsSumLbl.setText(String.valueOf(OrderController.getTotalPrice()) + "₪");
		totulDiscountSumLbl.setText(String.format("%.2f₪", OrderController.getTotalDiscounts()));
		totalSumLbl.setText(String.format("%.2f₪", OrderController.getPriceAfterDiscounts()));
		firstNameTxtField.setText(user.getFirst_name());
		firstNameTxtField.setDisable(true);
		lastNameTxtField.setText(user.getLast_name());
		lastNameTxtField.setDisable(true);
		phoneNumTxtField.setText(user.getPhone_number());
		phoneNumTxtField.setDisable(true);
	}

	/**
	 * Check if there are any sales and handle this case
	 * 
	 * @throws Exception
	 */
	private void checkAndApplyDiscounts() throws Exception {
		StringBuilder sb = new StringBuilder("You got some discounts:\n");

		if (OrderController.getTotalDiscounts() != 0) {
			String salesDiscounts = OrderController.getActiveSalesTypeAsString();
			for (String discount : salesDiscounts.split(" ")) {
				sb.append("* " + discount + "\n");
			}
		}

		if (NavigationStoreController.connectedUser.getRole_type().equals(RolesEnum.member)
				&& !OrderController.isFirstPurchaseDiscountApplied) {
			waitOn(new Message(TaskType.isMemberFirstPurchase, NavigationStoreController.connectedUser));
			if ((boolean) data) {
				// give discount just one time
				OrderController.addMemberFirstPurchaseDiscount();
				sb.append("* 20% for first purchase as a member!\n");
				// show a relevant label / tooltip

			}

			// show special label or something
			totulDiscountSumLbl.setText(String.format("%.2f₪", OrderController.getTotalDiscounts()));
			totalSumLbl.setText(String.format("%.2f₪", OrderController.getPriceAfterDiscounts()));
		}
		if (!totulDiscountSumLbl.getText().equals("0₪")) {
			// set tooltip text and apply to label (will be with an image)
			totulDiscountSumLbl.setTooltip((new TooltipSetter(sb.toString()).getTooltip()));
		}

	}

	/**
	 * Checks the app. configuration and handle the case it's 'EK'
	 */
	private void rightGridHandle() {
		if (AppConfig.SYSTEM_CONFIGURATION.equals("EK")) {
			rightGridPane.setVisible(false);
//		rightGridPane.getChildren().clear();
//		Image image = new Image();
//		ImageView imageView = new ImageView(image);
//		rightGridPane.add(imageView, 0, 2);
//		GridPane.setColumnSpan(imageView, 2);
//		GridPane.setRowSpan(imageView, 2);
		}

	}

	/**
	 * Handle the answers from server when needed
	 * 
	 * @param dataRecived
	 */
	public static void getDataFromServer(Object dataRecived) {
		data = dataRecived;
		isDataRecived = true;
	}

	/**
	 * local function to handle sending and waiting for answer
	 * 
	 * @param msg
	 * @throws Exception
	 */
	private void waitOn(Message msg) throws Exception {
		isDataRecived = false;
		chat.acceptObj(msg);
		while (!isDataRecived)
			Thread.sleep(100);
	}

	@FXML
	void startProcess(ActionEvent event) {
		try {
			reviewProcessManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Manage the review-payment process
	 * 
	 * @throws Exception
	 * 
	 * @throws InterruptedException
	 */
	private void reviewProcessManager() throws Exception {
		int orderId = -1;
		String successMsg = "Yayy!\n";
		MachineEntity machine = OrderController.getCurrentMachine(); // by default the same machine
		String supplyMethod = orderEntity.getSupplyMethod();
		if (cart.size() == 0) {
			CommonFunctions.createPopup(PopupTypeEnum.Error, "Please select items to order :)");
			return;
		}
		// if member he always pay in the end of the month
		String paymentStatus = NavigationStoreController.connectedUser.getRole_type().equals(RolesEnum.member) ? "later"
				: "paid";
		orderEntity.setPaymentStatus(paymentStatus);

		// setup params according to configurations
		switch (supplyMethod) {
		case "Delivery": // online order
			String resValid = isValidDeliveryDetails();
			if (!CommonFunctions.isNullOrEmpty(resValid)) {
				CommonFunctions.createPopup(PopupTypeEnum.Error,
						"You must provide valid delivery information\n" + resValid + "\n");
				return;
			}
			DeliveryEntity deliveryEntity = new DeliveryEntity(user.getRegion(), address.toString());
			orderEntity.setMachine_id(-1);
			
			// insert new order
			waitOn(new Message(TaskType.NewOrderCreation, orderEntity));
			if (data instanceof Integer && (int) data == -1) {
				// error inserting the order
				CommonFunctions.createPopup(PopupTypeEnum.Error, "Error creating order, Please try again\nAbort");
				return;
			}
			orderId = (int) data;
			if (orderId == -1) {
				RollBack();
				CommonFunctions.createPopup(PopupTypeEnum.Error, "Error creating order, Please try again\nAbort");
				return;
			}
			deliveryEntity.setOrderId(orderId); // set the order id from callback
			waitOn(new Message(TaskType.AddNewDelivery, deliveryEntity));

			successMsg += "Order #" + orderId
					+ " placed successfuly\n\nYou will receive an SMS once \nthe delivery approved!";
			break;

		// ---- Pickup / On-site ----
		case "Pickup":
		case "On-site":
			waitOn(new Message(TaskType.UpdateItemsWithAnswer, cart));

			// check validation of items
			if (data instanceof String && !CommonFunctions.isNullOrEmpty((String) data)) {
				// error inserting items (Roll back of this should be taken on server side)
				CommonFunctions.createPopup(PopupTypeEnum.Error,
						"We sorry but the following items no longer available:\n" + ((String) data) + "\nAbort");
				return;
			}

			// insert new order
			waitOn(new Message(TaskType.NewOrderCreation, orderEntity));
			if (data instanceof Boolean && !(boolean) data) {
				// error inserting the order
				CommonFunctions.createPopup(PopupTypeEnum.Error, "Error creating order, Please try again\nAbort");
				// ROLL BACK
				RollBack();
				return;
			}

			orderId = (int) data;

			if (supplyMethod.equals("Pickup")) {
				successMsg += "Order #" + orderId + " is waiting for you in machine #" + machine.getMachineId() + " ("
						+ machine.getMachineName() + ")\n";
				PickupEntity pickup = new PickupEntity(orderId, PickupEntity.Status.inProgress, machine.getMachineId());
				waitOn(new Message(TaskType.InsertNewPickup, pickup));
			} else
				successMsg += "Order #" + orderId + " was placed successfuly\n";
			// check and act if some items under min amount
			handleItemsUnderMinAmount();
			break;

		default:
			CommonFunctions.createPopup(PopupTypeEnum.Error, "Can not detect System Configuration (OL / EK)\nAbort");
			return;
		}

		if (!user.getRole_type().equals(RolesEnum.member)) {
			paymentProccess();
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				System.out.println("Thread end?");
			}
		}

		successfullEndProcess(successMsg);

	}

	/**
	 * Handle the end of the process
	 * 
	 * @param successMsg
	 */
	private void successfullEndProcess(String successMsg) {
		CommonFunctions.createPopup(PopupTypeEnum.Success, successMsg);

		CommonFunctions.SleepFor(200, () -> {
			OrderController.refreshOrderToHomePage();
		});
	}

	static boolean staySleep = true;

	/**
	 * External payment process. Will Always success
	 * 
	 * @throws InterruptedException
	 */
	private void paymentProccess() throws InterruptedException {
		// make a popup for simulation of payment process

		Platform.runLater(() -> {
			Stage primaryStage = new Stage();
			Parent root = null;
			FXMLLoader loader;
			String path = "/boundary/PaymentPopupBoundary.fxml";
			try {
				loader = new FXMLLoader(getClass().getResource(path));
				root = loader.load();

				// get controller and use it
				PaymentPopupController paymentController = loader.getController();
				paymentController.setThread(Thread.currentThread());

				primaryStage.setScene(new Scene(root));
				primaryStage.setTitle("External payment");
				primaryStage.initModality(Modality.APPLICATION_MODAL);
				primaryStage.showAndWait();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

//				// set actions
//				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//					public void handle(WindowEvent we) {
//						staySleep = false;
//
//					}
//				});

//			while(staySleep)

	}

	/**
	 * Checks if there are items under min amount
	 * 
	 * @throws Exception
	 */
	private void handleItemsUnderMinAmount() throws Exception {
		StringBuilder sb = new StringBuilder(); // string for formal message
		ArrayList<int[]> machineItemsId = new ArrayList<>(); // list to send to server
		LinkedHashMap<ItemInMachineEntity, Integer> cart = OrderController.getCart();
		MachineEntity machine = OrderController.getCurrentMachine();
		int minAmount = machine.getMinamount();
		sb.append(
				"Alert!\nThere are some items under the minimum amount of Machine #" + machine.getMachineId() + ":\n");

		for (ItemInMachineEntity item : cart.keySet()) {
			int totalAmount = item.getCurrentAmount();
			int requestedAmount = cart.get(item);
			if (minAmount > totalAmount - requestedAmount) {
				sb.append(item.getName() + ", ");
				machineItemsId.add(new int[] { machine.getMachineId(), item.getId() });
			}
		}
		// delete the last ', '
		sb.deleteCharAt(sb.length() - 1);
		sb.deleteCharAt(sb.length() - 1);
		chat.acceptObj(new Message(TaskType.UpdateItemsUnderMin, machineItemsId));

		if (machineItemsId.size() > 0) {
			// get region manager of machine
			waitOn(new Message(TaskType.RequesManagerInfoFromServerDB, machine.getRegionName()));

			UserEntity regionManager = (UserEntity) data;
			if (regionManager != null) {
				SMSMailHandlerController.SendSMSOrMail("SMS", regionManager, "Minimum amount Alert", sb.toString());
				SMSMailHandlerController.SendSMSOrMail("Mail", regionManager, "Minimum amount Alert", sb.toString());
			}
		}

	}

	/**
	 * Checks all fields of delivery and return true if valid
	 * 
	 * @return
	 */
	private String isValidDeliveryDetails() {
		StringBuilder errMsg = new StringBuilder();

		if (CommonFunctions.isNullOrEmpty(streetTxtField.getText()))
			errMsg.append("Please enter street\n");

		if (CommonFunctions.isNullOrEmpty(aptTxtField.getText()))
			errMsg.append("Please enter apt number or 0 if none\n");

		if (CommonFunctions.isNullOrEmpty(cityTxtField.getText()))
			errMsg.append("Please enter city\n");

		// send error on null/empty information
		if (!CommonFunctions.isNullOrEmpty(errMsg.toString()))
			return errMsg.toString();

		// validate the information strings
		if (!Pattern.matches("^[a-zA-Z][a-zA-Z ]{1,12}$", cityTxtField.getText()))
			errMsg.append("City can contain letters and space only in length of 2-12\n");

		if (!Pattern.matches("^[a-zA-Z][a-zA-Z 0-9]{1,12}$", streetTxtField.getText()))
			errMsg.append("Street can contain number and '/' only in length of 1-5\n");

		if (!Pattern.matches("^[0-9][0-9/]{0,4}$", aptTxtField.getText()))
			errMsg.append("Apartment number can contain number and '/' only in length of 1-5\n");

		address.append("Street: " + streetTxtField.getText());
		address.append("Apartment: " + aptTxtField.getText());
		address.append("City: " + cityTxtField.getText());

		return errMsg.toString();
	}

	/**
	 * Roll back the process if problem has been detected when inserting order
	 */
	private void RollBack() {

	}

	/**
	 * Build all graphical side for all items
	 */
	private void buildReviewOrder() {
		ArrayList<GridPane> gridsToAdd = new ArrayList<>();
		for (ItemInMachineEntity item : cart.keySet()) {
			gridsToAdd.add(buildSingleRow(item));
		}

		// add all to main gridpane
		int index = 1;
		for (GridPane grid : gridsToAdd)
			productsGrid.add(grid, 0, index++);
		productsGrid.getRowConstraints().setAll(new RowConstraints(50, 50, 50));
	}

	/**
	 * Build graphical side for single item
	 * 
	 * @param item
	 */
	private GridPane buildSingleRow(ItemInMachineEntity item) {
		GridPane gridpane = new GridPane();
		Label productName = new Label();
		Label price = new Label();
		Label quantity = new Label();
		Label sum = new Label();
		Line line = new Line();
		Image image = OrderController.getImageOfItem(item.getName());
		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(45);
		imageView.setFitWidth(45);
		imageView.setPreserveRatio(true);
		double tempSum = 0;

		gridpane.setPrefSize(348, 69);
		gridpane.getColumnConstraints().add(new ColumnConstraints(10, 70, 107, Priority.SOMETIMES, HPos.CENTER, false));
		gridpane.getColumnConstraints()
				.add(new ColumnConstraints(10, 192, 207, Priority.SOMETIMES, HPos.CENTER, false));
		gridpane.getColumnConstraints().add(new ColumnConstraints(10, 57, 161, Priority.SOMETIMES, HPos.CENTER, false));
		gridpane.getColumnConstraints().add(new ColumnConstraints(10, 64, 230, Priority.SOMETIMES, HPos.CENTER, false));
		gridpane.getRowConstraints().add(new RowConstraints(20, 31, 47));
		gridpane.getRowConstraints().add(new RowConstraints(20, 32, 60));
		gridpane.getRowConstraints().add(new RowConstraints(20, 32, 60));

		// product
		productName.setText(item.getName());
		productName.setPrefSize(229, 18);
		productName.getStyleClass().add("Label-list");
		GridPane.setHalignment(productName, HPos.LEFT);

		// price
		price.setText(String.valueOf(item.getPrice()) + "₪");
		price.setPrefSize(262, 18);
		price.getStyleClass().add("Label-list");
		GridPane.setHalignment(price, HPos.LEFT);

		// quantity
		tempSum = item.getPrice();
		quantity.setText(String.valueOf(cart.get(item)));
		quantity.setPrefSize(57, 18);
		quantity.getStyleClass().add("Label-list");
		GridPane.setHalignment(quantity, HPos.LEFT);

		// sum
		tempSum *= cart.get(item);
		sum.setText(String.valueOf(tempSum) + "₪");
		sum.setPrefSize(62, 18);
		sum.getStyleClass().add("Label-list");
		GridPane.setHalignment(sum, HPos.LEFT);

		// line
		line.setStartX(-72);
		line.setStartY(0.7216961979866028);
		line.setEndX(260);
		line.setEndY(0.7214934229850769);
		line.setFill(Paint.valueOf("#908e8e"));
		GridPane.setValignment(line, VPos.BOTTOM);
		// GridPane.setHalignment(line, HPos.LEFT);
		GridPane.setColumnSpan(line, 5);

		GridPane.setRowSpan(imageView, 3);

		gridpane.add(imageView, 0, 0);
		gridpane.add(productName, 1, 0);
		gridpane.add(price, 1, 1);
		gridpane.add(quantity, 2, 1);
		gridpane.add(sum, 3, 1);
		gridpane.add(line, 0, 2);
		return gridpane;

	}

}