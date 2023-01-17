package utils;

/**
 * Class defines global configuration for client side
 *
 */
public final class AppConfig {
	
	// System definition
	public static String SYSTEM_CONFIGURATION = "OL"; 						// can be changed to OL / EK
	public static int MACHINE_ID = 1; 										// machine id will be dynamically change from arguments
	public static final int INACTIVITY_LOGOUT = 1000*60*15;					// time in sec
	
	// Login restrictions
	public static final int USERNAME_MIN_LENGTH = 4;
	public static final int USERNAME_MAX_LENGTH = 16;
	public static final int PASSWORD_MIN_LENGTH = 4;
	public static final int PASSWORD_MAX_LENGTH = 16;
	public static final String USERNAME_ALPHA_ALLOWED = "^[a-zA-Z][\\w]*$";
	
	// EKT timers in mills
	public static final int WAIT_BEFORE_SIMULATE_LOGIN = 4000; 				// define time to simulate success
	public static final int WAIT_AFTER_VALIDATION_SUCCESS = 2000; 			// define time to simulate data validation
	public static final int WAIT_BEFORE_MSG = 15000; 						// define time to simulate timeout of connection
	public static final int WAIT_AFTER_MSG = 5000;   						// define timer to read a message before closing the window

	// Path
	public static final String PRODUCTS_PATH_CLIENT = "../EKrut_Client/src/styles/products/"; 
	public static final String RELAITVE_PRODUCTS_PATH = "../styles/products/";

} 
