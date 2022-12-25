package controllerGui;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import common.CommonData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
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
    private ComboBox<String> regionCmb;

    @FXML
    private Label regionLabel;

    @FXML
    private Label startDateLbl;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;


    @FXML
    private ComboBox<LocalTime> startTimeCmb;

    @FXML
    private ComboBox<?> typeCmb;

    @FXML
    private Label typeLbl;
    
    @FXML
	// Setup screen before launching view
	public void initialize() throws Exception {
    	initTimeCmb();
    	ObservableList<String> regions = FXCollections.observableArrayList(CommonData.getRegions());
		regionCmb.setItems(regions);
		Locale.setDefault(Locale.ENGLISH);
		startDatePicker.setValue(LocalDate.now());
		endDatePicker.setValue(LocalDate.now());
		
		StringConverter converter =convertDate();
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
    private void initTimeCmb() {
    	ObservableList<LocalTime> times = FXCollections.observableArrayList();
   	 	for (int i = 0; i < 24; i++) {
                LocalTime time = LocalTime.of(i, 0);
                times.add(time);
        	}
		startTimeCmb.setItems(times);
		endTimeCmb.setItems(times);
    }
    private StringConverter convertDate() {
    	 StringConverter converter = new StringConverter<LocalDate>() {
             DateTimeFormatter dateFormatter = 
                 DateTimeFormatter.ofPattern("dd/mm/yyyy");
             @Override
             public String toString(LocalDate date) {
                 if (date != null) {
                     return dateFormatter.format(date);
                 } else {
                     return "";
                 }
             }
             @Override
             public LocalDate fromString(String string) {
                 if (string != null && !string.isEmpty()) {
                     return LocalDate.parse(string, dateFormatter);
                 } else {
                     return null;
                 }
             }
         };
		return converter;   
    }
    
    
}
