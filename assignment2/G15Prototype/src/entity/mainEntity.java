package entity;

import java.io.Serializable;

public class mainEntity implements Serializable{
	int id;

	public mainEntity(int id) {
		this.id = id;
	}
	
		public int getId() {
		return id;
	}
}
