package controllerGui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class changeme extends Application{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PieChart PieChart;

    @FXML
    private Button backBtn;

    @FXML
    private ImageView logo;

    @FXML
    void initialize() {

    	 ObservableList<PieChart.Data> pieChartData =
                 FXCollections.observableArrayList(
                 new PieChart.Data("Grapefruit", 13),
                 new PieChart.Data("Oranges", 25),
                 new PieChart.Data("Plums", 10),
                 new PieChart.Data("Pears", 22),
                 new PieChart.Data("Apples", 30));
    	 PieChart.setData(pieChartData);

    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/boundary/orderReportUI.fxml"));
		Scene scene = new Scene(root);
		initialize();
		primaryStage.setScene(scene);
		
		primaryStage.show();	 
		
	}
	 public static void main(String[] args) {
	        launch(args);
	    }
    

}
