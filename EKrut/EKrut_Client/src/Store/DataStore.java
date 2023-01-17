package Store;

import java.util.ArrayList;
import java.util.HashMap;

import client.ClientController;
import common.Message;
import controller.OrderController;
import controllerGui.HostClientController;
import entity.MachineEntity;
import enums.TaskType;
import javafx.application.Platform;
import utils.AppConfig;

/**
 * The class saves and handles the data needs to be stored for whole process
 * 
 * @author Lidor
 *
 */
public class DataStore {
	private static ClientController chat = HostClientController.getChat(); // one instance
	private static ArrayList<String> allRegions;
	private static ArrayList<MachineEntity> allMachines;
	private static HashMap<String, String> allUsers;
	private static MachineEntity currentMachine;
	private static boolean isDataRecived = false;

	/**
	 * First initialize data, fetch from server
	 */
	public static void initData() {
		waitOnTaskType(TaskType.InitRegions);
		waitOnTaskType(TaskType.InitMachines);
	}		

public static void initUsers() {
	waitOnTaskType(TaskType.InitUsers);
}
	private static void waitOnTaskType(TaskType task) {
		isDataRecived = false;
		chat.acceptObj(new Message(task, null));

		while (!isDataRecived) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * saves regions from server
	 * @param regions
	 */
	public static void recieveRegions(ArrayList<String> regions) {
		allRegions = regions;
		isDataRecived = true;
		return;
	}

	/**
	 * saves regions from server
	 * 
	 * @param regions
	 */
	public static void recieveUsers(HashMap<String, String> usersMap) {
		allUsers = usersMap;
		isDataRecived = true;
		return;
	}

	/**
	 * receive machines from server
	 * 
	 * @param machines
	 */
	public static void recieveMachines(ArrayList<MachineEntity> machines) {
		allMachines = machines;
		for (MachineEntity m : allMachines)
			if (m.getId() == AppConfig.MACHINE_ID) {
				setCurrentMachine(m);
				OrderController.setCurrentMachine(m);
			}

		isDataRecived = true;
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
	
	/**
	 * Get all users to select one
	 * 
	 * @return
	 */
	public static HashMap<String, String> getUsers() {
		return allUsers;
	}

}
