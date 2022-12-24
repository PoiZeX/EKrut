package entity;

public class MachineEntity extends MainEntity {
	

	public int reigonID;
	public String reigonName;
	public int machineID;
	public String machineName;
	
	public MachineEntity(int reigonID, String reigonName, int machineID, String machineName) {
		super(machineID);
		this.reigonID = reigonID;
		this.reigonName = reigonName;
		this.machineID = machineID;
		this.machineName = machineName;
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

	public String getMachineName() {
		return machineName;
	}
	@Override
	public String toString() {
		return machineName;
	}
}
