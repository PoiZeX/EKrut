package controllerGui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import utils.TooltipSetter;

public class ReviewOrderController {
	
	private TooltipSetter tooltip;

    @FXML
    private ComboBox<String> MachinesCmb;

    @FXML
    private Button MinusQuantityBtn22211;

    @FXML
    private Label ReviewOrderTitleLbl;

    @FXML
    private Label TitleLbl;

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
    private RadioButton deliveryRb;

    @FXML
    private TextField firstNameTxtField;

    @FXML
    private TextField lastNameTxtField;

    @FXML
    private AnchorPane paymentAnchorPane;

    @FXML
    private Button paymentBtn;

    @FXML
    private TextField phoneNumTxtField;

    @FXML
    private Label priceLbl11;

    @FXML
    private Label priceLbl111;

    @FXML
    private Label priceLbl112;

    @FXML
    private Label priceLbl113;

    @FXML
    private Label priceLbl1131;

    @FXML
    private Label priceLbl114;

    @FXML
    private Label priceLbl1141;

    @FXML
    private Label priceLbl11411;

    @FXML
    private ImageView productItemImg2221;

    @FXML
    private ImageView productItemImg22211;

    @FXML
    private ImageView productItemImg22212;

    @FXML
    private Label productNameLbl11;

    @FXML
    private Label productNameLbl111;

    @FXML
    private Label productNameLbl112;

    @FXML
    private ScrollPane reviewOrderScrollPane;

    @FXML
    private RadioButton selfPickRb;

    @FXML
    private ToggleGroup shippmentSelcetion;

    @FXML
    private TextField streetTxtField;

    @FXML
    private Label totalSumLbl;

    @FXML
    private Label totulDiscountSumLbl;

    @FXML
    private Label totulProductsSumLbl;

    @FXML
    private Label addressDetailsLbl;
    
    private boolean isSelfPickup;
    
    public void initialize() {
    	//tooltip = new TooltipSetter("Cancel the order");
    	//cancelOrderBtn.setTooltip(tooltip.getTooltip());
    	shippingToggleVisInit();
    	
        ObservableList<String> ol = FXCollections.observableArrayList();
        ol.add("Big Karmiel");
        ol.add("Ort Brauda");
        ol.add("City hall");
        ol.add("Psagot high-school");
        ol.add("Lev Karmiel mall");
    	MachinesCmb.setItems(ol);
    }
    
    private void shippingToggleVisInit() {
		contactDetailsLbl.setVisible(false);
		contactDetalisGridPane.setVisible(false);
		
		addressDetailsLbl.setVisible(false);
		addressDetalisGridPane.setVisible(false);
		MachinesCmb.setVisible(false);
		
    	deliveryRb.selectedProperty().addListener(((observable, oldValue, newValue) -> {
    		shippingChangeVisibility(false);
    	}));

    	selfPickRb.selectedProperty().addListener(((observable, oldValue, newValue) -> {
    		shippingChangeVisibility(true);

    	}));
    }
    private void shippingChangeVisibility(boolean isSelfPickup) {
    	if(isSelfPickup) {
    		contactDetailsLbl.setVisible(false);
    		contactDetalisGridPane.setVisible(false);
    		
    		addressDetailsLbl.setVisible(false);
    		addressDetalisGridPane.setVisible(false);
    		MachinesCmb.setVisible(true);

    	}
    	else {
    		contactDetailsLbl.setVisible(true);
    		contactDetalisGridPane.setVisible(true);
    		
    		addressDetailsLbl.setVisible(true);
    		addressDetalisGridPane.setVisible(true);
    		MachinesCmb.setVisible(false);
    	}
		this.isSelfPickup = isSelfPickup;
    }
}