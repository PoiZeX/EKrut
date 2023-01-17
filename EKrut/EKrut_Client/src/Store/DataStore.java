package Store;

import java.util.ArrayList;
import java.util.HashMap;

import client.ClientController;
import common.Message;
import controller.OrderController;
import controllerGui.HostClientController;
import entity.MachineEntity;
import entity.UserEntity;
import enums.TaskType;
import javafx.application.Platform;
import utils.AppConfig;

/**
 * The class saves and handles the data needs to be stored for whole process
 */
public class DataStore {
	private static ClientController chat = HostClientController.getChat(); // one instance
	private static ArrayList<String> allRegions;
	private static ArrayList<MachineEntity> allMachines;
	private static HashMap<String, String> allUsers;
	private static MachineEntity currentMachine;
	public static boolean recievedData = false;

	/**
	 * First initialize data, fetch from server
	 */
	public static void initData() {
		chat.acceptObj(new Message(TaskType.InitRegions, null));
		chat.acceptObj(new Message(TaskType.InitMachines, null));
	}

	public static void InitAllUsers() {
		chat.acceptObj(new Message(TaskType.InitUsers));
	}

	/**
	 * @return all users that exist in database
	 */
	public static HashMap<String, String> getUsers() {
		if (allUsers != null)
			return allUsers;
		return new HashMap<String, String>();
	}

	/**
	 * saves users from server in the formula of {username:password}
	 * 
	 * @param users
	 */
	public static void recieveUsers(HashMap<String, String> users) {
		Platform.runLater(() -> {
			allUsers = users;
		});
		recievedData  = true;
		return;
	}

	/**
	 * saves regions from server
	 * 
	 * @param regions
	 */
	public static void recieveRegions(ArrayList<String> regions) {
		Platform.runLater(() -> {
			allRegions = regions;
		});
		return;
	}

	/**
	 * Return the regions (or new if null)
	 * 
	 * @return
	 */
	public static ArrayList<String> getRegions() {
		if (allRegions != null)
			return allRegions;
		return new ArrayList<String>();
	}

	/**
	 * receive machines from server
	 * 
	 * @param machines
	 */
	public static void recieveMachines(ArrayList<MachineEntity> machines) {
		Platform.runLater(() -> {
			allMachines = machines;
			for (MachineEntity m : allMachines)
				if (m.getId() == AppConfig.MACHINE_ID) {
					setCurrentMachine(m);
					OrderController.setCurrentMachine(m);
				}
		});
		return;
	}

	/**
	 * return the machines or null
	 * 
	 * @return
	 */
	public static ArrayList<MachineEntity> getMachines() {
		if (allMachines != null)
			return allMachines;
		return new ArrayList<MachineEntity>();
	}

	/**
	 * Get current machine working on
	 * 
	 * @return
	 */
	public static MachineEntity getCurrentMachine() {
		return currentMachine;
	}

	/**
	 * Sets the current machine working on
	 * 
	 * @param currentMachine
	 */
	public static void setCurrentMachine(MachineEntity currentMachine) {
		DataStore.currentMachine = currentMachine;
	}

}
