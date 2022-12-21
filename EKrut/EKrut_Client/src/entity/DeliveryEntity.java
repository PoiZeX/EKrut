package entity;

import java.io.Serializable;

import common.CustomerStatus;
import common.DeliveryStatus;

public class DeliveryEntity implements Serializable{
	private String actualTime,estimatedTime;
	private int customerId,orderId;
	//private AddressEntity address;
	private String address, city; //change later to AddressEntity
	private DeliveryStatus deliveryStatus;
	private CustomerStatus customerStatus;

	
	

	public DeliveryEntity(int orderId,int costumerId, String address,String city, String estimatedTime, String actualTime,
			DeliveryStatus deliveryStatus,CustomerStatus customerStatus) {
		this.orderId = orderId;
		this.customerId = costumerId;
		this.address = address;
		this.city = city;
		this.estimatedTime = estimatedTime;
		this.actualTime = actualTime;
		this.deliveryStatus = deliveryStatus;
		this.customerStatus=customerStatus;
	}



	public DeliveryEntity(int orderId, int customerId, String address,String city, String estimatedTime ) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.address=address;
		this.city = city;
		this.estimatedTime = estimatedTime;
		this.deliveryStatus = DeliveryStatus.pendingApproval;
		this.customerStatus= CustomerStatus.NOT_APPROVED;
	}
	

	public String getCity() {
		return city;
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
	public int getCustomerId() {
		return customerId;
	}
	public int getOrderId() {
		return orderId;
	}
	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}
	public void setDeliveryStatus(DeliveryStatus status) {
		this.deliveryStatus = status;
	}
	public String getAddress() {
		return address;
	}
	public CustomerStatus getCustomerStatus() {
		return customerStatus;
	}
	public void setCustomerStatus(CustomerStatus customerStatus) {
		this.customerStatus = customerStatus;
	}
	/*
	public AddressEntity getAddress() {
		return address;
	}*/
	
}
