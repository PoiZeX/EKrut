package entity;

public class MachineEntity extends MainEntity {
	

	public int regionId;
	public String regionName;
	public int machineId;
	public String machineName;
	private int minamount;
	
	/**
	 * Instantiates a new machine entity.
	 *
	 * @param regionId the region id
	 * @param regionName the region name
	 * @param machineId the machine id
	 * @param machineName the machine name
	 * @param minamount the minamount
	 */
	public MachineEntity(int regionId, String regionName, int machineId, String machineName ,int minamount) {
		super(machineId);
		this.regionId = regionId;
		this.regionName = regionName;
		this.machineId = machineId;
		this.machineName = machineName;
		this.minamount=minamount;
	}
	
	/**
	 * Gets the minamount.
	 *
	 * @return the minamount
	 */
	public int getMinamount() {
		return minamount;
	}

	/**
	 * Sets the minamount.
	 *
	 * @param minamount the new minamount
	 */
	public void setMinamount(int minamount) {
		this.minamount = minamount;
	}

	/**
	 * Gets the region id.
	 *
	 * @return the region id
	 */
	public int getRegionId() {
		return regionId;
	}

	/**
	 * Gets the region name.
	 *
	 * @return the region name
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * Gets the machine id.
	 *
	 * @return the machine id
	 */
	public int getMachineId() {
		return machineId;
	}

	/**
	 * Gets the machine name.
	 *
	 * @return the machine name
	 */
	public String getMachineName() {
		return machineName;
	}
	@Override
	public String toString() {
		return machineName;
	}
}
