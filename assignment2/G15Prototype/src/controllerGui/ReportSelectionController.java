package controllerGui;

import Store.NavigationStoreController;
import common.ScreensNames;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;

public class ReportSelectionController {
    private String selectedReport = "";
    
    @FXML
    private ComboBox<String> monthItemsCmb;

    @FXML
    private ComboBox<Integer> yearItemsCmb;
    
    @FXML
    private Label errorMsgLabel;

    @FXML
    private ToolBar reportsToolBar;

    @FXML
    private Button clientReportBtn;

    @FXML
    private Button supplyReportBtn;

    @FXML
    private Button ordersReportBtn;

    @FXML
    private Button viewReportBtn;
  
    @FXML
    void viewReport(ActionEvent event) {
    	errorMsgLabel.setText(validateFields());
    	if (selectedReport == "ordersReport") {
    		Store.NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.OrdersReport);
    	}
    }
     
    public void initialize() {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        for (int i = 2016; i <= 2023; i++) {
            years.add(i);
        }    
        ObservableList<String> months = FXCollections.observableArrayList();
        months.addAll("January", "February", "March", "April", "May", "June",
                      "July", "August", "September", "October", "November", "December");
        
        setReportButtons();
        monthItemsCmb.setItems(months);
        yearItemsCmb.setItems(years);
    }
    

	String validateFields() {
    	String errorMsg = "";
    	if (monthItemsCmb.getSelectionModel().isEmpty() && yearItemsCmb.getSelectionModel().isEmpty()) {
    		errorMsg = "Please Select Month and Year";
    	}
    	else if (monthItemsCmb.getSelectionModel().isEmpty()) {
    		errorMsg = "Please Select Month";
    	}
    	else if (yearItemsCmb.getSelectionModel().isEmpty()) {
    		errorMsg = "Please Select Year";
    	}
    	if (errorMsg != "" && selectedReport == "") {
    		errorMsg += " and Report Type";
    	}
    	if (errorMsg == "" && selectedReport == "") {
    		errorMsg = "Please Select Report Type";
    	}
    	return errorMsg;
    }
	
    private void setReportButtons() {
        supplyReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				selectedReport = "supplyReport";
			}  	
        });
        ordersReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				selectedReport = "ordersReport";
			}  	
        });
        clientReportBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ce) {
				selectedReport = "clientsReport";
			}  	
        });
	}
    
    private String getSelectedReport() {
    	return this.selectedReport;
    }
}
