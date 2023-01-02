package common;

import java.util.ArrayList;

import client.ClientController;
import controllerGui.HostClientController;
import entity.MachineEntity;

public class CommonData {
	private static ClientController chat = HostClientController.chat; // one instance
	private static ArrayList<String> allRegions;
	private static ArrayList<MachineEntity> allMachines;

	public static void initData() {
		chat.acceptObj(new Message(TaskType.InitRegions, null));
		//chat.acceptObj(new Message(TaskType.InitMachines, null));
	}
	
	public static void recieveRegions(ArrayList<String> regions) {
		allRegions = regions;
		return;
	}
	
	public static ArrayList<String> getRegions() {
		if (allRegions!=null)
			return allRegions;
		return new ArrayList<String>();
	}
	public static void recieveMachines(ArrayList<MachineEntity> machines) {
		allMachines = machines;
		return;
	}
	
	public static ArrayList<MachineEntity> getMachines() {
		if (allMachines!=null)
			return allMachines;
		return new ArrayList<MachineEntity>();
	}
}
