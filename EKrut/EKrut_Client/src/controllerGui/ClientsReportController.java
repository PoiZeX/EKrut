package controllerGui;

import java.util.Map;

import entity.ClientsReportEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;

public class ClientsReportController {

    @FXML
    private BarChart<String, Integer> activityBarChart;

    @FXML
    private PieChart pieChartMethod;

    @FXML
    private Label totalSalesLabel;

    @FXML
    private Label titleLabel;

    
	protected static ClientsReportEntity reportDetails;	
	protected static boolean RecievedData = false;

	public void initialize() {
		titleLabel.setText("Clients Report : " + reportDetails.getRegion());
		initCharts();
		totalSalesLabel.setText("Total Sales : " + reportDetails.getTotalOrders());
		return;
	}

	public static void recieveDataFromServer(ClientsReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCharts() {
		Map<String,Integer> supplyMethods = reportDetails.getSupplyMethodsArr();
		Map<String,Integer> totalSales = reportDetails.getTotalSalesArr();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		ObservableList<String> categories = FXCollections.<String>observableArrayList();
		for (String key : reportDetails.getSupplyMethodsArr().keySet()) {
			pieChartData.add(new PieChart.Data(String.format( "%s: %s", key,Integer.toString( supplyMethods.get(key)) ), supplyMethods.get(key)));	
		}	
		for (String key : reportDetails.getTotalSalesArr().keySet()) {
			Series a = new Series();
			a.setName(key);
			a.getData().add(new XYChart.Data(key, totalSales.get(key)));
			activityBarChart.getData().addAll(a);
			categories.add(key);
		}
		pieChartMethod.setData(pieChartData);
	}
}
