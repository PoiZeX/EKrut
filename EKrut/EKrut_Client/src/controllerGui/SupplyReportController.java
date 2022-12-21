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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

public class SupplyReportController {

	@FXML
	private TableColumn<ItemSupply, Integer> currentAmountCol;

	@FXML
	private TableColumn<ItemSupply, Integer> itemIDCol;

	@FXML
	private TableColumn<ItemSupply, Integer> minAmountCol;

	@FXML
	private TableColumn<ItemSupply, String> nameCol;

	@FXML
	private ComboBox<String> saleMachineCmb;

	@FXML
	private TableColumn<ItemSupply, Integer> startAmountCol;

	@FXML
	private TableView<ItemSupply> supplyMachineTbl;
	
	protected static SupplyReportEntity reportDetails;	
	protected static boolean RecievedData = false;
	
	
	
	public static void recieveDataFromServer(SupplyReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}
	
	public void initialize() {

	}
	
	private class ItemSupply {
		
	}
//	private void setupTable() {
//		// factory
//		currentAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemSupply, Integer>("currentAmountCol"));
//		itemIDCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemSupply, Integer>("itemIDCol"));
//		minAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemSupply, Integer>("minAmountCol"));
//		nameCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemSupply, String>("nameCol"));
//		startAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<ItemSupply, Integer>("startAmountCol"));
//	}
//	
//	private class ItemSupply {
//		private int itemIDCol, startAmountCol, currentAmountCol, minAmountCol;
//		private String nameCol;
//
//		public ItemSupply(int itemIDCol, int startAmountCol, int currentAmountCol, int minAmountCol, String nameCol) {
//			super();
//			this.itemIDCol = itemIDCol;
//			this.startAmountCol = startAmountCol;
//			this.currentAmountCol = currentAmountCol;
//			this.minAmountCol = minAmountCol;
//			this.nameCol = nameCol;
//		}
//
//		public int getItemIDCol() {
//			return itemIDCol;
//		}
//
//		public void setItemIDCol(int itemIDCol) {
//			this.itemIDCol = itemIDCol;
//		}
//
//		public int getStartAmountCol() {
//			return startAmountCol;
//		}
//
//		public void setStartAmountCol(int startAmountCol) {
//			this.startAmountCol = startAmountCol;
//		}
//
//		public int getCurrentAmountCol() {
//			return currentAmountCol;
//		}
//
//		public void setCurrentAmountCol(int currentAmountCol) {
//			this.currentAmountCol = currentAmountCol;
//		}
//
//		public int getMinAmountCol() {
//			return minAmountCol;
//		}
//
//		public void setMinAmountCol(int minAmountCol) {
//			this.minAmountCol = minAmountCol;
//		}
//
//		public String getNameCol() {
//			return nameCol;
//		}
//
//		public void setNameCol(String nameCol) {
//			this.nameCol = nameCol;
//		}
//	}
//	
//	supplyMachineTbl.setItems(ol);
//	setupTable();
//
//	ObservableList<String> cmbOl = FXCollections.observableArrayList("Big Karmiel", "Ort Brauda", "City hall",
//			"Psagot High-School", "Lev Karmiel Mall");
//	saleMachineCmb.setItems(cmbOl);
//
//	saleMachineCmb.valueProperty().addListener(((observable, oldValue, newValue) -> {
//		supplyMachineTbl.setVisible(true);
//	}));
//
//	// mark red action
//	supplyMachineTbl.setRowFactory(tv -> new TableRow<ItemSupply>() {
//
//		@Override
//		protected void updateItem(ItemSupply item, boolean empty) {
//			super.updateItem(item, empty);
//
//			if (item != null) {
//				if (item.getMinAmountCol() >= item.getCurrentAmountCol())
//					setStyle("-fx-background-color: #f94144;");
//				else
//					setStyle("-fx-background-color: #ffffff;");
//			}
//
//		}
//	});
}
