package controllerGui;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SupplyReportController {

    @FXML
    private TableColumn<?, ?> currentAmountCol;

    @FXML
    private TableColumn<?, ?> itemIDCol;

    @FXML
    private TableColumn<?, ?> minAmountCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private ComboBox<?> saleMachineCmb;

    @FXML
    private TableColumn<?, ?> startAmountCol;

    @FXML
    private TableView<?> supplyMachineTbl;

}
