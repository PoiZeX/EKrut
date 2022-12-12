package controllerGui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

public class OrdersReportController {

    @FXML
    private PieChart pieChartOrders;
    
    public void initialize() {
    	 ObservableList<PieChart.Data> pieChartData =
    	            FXCollections.observableArrayList(
    	            new PieChart.Data("Lidor", 10.5),
    	            new PieChart.Data("Dima", 75),
    	            new PieChart.Data("David",100),
    	 			new PieChart.Data("Vital", 200),
    	 			new PieChart.Data("Neta", 200));
    	 pieChartOrders.setData(pieChartData);
    }

}
