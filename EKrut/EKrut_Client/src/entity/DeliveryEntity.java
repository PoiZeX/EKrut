package entity;

import java.io.Serializable;

import common.CustomerStatus;
import common.DeliveryStatus;

public class DeliveryEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String customerId, estimatedTime;
	private int orderId;
	private String address, region; //change later to AddressEntity
	private DeliveryStatus deliveryStatus;
	private CustomerStatus customerStatus;

	

	public DeliveryEntity(int orderId, String region, String customerId,  String address, String estimatedTime,
			DeliveryStatus deliveryStatus, CustomerStatus customerStatus) {
		super();
		this.estimatedTime = estimatedTime;
		this.customerId = customerId;
		this.orderId = orderId;
		this.address = address;
		this.region = region;
		this.deliveryStatus = deliveryStatus;
		this.customerStatus = customerStatus;
	}


	public DeliveryEntity(int orderId, String region, String customerId, String address, String estimatedTime ) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.address=address;
		this.region = region;
		this.estimatedTime = estimatedTime;
		this.deliveryStatus = DeliveryStatus.pendingApproval;
		this.customerStatus= CustomerStatus.NOT_APPROVED;
	}


	public String getEstimatedTime() {
		return estimatedTime;
	}


	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}


	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}


	public CustomerStatus getCustomerStatus() {
		return customerStatus;
	}


	public void setCustomerStatus(CustomerStatus customerStatus) {
		this.customerStatus = customerStatus;
	}
	

	
}
