package controllerGui;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import utils.TooltipSetter;

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
    private ComboBox<LocalTime> endTimeCmb;

    @FXML
    private Label errLbl;

    @FXML
    private ComboBox<?> regionCmb;

    @FXML
    private Label regionLabel;

    @FXML
    private Label startDateLbl;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<LocalTime> startTimeCmb;

    @FXML
    private ComboBox<?> typeCmb;

    @FXML
    private Label typeLbl;
    
    @FXML
	// Setup screen before launching view
	public void initialize() throws Exception {
    	
    	ObservableList<LocalTime> times = FXCollections.observableArrayList();
    	 for (int i = 0; i < 24; i++) {
                 LocalTime time = LocalTime.of(i, 0);
                 times.add(time);
         	}
		startTimeCmb.setItems(times);
		endTimeCmb.setItems(times);
	}
    @FXML
    void createSale(ActionEvent event) {

    }

    @FXML
    void endDate(ActionEvent event) {

    }

    @FXML
    void startDate(ActionEvent event) {

    }

}
