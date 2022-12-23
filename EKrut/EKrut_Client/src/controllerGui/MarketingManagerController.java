package controllerGui;

import common.SaleType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class MarketingManagerController {

    @FXML
    private Label EndTimeLbl;

    @FXML
    private Label StartTimeLbl;

    @FXML
    private Button createBtn;

    @FXML
    private Label endDateLbl;

    @FXML
    private Label errLbl;

    @FXML
    private ComboBox<String> regionCmb;

    @FXML
    private Label regionLabel;

    @FXML
    private Label startDateLbl;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<SaleType> typeCmb;

    @FXML
    private Label typeLbl;


    @FXML
    void createSale(ActionEvent event) {

    }

    @FXML
    void endDate(ActionEvent event) {

    }

    @FXML
    void endTime(ActionEvent event) {

    }

    @FXML
    void startDate(ActionEvent event) {

    }

    @FXML
    void startTime(ActionEvent event) {

    }

}
