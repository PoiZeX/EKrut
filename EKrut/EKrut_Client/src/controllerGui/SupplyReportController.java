package controllerGui;

import java.util.ArrayList;
import java.util.Map;

import client.ChatClient;
import entity.OrderReportEntity;
import entity.SubscriberEntity;
import entity.SupplyReportEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

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
    private TableColumn<?, ?> startAmountCol;

    @FXML
    private TableView<?> supplyMachineTbl;

    @FXML
    private Label titleLabel;
    
	protected static SupplyReportEntity reportDetails;	
	protected static boolean RecievedData = false;
	
	public void initialize() {
		titleLabel.setText("Suplly Report : " + reportDetails.getRegion());
		initCharts();
		return;
	}
	
	public static void recieveDataFromServer(SupplyReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}
	


	private void initCharts() {
		ArrayList<String[]> itemsArray = reportDetails.getReportsList();

	}
}
