package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class HostClientUIController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private TextField txtConnectToServerArea;

    @FXML
    private Button connectBtnclient;

    @FXML
    private Label gridLabel;

    @FXML
    private Label headLine;

    @FXML
    void SendPort(ActionEvent event) {

    }

}
