package controllerGui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ReviewOrderController {

    @FXML
    private ComboBox<?> MachinesCmb;

    @FXML
    private Label ReviewOrderTitleLbl;

    @FXML
    private Label TitleLbl;

    @FXML
    private GridPane addressDetalisGridPane;

    @FXML
    private Button cancleOrderBtn;

    @FXML
    private TextField cityTxtField;

    @FXML
    private GridPane contactDetalisGridPane;

    @FXML
    private Label contuctDetailsLbl;

    @FXML
    private RadioButton deliveryRb;

    @FXML
    private TextField firstNameTxtField1;

    @FXML
    private TextField lastNameTxtField;

    @FXML
    private AnchorPane paymentAnchorPane;

    @FXML
    private Button paymentBtn;

    @FXML
    private TextField phoneNumTxtField;

    @FXML
    private ScrollPane reviewOrderScrollPane;

    @FXML
    private RadioButton selfPickRb;

    @FXML
    private ToggleGroup shippmentSelcetion;

    @FXML
    private TextField streetTxtField;

    @FXML
    private Label totulDisscountSumLbl;

    @FXML
    private Label totulProductsSumLbl;

    @FXML
    private Label totulSumLbl;

}
