package entity;

import entity.ItemInMachineEntity.Call_Status;

public class ItemInMachineEntity extends MainEntity {
	
	public enum Call_Status{Processed("Processed"), Complete("Complete"), NotOpened("NotOpened");
		 Call_Status(String string) {
		this.name=string;
	}

		public String getName() {
			return name;
		}

		private final String name;
		} ;
		
	private int machineID;
	private int itemID;
	private int  currentAmount;	
	private boolean isCallOpen;


	private Call_Status callStatus=Call_Status.NotOpened;



	//machine_id, item_id, current_amount, minimum_amount, call_status, times_under_min, calls_amount, name, item_img_name
	public ItemInMachineEntity(int machineID, int item_id, int currentAmount,Call_Status callStatus) {
		super(item_id);
		this.itemID=item_id;
		this.machineID = machineID;
		this.currentAmount = currentAmount;
		this.callStatus = callStatus;
		if(this.callStatus.equals(callStatus.NotOpened))
			this.isCallOpen=false;
		else this.isCallOpen=true;
	}

	public boolean isCallOpen() {
		return isCallOpen;
	}

	public void setCallOpen(boolean isCallOpen) {
		this.isCallOpen = isCallOpen;
	}

	public int getItemID() {
		return itemID;
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

	public int getMachineID() {
		return machineID;
	}

}
		