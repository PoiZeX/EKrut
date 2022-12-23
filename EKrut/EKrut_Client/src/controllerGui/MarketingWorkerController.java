package controllerGui;

import common.SaleType;
import entity.SaleEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

public class MarketingWorkerController {

    @FXML
    private TableColumn<SaleEntity, String> endDateCol;

    @FXML
    private TableColumn<SaleEntity, String> endTimeCol;

    @FXML
    private TableColumn<SaleEntity, String> startDateCol;

    @FXML
    private TableColumn<SaleEntity, String> startTimeCol;

    @FXML
    private TableColumn<SaleEntity, String> statusCol;

    @FXML
    private TableView<SaleEntity> usersTable;

    @FXML
    private TableColumn<SaleEntity, SaleType> saleTypeCol;
    
    @FXML
    private ImageView refreshBtn;

    @FXML
    private Button refreshBtn1;

    @FXML
    private Button returnBtn;
    
    @FXML
    private ImageView saveBtn;

    @FXML
    void refresh(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

}
