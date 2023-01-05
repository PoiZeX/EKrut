package controllerGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import Store.NavigationStoreController;
import common.Message;
import common.ScreensNames;
import common.TaskType;
import entity.ItemInMachineEntity;
import entity.UserEntity;
import entity.ItemInMachineEntity.Call_Status;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import utils.AppConfig;

public class ViewCatalogContoller {
    @FXML
    private Button viewCartBtn;
    @FXML
    private ImageView itemImageView;
    @FXML
    private Button AddToCartBtn;

    @FXML
    private Button cancelOrderBtn;
    @FXML
    private GridPane itemViewGridpane;

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

    private static Map<String, ItemInMachineEntity> itemsList;
    
    public void initialize() {
    	itemsList = new HashMap<>();
		// demo
		ItemInMachineEntity newItem = new ItemInMachineEntity(0, 0, 50, ItemInMachineEntity.Call_Status.NotOpened, 0, 0, "Bamba", 100.0,  "Bamba.png");
    	newItem.setImg_relative_path(AppConfig.PRODUCTS_PATH_CLIENT);
		addProductToCatalog(newItem, 0);
	}
    
    // THIS NEEDS TO BE MOVED // ***
    // public ItemInMachineEntity(int machineId, int item_id, int currentAmount, Call_Status callStatus, int timeUnderMin, 
    // int workerId, String name, double price, String manufacturer, String description, String item_img_nam)
    public void addProductToCatalog(ItemInMachineEntity item, int discountPrice) {
    	itemsList.put(item.getName(), item);
    	ObservableList<Node> itemList = catalogViewGridpane.getChildren();
    	GridPane itemView = (GridPane) itemList.get(0);
    	
    	ImageView image = (ImageView) itemView.getChildren().get(0);
    	Button addToCartBtn = (Button) itemView.getChildren().get(1);
    	Label priceLabel = (Label) itemView.getChildren().get(2);
    	Label productNameLabel = (Label) itemView.getChildren().get(3);
    	Label discountPriceLabel = (Label) itemView.getChildren().get(4);
    	
    	discountPriceLabel.setText(discountPrice + "");
    	productNameLabel.setText(item.getName());
    	priceLabel.setText(item.getPrice() + "₪");
    	//image.setImage(new Image(getClass().getResourceAsStream(item.getImg_relative_path() + item.getItemImg().getImgName())));
    	
    	GridPane newItemView = new GridPane();
    	newItemView.setMinSize(210, 250);
    	newItemView.getStyleClass().add("GridPaneChild");
    	newItemView.setPadding(new Insets(10));
    	addToCartBtn.setAlignment(Pos.CENTER);
    	
//    	newItemView.getChildren().add(image);
//    	newItemView.getChildren().add(addToCartBtn);
//    	newItemView.getChildren().add(priceLabel);
//    	newItemView.getChildren().add(productNameLabel);
//    	newItemView.getChildren().add(discountPriceLabel);
    	ColumnConstraints cc0 = new ColumnConstraints(50); 
    	cc0.setMinWidth(Region.USE_PREF_SIZE); 
    	cc0.setMaxWidth(Region.USE_PREF_SIZE); 
    	newItemView.getColumnConstraints().addAll(cc0);
    	
    	RowConstraints rc0 = new RowConstraints(135);
    	RowConstraints rc1 = new RowConstraints(25);
    	RowConstraints rc2 = new RowConstraints(20);
    	RowConstraints rc3 = new RowConstraints(50);
    	newItemView.getRowConstraints().addAll(rc0, rc1, rc2, rc3);
    	
    	newItemView.add(image, 0, 0);
    	newItemView.add(discountPriceLabel, 1, 2);
    	newItemView.add(priceLabel, 0, 2);
    	newItemView.add(productNameLabel, 0, 1);
    	newItemView.add(addToCartBtn, 0, 3);
    	
    	catalogViewGridpane.add(newItemView, 2, 0);
    }
    
    // ********************** // ***
    @FXML
    void cancelOrder(ActionEvent event) {

    }

    @FXML
    void placeOrder(ActionEvent event) {
    	NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ReviewOrder);
    }
    

    @FXML
    void viewCart(ActionEvent event) {

    }

}
