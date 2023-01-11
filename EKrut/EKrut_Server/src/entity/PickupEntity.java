package entity;

import java.io.Serializable;

public class PickupEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Status {
		inProgress, done;
	};

	private int order_id, machine_id;
	private Status status;

	public PickupEntity(int order_id) {
		this.order_id = order_id;
		this.status = Status.inProgress;
	}

	public PickupEntity(int order_id, PickupEntity.Status status, int machine_id) {
		this.order_id = order_id;
		this.status = status;
		this.machine_id = machine_id;
	}

	public int getOrderId() {
		return order_id;
	}

	public void setOrderId(int order_id) {
		this.order_id = order_id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}

}
