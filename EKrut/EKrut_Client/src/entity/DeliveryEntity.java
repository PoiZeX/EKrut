package entity;

import common.DeliveryStatus;

public class DeliveryEntity {
	private String actualTime,estimatedTime;
	private Integer costumerId,orderId;
	private AddressEntity address;
	private DeliveryStatus status;
	
	public DeliveryEntity(String estimatedTime,AddressEntity address, Integer costumerId, Integer orderId) {
		this.estimatedTime = estimatedTime;
		this.costumerId = costumerId;
		this.orderId = orderId;
		this.address=address;
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
	public Integer getCostumerId() {
		return costumerId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public DeliveryStatus getStatus() {
		return status;
	}
	public void setStatus(DeliveryStatus status) {
		this.status = status;
	}
	public AddressEntity getAddress() {
		return address;
	}
	
}
