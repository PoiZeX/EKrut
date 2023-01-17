package entity;

import java.io.Serializable;

public class PickupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Status {
		inProgress, done;
	};

	private int order_id, machine_id;
	private Status status;

	/**
	 * Instantiates a new pickup entity.
	 *
	 * @param order_id the order id
	 */
	public PickupEntity(int order_id) {
		this.order_id = order_id;
		this.status = Status.inProgress;
	}

	/**
	 * Instantiates a new pickup entity.
	 *
	 * @param order_id the order id
	 * @param status the status
	 * @param machine_id the machine id
	 */
	public PickupEntity(int order_id, PickupEntity.Status status, int machine_id) {
		this.order_id = order_id;
		this.status = status;
		this.machine_id = machine_id;
	}

	/**
	 * Gets the order id.
	 *
	 * @return the order id
	 */
	public int getOrderId() {
		return order_id;
	}

	/**
	 * Sets the order id.
	 *
	 * @param order_id the new order id
	 */
	public void setOrderId(int order_id) {
		this.order_id = order_id;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
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

}
