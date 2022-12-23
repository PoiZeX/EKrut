package controllerGui;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.ScreensNames;
import common.TaskType;
import entity.UserEntity;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Cell;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
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
	private static ArrayList<UserEntity> toApprove;
	private static ClientController chat = HostClientController.chat; // one instance
	ArrayList<TableCell<UserEntity, Boolean>> checkboxCellsList = new ArrayList<>();
	Timer timer;
	private boolean allSelected;

	public void initialize() {
		timer = new Timer();
		chat.acceptObj(new Message(TaskType.RequestUnapprovedUsers, null));
		while (!recievedData) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		toApprove = new ArrayList<UserEntity>();
		initTable();
	}

	public static void recieveUnapprovedUsers(ArrayList<UserEntity> obj) {
		unapprovedUsers = obj;
		recievedData = true;
		return;
	}

	@FXML
	public void approveSelected(ActionEvent event) throws InterruptedException {
		chat.acceptObj(new Message(TaskType.RequestUsersApproval, toApprove));

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					NavigationStoreController.getInstance().refreshStage(ScreensNames.UsersManagement);
				});
			}
		}, 1000);
	}

	@FXML
	void refresh(ActionEvent event) {
		NavigationStoreController.getInstance().refreshStage(ScreensNames.UsersManagement);
	}

	@FXML
	private void selectAll(ActionEvent event) {
		for (TableCell<UserEntity, Boolean> cell : checkboxCellsList)
			if ((CheckBox) cell.getGraphic() != null) {
				CheckBox checkBox = (CheckBox) cell.getGraphic();
				UserEntity user = (UserEntity) cell.getTableRow().getItem();
				if (!allSelected) {
					checkBox.setSelected(true);
					if (!toApprove.contains(user))
						toApprove.add(user);
				} else {
					checkBox.setSelected(false);
					if (toApprove.contains(user))
						toApprove.remove(user);
				}
			}
		allSelected = !allSelected;
	}

	private void initTable() {
		if (unapprovedUsers == null || unapprovedUsers.isEmpty())
			return;
		setFactoryCols();
		ObservableList<UserEntity> ol = FXCollections.observableArrayList(unapprovedUsers);
		usersTable.setItems(ol);

	}

	@SuppressWarnings("unchecked")
	private void setFactoryCols() {
		// recieving arraylist of userentitys need to setup table.
		customerIdCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, Integer>("id"));
		IdCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, String>("id_num"));
		firstNameCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, String>("first_name"));
		lastNameCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, String>("last_name"));
		phoneNumberCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, String>("phone_number"));
		emailCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, String>("email"));
		creditCardNumberCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, String>("cc_num"));
		subscriberIdCol.setCellValueFactory((Callback) new PropertyValueFactory<UserEntity, Integer>("id"));
		approveCol.setCellFactory(column -> {
			TableCell<UserEntity, Boolean> cell = new CheckBoxTableCell<>();
			checkboxCellsList.add(cell); // save the checkbox

			cell.setOnMouseClicked(event -> {
				if (event.getClickCount() > 0) {
					CheckBox checkBox = (CheckBox) cell.getGraphic();
					UserEntity user = (UserEntity) cell.getTableRow().getItem();
					if (checkBox != null) {
						if (checkBox.isSelected()) {
							checkBox.setSelected(false);
							toApprove.remove(user);
							if (allSelected)
								allSelected = false;

						} else {
							checkBox.setSelected(true);
							toApprove.add(user);
						}
					}
				}
			});
			return cell;
		});
		return;
	}

}
