package controllerGui;

import java.util.Map;

import entity.ClientsReportEntity;
import interfaces.IScreen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;

/**
 * This class is responsible for providing the functionality for the clients
 * report screen in the GUI. It displays data from a ClientsReportEntity in the
 * form of bar charts and pie charts.
 * 
 * @author David
 * @see ClientsReportEntity
 * @see IScreen
 */
public class ClientsReportController implements IScreen {

	@FXML
	private BarChart<String, Integer> activityBarChart;

	@FXML
	private Label reportDetailsLabel;

	@FXML
	private PieChart usersStatusPie;

	@FXML
	private PieChart pieChartMethod;

	@FXML
	private Label totalSalesLabel;

	protected static ClientsReportEntity reportDetails;
	protected static boolean RecievedData = false;

	/**
	 * Initializes the charts with data from the ClientsReportEntity.
	 */
	@Override
	public void initialize() {
		reportDetailsLabel.setText(String.format("%s - %s/%s", reportDetails.getRegion(), reportDetails.getYear(),
				reportDetails.getMonth()));
		initCharts();
		return;
	}

	/**
	 * Accepts data from the server in the form of a ClientsReportEntity.
	 * 
	 * @param report the ClientsReportEntity containing data to be displayed
	 */
	public static void recieveDataFromServer(ClientsReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}

	/***
	 * set the charts with the report data
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initCharts() {
		Map<String, Integer> supplyMethods = reportDetails.getSupplyMethodsArr();
		Map<String, Integer> totalSales = reportDetails.getTotalSalesArr();
		Map<String, Integer> userStatus = reportDetails.getUserStatusArr();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		ObservableList<PieChart.Data> users = FXCollections.observableArrayList();
		ObservableList<String> categories = FXCollections.<String>observableArrayList();

		for (String key : userStatus.keySet()) {
			users.add(new PieChart.Data(String.format("%s: %s", key, Integer.toString(userStatus.get(key))),
					userStatus.get(key)));
		}
		for (String key : supplyMethods.keySet()) {
			pieChartData.add(new PieChart.Data(String.format("%s: %s", key, Integer.toString(supplyMethods.get(key))),
					supplyMethods.get(key)));
		}
		for (String key : totalSales.keySet()) {
			Series a = new Series();
			a.setName(key);
			a.getData().add(new XYChart.Data(key, totalSales.get(key)));
			activityBarChart.getData().addAll(a);
			categories.add(key);
		}

		pieChartMethod.setData(pieChartData);
		pieChartMethod.setLegendVisible(false);
		usersStatusPie.setData(users);
		usersStatusPie.setLegendVisible(false);

	}
}
