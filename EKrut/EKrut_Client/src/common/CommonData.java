package common;

import java.util.ArrayList;

import client.ClientController;
import controllerGui.HostClientController;

public class CommonData {
	private static ClientController chat = HostClientController.chat; // one instance
	private static ArrayList<String> allRegions;
	public static void initData() {
		chat.acceptObj(new Message(TaskType.InitRegions, null));
			
		
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
}
