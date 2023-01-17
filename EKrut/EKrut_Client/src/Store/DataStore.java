package Store;

import java.util.ArrayList;

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
<<<<<<< HEAD
=======
 * @author דוד
 *
>>>>>>> parent of a63f8de (testing)
 */
public class DataStore {
	private static ClientController chat = HostClientController.getChat(); // one instance
	private static ArrayList<String> allRegions;
	private static ArrayList<MachineEntity> allMachines;
	private static MachineEntity currentMachine;

	/**
	 * First initialize data, fetch from server
	 */
	public static void initData() {
		chat.acceptObj(new Message(TaskType.InitRegions, null));
		chat.acceptObj(new Message(TaskType.InitMachines, null));
	}

	/**
	 * saves regions from server
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
	 * @return
	 */
	public static ArrayList<String> getRegions() {
		if (allRegions != null)
			return allRegions;
		return new ArrayList<String>();
	}

	/**
	 * receive machines from server
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
	 * @return
	 */
	public static ArrayList<MachineEntity> getMachines() {
		if (allMachines != null)
			return allMachines;
		return new ArrayList<MachineEntity>();
	}

	/**
	 * Get current machine working on
	 * @return
	 */
	public static MachineEntity getCurrentMachine() {
		return currentMachine;
	}
	
	/**
	 * Sets the current machine working on
	 * @param currentMachine
	 */
	public static void setCurrentMachine(MachineEntity currentMachine) {
		DataStore.currentMachine = currentMachine;
	}

}
