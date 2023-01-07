package common;

import java.util.ArrayList;

import client.ClientController;
import controllerGui.HostClientController;
import entity.MachineEntity;
import entity.UserEntity;
import utils.AppConfig;

public class CommonData {
	private static ClientController chat = HostClientController.chat; // one instance
	private static ArrayList<String> allRegions;
	private static ArrayList<MachineEntity> allMachines;
	private static MachineEntity currentMachine;
	
	public static void initData() {
		chat.acceptObj(new Message(TaskType.InitRegions, null));
		chat.acceptObj(new Message(TaskType.InitMachines, null));
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
	
	@SuppressWarnings("unused")
	public static void recieveMachines(ArrayList<MachineEntity> machines) {
		allMachines = machines;
		if(AppConfig.SYSTEM_CONFIGURATION == "EK")
			for (MachineEntity m : allMachines)
				if (m.getId() == AppConfig.MACHINE_ID)
					setCurrentMachine(m);
		return;
	}
	
	public static ArrayList<MachineEntity> getMachines() {
		if (allMachines!=null)
			return allMachines;
		return new ArrayList<MachineEntity>();
	}

	public static MachineEntity getCurrentMachine() {
		return currentMachine;
	}

	public static void setCurrentMachine(MachineEntity currentMachine) {
		CommonData.currentMachine = currentMachine;
	}
	
}
