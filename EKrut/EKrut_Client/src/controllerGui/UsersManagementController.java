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
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	ArrayList<BooleanCheckBox> checkboxCellsList = new ArrayList<>();
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
	/**

	This method is used to receive a list of unapproved users from another class.
	It updates the 'unapprovedUsers' variable and sets the 'recievedData' variable to true.
	@param obj The list of unapproved users to be stored
	*/
	public static void recieveUnapprovedUsers(ArrayList<UserEntity> obj) {
		unapprovedUsers = obj;
		recievedData = true;
		return;
	}
	
	/**
	This method is used to approve the selected users. It sends a message to the chat object to accept the user
	objects passed in the 'toApprove' variable. It then sends an SMS and email to each user, containing information
	about their approved request and login credentials. A simulation pop-up is also displayed for both the SMS and email
	messages. After sending the messages, the method sleeps for one second and then refreshes the stage of the UsersManagement
	screen.
	@param event The action event that triggered this method
	@throws InterruptedException If the thread is interrupted while sleeping
	*/
	@FXML
	public void approveSelected(ActionEvent event) throws InterruptedException {
		chat.acceptObj(new Message(TaskType.RequestUsersApproval, toApprove));
		String SMSMsg = "Congratulations your request has been approved!\nSee Mail for full details";
		String emailMsg;
		// send sms to user
		for (UserEntity user : toApprove) {
			emailMsg = "Congratulations your request has been approved!\nYour login info:\n";
			emailMsg += "Username: " + user.getUsername() + "\nPassword: " + user.getPassword();
			if (user.getRole_type().equals(RolesEnum.member))
				emailMsg += "\nAs a member you get the following benefits:\n"
						+ "* From time to time there are special sales in some regions\n"
						+ "* 20% Discount on your first purchase\n"
						+ "* A quick login using EKT app. Download link: www.ekt.ekrut.com";

			// SMS sending msg & popup
			SMSMailHandlerController.SendSMSOrMail("SMS", user, "Request Approved", SMSMsg);
			CommonFunctions.createPopup(PopupTypeEnum.Simulation, SMSMailHandlerController.lastMsg);

			// Mail sending msg & popup
			SMSMailHandlerController.SendSMSOrMail("Mail", user, "Request Approved", emailMsg);
			CommonFunctions.createPopup(PopupTypeEnum.Simulation, SMSMailHandlerController.lastMsg);

		}
		CommonFunctions.SleepFor(1000, () -> {
			NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.UsersManagement);
		});
	}
	/**

	This method is used to refresh the stage of the UsersManagement screen.
	@param event The action event that triggered this method
	*/
	@FXML
	void refresh(ActionEvent event) {
		NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.UsersManagement);
	}

	@FXML
	private void selectAll(ActionEvent event) {
		for (BooleanCheckBox cell : checkboxCellsList) {
			UserEntity user = (UserEntity) cell.getTableRow().getItem();
			if (user != null)
				if (!allSelected) {
					cell.updateItem(true, false);
					if (!toApprove.contains(user))
						toApprove.add(user);
				} else {
					cell.updateItem(false, false);
					if (toApprove.contains(user))
						toApprove.remove(user);
				}
		}
		allSelected = !allSelected;
	}
	/**

	This method is used to initialize the users table with data from 'unapprovedUsers' variable.
	It first checks if the 'unapprovedUsers' variable is null or empty, and if it is, the method returns.
	Otherwise, it sets the factory columns for the table and creates an observable list from the 'unapprovedUsers' variable.
	Finally, it sets the items of the table to the created observable list.
	*/
	private void initTable() {
		if (unapprovedUsers == null || unapprovedUsers.isEmpty())
			return;
		setFactoryCols();
		ObservableList<UserEntity> ol = FXCollections.observableArrayList(unapprovedUsers);
		usersTable.setItems(ol);

	}

	/**

	This method is used to set the factory columns for the table.
	It sets the cell value factory for each column using the appropriate field from the UserEntity class,
	and sets a custom cell factory for the 'approveCol' column to use a BooleanCheckBox.
	*/
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
		Callback<TableColumn<UserEntity, Boolean>, TableCell<UserEntity, Boolean>> booleanCellFactory = new Callback<TableColumn<UserEntity, Boolean>, TableCell<UserEntity, Boolean>>() {
			@Override
			public TableCell<UserEntity, Boolean> call(TableColumn<UserEntity, Boolean> p) {
				return new BooleanCheckBox();
			}
		};
		approveCol.setCellFactory(booleanCellFactory);
		return;
	}
	/**

	This is a private class that extends the TableCell class, it is used to create a CheckBox in each cell of the approveCol column.
	The class have a constructor that creates a checkbox and sets on action event to add or remove user to the list of users to approve.
	Also the checkbox is added to the list checkboxCellsList.
	The updateItem method updates the checkbox with the value of the cell and aligns it center.
	*/
	private class BooleanCheckBox extends TableCell<UserEntity, Boolean> {
		private CheckBox checkBox;

		public BooleanCheckBox() {
			checkBox = new CheckBox();
			checkBox.setOnAction((evt) -> {
				if (checkBox.isSelected()) {
					toApprove.add(getTableView().getItems().get(getIndex()));
					if (allSelected)
						allSelected = false;
				} else {
					toApprove.remove(getTableView().getItems().get(getIndex()));
				}

			});
			checkBox.setId("myCb");
			this.setGraphic(checkBox);
			this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			this.setEditable(true);
			checkboxCellsList.add(this);
		}

		@Override
		public void updateItem(Boolean item, boolean empty) {
			super.updateItem(item, empty);
			if (empty) {
				setGraphic(null);
			} else {
				if (item != null) {
					checkBox.setAlignment(Pos.CENTER);
					checkBox.setSelected(item);
				}
				setAlignment(Pos.CENTER);
				setGraphic(checkBox);
			}
		}
	}

}
