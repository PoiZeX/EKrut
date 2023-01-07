package entity;

import java.io.Serializable;

public class OrderEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; 
	private int machine_id;
	private int total_sum;
	private int user_id;  
	private String buytime;
	private int  productsAmount;
	private String productsDescription;
	private String supplyMethod;
	
	public OrderEntity(int user_id,String supplyMethod) {
		this.user_id = user_id;
		this.supplyMethod=supplyMethod;
		this.machine_id = -1;
		this.total_sum = -1;
		this.buytime = "";
		this.productsAmount = -1;
		this.productsDescription = "";
	}
	
	public OrderEntity(int id, int machine_id, int total_sum, int user_id, String buytime, int productsAmount,
			String productsDescription, String supplyMethod) {
		this(user_id,supplyMethod);
		this.id = id;
		this.machine_id = machine_id;
		this.total_sum = total_sum;
		this.buytime = buytime;
		this.productsAmount = productsAmount;
		this.productsDescription = productsDescription;
	}

	public int getMachine_id() {
		return machine_id;
	}
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}
	public int getTotal_sum() {
		return total_sum;
	}
	public void setTotal_sum(int total_sum) {
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
	public int getProductsAmount() {
		return productsAmount;
	}
	public void setProductsAmount(int productsAmount) {
		this.productsAmount = productsAmount;
	}
	public String getProductsDescription() {
		return productsDescription;
	}
	public void setProductsDescription(String productsDescription) {
		this.productsDescription = productsDescription;
	}
	public String getSupplyMethod() {
		return supplyMethod;
	}
	public void setSupplyMethod(String supplyMethod) {
		this.supplyMethod = supplyMethod;
	}

}
