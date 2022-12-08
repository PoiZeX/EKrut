/**
 * Sample Skeleton for 'viewReportUI.fxml' Controller Class
 */

package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class SelectionReportController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="clientReportBtn"
    private Button clientReportBtn; // Value injected by FXMLLoader

    @FXML // fx:id="monthItemsCmb"
    private ComboBox<?> monthItemsCmb; // Value injected by FXMLLoader

    @FXML // fx:id="ordersReportBtn"
    private Button ordersReportBtn; // Value injected by FXMLLoader

    @FXML // fx:id="returnBtn"
    private Button returnBtn; // Value injected by FXMLLoader

    @FXML // fx:id="supplyReportBtn"
    private Button supplyReportBtn; // Value injected by FXMLLoader

    @FXML // fx:id="viewReportCmb"
    private Button viewReportCmb; // Value injected by FXMLLoader

    @FXML // fx:id="yearItemsCmb"
    private ComboBox<?> yearItemsCmb; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert clientReportBtn != null : "fx:id=\"clientReportBtn\" was not injected: check your FXML file 'viewReportUI.fxml'.";
        assert monthItemsCmb != null : "fx:id=\"monthItemsCmb\" was not injected: check your FXML file 'viewReportUI.fxml'.";
        assert ordersReportBtn != null : "fx:id=\"ordersReportBtn\" was not injected: check your FXML file 'viewReportUI.fxml'.";
        assert returnBtn != null : "fx:id=\"returnBtn\" was not injected: check your FXML file 'viewReportUI.fxml'.";
        assert supplyReportBtn != null : "fx:id=\"supplyReportBtn\" was not injected: check your FXML file 'viewReportUI.fxml'.";
        assert viewReportCmb != null : "fx:id=\"viewReportCmb\" was not injected: check your FXML file 'viewReportUI.fxml'.";
        assert yearItemsCmb != null : "fx:id=\"yearItemsCmb\" was not injected: check your FXML file 'viewReportUI.fxml'.";

    }

}
