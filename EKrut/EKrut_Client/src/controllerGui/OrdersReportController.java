package controllerGui;

import java.util.Map;

import entity.OrderReportEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;

public class OrdersReportController {

	@FXML
	private PieChart pieChartOrders;
	@FXML
	private BarChart<String, Number> orderBarChart;
    @FXML
    private Label titleLabel;

	
	protected static OrderReportEntity reportDetails;	
	protected static boolean RecievedData = false;

	public void initialize() {
		titleLabel.setText("Orders Report : " + reportDetails.getRegion());
		initCharts();
		return;
	}

	public static void recieveDataFromServer(OrderReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}

	private void initCharts() {
		// TODO Verify array
		Map<String,Double[]> itemsMap = reportDetails.getReportsList();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (String key : reportDetails.getReportsList().keySet()) {
			pieChartData.add(new PieChart.Data(key, itemsMap.get(key)[0]));	
			Series a = new Series();
			a.setName(key);
			a.getData().add(new XYChart.Data(key, itemsMap.get(key)[1]));
			orderBarChart.getData().addAll(a);
		}			
		pieChartOrders.setData(pieChartData);	
	}

}
