package Store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.TaskType;
import common.ScreensNames;
import controllerGui.HomePageController;
import controllerGui.HostClientController;
import entity.UserEntity;
import javafx.animation.PauseTransition;
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
import javafx.scene.input.InputEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.AppConfig;

/**
 * The class handles the navigation store for different pages
 * 
 * @category Navigation
 *
 */
public class NavigationStoreController {
	private HashMap<ScreensNames, Scene> screenScenes; // saves the instance of the screen
	private Stack<Scene> history; // saves the history of screens changes
	private static NavigationStoreController instance = null;
	private Stage primaryStage; // the main stage (window)
	private ScreensNames[] isSkipped = { ScreensNames.HostClient, ScreensNames.HomePage, ScreensNames.Login };
	public static UserEntity connectedUser; // hold the current connected user

	/**
	 * Constructor, creates the new instances
	 */
	private NavigationStoreController() {
		// create objects
		screenScenes = new HashMap<>();
		history = new Stack<>();
		primaryStage = new Stage();
		setupTimeout();
		primaryStage.getIcons().add(new Image("/styles/icons/logotaskbar.png"));
		primaryStage.show(); // show primary stage

	}

	/**
	 * singleton design pattern, create / get the instance
	 * 
	 * @return
	 */
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
		Scene scene = screenScenes.get(scName);

		// if null create new instance (should not happens)
		if (scene == null)
			scene = createSingleScene(scName);
		handleTitle(scName);
		// save to stack
		primaryStage.setScene(history.push(scene));
	}

	private void handleTitle(ScreensNames scName) {
		// Set title
		String[] splitString = scName.toString().split("(?<=[^A-Z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][^A-Z])");
		if (splitString.length == 2) {
			primaryStage.setTitle(splitString[0] + " " + splitString[1]);
		} else {
			primaryStage.setTitle(scName.toString());
		}

	}

	/**
	 * sets the current view to previous
	 */
	public void goBack() {
		// show the last stage
		// history will never be null, you can't go back to login page (and even before)
		if (history.size() >= 1) {
			history.pop(); // throw the current
			for (ScreensNames key : screenScenes.keySet()) {
				if (screenScenes.get(key).equals(history.peek()))
					primaryStage.setTitle(key.toString());
			}
			primaryStage.setScene(history.peek());

		}

	}

	/**
	 * Creates new instance of the screen
	 * 
	 * @param screenName
	 * @return
	 */
	public boolean refreshStage(ScreensNames screenName) {
		Scene scene = createSingleScene(screenName); // create new instance
		if (scene == null)
			return false;
		screenScenes.replace(screenName, scene); // replace the last stage with new

		// REPLACE the stack head
		handleTitle(screenName);
		if (history.size() > 0)
			history.pop(); // remove the last instance of the current screen and sets a new one
		primaryStage.setScene(history.push(scene));
		return true;
	}

	/**
	 * Set all stages into HashMap, using 'setSingleStage'
	 */
	private void setAllScenes() {
		for (ScreensNames screenName : ScreensNames.values()) {
			Scene scene = createSingleScene(screenName);
			if (scene != null)
				screenScenes.put(screenName, scene);
		}
	}

	/**
	 * Creates one scene and attach to the primary stage (to save in dictionary)
	 * 
	 * @param screenName
	 * @return
	 */
	private Scene createSingleScene(ScreensNames screenName) {
		Scene scene = null;
		ArrayList<ScreensNames> skippedScreens = new ArrayList<>(Arrays.asList(isSkipped));
		try {
			String path = "/boundary/" + screenName.toString() + "Boundary.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			Parent root = loader.load();

			if (!skippedScreens.contains(screenName))// for submit
				scene = new Scene(setBottomBar(root));
			else
				scene = new Scene(root);
			scene.setUserData(loader.getController());

			// refresh activity
			if ((connectedUser != null && connectedUser.isLogged_in()))
				scene.addEventFilter(InputEvent.ANY, evt -> transition.playFromStart());

			// set actions
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if (connectedUser != null && connectedUser.isLogged_in()) {
						connectedUser.setLogged_in(false); // logout the user
						HostClientController.chat.acceptObj(new Message(TaskType.SetUserLoggedIn, connectedUser));
					}
					if (HostClientController.chat != null)
						HostClientController.chat.acceptObj(new Message(TaskType.ClientDisconnect, null));
					closeAllScreens();
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scene;
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
		returnImage.setFitHeight(40.0);
		returnImage.setFitWidth(40.0);
		returnImage.setPickOnBounds(true);
		returnImage.setPreserveRatio(true);
		returnImage.getStyleClass().add("Button-return");

		returnBtn.setId("returnBtn");
		returnBtn.setAlignment(Pos.CENTER);
		returnBtn.setContentDisplay(ContentDisplay.BOTTOM);
		returnBtn.setGraphic(returnImage);
		returnBtn.setPrefSize(24.0, 27.0);
		returnBtn.getStyleClass().add("Button-return");

		returnBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				NavigationStoreController.getInstance().goBack();
			}
		});

		if (((BorderPane) stage).bottomProperty().getValue() instanceof GridPane) {
			GridPane t = (GridPane) ((BorderPane) stage).bottomProperty().getValue();
			t.add(returnBtn, 0, 0);
		} else {
			((BorderPane) stage).setBottom(returnBtn);
		}

		return stage;

	}

	/**
	 * Close all screens, and exits from platform & system
	 */
	public static void closeAllScreens() {

		Platform.exit(); // exit JavaFx
		System.exit(0); // exit system

	}

	public Object getController() {
		return primaryStage.getScene().getUserData();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * logout should call this method
	 */
	public void clearAll() {
		screenScenes.clear();
		history.clear();
	}

	public static PauseTransition transition;

	/**
	 * setup timeout
	 */
	public void setupTimeout() {
		transition = new PauseTransition(new javafx.util.Duration(AppConfig.INACTIVITY_LOGOUT));
		// create transition for logout
		transition.setOnFinished(evt -> logoutFromTimeout());
	}

	/**
	 * logout user after reaching inactivity time limit
	 */
	private void logoutFromTimeout() {
		if (connectedUser != null && connectedUser.isLogged_in()) {
			connectedUser.setLogged_in(false); // logout user
			HostClientController.chat.acceptObj(new Message(TaskType.SetUserLoggedIn, connectedUser));
			connectedUser = null;
			NavigationStoreController.getInstance().clearAll();
			NavigationStoreController.getInstance().refreshStage(ScreensNames.Login);
			CommonFunctions.createPopup(PopupTypeEnum.Information, "Disconnected due to inactivity");
		}
	}

}
