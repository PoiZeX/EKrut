package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class EditUsersController {

	
    @FXML
    private Button backBtn;
    @FXML
    private TableColumn<?, ?> creditCol;

    @FXML
    private TableColumn<?, ?> emailCol;

    @FXML
    private TableColumn<?, ?> fnameCol;

    @FXML
    private TableColumn<?, ?> idCol;

    @FXML
    private TableColumn<?, ?> phoneCol;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private TableColumn<?, ?> subscriberCol;

    @FXML
    private TableView<?> usersTable;


    @FXML
    void back(ActionEvent event) {
    	
    }

    @FXML
    void refresh(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
    	
    }

    

    
}
