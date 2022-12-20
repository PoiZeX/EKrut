package controllerGui;

import java.util.ArrayList;

import client.ClientController;
import common.Message;
import common.TaskType;
import controllerGui.SupplyReportController.item_supply;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class UsersManagementController {

    @FXML
    private Button returnBtn;

    @FXML
    private TableView<UserEntity> usersTable;

    @FXML
    private TableColumn<UserEntity, Integer> customerIdCol;

    @FXML
    private TableColumn<UserEntity, String> IdCol;

    @FXML
    private TableColumn<UserEntity, String> firstNameCol;

    @FXML
    private TableColumn<UserEntity, String> lastNameCol;

    @FXML
    private TableColumn<UserEntity, String> phoneNumberCol;

    @FXML
    private TableColumn<UserEntity, String> emailCol;

    @FXML
    private TableColumn<UserEntity, String> creditCardNumberCol;

    @FXML
    private TableColumn<UserEntity, Integer> subscriberIdCol;

    @FXML
    private TableColumn<UserEntity, Boolean> approveCol;

    @FXML
    private Button approveBtn;

    @FXML
    private Button selectBtn;

    @FXML
    private Button refreshBtn;

	private static boolean recievedData = false;
	private static ArrayList<UserEntity> unapprovedUsers;
	private static ClientController chat = HostClientController.chat; // one instance
	
	public void initialize() {
		chat.acceptObj(new Message(TaskType.RequestUnapprovedUsers, null));
		while (!recievedData) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		initTable();
	}
	
	public static void recieveUnapprovedUsers(ArrayList<UserEntity> obj) {
		unapprovedUsers = obj;
		recievedData = true;
		return;
	}

	@FXML
    void approveSelected(ActionEvent event) {

    }

    @FXML
    void refresh(ActionEvent event) {

    }

    @FXML
    void selectAll(ActionEvent event) {

    }
    
    private void initTable() {
    	if (unapprovedUsers == null || unapprovedUsers.isEmpty())
    		return;
    	setFactoryCols();
    	ObservableList<UserEntity> ol = FXCollections.observableArrayList(unapprovedUsers);
		usersTable.setItems(ol);
		
    	
	}

	private void setFactoryCols() {
    	//recieving arraylist of userentitys need to setup table.
    	customerIdCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,Integer>("id"));
    	IdCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,String>("id_num"));
    	firstNameCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,String>("first_name"));
    	lastNameCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,String>("last_name"));
    	phoneNumberCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,String>("phone_number"));
    	emailCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,String>("email"));
    	creditCardNumberCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,String>("cc_num"));
    	subscriberIdCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity,Integer>("id"));
    	approveCol.setCellFactory(CheckBoxTableCell.forTableColumn(approveCol));
    	return;
	}

}
