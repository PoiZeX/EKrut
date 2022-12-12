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
                    new PieChart.Data("Apple", 20),
                    new PieChart.Data("Banana", 30),
                    new PieChart.Data("Orange", 10));
    	pieChartOrders = new PieChart(pieChartData);	
    }

}
