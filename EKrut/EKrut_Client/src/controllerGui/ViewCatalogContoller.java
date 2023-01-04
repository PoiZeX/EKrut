package controllerGui;

import java.util.ArrayList;
import java.util.Timer;

import common.Message;
import common.TaskType;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ViewCatalogContoller {

    @FXML
    private Button AddToCartBtn;

    @FXML
    private Button cancelOrderBtn;

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

    private static ArrayList<ProductInCatalog> productList;
    private class ProductInCatalog {
    	private String productName, price, discountPrice;
    	public ProductInCatalog(String productName, String price, String discountPrice) {
    		this.discountPrice = discountPrice;
    		this.price = price;
    		this.productName = productName;
    	}
		public String getProductName() {
			return productName;
		}
		public String getPrice() {
			return price;
		}
		public String getDiscountPrice() {
			return discountPrice;
		}
    }
    
    
    public void initialize() {
		productList = new ArrayList<>();
	}
    // THIS NEEDS TO BE MOVED // ***
    public static void addProductToCatalog(String productName, String price, String discountPrice) {
    	ViewCatalogContoller.ProductInCatalog newProduct = new ViewCatalogContoller().new ProductInCatalog(productName, price, discountPrice);
    	
    }
    // ********************** // ***
    @FXML
    void cancelOrder(ActionEvent event) {

    }

    @FXML
    void placeOrder(ActionEvent event) {

    }

}
