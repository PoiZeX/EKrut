package entity;

public class MachineEntity extends MainEntity {
	

	public int reigonID;
	public String reigonName;
	public int machineID;
	public String machineName;
	private int minamount;
	
	public MachineEntity(int reigonID, String reigonName, int machineID, String machineName ,int minamount) {
		super(machineID);
		this.reigonID = reigonID;
		this.reigonName = reigonName;
		this.machineID = machineID;
		this.machineName = machineName;
		this.minamount=minamount;
	}
	
	public int getMinamount() {
		return minamount;
	}

	public void setMinamount(int minamount) {
		this.minamount = minamount;
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
