package entity;

public class ItemInMachineEntity extends ItemEntity {
	public enum call_Status{ Opend, Processed, Complete, NotOpened } ;
	private int machineID;
	private int minAmount ,currentAmount;	


	private call_Status callStatus=call_Status.NotOpened;
	boolean availability ;
	private int amount_under_min ;
	int amount_calls;

	
	public ItemInMachineEntity(int item_id, String name, double price, String manufacturer, String description,
			String item_img_name, int machineID, int minAmount, int currentAmount, call_Status callStatus,
			boolean availability, int amount_under_min, int amount_calls) {
		super(item_id, name, price, manufacturer, description, item_img_name);
		this.machineID = machineID;
		this.minAmount = minAmount;
		this.currentAmount = currentAmount;
		this.callStatus = callStatus;
		this.availability = availability;
		this.amount_under_min = amount_under_min;
		this.amount_calls = amount_calls;
	}

	public call_Status getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(call_Status callStatus) {
		this.callStatus = callStatus;
	}

	public int getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(int minAmount) {
		this.minAmount = minAmount;
	}

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
	}

	public boolean isAvailability() {
		return availability;
	}

	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	public int getAmount_under_min() {
		return amount_under_min;
	}

	public void setAmount_under_min(int amount_under_min) {
		this.amount_under_min = amount_under_min;
	}

	public int getAmount_calls() {
		return amount_calls;
	}

	public void setAmount_calls(int amount_calls) {
		this.amount_calls = amount_calls;
	}

	public int getMachineID() {
		return machineID;
	}

}
		