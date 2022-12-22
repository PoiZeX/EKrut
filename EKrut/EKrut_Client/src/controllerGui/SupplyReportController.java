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
    private TableView<ItemInTable> supplyMachineTbl;

    @FXML
    private Label titleLabel;
    
	protected static SupplyReportEntity reportDetails;	
	protected static boolean RecievedData = false;
	
	private class ItemInTable {
		private String itemID, itemName, minStock, startStock, curStock;
		public ItemInTable(String itemID, String itemName, String minStock, String startStock, String curStock) {
			this.itemID = itemID;
			this.itemName = itemName;
			this.minStock = minStock;
			this.startStock = startStock;
			this.curStock = curStock;
		}
		public String getItemID() {
			return itemID;
		}
		public String getItemName() {
			return itemID;
		}
		public String getMinStock() {
			return itemID;
		}
		public String getStartStock() {
			return itemID;
		}
		public String getCurStock() {
			return itemID;
		}

	}
	
	public void initialize() {
		titleLabel.setText("Supply Report : " + reportDetails.getRegion());
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
		itemIDCol.setCellValueFactory(new PropertyValueFactory<>("itemID"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));
		minAmountCol.setCellValueFactory(new PropertyValueFactory<>("minStock"));
		startAmountCol.setCellValueFactory(new PropertyValueFactory<>("startStock"));
		currentAmountCol.setCellValueFactory(new PropertyValueFactory<>("curStock"));
		for (String[] item : itemsArray) {
			ItemInTable newItem = new ItemInTable(item[0], item[4], item[3], item[1], item[2]);
			supplyMachineTbl.getItems().add(newItem);
		}

	}
}
