package Store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

import common.CommonFunctions;
import common.Message;
import common.PopupTypeEnum;
import common.TaskType;
import common.ScreensNames;
import controllerGui.HomePageController;
import controllerGui.HostClientController;
import entity.ScreenEntity;
import entity.UserEntity;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.TextAlignment;
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
	private HashMap<ScreensNames, ScreenEntity> screenScenes; // saves the instance of the screen
	private Stack<ScreenEntity> history; // saves the history of screens changes
	private static NavigationStoreController instance = null;
	private Stage primaryStage; // the main stage (window)
	private ScreensNames[] isSkipped = { ScreensNames.HostClient, ScreensNames.HomePage, ScreensNames.Login };
	public static UserEntity connectedUser; // hold the current connected user
	private static boolean isFirstTimeClosing = true;

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
		ScreenEntity se = screenScenes.get(scName);
		Scene scene = null;
		if (se == null)
			se = new ScreenEntity(scName, null);

		scene = se.getScene();

		// if null create new instance (should not happens)
		if (scene == null) {
			scene = createSingleScene(scName, se);

			se.setScene(scene);
			screenScenes.put(scName, se);
		}
		setWindowTitle(scName);
		// save to stack
		primaryStage.setScene(history.push(se).getScene());

		setTopBarLabels(se);
	}

	/**
	 * Set the screen title of top bar for each screen
	 */
	private void setTopBarLabels(ScreenEntity se) {
		if (se.getHeadline() == null || se.getPath() == null)
			return;
		if (history.size() > 0)
			se.getHeadline().setText((history.peek().toString()));
		String res = "";

		// for every screen exclude the 'hostclient' and 'login'
		for (int i = 2; i < history.size(); i++) {
			res += history.get(i).toString() + " ➜ ";
		}

		if (!res.equals(""))
			res = res.substring(0, res.length() - 4); // delete the last arrow
		se.getPath().setText(res);
	}

	/**
	 * Set the title for the window
	 * 
	 * @param scName
	 */
	private void setWindowTitle(ScreensNames scName) {
		// Set title
		primaryStage.setTitle(CommonFunctions.splitByUpperCase(scName.toString()));
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
				if (screenScenes.get(key).equals(history.peek())) {
					primaryStage.setTitle(key.toString());
					break;
				}
			}
			primaryStage.setScene(history.peek().getScene());

			// setTopBarLabels(se);

		}

	}

	/**
	 * Creates new instance of the screen
	 * 
	 * @param screenName
	 * @return
	 */
	public boolean refreshStage(ScreensNames screenName) {
		ScreenEntity se = new ScreenEntity(screenName, null);
		Scene scene = createSingleScene(screenName, se); // create new instance
		if (scene == null)
			return false;

		se.setScene(scene);

		screenScenes.replace(screenName, se); // replace the last stage with new

		// REPLACE the stack head
		setWindowTitle(screenName);
		if (history.size() > 0)
			history.pop(); // remove the last instance of the current screen and sets a new one

		primaryStage.setScene(history.push(se).getScene());
		setTopBarLabels(se);

		return true;
	}

//	/**
//	 * Set all stages into HashMap, using 'setSingleStage'
//	 */
//	private void setAllScenes() {
//		for (ScreensNames screenName : ScreensNames.values()) {
//			Scene scene = createSingleScene(screenName);
//			if (scene != null)
//				screenScenes.put(screenName, scene);
//		}
//	}

	/**
	 * Creates one scene and attach to the primary stage (to save in dictionary)
	 * 
	 * @param screenName
	 * @return
	 */
	private Scene createSingleScene(ScreensNames screenName, ScreenEntity se) {
		Scene scene = null;

		ArrayList<ScreensNames> skippedScreens = new ArrayList<>(Arrays.asList(isSkipped));
		try {
			String path = "/boundary/" + screenName.toString() + "Boundary.fxml";
			FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
			Parent root = loader.load();

			if (!skippedScreens.contains(screenName))// for submit
				scene = new Scene(setBottomBar(root, se));
			else
				scene = new Scene(root);
			scene.setUserData(loader.getController());
			se.setScene(scene); // set scene in entity

			// refresh activity
			if ((connectedUser != null && connectedUser.isLogged_in()))
				scene.addEventFilter(InputEvent.ANY, evt -> transition.playFromStart());

			// set actions
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					ExitHandler(true);
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
	private Parent setBottomBar(Parent stage, ScreenEntity se) {
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
		
		if ( ((BorderPane) stage).getTop() == null )
			((BorderPane) stage).setTop(getTopBar(se));
		
		else if (((BorderPane) stage).getTop() instanceof GridPane) {
			GridPane top = (GridPane) ((BorderPane) stage).getTop();
			top.add(getTopBar(se), 0, 0, top.getColumnConstraints().size(), 1);
		}
		
		return stage;
	}

	/**
	 * Set the navigation Top bar for all pages after login
	 * 
	 * @param stage
	 * @return
	 */
	private GridPane getTopBar(ScreenEntity se) {
		GridPane gridPane = new GridPane();
		Label nameLbl = new Label(); // se.getHeadline();
		Label roleLbl = new Label(); // se.getPath();

		// grid pane setup
		gridPane.setId("headerBar");
		gridPane.getColumnConstraints()
				.add(new ColumnConstraints(10.0, 900.0, 900.0, Priority.SOMETIMES, HPos.LEFT, true));
		gridPane.getRowConstraints().add(new RowConstraints(10.0, 20.0, 20.0, Priority.NEVER, VPos.TOP, true));
		gridPane.getRowConstraints().add(new RowConstraints(10.0, 37.0, 45.0, Priority.NEVER, VPos.CENTER, true));
		gridPane.setMouseTransparent(true);
		// gridPane.setPadding(new Insets(22.0, 0, 0, 5.0));

		// main label setup
		nameLbl.setId("welcomeLabel");
		nameLbl.setAlignment(Pos.CENTER);
		nameLbl.setTextAlignment(TextAlignment.CENTER);
		nameLbl.setPrefSize(900, 35);
		nameLbl.getStyleClass().add("LabelTitle");

		// sub-label setup
		roleLbl.setId("roleLabel");
		roleLbl.setPrefSize(600, 40);
		roleLbl.getStyleClass().add("LabelLocations");

		gridPane.add(nameLbl, 0, 1);
		gridPane.add(roleLbl, 0, 0);

		se.setHeadline(nameLbl);
		se.setPath(roleLbl);

		return gridPane;

	}

	/**
	 * return the current 'user data' which hold the controller
	 * 
	 * @return
	 */
	public Object getController() {
		return primaryStage.getScene().getUserData();
	}

	/**
	 * return the primary stage
	 * 
	 * @return
	 */
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
			CommonFunctions.SleepFor(200, () -> {
				CommonFunctions.createPopup(PopupTypeEnum.Information, "Disconnected due to inactivity");
			});
		}
	}

	/**
	 * exit handler which can target from different places
	 * 
	 * @param closeAllScreens
	 */
	public static void ExitHandler(boolean closeAllScreens) {
		if (isFirstTimeClosing) {
			isFirstTimeClosing = false;
			if (connectedUser != null && connectedUser.isLogged_in()) {
				connectedUser.setLogged_in(false); // logout the user
				HostClientController.chat.acceptObj(new Message(TaskType.SetUserLoggedIn, connectedUser));
			}
			if (HostClientController.chat != null)
				HostClientController.chat.acceptObj(new Message(TaskType.ClientDisconnect, null));
			if (!closeAllScreens) { // reset all and refresh
				NavigationStoreController.getInstance().clearAll();
				connectedUser = null;
				NavigationStoreController.getInstance().refreshStage(ScreensNames.Login);
			} else
				closeAllScreens();
		}

	}

	/**
	 * Close all screens, and exits from platform & system
	 */
	public static void closeAllScreens() {
		Platform.exit(); // exit JavaFx
		System.exit(0); // force the system to exit
		// exit
		// function)

	}

}
