package entity;

import java.io.Serializable;

public class OrderEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int machine_id;
	private double total_sum;
	private int user_id;
	private String buytime;
	private double productsAmount;
	private String payment_status;
	private String supplyMethod;

	public OrderEntity(int user_id, String supplyMethod) {
		this.user_id = user_id;
		this.supplyMethod = supplyMethod;
		this.machine_id = -1;
		this.total_sum = -1;
		this.buytime = "";
		this.productsAmount = -1;
		this.payment_status = "";
	}

	public OrderEntity(int id, int machine_id, double total_sum, int user_id, String buytime, double productsAmount,
			String payment_status, String supplyMethod) {
		this(user_id, supplyMethod);
		this.id = id;
		this.machine_id = machine_id;
		this.total_sum = total_sum;
		this.buytime = buytime;
		this.productsAmount = productsAmount;
		this.payment_status = payment_status;
	}

	public int getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}

	public double getTotal_sum() {
		return total_sum;
	}

	public void setTotal_sum(double total_sum) {
		this.total_sum = total_sum;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getBuytime() {
		return buytime;
	}

	public void setBuytime(String buytime) {
		this.buytime = buytime;
	}

	public double getProductsAmount() {
		return productsAmount;
	}

	public void setProductsAmount(double productsAmount) {
		this.productsAmount = productsAmount;
	}

	public String getPaymentStatus() {
		return payment_status;
	}

	public void setPaymentStatus(String payment_status) {
		this.payment_status = payment_status;
	}

	public String getSupplyMethod() {
		return supplyMethod;
	}

	public void setSupplyMethod(String supplyMethod) {
		this.supplyMethod = supplyMethod;
	}

}
