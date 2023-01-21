package controllerGui;

import java.util.Map;

import common.CommonFunctions;
import entity.OrderReportEntity;
import enums.PopupTypeEnum;
import enums.ScreensNamesEnum;
import interfaces.IScreen;
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
import utils.PopupSetter;
import utils.TooltipSetter;

/**
 * Order report GUI controller, implements Screen interface
 * Getting the report and showing it to user 
 * @author Lidor
 *
 */
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
    private Button helpBtn;

	@FXML
	private VBox quantityVBox;

	protected static OrderReportEntity reportDetails;
	protected static boolean RecievedData = false;
	/**

	Initializes the controller class. This method is automatically called after the fxml file has been loaded.
	It sets the tooltip for the help button and sets the report details label. Also it initializes the charts,
	sets the quantityVBox to be invisible and brings the displayTypeBtn to front.
	*/
	@Override
	public void initialize() {
		helpBtn.setTooltip((new TooltipSetter("Click for help").getTooltip()));
		reportDetailsLabel.setText(String.format("%s - %s/%s", reportDetails.getRegion(), reportDetails.getMonth(),
				reportDetails.getYear()));
		initCharts();
		quantityVBox.setVisible(false);
		displayTypeBtn.toFront();
		return;
	}
	/**

	This method receives data from the server about the order report.
	@param report an object of OrderReportEntity that contains the report details
	*/
	public static void recieveDataFromServer(OrderReportEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	} 
	/**

	This method initializes the charts used in the report screen.
	The method creates a pie chart and a bar chart that display the order statistics.
	The pie chart shows the percentage of each item in the total orders, while the bar chart shows the quantity of each item sold.
	*/
	@SuppressWarnings("unchecked")
	private void initCharts() {
		Map<String, Double[]> itemsMap = reportDetails.getReportsList();
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		for (String key : reportDetails.getReportsList().keySet()) {
			pieChartData.add(new PieChart.Data(key + " - " + Math.round(itemsMap.get(key)[0]), itemsMap.get(key)[0]));
			@SuppressWarnings("rawtypes")
			Series a = new Series();
			a.setName(key);
			a.getData().add(new XYChart.Data(key, itemsMap.get(key)[1]));
			orderBarChart.getData().addAll(a);
		}
		pieChartOrders.setData(pieChartData);
		pieChartOrders.setLegendVisible(false);
	}
	/**

	Method that changes the display type of the chart from "By Quantity" to "By Profit" and vice versa.
	@param event the event that triggers the method.
	*/
	@FXML
	void changeDisplayType(ActionEvent event) {
		String txt = !quantityVBox.isVisible() ? "By Profit" : "By Quantity";
		quantityVBox.setVisible(!quantityVBox.isVisible());
		profitVBox.setVisible(!quantityVBox.isVisible());
		displayTypeBtn.setText(txt);
	}
	/**

	Show the description of the Orders Report screen.
	@param event the event that triggered the method
	*/
    @FXML
    void showDescription(ActionEvent event) {
    	PopupSetter.createPopup(PopupTypeEnum.Information, ScreensNamesEnum.OrdersReport.getDescription());	
    }
}
