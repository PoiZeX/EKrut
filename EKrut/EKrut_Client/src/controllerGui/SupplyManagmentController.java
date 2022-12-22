package controllerGui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class SupplyManagmentController {

    @FXML
    private TableColumn<?, ?> callStatusCall;

    @FXML
    private ComboBox<?> callStatusCmb;

    @FXML
    private Label callStatusLbl;

    @FXML
    private Label callStatusTitleLbl;

    @FXML
    private ComboBox<?> chooseWorkerCmb;

    @FXML
    private TableColumn<?, ?> currentAmountCol;

    @FXML
    private Label currentAmountLbl;

    @FXML
    private GridPane itemDisplayGridPane;

    @FXML
    private TableColumn<?, ?> itemIdCol;

    @FXML
    private ImageView itemImg;

    @FXML
    private Label itemNameLbl;

    @FXML
    private ComboBox<?> machineCmb;

    @FXML
    private TableColumn<?, ?> minAmountCol;

    @FXML
    private TextField minAmountTxtField;

    @FXML
    private TableColumn<?, ?> previewEyeBtnCol;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button saveItemChangesBtn;

    @FXML
    private Button sendCallBtn;

    @FXML
    private Button showItemsInTableBtn;

    @FXML
    private TableView<?> supplyMangmentTbl;

    @FXML
    private Label titleLbl;

    @FXML
    private Label workerLbl;

}
