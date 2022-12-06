package entity;

import java.io.Serializable;

public class MainEntity implements Serializable{
	int id;

	public MainEntity(int id) {
		this.id = id;
	}
	
		public int getId() {
		return id;
	}
}
