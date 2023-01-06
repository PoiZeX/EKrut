package controllerGui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import java.util.LinkedHashMap;
import java.util.Map;

import java.util.concurrent.Semaphore;


import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.ScreensNames;
import common.TaskType;
import controller.OrderController;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
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
    private TextField postCodeTxtField;

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

	private boolean isSelfPickup;

	private static Map<ItemInMachineEntity, Integer> cart;
	private UserEntity user=NavigationStoreController.connectedUser;
	private StringBuilder address=new StringBuilder();
	private String errMsg="";

	public void initialize() {
		// tooltip = new TooltipSetter("Cancel the order");
		// cancelOrderBtn.setTooltip(tooltip.getTooltip());

//		ItemInMachineEntity newItem = new ItemInMachineEntity(1, 1, 50, ItemInMachineEntity.Call_Status.NotOpened, 0, 0,
//				"Bamba", 100.0, "Bamba.png");
//		ItemInMachineEntity newItem1 = new ItemInMachineEntity(1, 2, 50, ItemInMachineEntity.Call_Status.NotOpened, 0,
//				0, "Bamba1", 100.0, "Bamba.png");
//		ItemInMachineEntity newItem2 = new ItemInMachineEntity(1, 3, 50, ItemInMachineEntity.Call_Status.NotOpened, 0,
//				0, "Bamba2", 100.0, "Bamba.png");
//		ItemInMachineEntity newItem3 = new ItemInMachineEntity(1, 4, 50, ItemInMachineEntity.Call_Status.NotOpened, 0,
//				0, "Bamba3", 100.0, "Bamba.png");
//		ItemInMachineEntity newItem4 = new ItemInMachineEntity(2, 5, 50, ItemInMachineEntity.Call_Status.NotOpened, 0,
//				0, "Bamba4", 100.0, "Bamba.png");
//
//		
//		OrderController.addItemToCart(newItem, 0);
//		OrderController.addItemToCart(newItem1, 0);
//		OrderController.addItemToCart(newItem2, 0);
//		OrderController.addItemToCart(newItem3, 0);
//		OrderController.addItemToCart(newItem4, 0);

		cart = OrderController.getCart();
		buildReviewOrder();

		// build graphical side

		// calculate data

		// -- get sales / discounts (if subscriber)

		// check if OL/EK (for delivery)

		// add option for future payment
		// make popup external payment

	}

	private static Object data;
	private static boolean isDataRecived = false;
	
	public static void getDataFromServer(Object dataRecived) {
		data = dataRecived;
		semaphore.release();
	}

	private static Semaphore semaphore = new Semaphore(1);

	/**
	 * Manage the review process
	 * 
	 * @throws InterruptedException
	 */
	private void reviewProcessManager() throws InterruptedException {
		/*
		 * Flow: 1. User press 'payment' 1.1 Check if (OL && Delivery details good) 2.
		 * Send to server all items to update 3. Get Answer on success (String, null or
		 * info) 3.1. if yes - continue to 4 3.2. if no - stay in review order with
		 * information popup 4. Make external Payment
		 * 
		 * ROLL BACK HERE?
		 */

		// setup params according to configurations
		switch (AppConfig.SYSTEM_CONFIGURATION) {
		case "OL":  // online order
			if(!isValidDeliveryDetails())
			{
				CommonFunctions.createPopup(PopupTypeEnum.Error, "You must provide valid delivery information");
				return;
			}
			
			break;

		case "EK":  // real machine order

			break;

		default:
			CommonFunctions.createPopup(PopupTypeEnum.Error, "Can not detect System Configuration (OL / EK)\nAbort");
			return;
		}

		isDataRecived = false;
//		chat.acceptObj(new Message(TaskType.UpdateItems, OrderController.getCart()));  // error for now in purpose

		if(data instanceof String && !CommonFunctions.isNullOrEmpty((String) data))
		{
			// error inserting items (Roll back of this should be taken on server side)
			CommonFunctions.createPopup(PopupTypeEnum.Error, "We sorry but the following items no longer available:\n"+
					((String) data)+"\nAbort");
			return;
		}
		else {
			isDataRecived = false;
			chat.acceptObj(new Message(TaskType.NewOrderCreation, OrderController.getCart()));
			if(data instanceof Boolean && !(boolean)data)
			{
				// error inserting the order
				CommonFunctions.createPopup(PopupTypeEnum.Error, "We sorry but the following items no longer available:\n"+
						((String) data)+"\nAbort");
				
				// ROLL BACK
				RollBack();
				return;
			}
			else {
				isDataRecived = false;

				DeliveryEntity deliveryEntity=new DeliveryEntity(user.getRegion(),user.getId_num(),address.toString());
				chat.acceptObj(new Message(TaskType.AddNewDelivery, deliveryEntity));

//				chat.acceptObj(new Message(TaskType.AddNewDelivery, OrderController.getCart()));

				
			}
		}
		
		// send message to Server to enter delivery details (after order is inserted)
	}

	private void paymentProccess() {
		// make a popup for simulation of payment process

		// refresh stages
		NavigationStoreController.getInstance().refreshStage(ScreensNames.ReviewOrder);
		NavigationStoreController.getInstance().refreshStage(ScreensNames.ViewCatalog);
	}

	/**
	 * Checks all fields of delivery and return true if valid
	 * @return
	 */
	private boolean isValidDeliveryDetails() {
		
		if(CommonFunctions.isNullOrEmpty(streetTxtField.getText())){
			errMsg="Please enter street";
			return false;
		}
		if(CommonFunctions.isNullOrEmpty(aptTxtField.getText())){
			errMsg="Please enter apt number or 0 if none";
			return false;
		}
		if(CommonFunctions.isNullOrEmpty(cityTxtField.getText())){
			errMsg="Please enter city";
			return false;
		}
		if(CommonFunctions.isNullOrEmpty(postCodeTxtField.getText())){
			errMsg="Please enter postcode"; //its must? 
			return false;
		}
		address.append(streetTxtField.getText());
		address.append(aptTxtField.getText());
		address.append(cityTxtField.getText()+", ");
		address.append(postCodeTxtField.getText());
		
		/*add: 
		 * streetTxtField conatains only letters
		 * aptTxtField-number
		 * cityTxtField-letters
		 * postcode- 7 digits*/
		return true;
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
		Image image = new Image("/styles/products/" + item.getItemImg().getImgName());
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
		quantity.setText(String.valueOf(item.getCurrentAmount()));
		quantity.setPrefSize(57, 18);
		quantity.getStyleClass().add("Label-list");
		GridPane.setHalignment(quantity, HPos.LEFT);

		// sum
		tempSum *= item.getCurrentAmount();
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