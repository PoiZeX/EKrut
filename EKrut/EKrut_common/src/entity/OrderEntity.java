package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

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
	private ArrayList<String[]> orderCart;
	

	/**
	 * Instantiates a new order entity.
	 *
	 * @param user_id the user id
	 * @param supplyMethod the supply method
	 */
	public OrderEntity(int user_id, String supplyMethod) {
		this.user_id = user_id;
		this.supplyMethod = supplyMethod;
		this.machine_id = -1;
		this.total_sum = -1;
		this.buytime = "";
		this.productsAmount = -1;
		this.payment_status = "";
		this.orderCart = new ArrayList<>();
	}

	/**
	 * Instantiates a new order entity.
	 *
	 * @param id the id
	 * @param machine_id the machine id
	 * @param total_sum the total sum
	 * @param user_id the user id
	 * @param buytime the buytime
	 * @param productsAmount the products amount
	 * @param payment_status the payment status
	 * @param supplyMethod the supply method
	 */
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

	/**
	 * Gets the machine id.
	 *
	 * @return the machine id
	 */
	public int getMachine_id() {
		return machine_id;
	}

	/**
	 * Sets the machine id.
	 *
	 * @param machine_id the new machine id
	 */
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}

	/**
	 * Gets the total sum.
	 *
	 * @return the total sum
	 */
	public double getTotal_sum() {
		return total_sum;
	}

	/**
	 * Sets the total sum.
	 *
	 * @param total_sum the new total sum
	 */
	public void setTotal_sum(double total_sum) {
		this.total_sum = total_sum;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public int getUser_id() {
		return user_id;
	}

	/**
	 * Sets the user id.
	 *
	 * @param user_id the new user id
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	/**
	 * Gets the buytime.
	 *
	 * @return the buytime
	 */
	public String getBuytime() {
		return buytime;
	}

	/**
	 * Sets the buytime.
	 *
	 * @param buytime the new buytime
	 */
	public void setBuytime(String buytime) {
		this.buytime = buytime;
	}

	/**
	 * Gets the products amount.
	 *
	 * @return the products amount
	 */
	public double getProductsAmount() {
		return productsAmount;
	}

	/**
	 * Sets the products amount.
	 *
	 * @param productsAmount the new products amount
	 */
	public void setProductsAmount(double productsAmount) {
		this.productsAmount = productsAmount;
	}

	/**
	 * Gets the payment status.
	 *
	 * @return the payment status
	 */
	public String getPaymentStatus() {
		return payment_status;
	}

	/**
	 * Sets the payment status.
	 *
	 * @param payment_status the new payment status
	 */
	public void setPaymentStatus(String payment_status) {
		this.payment_status = payment_status;
	}

	/**
	 * Gets the supply method.
	 *
	 * @return the supply method
	 */
	public String getSupplyMethod() {
		return supplyMethod;
	}

	/**
	 * Sets the supply method.
	 *
	 * @param supplyMethod the new supply method
	 */
	public void setSupplyMethod(String supplyMethod) {
		this.supplyMethod = supplyMethod;
	}

	public ArrayList<String[]> getOrderCart() {
		return orderCart;
	}

	public void setOrderCart(HashMap<ItemInMachineEntity,Integer> cart) {
		for (ItemInMachineEntity item : cart.keySet()) {
			orderCart.add(new String[] {String.valueOf(item.getId()), String.valueOf(cart.get(item))});
		}
	}
	
	public String getCartString() {
		String res = "";
		for (String[] items : orderCart) {
			res+= String.format("[%s,%s],", items[0],items[1]);
		}
		return res.substring(0, res.length()-1);
	}

}
