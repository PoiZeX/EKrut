package controllerGui;


import java.util.ArrayList;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.RolesEnum;
import common.ScreensNamesEnum;
import common.TaskType;
import controller.SMSMailHandlerController;
import entity.UserEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class UsersManagementController  implements IScreen {

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
	private static ClientController chat = HostClientController.getChat(); // one instance
	ArrayList<TableCell<UserEntity, Boolean>> checkboxCellsList = new ArrayList<>();
	private boolean allSelected;

	@Override
	public void initialize() {
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
		String SMSMsg = "Congratulations your request has been approved!\nSee Mail for full details";
		String emailMsg;
		// send sms to user
		for(UserEntity user : toApprove) {
			emailMsg = "Congratulations your request has been approved!\nYour login info:\n";
			emailMsg += "Username: " + user.getUsername() +"\nPassword: "+user.getPassword();
			if(user.getRole_type().equals(RolesEnum.member))
				emailMsg +="\nAs a member you get the following benefits:\n"
						+ "*From time to time there are special sales on some regions.\n"
						+ "*Discount of 20% on your first purchase\n"
						+ "*A quick login using EKT app. Here is a link for download: www.ekt.ekrut.com";
			
			// SMS sending msg & popup
			SMSMailHandlerController.SendSMSOrMail("SMS", user, "Request Approved", SMSMsg);
			CommonFunctions.createPopup(PopupTypeEnum.Simulation, SMSMailHandlerController.lastMsg);
			
			// Mail sending msg & popup
			SMSMailHandlerController.SendSMSOrMail("Mail", user, "Request Approved", emailMsg);
			CommonFunctions.createPopup(PopupTypeEnum.Simulation, SMSMailHandlerController.lastMsg);
			
		
		}
		CommonFunctions.SleepFor(1000, () -> 
		{
			NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.UsersManagement);
		});
	}

	@FXML
	void refresh(ActionEvent event) {
		NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.UsersManagement);
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
