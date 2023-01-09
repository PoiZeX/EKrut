package controllerGui;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import client.ClientController;
import common.CommonData;
import common.CommonFunctions;
import common.DeliveryStatus;
import common.Message;
import common.PopupTypeEnum;
import common.SaleType;
import common.TaskType;
import entity.SaleEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.VBox;
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
    private MenuButton daysMb;
    
    @FXML
    private ComboBox<LocalTime> startTimeCmb;

    @FXML
    private ComboBox<String> typeCmb;

    @FXML
    private Label typeLbl;
    
    // for the entity
    private LocalTime startTime, endTime;
    private String saleType;
    private String region;
    private TooltipSetter tooltip;
    
    ArrayList<String> daysArr=new ArrayList<>();
    ClientController chat = HostClientController.chat; // define the chat for the controller
    
    
    /** send new sale entity to the server*/
    @FXML
    void createSale(ActionEvent event) {
    	String errMsg=validateFields();
    	errLbl.setText(errMsg);
    	if(errMsg.equals("")) {
    		StringBuilder days=new StringBuilder(daysArr.toString());
    		days.deleteCharAt(0);
    		days.deleteCharAt(days.length()-1);
    		SaleEntity saleEntity=new SaleEntity(region,saleType,days.toString(), startTime, endTime);
    		chat.acceptObj(new Message(TaskType.RequestInsertNewSale, saleEntity));
    		CommonFunctions.createPopup(PopupTypeEnum.Success, "Yay! succeeding in creating a new sale");
    	}
    	
    }
    
    @FXML
	/** Setup screen before launching view */
	public void initialize() throws Exception {
    	initTimeCmb();
    	 initDays();
    	//initDatePickers();
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
				startTime=startTimeCmb.getSelectionModel().getSelectedItem();
				
			}
		});
		endTimeCmb.addEventHandler(ComboBox.ON_HIDING, new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				endTime=endTimeCmb.getSelectionModel().getSelectedItem();
			}
		});
		daysMb.setOnAction(event -> {
			event.consume();
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
    	if(regionCmb.getSelectionModel().isEmpty() && typeCmb.getSelectionModel().isEmpty() && daysArr.isEmpty())
    		msg="Please Select Region, Days and Sale type";
    	else if(regionCmb.getSelectionModel().isEmpty())
    		msg="Please Select Region";
    	else if(typeCmb.getSelectionModel().isEmpty())
    		msg="Please Select Sale type";
    	else if(daysArr.isEmpty())
    		msg="Please select at least one day";
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
		startTime=LocalTime.of(8, 0);
		endTime=LocalTime.of(20, 0);
		startTimeCmb.setValue(startTime);
		endTimeCmb.setValue(endTime);
		
    }
    /** Initialize listView of days*/
    private void initDays() {
    	
		CustomMenuItem monday = new CustomMenuItem(new CheckBox("Monday")); 
		CustomMenuItem tuesday = new CustomMenuItem(new CheckBox("Tuesday"));
		CustomMenuItem wednesday = new CustomMenuItem(new CheckBox("Wednesday"));
		CustomMenuItem thursday = new CustomMenuItem(new CheckBox("Thursday"));
		CustomMenuItem friday = new CustomMenuItem(new CheckBox("Friday"));
		CustomMenuItem saturday =new CustomMenuItem( new CheckBox("Saturday")); 
		CustomMenuItem sunday = new CustomMenuItem(new CheckBox("Sunday"));
		CustomMenuItem[] lst = new CustomMenuItem[] {sunday,monday,tuesday,wednesday,thursday,friday,saturday};
		for (CustomMenuItem item : lst) {
			item.setHideOnClick(false);
			daysMb.getItems().add(item);
			((CheckBox)item.getContent()).setOnMouseClicked(event -> {
				String currDay = ((CheckBox)event.getSource()).getText();
				if (((CheckBox)event.getSource()).isSelected()) {
					daysArr.add(currDay);
					daysMb.setText(daysArr.toString());
				}
				else {
					daysArr.remove(currDay);
					if(daysArr.isEmpty())
						daysMb.setText("Choose days");
					else {
						daysMb.setText(daysArr.toString());
					}
				}
			});
		}
    }
}
