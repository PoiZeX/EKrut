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
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
	private Button viewCartBtn;
	
    @FXML
    private GridPane cartViewGridpane;

	@FXML
	private ImageView itemImageView;

	@FXML
	private Button AddToCartBtn;

	@FXML
	private Button cancelOrderBtn;

	@FXML
	private GridPane itemViewGridpane;

	@FXML
	private ButtonBar changeQuantityBtn;

	@FXML
	private GridPane catalogViewGridpane;

	@FXML
	private Label discountPriceLabel;

	@FXML
	private Label discountPriceLabel1;

	@FXML
	private Button minusBtn;

	@FXML
	private Button placeOrderBtn;

	@FXML
	private Button plusBtn;

	@FXML
	private Label priceLabel;

	@FXML
	private Label priceLabel1;

	@FXML
	private Label productLabel;

	@FXML
	private Label productLabel1;

	@FXML
	private Label quntityLabel;

	@FXML
	private Button searchBtn;

	@FXML
	private Button sortBtn;
	
    @FXML
    private Pane viewCartPane;

	private int machineDiscount = 0;

	private int machineId = 7;
	private static Map<String, ItemInMachineEntity> itemsList;
	private static Map<String, Integer> itemsInCartList;
	private static ClientController chat = HostClientController.chat; // define the chat for th
	private static boolean recievedData = false;

	private boolean visibleViewCart = true;

	public void initialize() throws InterruptedException {
		//viewCartGridpane.setVisible(false);
		itemsList = new LinkedHashMap<>();
		itemsInCartList = new LinkedHashMap<>();
		chat.acceptObj(new Message(TaskType.RequestItemsInMachine, machineId));
		while (!recievedData)
			Thread.sleep(100);
		generateCatalog();
//		recievedData = false;
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
	// public ItemInMachineEntity(int machineId, int item_id, int currentAmount,
	// Call_Status callStatus, int timeUnderMin,
	// int workerId, String name, double price, String manufacturer, String
	// description, String item_img_nam)
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
//			image.setImage(new Image(getClass().getResourceAsStream(
//					AppConfig.RELAITVE_PRODUCTS_PATH + item.getItemImg().getImgName())));
			
			
			// FOR DAVID
			GridPane newItemInCart = createGridPane("ItemInViewCartBoundary");
			ImageView newItemInCartimage = (ImageView) newItemInCart.getChildren().get(0);
			Label itemInCartNameLabel = (Label) newItemInCart.getChildren().get(1);
			Label itemInCartAmountLabel = (Label) newItemInCart.getChildren().get(2);
			Button itemInCartminusBtn = (Button) newItemInCart.getChildren().get(3);
			Button itemInCartplusBtn = (Button) newItemInCart.getChildren().get(4);
			// FOR DAVID
			itemInCartNameLabel.setText(item.getName());
			
			addToCartBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					addToCartBtn.setVisible(false);
					amountLabel.setText("1");
					int amount = Integer.parseInt(amountLabel.getText());
					
					itemsInCartList.put(item.getName(), amount);
					itemInCartAmountLabel.setText(amount + "");
					cartViewGridpane.add(newItemInCart, 0, 0);
					
					System.out.println(itemsInCartList);
				}
			});
			
			plusBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					int amount = Integer.parseInt(amountLabel.getText());
					amountLabel.setText(String.valueOf(amount + 1));
					amount = Integer.parseInt(amountLabel.getText());
					if (amount == item.getCurrentAmount()) {
						plusBtn.setDisable(true);
					}
					// Update amount in list
					itemsInCartList.put(item.getName(), amount);
					itemInCartAmountLabel.setText(amount + "");
					System.out.println(itemsInCartList);
				}
			});
			
			minusBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent e) {
					int amount = Integer.parseInt(amountLabel.getText());
					amountLabel.setText(String.valueOf(amount - 1));
					amount = Integer.parseInt(amountLabel.getText());
					if (plusBtn.isDisabled()) {
						plusBtn.setDisable(false);
					}
					if (amount == 0) {
						addToCartBtn.setVisible(true);
						cartViewGridpane.getChildren().remove(0);
						itemsInCartList.remove(item.getName());
					}
					else {
						itemInCartAmountLabel.setText(amount + "");
						itemsInCartList.put(item.getName(), amount);
					}
					System.out.println(itemsInCartList);
				}
			});
			
			catalogViewGridpane.add(newItem, i, j);

		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}

	@FXML
	void cancelOrder(ActionEvent event) {

	}

	@FXML
	void placeOrder(ActionEvent event) {
		NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ReviewOrder);
	}

	@FXML
	void viewCart(ActionEvent event) {
		if (viewCartPane.isVisible()) {
			viewCartPane.setVisible(false);
		}
		else
			viewCartPane.setVisible(true);

	}

	private GridPane createGridPane(String boundaryName) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/boundary/" + boundaryName + ".fxml"));
		GridPane gridPane = (GridPane) loader.load();
		return gridPane;
	}
	/**
	 * Receives the items from a specific machine
	 * @param obj
	 */
	public static void recevieItemsInMachine(ArrayList<ItemInMachineEntity> obj) {
		if (!itemsList.isEmpty()) {
			itemsList.clear();
		}
		for (ItemInMachineEntity item : obj) {
			itemsList.put(item.getName(), item);
		}
		recievedData  = true;
	}
}
