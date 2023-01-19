package controllerGui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import controller.ItemsController;
import controller.OrderController;
import entity.ItemEntity;
import entity.ItemInMachineEntity;
import enums.PopupTypeEnum;
import enums.RolesEnum;
import enums.ScreensNamesEnum;
import enums.TaskType;
import interfaces.IScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import utils.AppConfig;
import utils.PopupSetter;
import utils.TooltipSetter;

/**
 * Controller class for the view catalog screen.
 */
public class ViewCatalogController implements IScreen {

	@FXML
	private BorderPane viewCatalogBorderpane;

	@FXML
	private Button placeOrderBtn;

	@FXML
	private Button cancelOrderBtn;

	@FXML
	private Button viewCartBtn;

	@FXML
	private TextField searchTextLabel;

	@FXML
	private Label shipmentMethodLabel;

	@FXML
	private Group cartGroup;

	@FXML
	private Label cartPopupAmountLabel;

	@FXML
	private GridPane catalogViewGridpane;

	@FXML
	private ImageView searchImg;

	@FXML
	private Pane viewCartPane;

	@FXML
	private Label cartSizeLabel;

	@FXML
	private Label totalPriceLabel;

	@FXML
	private Label discountTotalLabel;

	@FXML
	private ImageView totalMoneyImage;

	@FXML
	private GridPane cartViewGridpane;

	@FXML
	private ScrollPane catalogScrollPane;

	@FXML
	private Button helpBtn;

	private double machineDiscount = 1;
	private Object lockSync = new Object();
	private boolean isMember = OrderController.isMember;
	private int machineId = AppConfig.MACHINE_ID;
	private ObservableList<Node> allCatalogItems;
	private String currentSupplyMethod;
	private static ClientController chat = HostClientController.getChat(); // define the chat for th
	private static boolean recievedData = false;

	/**
	 * Initialize the view catalog screen.
	 */
	@Override
	public void initialize() {
		try {
			helpBtn.setTooltip((new TooltipSetter("Click for help").getTooltip()));
			isMember = NavigationStoreController.connectedUser.getRole_type() == RolesEnum.member ? true
					: false;
			checkRequestType();
			while (!recievedData)
				Thread.sleep(100);
			generateCatalog(OrderController.getItemsList());
			cartGroup.setVisible(false);
			viewCartPane.setVisible(false);
			viewCartPane.setMouseTransparent(true);
			searchTextLabel.textProperty().addListener((observable, oldValue, newValue) -> {
				reorderCatalog(newValue);
			});

			if (currentSupplyMethod.equals("Delivery") || !OrderController.isActiveSale() || !isMember) {
				discountTotalLabel.setVisible(false);
				GridPane.setRowIndex(totalPriceLabel, 0);
				GridPane.setRowSpan(totalPriceLabel, 2);
				GridPane.setFillHeight(totalPriceLabel, true);

			}
			shipmentMethodLabel.setMouseTransparent(true);
			setTooltips();
			recievedData = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * set tooltips for all nodes to display information about action.
	 */
	private void setTooltips() {
		placeOrderBtn.setTooltip(new TooltipSetter("Procceed to Order Review and Payment.").getTooltip());
		cancelOrderBtn.setTooltip(new TooltipSetter("Cancel Order and return to Home Page.").getTooltip());
		viewCartBtn.setTooltip(new TooltipSetter("View your shopping cart.").getTooltip());
		Tooltip.install(searchImg, new TooltipSetter("Search item by name.").getTooltip());
	}

	/**
	 * Check the request type and set the current supply method and machine ID
	 * accordingly.
	 * 
	 * @return true if the request type was successfully set, false otherwise
	 */
	private boolean checkRequestType() {
		OrderController.calculateDiscountsPercentage();
		if (OrderController.getCurrentOrder() == null) { // EK
			OrderController.clearAll();
			OrderController.setCurrentOrder(NavigationStoreController.connectedUser.getId(), "On-site");
			OrderController.getCurrentOrder().setMachine_id(AppConfig.MACHINE_ID);
			currentSupplyMethod = OrderController.getCurrentOrder().getSupplyMethod();
			shipmentMethodLabel.setText(DataStore.getCurrentMachine().getMachineName());
			chat.acceptObj(new Message(TaskType.RequestItemsInMachine, machineId));
			return true;
		} else {
			currentSupplyMethod = OrderController.getCurrentOrder().getSupplyMethod();
			switch (currentSupplyMethod) {
			case "Pickup":
				machineId = OrderController.getCurrentOrder().getMachine_id();
				shipmentMethodLabel.setText("Pickup - " + OrderController.getCurrentMachine().getMachineName());
				chat.acceptObj(new Message(TaskType.RequestItemsInMachine, machineId));
				return true;
			case "Delivery":
				shipmentMethodLabel.setText("Delivery");
				generateAllItems();
				return true;
			}
		}
		return false;
	}

	/**
	 * Generate all items in the catalog by creating new ItemInMachineEntity objects
	 * and adding them to the order.
	 */
	private void generateAllItems() {
		ArrayList<ItemEntity> tempItems = ItemsController.allItems;
		for (ItemEntity item : tempItems) {
			OrderController.putItemInList(new ItemInMachineEntity(item));
		}
		recievedData = true;
	}

	/**
	 * Handle the cancel order button press.
	 * 
	 * @param event the action event triggered by the button press
	 */
	@FXML
	public void cancelOrder(ActionEvent event) {
		PopupSetter.createPopup(PopupTypeEnum.Decision,
				"You about to cancel the current order.\nAll data will lost.\n\nTo cancel click 'YES', 'NO' otherwise");
		if ((boolean) PopupController.isOkPressed) {
			OrderController.refreshOrderToHomePage();
		}
	}

	/**
	 * Handle the place order button press by navigating to the review order screen.
	 * 
	 * @param event the action event triggered by the button press
	 */
	@FXML
	void placeOrder(ActionEvent event) {
		if (OrderController.getCartSize() == 0)
			PopupSetter.createPopup(PopupTypeEnum.Error,
					"You can't place order with no items, \nif you want to proceed please add items to your cart.");
		else {
			NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.ReviewOrder);
		}
	}

	/**
	 * Handle the view cart button press by toggling the visibility of the view cart
	 * pane.
	 * 
	 * @param event the action event triggered by the button press
	 */
	@FXML
	void viewCart(ActionEvent event) {
		updateCartTotalLabels();
		viewCartPane.setVisible(!viewCartPane.isVisible());
	}

	/**
	 * Receives the items from a specific machine
	 * 
	 * @param obj
	 */
	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		OrderController.clearItemsList();
		for (ItemInMachineEntity item : obj) {
			convertImage(item);
			OrderController.putItemInList(item);
		}
		recievedData = true;
	}

	/**
	 * Converting image from bytes to image
	 * 
	 * @param item
	 */
	private static void convertImage(ItemInMachineEntity item) {
		InputStream fis = new ByteArrayInputStream(item.getItemImg().mybytearray);
		Image fileImg = new Image(fis);
		item.setItemImage(fileImg);
	}

	/**
	 * generate catalog manager
	 * 
	 * @param itemsList
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private void generateCatalog(Map<String, ItemInMachineEntity> itemsList)
			throws InterruptedException, ExecutionException {
		generateRowConstraints(itemsList);
		if (OrderController.isActiveSale())
			machineDiscount = OrderController.getDiscountsPercentage();
		ExecutorService executor = Executors.newFixedThreadPool(itemsList.size());
		List<Callable<Boolean>> tasks = new ArrayList<>();
		int j = 0;
		for (int i = 1; i <= itemsList.size(); i++) {
			int index = i;
			int col = (i - 1) % 4;
			int row = i % 4 == 0 ? j++ : j;
			tasks.add(() -> generateItem((ItemInMachineEntity) itemsList.values().toArray()[index], machineDiscount,
					col, row));
		}
		// Invoke all the tasks
		executor.invokeAll(tasks);
		// Shutdown the executor
		executor.shutdown();

		allCatalogItems = FXCollections.observableArrayList();

		for (Node item : catalogViewGridpane.getChildren()) {
			GridPane gp = (GridPane) item;
			if (!gp.isDisable())
				allCatalogItems.add(gp);
		}
		for (Node item : catalogViewGridpane.getChildren()) {
			GridPane gp = (GridPane) item;
			if (gp.isDisable())
				allCatalogItems.add(gp);
		}

		renewCatalog();
		if (OrderController.isOnePlusOneSaleExist() && OrderController.isActiveSale() && isMember) {
			PopupSetter.createPopup(PopupTypeEnum.Sale, "1+1 sale will be updated in order review");
		}
	}

	/**
	 * @param itemsList functions to determine how many rows the catalog will have
	 *                  and generate row constraints for each row
	 */
	private void generateRowConstraints(Map<String, ItemInMachineEntity> itemsList) {
		int rowsAmount = itemsList.size() / 4;
		for (int i = 0; i < rowsAmount; i++)
			catalogViewGridpane.getRowConstraints().add(new RowConstraints(250.0, 250.0, 250.0));
	}

	/**
	 * The generateItem method generates a GUI element for displaying information
	 * about an ItemInMachineEntity object, as well as adding functionality for
	 * adding and removing the item from a cart, and modifying the item's quantity
	 * in the cart.
	 *
	 * @param item          The ItemInMachineEntity object to be displayed in the
	 *                      GUI element.
	 * @param discountPrice The discount price to be applied to the item.
	 * @param i             An integer value representing the row position of the
	 *                      GUI element in a grid.
	 * @param j             An integer value representing the column position of the
	 *                      GUI element in a grid.
	 * @return A GridPane object representing the generated GUI element.
	 */
	public boolean generateItem(ItemInMachineEntity item, double discountPrice, int i, int j) {
		try {
			// Prepare the gridpanes for the items in machine
			GridPane newItem = createSingleCatalogItem();
			ImageView image = (ImageView) newItem.getChildren().get(0);
			GridPane btnBar = (GridPane) ((ButtonBar) newItem.getChildren().get(1)).getButtons().get(0);
			Button minusBtn = (Button) btnBar.getChildren().get(0);
			Label amountLabel = (Label) btnBar.getChildren().get(1);
			Button plusBtn = (Button) btnBar.getChildren().get(2);
			//Button addToCartBtn = (Button) newItem.getChildren().get(2);
			Label priceLabel = (Label) newItem.getChildren().get(2);
			Label productNameLabel = (Label) newItem.getChildren().get(3);
			Text discountPriceLabel = (Text) newItem.getChildren().get(4);
			ImageView salePersentageIconImg = (ImageView) newItem.getChildren().get(5);
			ImageView onePlusOneImg = (ImageView) newItem.getChildren().get(6);

			productNameLabel.setText(item.getName());
			if (OrderController.isOnePlusOneSaleExist() && currentSupplyMethod != "Delivery" && isMember)
				onePlusOneImg.setVisible(OrderController.isOnePlusOneSaleExist());
			if (OrderController.isPercentageSaleExit() && currentSupplyMethod != "Delivery" && isMember) {
				salePersentageIconImg.setVisible(OrderController.isPercentageSaleExit());
				discountPriceLabel.setVisible(OrderController.isPercentageSaleExit());
				discountPriceLabel.setText(item.getPrice() + "₪");
				discountPriceLabel.setStrikethrough(true);
				priceLabel.getStyleClass().clear();
				priceLabel.getStyleClass().add("Label-list-red");
				priceLabel
						.setText(String.format("%.2f₪", (OrderController.getItemPriceAfterDiscounts(item.getPrice()))));

			} else {
				priceLabel.setText(item.getPrice() + "₪");
			}
			if (currentSupplyMethod != "Delivery" && item.getCurrentAmount() <= 0) {
				newItem.setDisable(true);
				image.setOpacity(0.5);

			}

			// Prepare the gridpanes for the items in the cart
			GridPane newItemInCart = createSingleCartItem();
			ImageView newItemInCartImage = (ImageView) newItemInCart.getChildren().get(0);
			Label itemInCartNameLabel = (Label) newItemInCart.getChildren().get(1);
			Label itemInCartAmountLabel = (Label) newItemInCart.getChildren().get(3);
			Button itemInCartMinusBtn = (Button) newItemInCart.getChildren().get(2);
			Button itemInCartPlusBtn = (Button) newItemInCart.getChildren().get(4);
			Button deleteItemBtn = (Button) newItemInCart.getChildren().get(5);
			itemInCartNameLabel.setText(item.getName());
			itemInCartNameLabel.setWrapText(true);
			// Handle delete button
			deleteItemBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if (!OrderController.changeItemQuantity(item, 0))
						PopupSetter.createPopup(PopupTypeEnum.Warning, "Couldn't change the item's amount");
					// Update total amount and price
					updateCartTotalLabels();
//					addToCartBtn.setOpacity(1);
//					addToCartBtn.setMouseTransparent(false);
					cartViewGridpane.getChildren().remove(cartViewGridpane.getChildren().indexOf(newItemInCart));
					reorderCart(cartViewGridpane);
				}
			});
			newItemInCartImage.setImage(item.getItemImage());
			image.setImage(item.getItemImage());

			// Add to cart button
////			addToCartBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
//				@Override
//				public void handle(MouseEvent e) {
//					// Check if it's possible to add the item
//					if (item.getCurrentAmount() > 0) {
//						//addToCartBtn.setOpacity(0);
//						addToCartBtn.setPrefSize(20, 20);
//						addToCartBtn.setMouseTransparent(true);
//						amountLabel.setText("1");
//						int amount = Integer.parseInt(amountLabel.getText());
//						itemInCartAmountLabel.setText(amountLabel.getText());
//						cartViewGridpane.add(newItemInCart, 0, cartViewGridpane.getChildren().size());
//						if (item.getCurrentAmount() == 1) {
//							plusBtn.setDisable(true);
//							itemInCartPlusBtn.setDisable(true);
//						}
//						if (!OrderController.addItemToCart(item, amount)) // Add item to cart
//							PopupSetter.createPopup(PopupTypeEnum.Warning, "Couldn't add the item to the cart");
//					}
//					cartGroup.setVisible(true);
//					viewCartPane.setVisible(false);
//					viewCartPane.setMouseTransparent(false);
//					itemInCartNameLabel.setTooltip(new TooltipSetter(itemInCartNameLabel.getText()).getTooltip());
//					updateCartTotalLabels();
//				}
//			});

			plusBtn.setOnMouseClicked(getPlusEvent(amountLabel, plusBtn, itemInCartPlusBtn, minusBtn, newItemInCart,
					item, itemInCartAmountLabel, true));
			minusBtn.setOnMouseClicked(getMinusEvent(amountLabel, plusBtn, itemInCartPlusBtn, minusBtn,
					newItemInCart, item, itemInCartAmountLabel, true));
			itemInCartPlusBtn.setOnMouseClicked(getPlusEvent(amountLabel, plusBtn, itemInCartPlusBtn, minusBtn,
					newItemInCart, item, itemInCartAmountLabel, false));
			itemInCartMinusBtn.setOnMouseClicked(getMinusEvent(amountLabel, plusBtn, itemInCartPlusBtn, minusBtn,
					newItemInCart, item, itemInCartAmountLabel, false));
			if (item.getCurrentAmount() == 0) {
				newItem.setDisable(true);
				image.setOpacity(0.5);
				btnBar.setVisible(false);
			//	addToCartBtn.setText("Not Available");
			}
			productNameLabel.setTooltip(new TooltipSetter(productNameLabel.getText()).getTooltip());
			//productNameLabel.getTooltip().setShowDelay(Duration.seconds(0.7));
			Tooltip.install(image, new TooltipSetter(productNameLabel.getText()).getTooltip());

			synchronized (lockSync) {
				catalogViewGridpane.add(newItem, i, j);
			}

			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return true;
	}

	/**
	 * This method creates and returns an EventHandler that handles MouseEvent. When
	 * the event is triggered (by clicking a button), it performs several actions:
	 * Decrement the value displayed in the amountLabel by 1. Check if the plus
	 * button is disabled, if true, it enables it. Check if the amount is now 0, if
	 * true, it makes the addToCartBtn visible and clickable and remove the item
	 * from the cart. If the amount is not 0, it updates the itemInCartAmountLabel
	 * with the new amount and updates the item's quantity in the cart. If the flag
	 * is true, it hides the viewCartPane and updates the cart total labels.
	 * 
	 * @param amountLabel           a Label that holds the current amount of the
	 *                              item
	 * @param plusBtn               a button that increases the amount of the item
	 * @param itemInCartPlusBtn     a button that increases the amount of the item
	 *                              in the cart
	 * @param addToCartBtn          a button that adds the item to the cart
	 * @param newItemInCart         a GridPane that holds the item information in
	 *                              the cart
	 * @param item                  an ItemInMachineEntity object that represents
	 *                              the item
	 * @param itemInCartAmountLabel a Label that holds the current amount of the
	 *                              item in the cart
	 * @param flag                  a boolean value that indicates whether to hide
	 *                              the viewCartPane or not.
	 * @return an EventHandler that handles the MouseEvent
	 */
	private EventHandler<MouseEvent> getMinusEvent(Label amountLabel, Button plusBtn, Button itemInCartPlusBtn,
			Button minusBtn, GridPane newItemInCart, ItemInMachineEntity item, Label itemInCartAmountLabel,
			boolean flag) {
		EventHandler<MouseEvent> minusEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				int amount = Integer.parseInt(amountLabel.getText());
				amountLabel.setText(String.valueOf(amount - 1));
				amount = Integer.parseInt(amountLabel.getText());
				if (plusBtn.isDisabled()) {
					plusBtn.setDisable(false);
					itemInCartPlusBtn.setDisable(false);
				}
				if (amount == 0) {
					minusBtn.setDisable(true);
//					//addToCartBtn.setOpacity(1);
//					addToCartBtn.setVisible(true);
//					addToCartBtn.setPrefHeight(40.0);
//					addToCartBtn.setPrefWidth(178.0);
//					addToCartBtn.setMouseTransparent(false);
					
					if (!OrderController.changeItemQuantity(item, 0))
						PopupSetter.createPopup(PopupTypeEnum.Warning, "Couldn't add the item to the cart");

					cartViewGridpane.getChildren().remove(cartViewGridpane.getChildren().indexOf(newItemInCart));
					reorderCart(cartViewGridpane);
				} else {
					itemInCartAmountLabel.setText(amountLabel.getText());
					if (!OrderController.changeItemQuantity(item, amount))
						PopupSetter.createPopup(PopupTypeEnum.Warning, "Couldn't add the item to the cart");
				}
				if (flag) {
					viewCartPane.setVisible(false);
					viewCartPane.setMouseTransparent(false);
				}
				
				
				updateCartTotalLabels();
			}
		};
		return minusEvent;
	}

	/**
	 * This method creates an event handler for the 'plus' button, which increases
	 * the amount of an item in the cart by 1.
	 * 
	 * @param amountLabel           The label that displays the current amount of
	 *                              the item in the cart
	 * @param plusBtn               The plus button that the event handler is being
	 *                              created for
	 * @param itemInCartPlusBtn     The plus button in the cart view of the item
	 * @param addToCartBtn          The add to cart button of the item
	 * @param newItemInCart         The GridPane representing the item in the cart
	 *                              view
	 * @param item                  The item that is being added to the cart
	 * @param itemInCartAmountLabel The label in the cart view that displays the
	 *                              current amount of the item
	 * @param flag                  A flag indicating whether the cart view should
	 *                              be closed when the button is clicked
	 * @return An EventHandler for the plus button that increases the amount of the
	 *         item in the cart.
	 */
	private EventHandler<MouseEvent> getPlusEvent(Label amountLabel, Button plusBtn, Button itemInCartPlusBtn,
			Button minusBtn, GridPane newItemInCart, ItemInMachineEntity item, Label itemInCartAmountLabel,
			boolean flag) {
		EventHandler<MouseEvent> plusEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				int amount = Integer.parseInt(amountLabel.getText());
				minusBtn.setDisable(false);
				amountLabel.setText(String.valueOf(amount + 1));
				amount = Integer.parseInt(amountLabel.getText());
				if(amount==1) {
					cartViewGridpane.add(newItemInCart, 0, cartViewGridpane.getChildren().size());
					cartGroup.setVisible(true);
					viewCartPane.setVisible(false);
					viewCartPane.setMouseTransparent(false);
					updateCartTotalLabels();
				}
				if (amount == item.getCurrentAmount()) {
					plusBtn.setDisable(true);
					itemInCartPlusBtn.setDisable(true);
				}
				if (!OrderController.changeItemQuantity(item, amount))
					PopupSetter.createPopup(PopupTypeEnum.Warning, "Couldn't add the item to the cart");

				itemInCartAmountLabel.setText(amountLabel.getText());
				if (flag) {
					viewCartPane.setVisible(false);
					viewCartPane.setMouseTransparent(false);
				}
				updateCartTotalLabels();
			}
			
//			int amount = Integer.parseInt(amountLabel.getText());
//			itemInCartAmountLabel.setText(amountLabel.getText());
//			cartViewGridpane.add(newItemInCart, 0, cartViewGridpane.getChildren().size());
//			if (item.getCurrentAmount() == 1) {
//				plusBtn.setDisable(true);
//				itemInCartPlusBtn.setDisable(true);
//			}
//			if (!OrderController.addItemToCart(item, amount)) // Add item to cart
//				PopupSetter.createPopup(PopupTypeEnum.Warning, "Couldn't add the item to the cart");
//		}
	
			
		};
		return plusEvent;
	}

	/**
	 * Update the cart bubble (shopping cart icon) on the user interface.
	 * 
	 * @param amount the current number of items in the cart
	 */
	private void updateCartBubble(int amount) {
		if (amount <= 0)
			cartGroup.setVisible(false);
		else {
			cartGroup.setVisible(true);
			if (amount > 99) {
				cartPopupAmountLabel.setStyle("-fx-font-size: 7pt;");
				cartPopupAmountLabel.setText("99+");
			} else {
				cartPopupAmountLabel.setText(amount + "");
				cartPopupAmountLabel.setStyle("-fx-font-size: 9pt;");
			}
		}
	}

	/**
	 * Update the cart labels according to changes
	 */
	private void updateCartTotalLabels() {
		updateCartBubble(OrderController.getCartSize());
		if (OrderController.getCartSize() == 0) {
			cartSizeLabel.setText("Cart is Empty");
			totalPriceLabel.setVisible(false);
			totalMoneyImage.setVisible(false);
			discountTotalLabel.setVisible(false);
		} else {
			totalMoneyImage.setVisible(true);
			totalPriceLabel.setVisible(true);
			cartSizeLabel.setText(OrderController.getCartSize() + " Items");// + (OrderController.getTotalPrice() -
																			// OrderController.getTotalDiscounts()) +
																			// "₪"

			if (currentSupplyMethod.equals("Delivery") || !OrderController.isActiveSale() || !isMember)
				totalPriceLabel.setText("Total: " + OrderController.getTotalPrice() + "₪");
			else {
				discountTotalLabel.setVisible(true);
				discountTotalLabel.setText(String.format("Discount: %.2f₪", OrderController.getTotalDiscounts()));
				totalPriceLabel.setText(String.format("Total: %.2f₪", OrderController.getPriceAfterDiscounts()));
			}
		}
	}

	/**
	 * This method is responsible for reordering the items in the cart view
	 * gridpane.
	 * 
	 * @param cartViewGridpane the gridpane containing the items in the cart.
	 */
	private void reorderCart(GridPane cartViewGridpane) {
		ObservableList<Node> tempItems = FXCollections.observableArrayList(cartViewGridpane.getChildren());
		cartViewGridpane.getChildren().clear();
		int i = 0;
		for (Node n : tempItems) {
			cartViewGridpane.add(n, 0, i);
			i++;
		}

	}

	/**
	 * functions to remove all catalog childs and re-add them to re-arrange the
	 * catalog - usage in search bar
	 */
	private void reorderCatalog(String newValue) {
		int i = 0, j = 0;
		if (newValue.equals("")) {
			renewCatalog();
			return;
		}
		catalogViewGridpane.getChildren().clear();
		for (Node item : allCatalogItems) {
			GridPane itemAsGrid = (GridPane) item;
			Label productNameLabel = (Label) itemAsGrid.getChildren().get(4);
			if (productNameLabel.getText().toLowerCase().contains(newValue.toLowerCase())) {
				catalogViewGridpane.add(item, (i++) % 4, i % 4 == 0 ? j++ : j);
			}
		}
		catalogScrollPane.setVvalue(0);
	}

	/**
	 * functions to remove all catalog childs and re-add them to re-arrange the
	 * catalog - usage in search bar
	 */
	private void renewCatalog() {
		int i = 0, j = 0;
		catalogViewGridpane.getChildren().clear();
		for (Node item : allCatalogItems) {
			catalogViewGridpane.add(item, (i++) % 4, i % 4 == 0 ? j++ : j);
		}
	}

	/**
	 * 
	 * This method creates a single catalog item for the catalog view. It creates a
	 * GridPane and sets its size and style. It also creates column and row
	 * constraints for the GridPane. An ImageView and a button bar are created and
	 * added to the GridPane.
	 * 
	 * @return A GridPane that represents a single catalog item.
	 */
	private GridPane createSingleCatalogItem() {
		// Create a GridPane
		GridPane itemViewGridpane = new GridPane();
		itemViewGridpane.setPrefSize(200, 250);
		itemViewGridpane.getStyleClass().add("GridPaneChild");
		itemViewGridpane.getStylesheets().add("/styles/css/generalStyleSheet.css");

		// Create column constraints for the GridPane
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPrefWidth(60);
		col1.setHgrow(Priority.SOMETIMES);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPrefWidth(120);
		col2.setHgrow(Priority.SOMETIMES);
		itemViewGridpane.getColumnConstraints().addAll(col1, col2);

		// Create row constraints for the GridPane
		RowConstraints row1 = new RowConstraints();
		row1.setPrefHeight(135);
		row1.setVgrow(Priority.SOMETIMES);
		RowConstraints row2 = new RowConstraints();
		row2.setPrefHeight(25);
		row2.setVgrow(Priority.SOMETIMES);
		RowConstraints row3 = new RowConstraints();
		row3.setPrefHeight(20);
		row3.setVgrow(Priority.SOMETIMES);
		RowConstraints row4 = new RowConstraints();
		row4.setPrefHeight(50);
		row4.setVgrow(Priority.SOMETIMES);
		itemViewGridpane.getRowConstraints().addAll(row1, row2, row3, row4);

		// Create an ImageView and add it to the GridPane
		ImageView itemImageView = new ImageView();
		itemImageView.setFitHeight(115);
		itemImageView.setFitWidth(160);
		itemImageView.setPickOnBounds(true);
		itemImageView.setPreserveRatio(true);
		DropShadow effect = new DropShadow();
		effect.setHeight(30.00);
		effect.setRadius(12.00);
		effect.setColor(new Color(0, 0, 0, 0.427));
		itemImageView.setEffect(effect);

		// ----------------------------------------------------------------------
		ButtonBar changeQuantityBtn = new ButtonBar();
		changeQuantityBtn.setId("buttonBar_quantity");
		changeQuantityBtn.setButtonMinWidth(40.0);
		changeQuantityBtn.setMaxHeight(Double.NEGATIVE_INFINITY);
		changeQuantityBtn.setMaxWidth(Double.NEGATIVE_INFINITY);
		changeQuantityBtn.setMinHeight(Double.NEGATIVE_INFINITY);
		changeQuantityBtn.setMinWidth(Double.NEGATIVE_INFINITY);
		changeQuantityBtn.setPrefHeight(40.0);
		changeQuantityBtn.setPrefWidth(178.0);

		GridPane buttonPane = new GridPane();
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.setMaxHeight(Double.NEGATIVE_INFINITY);
		buttonPane.setMaxWidth(Double.NEGATIVE_INFINITY);
		buttonPane.setMinHeight(Double.NEGATIVE_INFINITY);
		buttonPane.setMinWidth(Double.NEGATIVE_INFINITY);
		buttonPane.setPrefHeight(40.0);
		buttonPane.setPrefWidth(190.0);

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setHalignment(HPos.LEFT);
		column1.setHgrow(Priority.SOMETIMES);
		column1.setMaxWidth(Double.NEGATIVE_INFINITY);
		column1.setMinWidth(Double.NEGATIVE_INFINITY);
		column1.setPrefWidth(65.0);

		ColumnConstraints column2 = new ColumnConstraints();
		column2.setHalignment(HPos.CENTER);
		column2.setHgrow(Priority.SOMETIMES);
		column2.setMaxWidth(85.0);
		column2.setMinWidth(Double.NEGATIVE_INFINITY);
		column2.setPrefWidth(60.0);

		ColumnConstraints column3 = new ColumnConstraints();
		column3.setHalignment(HPos.RIGHT);
		column3.setHgrow(Priority.SOMETIMES);
		column3.setMaxWidth(76.0);
		column3.setMinWidth(Double.NEGATIVE_INFINITY);
		column3.setPrefWidth(76.0);
		buttonPane.getColumnConstraints().addAll(column1, column2, column3);

		RowConstraints row11 = new RowConstraints();
		row11.setMaxHeight(Double.NEGATIVE_INFINITY);
		row11.setMinHeight(Double.NEGATIVE_INFINITY);
		row11.setPrefHeight(35.0);
		row11.setVgrow(Priority.SOMETIMES);
		buttonPane.getRowConstraints().add(row11);

		Button minusBtn = new Button("\u2212"); // Unicode character for "minus" symbol
		minusBtn.setId("button_minusButton");
		minusBtn.setMaxWidth(Double.NEGATIVE_INFINITY);
		minusBtn.setMinHeight(Double.NEGATIVE_INFINITY);
		minusBtn.setMinWidth(Double.NEGATIVE_INFINITY);
		minusBtn.setMnemonicParsing(false);
		minusBtn.setPrefHeight(40.0);
		minusBtn.setPrefWidth(55.0);
		minusBtn.getStyleClass().add("Button-NoBG");

		Label quantityLabel = new Label("0");
		quantityLabel.setId("quntityLabel");

		Button plusBtn = new Button("+");
		plusBtn.setId("button_plusButton");
		plusBtn.setMaxHeight(Double.NEGATIVE_INFINITY);
		plusBtn.setMaxWidth(Double.NEGATIVE_INFINITY);
		plusBtn.setMinHeight(Double.NEGATIVE_INFINITY);
		plusBtn.setMinWidth(Double.NEGATIVE_INFINITY);
		plusBtn.setMnemonicParsing(false);
		plusBtn.setPrefHeight(40.0);
		plusBtn.setPrefWidth(55.0);
		plusBtn.getStyleClass().add("Button-NoBG");

		buttonPane.getChildren().addAll(minusBtn, quantityLabel, plusBtn);
		changeQuantityBtn.getButtons().add(buttonPane);
		// -------------------------------------------------------------------------

//		Button AddToCartBtn = new Button();
//		AddToCartBtn.setId("button_AddToCart");
//		AddToCartBtn.setMaxHeight(Double.NEGATIVE_INFINITY);
//		AddToCartBtn.setMaxWidth(Double.NEGATIVE_INFINITY);
//		AddToCartBtn.setMinHeight(Double.NEGATIVE_INFINITY);
//		AddToCartBtn.setMinWidth(Double.NEGATIVE_INFINITY);
//		AddToCartBtn.setMnemonicParsing(false);
//		AddToCartBtn.setPrefHeight(40.0);
//		AddToCartBtn.setPrefWidth(178.0);
//		AddToCartBtn.getStylesheets().add("/styles/css/generalStyleSheet.css");
//		AddToCartBtn.setText("Add To Cart");

		Label priceLabel = new Label();
		priceLabel.setPrefHeight(15.0);
		priceLabel.setPrefWidth(58.0);
		priceLabel.getStyleClass().add("Label-list");
		priceLabel.setText("0");

		Label productLabel = new Label();
		productLabel.getStyleClass().add("Label");
		productLabel.setText("Product Name");

		// Create a Text and add it to the GridPane
		Text text = new Text();
		text.setFill(javafx.scene.paint.Color.valueOf("#1e3d58"));
		text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
		text.setStrokeWidth(0.0);
		text.getStyleClass().add("Label-list");
		text.setText("0");
		text.setVisible(false);
		text.setWrappingWidth(116.0390625);

		ImageView salePersentageIconImg = new ImageView();
		salePersentageIconImg.setFitHeight(48.0);
		salePersentageIconImg.setFitWidth(46.0);
		salePersentageIconImg.setPickOnBounds(true);
		salePersentageIconImg.setPreserveRatio(true);
		salePersentageIconImg.setVisible(false);
		Image saleIcon = new Image("/styles/icons/saleIcon.png");
		salePersentageIconImg.setImage(saleIcon);

		ImageView onePlusOneImg = new ImageView();
		onePlusOneImg.setFitHeight(48.0);
		onePlusOneImg.setFitWidth(46.0);
		onePlusOneImg.setPickOnBounds(true);
		onePlusOneImg.setPreserveRatio(true);
		onePlusOneImg.setVisible(false);
		Image onePlusOneSale = new Image("/styles/icons/onePlusOneSale.png");
		onePlusOneImg.setImage(onePlusOneSale);

		itemViewGridpane.add(itemImageView, 0, 0);
		itemViewGridpane.add(changeQuantityBtn, 0, 3);
	//	itemViewGridpane.add(AddToCartBtn, 0, 3);
		itemViewGridpane.add(priceLabel, 0, 2);
		itemViewGridpane.add(productLabel, 0, 1);
		itemViewGridpane.add(text, 1, 2);
		itemViewGridpane.add(salePersentageIconImg, 1, 0);
		itemViewGridpane.add(onePlusOneImg, 0, 0);
		itemViewGridpane.setPadding(new Insets(10, 10, 10, 10));

		GridPane.setValignment(onePlusOneImg, VPos.TOP);
		GridPane.setColumnIndex(salePersentageIconImg, 1);
		GridPane.setHalignment(salePersentageIconImg, HPos.RIGHT);
		GridPane.setValignment(salePersentageIconImg, VPos.TOP);
		GridPane.setColumnIndex(text, 1);
		GridPane.setRowIndex(text, 2);
		GridPane.setColumnSpan(productLabel, 2);
		GridPane.setHalignment(productLabel, HPos.LEFT);
		GridPane.setRowIndex(productLabel, 1);
//		GridPane.setColumnSpan(AddToCartBtn, 2);
//		GridPane.setRowIndex(AddToCartBtn, 3);
		GridPane.setHalignment(plusBtn, HPos.RIGHT);
		GridPane.setRowIndex(plusBtn, 0);
		GridPane.setHalignment(quantityLabel, HPos.CENTER);
		GridPane.setColumnIndex(quantityLabel, 1);
		GridPane.setMargin(itemImageView, new Insets(10, 10, 10, 10));
		GridPane.setColumnSpan(itemImageView, 2);
		GridPane.setHalignment(itemImageView, HPos.CENTER);
		GridPane.setRowSpan(itemImageView, 2);
		GridPane.setValignment(itemImageView, VPos.TOP);
		GridPane.setColumnSpan(changeQuantityBtn, 2);
		GridPane.setRowIndex(changeQuantityBtn, 3);
		GridPane.setHalignment(minusBtn, HPos.LEFT);
		GridPane.setRowIndex(minusBtn, 0);
		GridPane.setHalignment(priceLabel, HPos.LEFT);
		GridPane.setRowIndex(priceLabel, 2);
		GridPane.setColumnIndex(plusBtn, 2);
		GridPane.setHalignment(plusBtn, HPos.CENTER);
		GridPane.setValignment(plusBtn, VPos.CENTER);

		return itemViewGridpane;
	}

	/**
	 * Creates a GridPane layout container that represents a single item in the
	 * cart. This method sets the layout, dimensions, and styles of the GridPane as
	 * well as creates and adds all of the child elements (such as ImageView and
	 * Label) to the GridPane.
	 * 
	 * @return gridPane, a GridPane layout container representing a single item in
	 *         the cart
	 */
	private GridPane createSingleCartItem() {
		// Create a GridPane layout container
		GridPane gridPane = new GridPane();
		gridPane.setPrefHeight(45.0);
		gridPane.setPrefWidth(270.0);
		gridPane.getStyleClass().add("GridPaneChild");
		gridPane.getStylesheets().add("/styles/css/generalStyleSheet.css");

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setHalignment(HPos.LEFT);
		column1.setHgrow(Priority.SOMETIMES);
		column1.setMaxWidth(51.0);
		column1.setMinWidth(-1.0);
		column1.setPrefWidth(35.0);

		ColumnConstraints column2 = new ColumnConstraints();
		column2.setHalignment(HPos.CENTER);
		column2.setHgrow(Priority.SOMETIMES);
		column2.setMaxWidth(62.0);
		column2.setMinWidth(-1.0);
		column2.setPrefWidth(54.0);

		ColumnConstraints column3 = new ColumnConstraints();
		column3.setHalignment(HPos.LEFT);
		column3.setHgrow(Priority.SOMETIMES);
		column3.setMaxWidth(109.0);
		column3.setMinWidth(10.0);
		column3.setPrefWidth(60.0);

		ColumnConstraints column4 = new ColumnConstraints();
		column4.setHalignment(HPos.CENTER);
		column4.setHgrow(Priority.SOMETIMES);
		column4.setMaxWidth(-1.0);
		column4.setMinWidth(-1.0);
		column4.setPrefWidth(25.0);

		ColumnConstraints column5 = new ColumnConstraints();
		column5.setHalignment(HPos.CENTER);
		column5.setHgrow(Priority.SOMETIMES);
		column5.setMaxWidth(-1.0);
		column5.setMinWidth(-1.0);
		column5.setPrefWidth(30.0);

		ColumnConstraints column6 = new ColumnConstraints();
		column6.setHalignment(HPos.CENTER);
		column6.setHgrow(Priority.SOMETIMES);
		column6.setMaxWidth(-1.0);
		column6.setMinWidth(-1.0);
		column6.setPrefWidth(25.0);

		gridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5, column6);

		RowConstraints row1 = new RowConstraints();
		row1.setMaxHeight(-1.0);
		row1.setMinHeight(-1.0);
		row1.setPrefHeight(50.0);
		row1.setValignment(VPos.CENTER);
		row1.setVgrow(Priority.SOMETIMES);

		gridPane.getRowConstraints().add(row1);

		// childs
		ImageView itemInCartImage = new ImageView();
		itemInCartImage.setFitHeight(45.0);
		itemInCartImage.setFitWidth(45.0);
		itemInCartImage.setPickOnBounds(true);
		itemInCartImage.setPreserveRatio(true);

		Label itemInCartNameLabel = new Label();
		itemInCartNameLabel.setContentDisplay(ContentDisplay.CENTER);
		itemInCartNameLabel.setMaxHeight(-1.0);
		itemInCartNameLabel.setMaxWidth(-1.0);
		itemInCartNameLabel.setMinHeight(-1.0);
		itemInCartNameLabel.setMinWidth(-1.0);
		itemInCartNameLabel.setPrefHeight(50.0);
		itemInCartNameLabel.setPrefWidth(65.0);
		itemInCartNameLabel.getStyleClass().add("Label-list");
		itemInCartNameLabel.setText("Label");

		Insets right10 = new Insets(0, 0, 0, 10.0);

		Button minusAmountInCartBtn = new Button();
		minusAmountInCartBtn.setAlignment(Pos.CENTER);
		minusAmountInCartBtn.setMaxHeight(-1.0);
		minusAmountInCartBtn.setMaxWidth(-1.0);
		minusAmountInCartBtn.setMinHeight(-1.0);
		minusAmountInCartBtn.setMinWidth(-1.0);
		minusAmountInCartBtn.setMnemonicParsing(false);
		minusAmountInCartBtn.setPrefHeight(25.0);
		minusAmountInCartBtn.setPrefWidth(40.0);
		minusAmountInCartBtn.getStyleClass().add("Button-NoBG");
		minusAmountInCartBtn.setText("");

		ImageView minusGraphic = new ImageView();
		minusGraphic.setFitHeight(20.0);
		minusGraphic.setFitWidth(20.0);
		minusGraphic.setPickOnBounds(true);
		minusGraphic.setPreserveRatio(true);

		Image minusImage = new Image(getClass().getResource("/styles/icons/minus.png").toExternalForm());
		minusGraphic.setImage(minusImage);

		minusAmountInCartBtn.setGraphic(minusGraphic);

		Label itemInCartAmountLabel = new Label();
		itemInCartAmountLabel.setAlignment(Pos.CENTER);
		itemInCartAmountLabel.setPrefHeight(15.0);
		itemInCartAmountLabel.setPrefWidth(33.0);
		itemInCartAmountLabel.setText("0");

		Button plusAmountInCartBtn = new Button();
		plusAmountInCartBtn.getStyleClass().add("Button-NoBG");
		plusAmountInCartBtn.setText("");
		plusAmountInCartBtn.setMinHeight(-1.0);
		plusAmountInCartBtn.setMinWidth(-1.0);
		plusAmountInCartBtn.setPrefHeight(25.0);
		plusAmountInCartBtn.setPrefWidth(40.0);

		ImageView plusAmountInCartBtnGraphic = new ImageView();
		plusAmountInCartBtnGraphic.setFitHeight(20.0);
		plusAmountInCartBtnGraphic.setFitWidth(20.0);
		plusAmountInCartBtnGraphic.setPickOnBounds(true);
		plusAmountInCartBtnGraphic.setPreserveRatio(true);
		Image plusAmountInCartBtnImage = new Image(getClass().getResourceAsStream("/styles/icons/plus.png"));
		plusAmountInCartBtnGraphic.setImage(plusAmountInCartBtnImage);
		plusAmountInCartBtn.setGraphic(plusAmountInCartBtnGraphic);

		Button deleteItemBtn = new Button();
		deleteItemBtn.getStyleClass().add("Button-NoBG");
		deleteItemBtn.setText("");
		deleteItemBtn.setMinHeight(-1.0);
		deleteItemBtn.setMinWidth(-1.0);
		deleteItemBtn.setPrefHeight(30.0);
		deleteItemBtn.setPrefWidth(41.0);

		ImageView deleteItemBtnGraphic = new ImageView();
		deleteItemBtnGraphic.setFitHeight(25.0);
		deleteItemBtnGraphic.setFitWidth(42.0);
		deleteItemBtnGraphic.setPickOnBounds(true);
		deleteItemBtnGraphic.setPreserveRatio(true);
		Image deleteItemBtnImage = new Image(getClass().getResourceAsStream("/styles/icons/xIcon_noCircle.png"));
		deleteItemBtnGraphic.setImage(deleteItemBtnImage);
		deleteItemBtn.setGraphic(deleteItemBtnGraphic);

		gridPane.add(itemInCartImage, 1, 0);
		gridPane.add(itemInCartNameLabel, 2, 0);
		gridPane.add(minusAmountInCartBtn, 3, 0);
		gridPane.add(itemInCartAmountLabel, 4, 0);
		gridPane.add(plusAmountInCartBtn, 5, 0);
		gridPane.add(deleteItemBtn, 0, 0);

		GridPane.setHalignment(minusAmountInCartBtn, HPos.CENTER);

		GridPane.setHalignment(itemInCartNameLabel, HPos.LEFT);
		GridPane.setValignment(itemInCartNameLabel, VPos.CENTER);

		GridPane.setMargin(itemInCartNameLabel, right10);
		gridPane.setPadding(right10);
		return gridPane;
	}

	/**
	 * This method is used to show the description of the ViewCatalog screen when
	 * the 'description' button is pressed. It creates a popup window with the
	 * information about the screen, using the CommonFunctions class and the
	 * ScreensNamesEnum.
	 * 
	 * @param event the event triggered when the 'description' button is pressed
	 */
	@FXML
	void showDescription(ActionEvent event) {
		PopupSetter.createPopup(PopupTypeEnum.Information, ScreensNamesEnum.ViewCatalog.getDescription());
	}

}
