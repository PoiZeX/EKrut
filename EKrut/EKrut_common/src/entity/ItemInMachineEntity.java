package entity;

public class ItemInMachineEntity extends ItemEntity {

	public enum Call_Status {
		Processed("Processed"), Complete("Complete"), NotOpened("NotOpened");

		/**
		 * Instantiates a new call status.
		 *
		 * @param string the string
		 */
		Call_Status(String string) {
			this.name = string;
		}

		/**
		 * Gets the name.
		 *
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		private final String name;
	};

	private int machineId;
	private int currentAmount;
	private int timeUnderMin;
	private Integer workerId;
	private boolean isCallOpen;
	private Call_Status callStatus = Call_Status.NotOpened;

	/**
	 * Instantiates a new item in machine entity.
	 *
	 * @param other the other
	 */
	public ItemInMachineEntity(ItemEntity other) {
		super(other.getId(), other.getName(), other.getPrice(), other.getItemImage());
		this.machineId = -1;
		this.currentAmount = Integer.MAX_VALUE;
	}

	/**
	 * Instantiates a new item in machine entity.
	 *
	 * @param machineId the machine id
	 * @param item_id the item id
	 * @param currentAmount the current amount
	 * @param callStatus the call status
	 * @param timeUnderMin the time under min
	 * @param workerId the worker id
	 * @param name the name
	 * @param price the price
	 * @param item_img_nam the item img nam
	 */
	public ItemInMachineEntity(int machineId, int item_id, int currentAmount, Call_Status callStatus, int timeUnderMin,
			int workerId, String name, double price, String item_img_nam) {
		super(item_id, name, price, item_img_nam);
		this.machineId = machineId;
		this.currentAmount = currentAmount;
		this.callStatus = callStatus;
		if (this.callStatus.equals(callStatus.NotOpened))
			this.isCallOpen = false;
		else
			this.isCallOpen = true;
		this.timeUnderMin = timeUnderMin;
		this.workerId = workerId;
	}

	@Override
	public String getName() {
		return super.getName();
	}

	/**
	 * Gets the time under min.
	 *
	 * @return the time under min
	 */
	public int getTimeUnderMin() {
		return timeUnderMin;
	}

	/**
	 * Sets the time under min.
	 *
	 * @param timeUnderMin the new time under min
	 */
	public void setTimeUnderMin(int timeUnderMin) {
		this.timeUnderMin = timeUnderMin;
	}

	/**
	 * Checks if is call open.
	 *
	 * @return true, if is call open
	 */
	public boolean isCallOpen() {
		return isCallOpen;
	}

	/**
	 * Sets the call open.
	 *
	 * @param isCallOpen the new call open
	 */
	public void setCallOpen(boolean isCallOpen) {
		this.isCallOpen = isCallOpen;
	}

	/**
	 * Gets the call status.
	 *
	 * @return the call status
	 */
	public Call_Status getCallStatus() {
		return callStatus;
	}

	/**
	 * Sets the call status.
	 *
	 * @param callStatus the new call status
	 */
	public void setCallStatus(Call_Status callStatus) {
		this.callStatus = callStatus;
	}

	/**
	 * Gets the current amount.
	 *
	 * @return the current amount
	 */
	public int getCurrentAmount() {
		return currentAmount;
	}

	/**
	 * Sets the current amount.
	 *
	 * @param currentAmount the new current amount
	 */
	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
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
	 * Gets the worker id.
	 *
	 * @return the worker id
	 */
	public Integer getWorkerId() {
		return workerId;
	}

	/**
	 * Sets the worker id.
	 *
	 * @param workerId the new worker id
	 */
	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

}
