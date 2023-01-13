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
import common.ScreensNamesEnum;
import controllerGui.HostClientController;
import controllerGui.ViewCatalogController;
import entity.MachineEntity;
import entity.ScreenEntity;
import entity.UserEntity;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import utils.TooltipSetter;

/**
 * The class handles the navigation store for different pages
 * 
 * @category Navigation
 *
 */
public class NavigationStoreController {
	private HashMap<ScreensNamesEnum, ScreenEntity> screenScenes; // saves the instance of the screen
	private Stack<ScreenEntity> history; // saves the history of screens changes
	private static NavigationStoreController instance = null;
	private Stage primaryStage; // the main stage (window)
	private ScreensNamesEnum[] isSkipped = { ScreensNamesEnum.HostClient, ScreensNamesEnum.HomePage,
			ScreensNamesEnum.Login };
	public static UserEntity connectedUser; // hold the current connected user
	public static PauseTransition transition;
	public static boolean isFirstTime = true;

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
	public void setCurrentScreen(ScreensNamesEnum scName) {
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
	private void setWindowTitle(ScreensNamesEnum scName) {
		try {
			String configuration = "";
			if (!scName.equals(isSkipped[0]) && !scName.equals(isSkipped[2]))
				if (AppConfig.SYSTEM_CONFIGURATION.equals("OL")) {
					configuration = " Online";
				} else {
					MachineEntity machine;
					do {
						Thread.sleep(100);
						machine = DataStore.getCurrentMachine();
					} while (machine == null);
					String machineName = machine.getMachineName();
					configuration = CommonFunctions.isNullOrEmpty(machineName) ? "" : " - " + machineName;

				}

			// Set title
			primaryStage.setTitle("EKrut" + configuration);
		} catch (Exception e) {
			e.printStackTrace();
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
			for (ScreensNamesEnum key : screenScenes.keySet()) {
				if (screenScenes.get(key) != null && screenScenes.get(key).equals(history.peek())) {
					primaryStage.setTitle(key.toString());
					break;
				}
			}
			primaryStage.setScene(history.peek().getScene());

		}

	}

	/**
	 * Creates new instance of the screen
	 * 
	 * @param screenName
	 * @return
	 */
	public boolean refreshStage(ScreensNamesEnum screenName) {
		ScreenEntity screenEntity = refreshWithoutScreenChange(screenName);
		if (screenEntity == null)
			return false;

		// checks if the screen already in the stack and remove
		// avoid circles (usually when going backwards)
		while (history.size() > 0 && history.contains(screenEntity)) {
			screenScenes.replace(history.pop().getSc(), null);
		}

		primaryStage.setScene(history.push(screenEntity).getScene());
		setTopBarLabels(screenEntity);

		return true;
	}

	public boolean stackCircleHandler(ScreenEntity screenEntity) {

		return true;
	}

	/**
	 * Sets a new ScreenEntity in the dictionary
	 * 
	 * @param screenName
	 * @return
	 */
	public ScreenEntity refreshWithoutScreenChange(ScreensNamesEnum screenName) {
		ScreenEntity se = new ScreenEntity(screenName, null);
		Scene scene = createSingleScene(screenName, se); // create new instance
		if (scene == null)
			return null;
		se.setScene(scene);
		if (!screenScenes.containsKey(screenName))
			screenScenes.put(screenName, se);
		else
			screenScenes.replace(screenName, se); // replace the last stage with new
		// REPLACE the stack head
		setWindowTitle(screenName);
		return se;
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
	private Scene createSingleScene(ScreensNamesEnum screenName, ScreenEntity se) {
		Scene scene = null;

		ArrayList<ScreensNamesEnum> skippedScreens = new ArrayList<>(Arrays.asList(isSkipped));
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
		returnBtn.setTooltip(new TooltipSetter("Return to the previous screen").getTooltip());
		returnBtn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {

				if (se.getSc().equals(ScreensNamesEnum.ViewCatalog))
					((ViewCatalogController) NavigationStoreController.getInstance().getController()).cancelOrder(null);
				else {
					NavigationStoreController.getInstance().goBack();
				}

			}
		});

		if (((BorderPane) stage).bottomProperty().getValue() instanceof GridPane) {
			GridPane t = (GridPane) ((BorderPane) stage).bottomProperty().getValue();
			t.add(returnBtn, 0, 0);

		} else {
			((BorderPane) stage).setBottom(returnBtn);
		}

		if (((BorderPane) stage).getTop() == null)
			((BorderPane) stage).setTop(getTopBar(se));

		else if (((BorderPane) stage).getTop() instanceof GridPane) {
			GridPane top = (GridPane) ((BorderPane) stage).getTop();
			if (!se.getSc().equals(ScreensNamesEnum.ViewCatalog)) // ignore top on view catalog
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
		Label headlineLabel = new Label(); // se.getHeadline();
		Label pathLbl = new Label(); // se.getPath();
		Button helpBtn = new Button();
		ImageView helpImage = new ImageView();

		// grid pane setup
		gridPane.setId("headerBar");
		gridPane.getColumnConstraints()
				.add(new ColumnConstraints(10.0, 900.0, 900.0, Priority.SOMETIMES, HPos.LEFT, true));
		gridPane.getRowConstraints().add(new RowConstraints(10.0, 20.0, 20.0, Priority.NEVER, VPos.TOP, true));
		gridPane.getRowConstraints().add(new RowConstraints(10.0, 37.0, 45.0, Priority.NEVER, VPos.CENTER, true));
		// gridPane.setPadding(new Insets(22.0, 0, 0, 5.0));
		// gridPane.setMouseTransparent(true);

		// main label setup
		headlineLabel.setId("headlineLabel");
		headlineLabel.setAlignment(Pos.CENTER);
		headlineLabel.setTextAlignment(TextAlignment.CENTER);
		headlineLabel.setPrefSize(900, 35);
		headlineLabel.getStyleClass().add("LabelTitle");

		// sub-label setup
		pathLbl.setId("pathLbl");
		pathLbl.setPrefSize(600, 40);
		pathLbl.getStyleClass().add("LabelLocations");

		// "help" button
		Image image = new Image(getClass().getResourceAsStream("/styles/icons/question.png"));
		helpImage.setImage(image);
		helpImage.setFitHeight(30.0);
		helpImage.setFitWidth(30.0);
		helpImage.setPickOnBounds(true);
		helpImage.setPreserveRatio(true);
		helpImage.getStyleClass().add("Button-return");

		helpBtn.setId("helpBtn");
		helpBtn.setAlignment(Pos.CENTER);
		helpBtn.setContentDisplay(ContentDisplay.BOTTOM);
		helpBtn.setGraphic(helpImage);
		helpBtn.setPrefSize(30.0, 30.0);
		helpBtn.getStyleClass().add("Button-return");
		helpBtn.setTooltip((new TooltipSetter("Click for help")).getTooltip());
		helpBtn.setMouseTransparent(false);

		helpBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				CommonFunctions.createPopup(PopupTypeEnum.Information, se.getSc().getDescription());
			}
		});

		// add to grid
		gridPane.add(pathLbl, 0, 0);
		gridPane.add(headlineLabel, 0, 1);
		gridPane.add(helpBtn, 2, 1);

		se.setHeadline(headlineLabel);
		se.setPath(pathLbl);

		return gridPane;

	}

	/**
	 * return the current 'user data' which hold the controller (for the CURRENT
	 * scene)
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
		isFirstTime = true;
	}

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
			HostClientController.getChat().acceptObj(new Message(TaskType.SetUserLoggedIn, connectedUser));
			connectedUser = null;
			NavigationStoreController.getInstance().clearAll();
			NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.Login);
			CommonFunctions.SleepFor(200, () -> {
				CommonFunctions.createPopup(PopupTypeEnum.Information, "Disconnected due to inactivity");
			});
		}
	}

	/**
	 * exit handler which can be targeted from different places
	 * 
	 * @param closeAllScreens
	 */
	public static void ExitHandler(boolean closeAllScreens) {
		// CommonFunctions.SleepFor(1000, () -> {
		if (connectedUser != null) {
//				ItemsController.deleteAllItemsInDir();

			if (connectedUser.isLogged_in()) {
				connectedUser.setLogged_in(false); // logout the user
				HostClientController.getChat().acceptObj(new Message(TaskType.SetUserLoggedIn, connectedUser));
			}
			connectedUser = null;
		}
		if (isFirstTime) {
			isFirstTime = false;
			if (HostClientController.getChat() != null)
				HostClientController.getChat().acceptObj(new Message(TaskType.ClientDisconnect, null));

			if (!closeAllScreens) { // reset all and refresh
				NavigationStoreController.getInstance().clearAll();
				NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.Login);

			} else
				closeAllScreens();
		}
		// });

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
