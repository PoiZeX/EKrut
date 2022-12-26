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

import client.ClientController;
import common.CommonData;
import common.DeliveryStatus;
import common.Message;
import common.SaleType;
import common.TaskType;
import entity.SaleEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Callback;
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
    private ComboBox<String> typeCmb;

    @FXML
    private Label typeLbl;
    
    // for the entity
    private LocalTime startTime, endTime;
    private LocalDate startDate, endDate; 
    private String saleType;
    private String region;
    
    private TooltipSetter tooltip;
    
    ClientController chat = HostClientController.chat; // define the chat for the controller
    
    
    /** send new sale entity to the server*/
    @FXML
    void createSale(ActionEvent event) {
    	String errMsg=validateFields();
    	errLbl.setText(errMsg);
    	if(errMsg.equals("")) {
    		SaleEntity saleEntity=new SaleEntity(region,saleType,startDate, endDate, startTime, endTime);
    		chat.acceptObj(new Message(TaskType.RequestInsertNewSale, saleEntity));
    	}
    }
    
    @FXML
	/** Setup screen before launching view */
	public void initialize() throws Exception {
    	initTimeCmb();
    	initDatePickers();
    	ObservableList<String> regions = FXCollections.observableArrayList(CommonData.getRegions());
		regionCmb.setItems(regions);
		ObservableList<String> types = FXCollections.observableArrayList();
		tooltip = new TooltipSetter("Create new sale");
		createBtn.setTooltip(tooltip.getTooltip());
		for(SaleType t: SaleType.values()) {
			types.add(t.getName());
		}
		typeCmb.setItems(types);
		/** save the selected values*/
		regionCmb.addEventHandler(ComboBox.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				region = regionCmb.getValue();
			}
		});
		startTimeCmb.addEventHandler(ComboBox.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				startTime=startTimeCmb.getValue();
			}
		});
		endTimeCmb.addEventHandler(ComboBox.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				endTime=endTimeCmb.getValue();
			}
		});
		startDatePicker.addEventHandler(DatePicker.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				startDate=startDatePicker.getValue();
			}
		});
		endDatePicker.addEventHandler(DatePicker.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				endDate=startDatePicker.getValue();
			}
		});
		typeCmb.addEventHandler(ComboBox.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				saleType=typeCmb.getValue();
			}
		});	
	}
    

    /**Validation:
     * validate that all fields are filled in correctly.
     * create error message according to the error.
     * **/
    String validateFields() {
    	String msg="";
    	if(regionCmb.getSelectionModel().isEmpty() && typeCmb.getSelectionModel().isEmpty())
    		msg="Please Select Region and Sale type";
    	else if(regionCmb.getSelectionModel().isEmpty())
    		msg="Please Select Region";
    	else if(typeCmb.getSelectionModel().isEmpty())
    		msg="Please Select Sale type";
    	else if(endTime.isBefore(startTime))
    		msg="The end time cannot be before the start time!";
		return msg;
    }
    
    /** Initialize the satartTime and endTime ComboBoxes:
     * Set satartTime and endTime ComboBoxes items to be from 1:00 to 00:00.
     * Set the start time to be 8:00 and the end date to be 20:00.
     * **/
    private void initTimeCmb() {
    	ObservableList<LocalTime> times = FXCollections.observableArrayList();
   	 	for (int i = 0; i < 24; i++) {
                LocalTime time = LocalTime.of(i, 0);
                times.add(time);
        	}
		startTimeCmb.setItems(times);
		endTimeCmb.setItems(times);
		startTimeCmb.setValue( LocalTime.of(8, 0));
		endTimeCmb.setValue( LocalTime.of(20, 0));
    }
    
    /** Initialize the satartDate and endDate DatePickers:
     * Unable to choose date that has already passed.
     * Unable to choose an end date that is before the start date.
     * Set the start date to be today date and the end date to be startDate+1.
     * The endDate will cahnge after select startDate to be startDate+1.
     * **/
    private void initDatePickers() {
    	Locale.setDefault(Locale.ENGLISH);
		startDatePicker.setValue(LocalDate.now());
		endDatePicker.setValue(LocalDate.now().plusDays(1));
		startDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
		    @Override
		    public DateCell call(final DatePicker datePicker) {
		        return new DateCell() {
		            @Override
		            public void updateItem(LocalDate item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item.isBefore(LocalDate.now())) {
		                    setStyle("-fx-background-color: #ffc0cb;");
		                    setDisable(true);
		                }
		            }
		        };
		    }
		});
		startDatePicker.setOnAction(event -> {
			endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
		});

		endDatePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
		    @Override
		    public DateCell call(final DatePicker datePicker) {
		        return new DateCell() {
		            @Override
		            public void updateItem(LocalDate item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item.isBefore(startDatePicker.getValue())) {
		                    setStyle("-fx-background-color: #ffc0cb;");
		                    setDisable(true);
		                }
		            }
		        };
		    }
		});
    }

}
