/**
 * Sample Skeleton for 'HomePageBoundary.fxml' Controller Class
 */

package controllerGui;

import Store.NavigationStoreController;
import common.ScreensNames;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.ChangeScreen;

public class HomePageController {

////--------------------------------

	private String[] rolesStub;

	protected class User {
		private String role;
		private String firstName = "Lidor", lastName = "Ankava";

		public User(String role) {
			this.role = role;
		}

		private String friendlyName() {
			return role + " " + firstName + " " + lastName;

		}
	}

	//// --------------------------------
	@FXML
	private VBox vbox;

	@FXML
	private Button bottomBtn;

	@FXML
	private Button logOutBtn;

	@FXML
	private Button middleBtn;

	@FXML
	private Button topBtn;

	@FXML
	private Label welcomeLabel;

	public void initialize() {
		// set hidden as default
		topBtn.setVisible(false);
		middleBtn.setVisible(false);
		bottomBtn.setVisible(false);

		// define stubs
		rolesStub = new String[] { "CEO", "RegionManager", "Register" };
		User ceo = new User("CEO");
		User regionManager = new User("RegionManager");
		User register = new User("Register");
		User selectedUser = regionManager;

		// switch case by role
		switch (selectedUser.role) {
		case "CEO":
		case "Register":
			setMiddleButton(selectedUser.role);
			break;

		case "RegionManager":
			// CEO has 3 buttons.
			setTopButton();
			setMiddleButton(selectedUser.role);
			setBottomButton();
			break;

		default:
			System.out.println("No role detected!");
			break;
		}

		welcomeLabel.setText("Welcome " + selectedUser.friendlyName() + "!");
		updateButtonsSize();
	}

	// updates the buttons width by the max width
	// bug here
	private void updateButtonsSize() {
		double maxWidth = 0;
		maxWidth = topBtn.getPrefWidth() > middleBtn.getPrefWidth() ? topBtn.getPrefWidth() : middleBtn.getPrefWidth();
		maxWidth = maxWidth > bottomBtn.getPrefWidth() ? maxWidth : bottomBtn.getPrefWidth();
		topBtn.setMinWidth(maxWidth);
		middleBtn.setMinWidth(maxWidth);
		bottomBtn.setMinWidth(maxWidth);
	}

	private void setTopButton() {
		topBtn.setText("Approve Users");
		topBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.UsersManagement);
			}
		});
		topBtn.setVisible(true);
	}

	private void setMiddleButton(String userRole) {
		if(userRole.equals("Register"))
		{
			middleBtn.setText("Create new order");
			middleBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.OrderSomething); // not working yet 
				}
			});
		}
		else {  // ceo / manager
			middleBtn.setText("View reports");
			middleBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ReportSelection);
				}
			});
		}
		middleBtn.setVisible(true);

	}

	/// register and Manager share the same button.
	private void setBottomButton() {
		bottomBtn.setText("Supply Management");
		bottomBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				bottomBtnAction(event);
			}
		});

		bottomBtn.setVisible(true);
	}

	@FXML
	private void bottomBtnAction(ActionEvent event) {
		System.out.println("Im bottom");
	}

	@FXML
	private void middleBtnAction(ActionEvent event) {
		System.out.println("Im middle");

	}

	@FXML
	private void topBtnAction(ActionEvent event) {
		System.out.println("Im top");

	}

	@FXML
	private void logOutAction(ActionEvent event) {
		System.out.println("Im logout");

	}

}
