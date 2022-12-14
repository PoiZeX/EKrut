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
    	            new PieChart.Data("Ort Brauda", 230),
    	            new PieChart.Data("Big Karmiel", 434),
    	            new PieChart.Data("City hall",388),
    	 			new PieChart.Data("Psagot high-school", 234),
    	 			new PieChart.Data("Lev Karmiel mall", 152));
    	 pieChartOrders.setData(pieChartData);
    	 applyCustomColorSequence(
    		      pieChartData, 
    		      "aqua", 
    		      "bisque", 
    		      "chocolate", 
    		      "coral", 
    		      "crimson"
    		    );
    }

    		  private void applyCustomColorSequence(ObservableList<PieChart.Data> pieChartData, String... pieColors) {
    		    int i = 0;
    		    for (PieChart.Data data : pieChartData) {
    		      data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
    		      i++;
    		    }
    		  }
 }


