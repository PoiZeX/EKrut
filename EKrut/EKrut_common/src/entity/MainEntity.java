package entity;

import java.io.Serializable;

public class MainEntity implements Serializable{
	int id;

	/**
	 * Instantiates a new main entity.
	 *
	 * @param id the id
	 */
	public MainEntity(int id) {
		this.id = id;
	}
	
		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public int getId() {
		return id;
	}
}
