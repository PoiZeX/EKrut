package entity;

import entity.ItemInMachineEntity.call_Status;

public class ItemInMachineEntity extends ItemEntity {
	
	public enum call_Status{ Opend("Opened"), Processed("Processed"), Complete("Complete"), NotOpened("NotOpened");
		 call_Status(String string) {
		this.name=string;
	}

		public String getName() {
			return name;
		}

		private final String name;
		} ;
	private int machineID;
	private int currentAmount;	


	private call_Status callStatus=call_Status.NotOpened;

	private int amount_under_min ;
	int amount_calls;

	//machine_id, item_id, current_amount, minimum_amount, call_status, times_under_min, calls_amount, name, item_img_name
	public ItemInMachineEntity(int machineID, int item_id, int currentAmount,call_Status callStatus,
			String name, double price, String manufacturer, String description,String item_img_name,    
			 int amount_under_min, int amount_calls) {
		super(item_id, name, price, manufacturer, description, item_img_name);
		this.machineID = machineID;

		this.currentAmount = currentAmount;
		this.callStatus = callStatus;
		this.amount_under_min = amount_under_min;
		this.amount_calls = amount_calls;
	}

	public call_Status getCallStatus() {
		return callStatus;
	}

	public void setCallStatus(call_Status callStatus) {
		this.callStatus = callStatus;
	}

	

	public int getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(int currentAmount) {
		this.currentAmount = currentAmount;
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
		