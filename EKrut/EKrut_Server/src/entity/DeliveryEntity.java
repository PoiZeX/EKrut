package entity;

import java.io.Serializable;

import common.CustomerStatusEnum;
import common.DeliveryStatusEnum;

public class DeliveryEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String estimatedTime;
	private int orderId;
	private String address, region; //change later to AddressEntity
	private DeliveryStatusEnum deliveryStatus;
	private CustomerStatusEnum customerStatus;

	

	/**
	 * For getting delivery entity
	 * @param orderId
	 * @param region
	 * @param customerId
	 * @param address
	 * @param estimatedTime
	 * @param deliveryStatus
	 * @param customerStatus
	 */
	public DeliveryEntity(int orderId, String region,  String address, String estimatedTime,
			DeliveryStatusEnum deliveryStatus, CustomerStatusEnum customerStatus) {
		super();
		this.estimatedTime = estimatedTime;
		this.orderId = orderId;
		this.address = address;
		this.region = region;
		this.deliveryStatus = deliveryStatus;
		this.customerStatus = customerStatus;
	}

	/**
	 * For build new delivery entity
	 * @param region
	 * @param customerId
	 * @param address
	 */
	public DeliveryEntity(String region, String address) {
		this.address=address;
		this.region = region;
		this.deliveryStatus = DeliveryStatusEnum.pendingApproval;
		this.customerStatus= CustomerStatusEnum.NOT_APPROVED;
	}


	public String getEstimatedTime() {
		return estimatedTime;
	}


	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
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


	public DeliveryStatusEnum getDeliveryStatus() {
		return deliveryStatus;
	}


	public void setDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}


	public CustomerStatusEnum getCustomerStatus() {
		return customerStatus;
	}


	public void setCustomerStatus(CustomerStatusEnum customerStatus) {
		this.customerStatus = customerStatus;
	}
	

	
}
