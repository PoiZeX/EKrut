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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
//		if (stage == null)
//			stage = setSingleStage("/boundary/" + scName.toString() + "Boundary.fxml");
		// hide the current view
		if (history.size() > 0) {
			history.peek().hide();
		}

		// set new current view
		stage.show();

		// save to stack
		history.push(stage);
	}

	/**
	 * sets the current view to previous
	 * 
	 */
	public void goBack(ActionEvent event) {
		// hide the 'current'
		//((Node) event.getSource()).getScene().getWindow().hide();

		// show the last stage
		// history will never be null, you can't go back to login page (and even before)
		history.pop().hide();
		history.pop().show();
		
	}

	/**
	 * Creates new instance of the screen
	 * 
	 * @param screenName
	 * @return
	 */
	public boolean refreshStage(ScreensNames screenName) {
		Stage stage = setSingleStage("/boundary/" + screenName.toString() + "Boundary.fxml");
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
			Stage stage = setSingleStage("/boundary/" + screenName.toString() + "Boundary.fxml");
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
	private Stage setSingleStage(String path) {
		Stage primaryStage = new Stage();

		try {
			Parent root = FXMLLoader.load(getClass().getResource(path));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
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
	 * Close all screens, and exits from platform & system
	 */
	private void closeAllScreens() {
		for (var screen : history)
			screen.close();
		Platform.exit(); // exit JavaFx
		System.exit(0); // exit system

	}
}
