package entity;

import java.io.Serializable;
import java.util.Date;

/**
 * The entity saves a single personal message
 *
 */
public class PersonalMessageEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String date, title, message;
	private int userId, id;

	
	/**
	 * @param userId current user id
	 * @param date current date
	 * @param title message title
	 * @param message message content
	 */
	public PersonalMessageEntity(int userId, String date, String title, String message) {
		super();
		this.userId = userId;
		this.date = date;
		this.title = title;
		this.message = message;
	}
	
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTitle() { 
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message; 
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;;
	}

}
