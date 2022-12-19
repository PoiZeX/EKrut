package entity;

import common.DeliveryStatus;

public class DeliveryEntity {
	private String actualTime,estimatedTime;
	private int costumerId,orderId;
	//private AddressEntity address;
	private String address; //change later to AddressEntity
	private DeliveryStatus status;

	
	public DeliveryEntity(int costumerId, int orderId, String address, String estimatedTime, String actualTime,
			DeliveryStatus status) {
		this.orderId = orderId;
		this.costumerId = costumerId;
		this.address = address;
		this.estimatedTime = estimatedTime;
		this.actualTime = actualTime;
		this.status = status;
	}



	public DeliveryEntity(int orderId, int costumerId, String address, String estimatedTime ) {
		this.orderId = orderId;
		this.costumerId = costumerId;
		this.address=address;
		this.estimatedTime = estimatedTime;
		this.status = DeliveryStatus.pendingApproval;
	}
	


	public String getActualTime() {
		return actualTime;
	}
	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}
	public String getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public int getCostumerId() {
		return costumerId;
	}
	public int getOrderId() {
		return orderId;
	}
	public DeliveryStatus getStatus() {
		return status;
	}
	public void setStatus(DeliveryStatus status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	/*
	public AddressEntity getAddress() {
		return address;
	}*/
	
}
