package controllerGui;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

public class OrdersReportController {

	@FXML
	private PieChart pieChartOrders;
	@FXML
	private BarChart<String, Number> orderBarChart;

	public void initialize() {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Ort Brauda", 230), new PieChart.Data("Big Karmiel", 434),
				new PieChart.Data("City hall", 388), new PieChart.Data("Psagot high-school", 234),
				new PieChart.Data("Lev Karmiel mall", 152));
		pieChartOrders.setData(pieChartData);

		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Machine name");
		yAxis.setLabel("Profit (in K)");
		HashMap<String, Number> map = new HashMap<>();
		map.put("Big Karmiel", 44);
		map.put("Ort Brauda", 25);
		map.put("City hall", 34);
		map.put("Psagot high-school", 20);
		map.put("Lev Karmiel mall", 14);

		for (String key : map.keySet()) {
			var a = new XYChart.Series();
			a.setName(key);
			a.getData().add(new XYChart.Data(key, map.get(key)));
			orderBarChart.getData().addAll(a);

		}

//         
//         
//    	 ObservableList<BarChart.Data> orderBarChart =
// 	            FXCollections.observableArrayList(
// 	            new PieChart.Data("Ort Brauda", 25),
// 	            new PieChart.Data("Big Karmiel", 44),
// 	            new PieChart.Data("City hall",34),
// 	 			new PieChart.Data("Psagot high-school", 20),
// 	 			new PieChart.Data("Lev Karmiel mall", 14));
// 	 pieChartOrders1.setData(pieChartData1);
	}

}
