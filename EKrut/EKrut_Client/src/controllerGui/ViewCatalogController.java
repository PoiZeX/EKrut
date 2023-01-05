package controllerGui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;

import Store.NavigationStoreController;
import client.ClientController;
import common.Message;
import common.ScreensNames;
import common.TaskType;
import entity.ItemInMachineEntity;
import entity.UserEntity;
import entity.ItemInMachineEntity.Call_Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import utils.AppConfig;

public class ViewCatalogController {

    @FXML
    private BorderPane viewCatalogBorderpane;

    @FXML
    private Label cartSizeLabel;

    @FXML
    private Label totalPriceLabel;

	@FXML
	private Button placeOrderBtn;

	@FXML
	private Button cancelOrderBtn;

	@FXML
	private Button viewCartBtn;

	@FXML
	private TextField searchTextLabel;

	@FXML
	private Button sortBtn;

	@FXML
	private Button searchBtn;

	@FXML
	private GridPane catalogViewGridpane;

	@FXML
	private Pane viewCartPane;

	@FXML
	private GridPane cartViewGridpane;

	private int machineDiscount = 0;
	private int machineId = 7;
	private static Map<String, ItemInMachineEntity> itemsList;
	private static Map<ItemInMachineEntity, Integer> itemsInCartList;
	private static ClientController chat = HostClientController.chat; // define the chat for th
	private static boolean recievedData = false;

	public void initialize() throws InterruptedException {
		itemsList = new LinkedHashMap<>();
		itemsInCartList = new LinkedHashMap<>();
		chat.acceptObj(new Message(TaskType.RequestItemsInMachine, machineId));
		while (!recievedData)
			Thread.sleep(100);
		generateCatalog();
		viewCartPane.setVisible(false);
		viewCartPane.setMouseTransparent(true);
//		recievedData = false;
	}

	@FXML
	void cancelOrder(ActionEvent event) {
		System.out.println("CANCEL");
	}

	@FXML
	void placeOrder(ActionEvent event) {
		NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ReviewOrder);
	}

	@FXML
	void searchItem(ActionEvent event) {
		System.out.println("SEARCH");
	}

	@FXML
	void sortCatalog(ActionEvent event) {
		System.out.println("SORT");
	}

	@FXML
	void viewCart(ActionEvent event) {
		viewCartPane.setVisible(!viewCartPane.isVisible());
	}

	private GridPane createGridPane(String boundaryName) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundary/" + boundaryName + ".fxml"));
		GridPane gridPane = (GridPane) loader.load();
		return gridPane;
	}

	/**
	 * Receives the items from a specific machine
	 * 
	 * @param obj
	 */
	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		if (!itemsList.isEmpty()) {
			itemsList.clear();
		}
		for (ItemInMachineEntity item : obj) {
			itemsList.put(item.getName(), item);
		}
		recievedData = true;
	}

	private void generateCatalog() {
		int i = 0, j = 0;
		for (ItemInMachineEntity item : itemsList.values()) {
			if (i == 4) {
				j++;
				i = 0;
			}
			generateItem(item, machineDiscount, i, j);
			i++;
		}
	}

	// THIS NEEDS TO BE MOVED // ***
	public void generateItem(ItemInMachineEntity item, int discountPrice, int i, int j) {
		try {
			GridPane newItem = createGridPane("ItemGridBoundary");
			ImageView image = (ImageView) newItem.getChildren().get(0);
			GridPane btnBar = (GridPane) ((ButtonBar) newItem.getChildren().get(1)).getButtons().get(0);
			Button minusBtn = (Button) btnBar.getChildren().get(0);
			Label amountLabel = (Label) btnBar.getChildren().get(1);
			Button plusBtn = (Button) btnBar.getChildren().get(2);
			Button addToCartBtn = (Button) newItem.getChildren().get(2);
			Label priceLabel = (Label) newItem.getChildren().get(3);
			Label productNameLabel = (Label) newItem.getChildren().get(4);
			Label discountPriceLabel = (Label) newItem.getChildren().get(5);

			discountPriceLabel.setText(discountPrice + "");
			productNameLabel.setText(item.getName());
			priceLabel.setText(item.getPrice() + "₪");
			image.setImage(new Image(
					getClass().getResourceAsStream(AppConfig.RELAITVE_PRODUCTS_PATH + item.getItemImg().getImgName())));

			GridPane newItemInCart = createGridPane("ItemInViewCartBoundary");
			ImageView newItemInCartImage = (ImageView) newItemInCart.getChildren().get(0);
			Label itemInCartNameLabel = (Label) newItemInCart.getChildren().get(1);
			Label itemInCartAmountLabel = (Label) newItemInCart.getChildren().get(3);
			Button itemInCartMinusBtn = (Button) newItemInCart.getChildren().get(2);
			Button itemInCartPlusBtn = (Button) newItemInCart.getChildren().get(4);
			itemInCartNameLabel.setText(item.getName());
			newItemInCartImage.setImage(new Image(getClass().getResourceAsStream(
					AppConfig.RELAITVE_PRODUCTS_PATH + item.getItemImg().getImgName())));
			
			addToCartBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					if (item.getCurrentAmount() > 0) {
						addToCartBtn.setOpacity(0);
						addToCartBtn.setMouseTransparent(true);
						
						amountLabel.setText("1");
						int amount = Integer.parseInt(amountLabel.getText());
						itemInCartAmountLabel.setText(amountLabel.getText());
						cartViewGridpane.add(newItemInCart, 0, cartViewGridpane.getChildren().size());
						if (item.getCurrentAmount() == 1) {
							plusBtn.setDisable(true);
							itemInCartPlusBtn.setDisable(true);
						}

						itemsInCartList.put(item, amount);
					} else
						addToCartBtn.setText("Not Available");
					viewCartPane.setVisible(false);
					viewCartPane.setMouseTransparent(false);
					updateCart();
				}
			});

			plusBtn.setOnMouseClicked(getPlusEvent(amountLabel, plusBtn, itemInCartPlusBtn, addToCartBtn, newItemInCart,
					item, itemInCartAmountLabel, true));
			minusBtn.setOnMouseClicked(getMinusEvent(amountLabel, plusBtn, itemInCartPlusBtn, addToCartBtn,
					newItemInCart, item, itemInCartAmountLabel, true));
			itemInCartPlusBtn.setOnMouseClicked(getPlusEvent(amountLabel, plusBtn, itemInCartPlusBtn, addToCartBtn,
					newItemInCart, item, itemInCartAmountLabel, false));
			itemInCartMinusBtn.setOnMouseClicked(getMinusEvent(amountLabel, plusBtn, itemInCartPlusBtn, addToCartBtn,
					newItemInCart, item, itemInCartAmountLabel, false));
			catalogViewGridpane.add(newItem, i, j);

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private EventHandler<MouseEvent> getMinusEvent(Label amountLabel, Button plusBtn, Button itemInCartPlusBtn,
			Button addToCartBtn, GridPane newItemInCart, ItemInMachineEntity item, Label itemInCartAmountLabel,
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
					addToCartBtn.setOpacity(1);
					addToCartBtn.setMouseTransparent(false);
					itemsInCartList.remove(item);
					cartViewGridpane.getChildren().remove(cartViewGridpane.getChildren().indexOf(newItemInCart));
					reorderCart(cartViewGridpane);
				} else {
					itemInCartAmountLabel.setText(amountLabel.getText());
					itemsInCartList.put(item, amount);
				}
				if (flag) {
					viewCartPane.setVisible(false);
					viewCartPane.setMouseTransparent(false);
				}
				
				updateCart();

				
			}
		};
		return minusEvent;
	}

	private EventHandler<MouseEvent> getPlusEvent(Label amountLabel, Button plusBtn, Button itemInCartPlusBtn,
			Button addToCartBtn, GridPane newItemInCart, ItemInMachineEntity item, Label itemInCartAmountLabel,
			boolean flag) {
		EventHandler<MouseEvent> plusEvent = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				int amount = Integer.parseInt(amountLabel.getText());
				amountLabel.setText(String.valueOf(amount + 1));
				amount = Integer.parseInt(amountLabel.getText());
				if (amount == item.getCurrentAmount()) {
					plusBtn.setDisable(true);
					itemInCartPlusBtn.setDisable(true);
				}
				itemsInCartList.put(item, amount);
				itemInCartAmountLabel.setText(amountLabel.getText());
				if (flag) {
					viewCartPane.setVisible(false);
					viewCartPane.setMouseTransparent(false);
				}
				updateCart();
			}
		};
		return plusEvent;
	}

	private void updateCart() {
		// Calculate cart size and total price
		int amountOfItemsInCart = 0;
		int totalPriceOfItemsInCart = 0;
		for (ItemInMachineEntity item : itemsInCartList.keySet()) {
			amountOfItemsInCart += itemsInCartList.get(item);
			totalPriceOfItemsInCart += (itemsInCartList.get(item) * item.getPrice());
		}
		cartSizeLabel.setText(amountOfItemsInCart + " Items");
		totalPriceLabel.setText("Total: " + totalPriceOfItemsInCart + "₪");
	}
	
	private void reorderCart(GridPane cartViewGridpane) {
		ObservableList<Node> tempItems = FXCollections.observableArrayList(cartViewGridpane.getChildren());
		cartViewGridpane.getChildren().clear();
		int i = 0;
		for (Node n : tempItems) {
			cartViewGridpane.add(n, 0, i);
			i++;
		}
	}
}
