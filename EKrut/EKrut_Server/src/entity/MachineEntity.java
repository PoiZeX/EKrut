package entity;

public class MachineEntity extends MainEntity {
	

	public int regionId;
	public String regionName;
	public int machineId;
	public String machineName;
	private int minamount;
	
	public MachineEntity(int regionId, String regionName, int machineId, String machineName ,int minamount) {
		super(machineId);
		this.regionId = regionId;
		this.regionName = regionName;
		this.machineId = machineId;
		this.machineName = machineName;
		this.minamount=minamount;
	}
	
	public int getMinamount() {
		return minamount;
	}

	public void setMinamount(int minamount) {
		this.minamount = minamount;
	}

	public int getRegionId() {
		return regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public int getMachineId() {
		return machineId;
	}

	public String getMachineName() {
		return machineName;
	}
	@Override
	public String toString() {
		return machineName;
	}
}
