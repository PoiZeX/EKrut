/**
 * Sample Skeleton for 'HomePageBoundary.fxml' Controller Class
 */

package controllerGui;

import Store.NavigationStoreController;
import common.ScreensNames;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import utils.TooltipSetter;

public class HomePageController {

////--------------------------------

	private TooltipSetter tooltip;
	private UserEntity currentUser = NavigationStoreController.connectedUser;

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

	@FXML
	private Label roleLabel;

	@FXML
	private VBox rigthVbox;

	public void initialize() {
		// set hidden as default
		topBtn.setVisible(false);
		middleBtn.setVisible(false);
		bottomBtn.setVisible(false);

		Image image = null;

		// switch case by role
		switch (currentUser.getRole_type()) {
		case "registered":
		case "subscribed":
			setTopButton();
			setMiddleButton();
			image = new Image(getClass().getResourceAsStream("/styles/images/vending-machineNOBG.png"));
			break;

		case "CEO":
		case "regionManager":
			// CEO has 3 buttons.
			setTopButton();
			setMiddleButton();
			setBottomButton();
			image = new Image(getClass().getResourceAsStream("../styles/images/manager.png"));
			break;

		default:
			System.out.println("No role detected!");
			break;
		}

		welcomeLabel.setText("Welcome " + currentUser.fullName() + "!");
		roleLabel.setText(currentUser.getRole_type());

		ImageView roleImg = new ImageView();
		if (image != null) {
			roleImg.setImage(image);
			roleImg.setFitHeight(350.0);
			roleImg.setFitWidth(350.0);
			rigthVbox.getChildren().addAll(roleImg);
		}

		// updateButtonsSize();
	}

//	// updates the buttons width by the max width
//	// bug here
//	private void updateButtonsSize() {
//		double maxWidth = 0;
//		maxWidth = topBtn.getPrefWidth() > middleBtn.getPrefWidth() ? topBtn.getPrefWidth() : middleBtn.getPrefWidth();
//		maxWidth = maxWidth > bottomBtn.getPrefWidth() ? maxWidth : bottomBtn.getPrefWidth();
//		topBtn.setMinWidth(200);
//		middleBtn.setMinWidth(200);
//		bottomBtn.setMinWidth(200);
//	}

	private void setTopButton() {
		String userRole = currentUser.getRole_type();
		if (userRole.equals("registered")) {
			topBtn.setText("Create New Order");
			tooltip = new TooltipSetter("View the catalog and create a new order");
			topBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ViewCatalog);
				}
			});
		} else {
			topBtn.setText("Approve Users");
			tooltip = new TooltipSetter("View, manage and approve users");
			topBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.UsersManagement);
				}
			});
		}
		topBtn.setTooltip(tooltip.getTooltip());
		topBtn.setVisible(true);

	}

	private void setMiddleButton() {
		String userRole = currentUser.getRole_type();
		if (userRole.equals("registered")) {
			middleBtn.setText("Collect An Order");
			tooltip = new TooltipSetter("Collect any orders that are ready");
			middleBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.CollectOrder);
					// // not working yet
				}
			});
		} else {
			middleBtn.setText("View Reports");
			tooltip = new TooltipSetter("View the current monthly reports");
			middleBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (userRole.equals("regionManager")) {
						NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.ReportSelection); // not
																												// working
																												// yet
					} else {
						NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.CEOReportSelection);
					}
				}
			});
		}
		middleBtn.setTooltip(tooltip.getTooltip());
		middleBtn.setVisible(true);

	}

	/// register and Manager share the same button.
	private void setBottomButton() {
		// ceo / manager etc
		bottomBtn.setText("Supply Management");
		tooltip = new TooltipSetter("Manage the available supply");

		bottomBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				NavigationStoreController.getInstance().setCurrentScreen(ScreensNames.SupplyReport);
			}
		});
		bottomBtn.setTooltip(tooltip.getTooltip());
		bottomBtn.setVisible(true);
	}

	/**
	 * Log out from the system. sets the current user to null and changes the view
	 * 
	 * @param event
	 */
	@FXML
	private void logOutAction(ActionEvent event) {
		currentUser = null;
		NavigationStoreController.getInstance().refreshStage(ScreensNames.Login);

	}

}
