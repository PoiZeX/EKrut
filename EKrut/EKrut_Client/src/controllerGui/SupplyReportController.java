package controllerGui;

import client.ChatClient;
import entity.SubscriberEntity;
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

	public void initialize() {
		supplyMachineTbl.setVisible(false);

		ObservableList<item_supply> ol = FXCollections.observableArrayList(new item_supply(1, 100, 50, 20, "Kif-Kef"),
				new item_supply(2, 84, 12, 30, "Bamba"), // ---- RED MARK ----
				new item_supply(3, 67, 44, 10, "Bissli"), new item_supply(4, 40, 3, 2, "Grape Water"));
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
