package controllerGui;



import common.DeliveryStatus;
import entity.AddressEntity;
import entity.DeliveryEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DeliveryManagementController {

    @FXML
    private TableColumn<DeliveryEntity, String> actualTimeCol;

    @FXML
    private TableColumn<DeliveryEntity, AddressEntity> addressCol;

    @FXML
    private TableColumn<DeliveryEntity, Integer> costumerIdCol;

    @FXML
    private TableView<DeliveryEntity> deliveryTable;

    @FXML
    private TableColumn<DeliveryEntity, String> estimatedTimeCol;
    
    @FXML
    private TableColumn<DeliveryEntity, Integer> orderIdCol;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TableColumn<DeliveryEntity, DeliveryStatus> statusCol;

    @FXML
    void refresh(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {

    }

}
