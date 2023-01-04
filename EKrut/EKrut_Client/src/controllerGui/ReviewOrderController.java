package controllerGui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import controller.OrderController;
import entity.ItemInMachineEntity;
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
import utils.TooltipSetter;

public class ReviewOrderController {
	
	private TooltipSetter tooltip;


    @FXML
    private Label ReviewOrderTitleLbl;

    @FXML
    private Label addressDetailsLbl;

    @FXML
    private GridPane addressDetalisGridPane;

    @FXML
    private TextField cityTxtField;

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
    private TextField streetTxtField;

    @FXML
    private Label totalSumLbl;

    @FXML
    private Label totulDiscountSumLbl;

    @FXML
    private Label totulProductsSumLbl;
    
    @FXML
    private GridPane productsGrid;
    
    private boolean isSelfPickup;
    
    private HashMap<Integer, ItemInMachineEntity> cart;
    
    public void initialize() {
    	//tooltip = new TooltipSetter("Cancel the order");
    	//cancelOrderBtn.setTooltip(tooltip.getTooltip());

    	cart = (HashMap<Integer, ItemInMachineEntity>)OrderController.getCart();
    	
    	// build graphical side
    	
    	// calculate data
    	
    	// -- get sales / discounts (if subscriber)
    	
    	// check if OL/EK (for delivery)
    	
    	
    	// add option for future payment
    	// make popup external payment
    	
    }
    
    /**
     * Build all graphical side for all items
     */
    private void buildReviewOrder() {
    	ArrayList<GridPane> gridsToAdd = new ArrayList<>();
    	for(ItemInMachineEntity item : cart.values())
    	{
    		gridsToAdd.add(buildSingleRow(item));
    	}    	
    	
    	// add all to main gridpane
    	int index = 2;
    	for(GridPane grid : gridsToAdd) 
    		productsGrid.add(grid, 0, index++);
    }

    /**
     * Build graphical side for single item
     * @param item
     */
    private GridPane buildSingleRow(ItemInMachineEntity item) {
    	GridPane gridpane = new GridPane();
    	Label productName = new Label();
    	Label price = new Label();
    	Label quantity = new Label();
    	Label sum = new Label();
    	Line line = new Line();
    	Image image = new Image(item.getImg_relative_path());
    	ImageView imageView = new ImageView(image);
    	double tempSum = 0;
    	
    	gridpane.setPrefSize(348, 69);
    	gridpane.getColumnConstraints().add(new ColumnConstraints(10, 70, 107, Priority.SOMETIMES, HPos.CENTER, false));
    	gridpane.getColumnConstraints().add(new ColumnConstraints(10, 192, 207, Priority.SOMETIMES, HPos.CENTER, false));
    	gridpane.getColumnConstraints().add(new ColumnConstraints(10, 57, 161, Priority.SOMETIMES, HPos.CENTER, false));
    	gridpane.getColumnConstraints().add(new ColumnConstraints(10, 64, 230, Priority.SOMETIMES, HPos.CENTER, false));
    	gridpane.getRowConstraints().add(new RowConstraints(10.0, 31, 47));
    	gridpane.getRowConstraints().add(new RowConstraints(10.0, 32, 60));
    	gridpane.getRowConstraints().add(new RowConstraints(10.0, 32, 60));
    	
    	// product
    	productName.setText(item.getName());
    	productName.setPrefSize(229, 18);
    	productName.getStylesheets().add("Label-list");
    	gridpane.addColumn(1, productName);
    	GridPane.setHalignment(productName, HPos.LEFT);
    	
        // price
    	price.setText(String.valueOf(item.getPrice()));
    	price.setPrefSize(262, 18);
    	price.getStylesheets().add("Label-list");
    	gridpane.addColumn(1, price);
    	gridpane.addRow(1, price);
    	GridPane.setHalignment(price, HPos.LEFT);
    	
    	// quantity
    	tempSum = item.getPrice();
    	quantity.setText(String.valueOf(item.getCurrentAmount()));
    	quantity.setPrefSize(57, 18);
    	quantity.getStylesheets().add("Label-list");
    	gridpane.addColumn(2, quantity);
    	GridPane.setHalignment(quantity, HPos.LEFT);
    	
    	// sum
    	tempSum *= item.getCurrentAmount();
    	sum.setText(String.valueOf(tempSum));
    	sum.setPrefSize(62, 18);
    	sum.getStylesheets().add("Label-list");
    	gridpane.addColumn(3, sum);
    	GridPane.setHalignment(sum, HPos.LEFT);
    	
    	// line
    	line.setStartX(-72.1041259765625);
    	line.setStartY(0.7216961979866028);
    	line.setEndX(265.18878173828125);
    	line.setEndY(0.7214934229850769);
    	line.setFill(Paint.valueOf("#908e8e"));
    	GridPane.setValignment(line, VPos.BOTTOM);
    	gridpane.addRow(2, line);
    	GridPane.setRowSpan(line, 5);

    	return gridpane;
    	
    }


    
}