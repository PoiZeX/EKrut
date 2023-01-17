package client;

import java.util.Arrays;

import com.sun.media.jfxmedia.logging.Logger;

import Store.NavigationStoreController;
import controllerGui.HostClientController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import utils.AppConfig;

/**
 * The start class of application
 *
 */
public class ClientUI extends Application {
	/**
	 * The main method of the application, it sets the system configuration and
	 * machine ID based on the command line arguments passed in. It also adds a
	 * shutdown hook to call the ExitHandler. If no arguments are passed, the system
	 * configuration is set to "OL" and the machine ID is ignored. The launch method
	 * is called to start the application. @param @throws
	 */
	public static void main(String[] args) throws Exception {

		if (args.length > 0) {
			// args should be like: <java -jar EKrut_server.jar arg OL> OR <java -jar
			// EKrut_server.jar arg EK <number>>
			// everything else will be ignored and set to default
			if (args[1].equals("OL")) {
				AppConfig.SYSTEM_CONFIGURATION = "OL";

			} else if (args[1].equals("EK")) {
				AppConfig.SYSTEM_CONFIGURATION = "EK";
				if (args.length >= 3)
					try {
						AppConfig.MACHINE_ID = (int) Integer.parseInt(args[2]);
					} catch (Exception e) {
						System.out.println("Please enter a valid machine number");
						System.exit(0);
					}

				else {
					System.out.println("Please enter a machine id when using EK Configuration");
					System.exit(0);
				}

			} else {
				System.out.println("If you want to work with EK / OL please insert Command as following:\n"
						+ "java -jar EKrut_server.jar arg OL> OR <java -jar EKrut_server.jar arg EK <number>>\n\nBy default work on OL configuration");
				// default
				AppConfig.SYSTEM_CONFIGURATION = "OL"; // and machine id doesn't matter
			}
		}
		// if args were not inserted - use default
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			NavigationStoreController.ExitHandler(true);
		}));
		launch(args);
	}

	/**
	 * This method starts the application by creating a new HostClientController
	 * object and calling its start method, passing the primary stage as a
	 * parameter. @param @throws
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		HostClientController aFrame = new HostClientController();
		aFrame.start(primaryStage);

	}
}
