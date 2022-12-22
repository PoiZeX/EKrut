package entity;

public class MachineEntity {
	public int reigonID;
	public String reigonName;
	public int machineID;
	public String machineAddress;
	
	public MachineEntity(int reigonID, String reigonName, int machineID, String machineAddress) {
		super();
		this.reigonID = reigonID;
		this.reigonName = reigonName;
		this.machineID = machineID;
		this.machineAddress = machineAddress;
	}
	
	public int getReigonID() {
		return reigonID;
	}

	public String getReigonName() {
		return reigonName;
	}

	public int getMachineID() {
		return machineID;
	}

	public String getMachineAddress() {
		return machineAddress;
	}
}
