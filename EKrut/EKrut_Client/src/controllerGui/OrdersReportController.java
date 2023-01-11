package controllerGui;

import java.util.Map;

import entity.OrderReportEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class OrdersReportController  implements IScreen {

	@FXML
	private PieChart pieChartOrders;
	@FXML
	private BarChart<String, Number> orderBarChart;
	@FXML
	private Label titleLabel;

	@FXML
	private BorderPane borderPane;

	@FXML
	private Button displayTypeBtn;

	@FXML
	private Label reportDetailsLabel;

	@FXML
	private VBox profitVBox;

	@FXML
	private VBox quantityVBox;

	protected static OrderReportEntity reportDetails;
	protected static boolean RecievedData = false;

	@Override
	public void initialize() {

		reportDetailsLabel.setText(String.format("%s - %s/%s", reportDetails.getRegion(), reportDetails.getMonth(),
				reportDetails.getYear()));
		initCharts();
		quantityVBox.setVisible(false);
		return;
	}

	public static void recieveDataFromServer(OrderReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCharts() {
		Map<String, Double[]> itemsMap = reportDetails.getReportsList();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (String key : reportDetails.getReportsList().keySet()) {
			pieChartData.add(new PieChart.Data(key + " - " + Math.round(itemsMap.get(key)[0]), itemsMap.get(key)[0]));
			Series a = new Series();
			a.setName(key);
			a.getData().add(new XYChart.Data(key, itemsMap.get(key)[1]));
			orderBarChart.getData().addAll(a);
		}
		pieChartOrders.setData(pieChartData);
		pieChartOrders.setLegendVisible(false);
	}

	@FXML
	void changeDisplayType(ActionEvent event) {
		String txt = !quantityVBox.isVisible() ? "By Profit" : "By Quantity";
		quantityVBox.setVisible(!quantityVBox.isVisible());
		profitVBox.setVisible(!quantityVBox.isVisible());
		displayTypeBtn.setText(txt);
	}

}
