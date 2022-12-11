package Store;


import java.util.HashMap;
import java.util.Stack;

import common.MessageType;
import common.ScreensNames;
import controllerGui.HostClientController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The class handles the navigation store for different pages
 * 
 * @category Navigation
 *
 */
public class NavigationStoreController {
	private HashMap<ScreensNames, Stage> screenStages; // saves the instance of the screen
	private Stack<Stage> history; // saves the history of screens changes
	private static NavigationStoreController instance = null;

	/**
	 * Constructor, creates the new instances
	 */
	private NavigationStoreController() {
		screenStages = new HashMap<>();
		history = new Stack<>();
		setAllStages(); // fill the hashMap
	}

	public static NavigationStoreController getInstance() {
		if (instance == null)
			instance = new NavigationStoreController();
		return instance;
	}

	/**
	 * Set the current screen as given in param
	 * 
	 * @param scName
	 */
	public void setCurrentScreen(ScreensNames scName) {
		Stage stage = screenStages.get(scName);

		// if null create new instance (should not happens)
		if (stage == null)
			stage = setSingleStage(scName);
		// hide the current view

		if (history.size() > 0) 
			history.peek().close();

		// save to stack
		history.push(stage).show();
	}

	/**
	 * sets the current view to previous
	 * 
	 */
	public void goBack(ActionEvent event) {
		// show the last stage
		// history will never be null, you can't go back to login page (and even before)
		if(history.size() >= 2) {
			history.pop().close(); // hide the 'current'
			history.peek().show();
		}
		
	}

	/**
	 * Creates new instance of the screen
	 * 
	 * @param screenName
	 * @return
	 */
	public boolean refreshStage(ScreensNames screenName) {
		Stage stage = setSingleStage(screenName);
		if (stage == null)
			return false;
		screenStages.put(screenName, stage); // replace the last stage with new
		return true;
	}

	/**
	 * Set all stages into HashMap, using 'setSingleStage'
	 */
	private void setAllStages() {
		for (ScreensNames screenName : ScreensNames.values()) {
			Stage stage = setSingleStage(screenName);
			if (stage != null)
				screenStages.put(screenName, stage);
		}
	}

	/**
	 * Creates one stage with the common properties.
	 * 
	 * @param path
	 * @return Stage
	 */
	private Stage setSingleStage(ScreensNames screenName) {
		String path = "/boundary/" + screenName.toString() + "Boundary.fxml";
		Stage primaryStage = new Stage();
		Scene scene;

		try {
			// load fxml
			Parent root = FXMLLoader.load(getClass().getResource(path));

			// load bottom bar (if need)
			if (screenName != ScreensNames.HostClient && screenName != ScreensNames.Login)
				scene = new Scene(setBottomBar(root));
			else
				scene = new Scene(root);

			// attach to stage
			primaryStage.setScene(scene);

			// set actions
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if (HostClientController.chat != null)
						HostClientController.chat.acceptObj(MessageType.ClientDisconnect);
					closeAllScreens();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return primaryStage;
	}

	/**
	 * Set the navigation bottom bar for all pages after login
	 * 
	 * @param stage
	 * @return
	 */
	private Parent setBottomBar(Parent stage) {

		Button returnBtn = new Button();
		ImageView returnImage = new ImageView();

		Image image = new Image(getClass().getResourceAsStream("/styles/icons/return.png"));
		returnImage.setImage(image);
		returnImage.setFitHeight(62.0);
		returnImage.setFitWidth(72.0);
		returnImage.setPickOnBounds(true);
		returnImage.setPreserveRatio(true);
		returnImage.getStyleClass().add("Button-return");

		returnBtn.setId("returnBtn");
		returnBtn.setAlignment(Pos.CENTER);
		returnBtn.setContentDisplay(ContentDisplay.BOTTOM);
		returnBtn.setGraphic(returnImage);
		returnBtn.setPrefSize(69.0, 55.0);
		returnBtn.getStyleClass().add("Button-return");

		returnBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				NavigationStoreController.getInstance().goBack(ae);
			}
		});

		((BorderPane) stage).setBottom(returnBtn);
		return stage;

	}

	/**
	 * Close all screens, and exits from platform & system
	 */
	private void closeAllScreens() {
		for (var screen : history)
			screen.close();
		Platform.exit(); // exit JavaFx
		System.exit(0); // exit system

	}
}
