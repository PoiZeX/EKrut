package entity;

public class ItemInMachineEntity extends ItemEntity {

	public enum Call_Status {
		Processed("Processed"), Complete("Complete"), NotOpened("NotOpened");

		Call_Status(String string) {
			this.name = string;
		}

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

	public ItemInMachineEntity(ItemEntity other) {
		super(other.getId(), other.getName(), other.getPrice(), other.getItemImage());
		this.machineId = -1;
		this.currentAmount = Integer.MAX_VALUE;
	}

	// machine_id, item_id, current_amount, call_status,
	// times_under_min, workerId, name, item_img_name
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
		this.workerId=workerId;
	}

	@Override
	public String getName() {
		return super.getName();
	}

	public int getTimeUnderMin() {
		return timeUnderMin;
	}

	public void setTimeUnderMin(int timeUnderMin) {
		this.timeUnderMin = timeUnderMin;
	}

	public boolean isCallOpen() {
		return isCallOpen;
	}

	public void setCallOpen(boolean isCallOpen) {
		this.isCallOpen = isCallOpen;
	}

	public Call_Status getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(Call_Status callStatus) {
		this.callStatus = callStatus;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public int getMachineId() {
		return machineId;
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

}
