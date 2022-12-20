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
	private TableColumn<item_supply, Integer> currentAmountCol;

	@FXML
	private TableColumn<item_supply, Integer> itemIDCol;

	@FXML
	private TableColumn<item_supply, Integer> minAmountCol;

	@FXML
	private TableColumn<item_supply, String> nameCol;

	@FXML
	private ComboBox<String> saleMachineCmb;

	@FXML
	private TableColumn<item_supply, Integer> startAmountCol;

	@FXML
	private TableView<item_supply> supplyMachineTbl;
	
	protected static SupplyReportEntity reportDetails;	
	protected static boolean RecievedData = false;
	
	public class item_supply {
		private int itemIDCol, startAmountCol, currentAmountCol, minAmountCol;
		private String nameCol;

		public item_supply(int itemIDCol, int startAmountCol, int currentAmountCol, int minAmountCol, String nameCol) {
			super();
			this.itemIDCol = itemIDCol;
			this.startAmountCol = startAmountCol;
			this.currentAmountCol = currentAmountCol;
			this.minAmountCol = minAmountCol;
			this.nameCol = nameCol;
		}

		public int getItemIDCol() {
			return itemIDCol;
		}

		public void setItemIDCol(int itemIDCol) {
			this.itemIDCol = itemIDCol;
		}

		public int getStartAmountCol() {
			return startAmountCol;
		}

		public void setStartAmountCol(int startAmountCol) {
			this.startAmountCol = startAmountCol;
		}

		public int getCurrentAmountCol() {
			return currentAmountCol;
		}

		public void setCurrentAmountCol(int currentAmountCol) {
			this.currentAmountCol = currentAmountCol;
		}

		public int getMinAmountCol() {
			return minAmountCol;
		}

		public void setMinAmountCol(int minAmountCol) {
			this.minAmountCol = minAmountCol;
		}

		public String getNameCol() {
			return nameCol;
		}

		public void setNameCol(String nameCol) {
			this.nameCol = nameCol;
		}

	}
	
	public static void recieveDataFromServer(SupplyReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}
	
	public void initialize() {
		supplyMachineTbl.setVisible(false);
		ArrayList<String[]> itemsArray = reportDetails.getReportsList();
		ObservableList<item_supply> ol = FXCollections.observableArrayList();
		for (int i = 0; i < itemsArray.size(); i++) {
			ol.add(new item_supply(
					Integer.parseInt(itemsArray.get(i)[0]),
					Integer.parseInt(itemsArray.get(i)[1]),
					Integer.parseInt(itemsArray.get(i)[2]),
					Integer.parseInt(itemsArray.get(i)[3]),
					itemsArray.get(i)[4]
					)
			);
		}
		supplyMachineTbl.setItems(ol);
		setupTable();

		ObservableList<String> cmbOl = FXCollections.observableArrayList("Big Karmiel", "Ort Brauda", "City hall",
				"Psagot high-school", "Lev Karmiel mall");
		saleMachineCmb.setItems(cmbOl);

		saleMachineCmb.valueProperty().addListener(((observable, oldValue, newValue) -> {
			supplyMachineTbl.setVisible(true);
		}));

		// mark red action
		supplyMachineTbl.setRowFactory(tv -> new TableRow<item_supply>() {

			@Override
			protected void updateItem(item_supply item, boolean empty) {
				super.updateItem(item, empty);

				if (item != null) {
					if (item.getMinAmountCol() >= item.getCurrentAmountCol())
						setStyle("-fx-background-color: #f94144;");
					else
						setStyle("-fx-background-color: #ffffff;");
				}

			}
		});
	}

	private void setupTable() {
		// factory
		currentAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<item_supply, Integer>("currentAmountCol"));
		itemIDCol.setCellValueFactory((Callback) new PropertyValueFactory<item_supply, Integer>("itemIDCol"));
		minAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<item_supply, Integer>("minAmountCol"));
		nameCol.setCellValueFactory((Callback) new PropertyValueFactory<item_supply, String>("nameCol"));
		startAmountCol.setCellValueFactory((Callback) new PropertyValueFactory<item_supply, Integer>("startAmountCol"));
	}
}
