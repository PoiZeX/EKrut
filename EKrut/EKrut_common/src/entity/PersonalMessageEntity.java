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
	
	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() { 
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message; 
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(int userId) {
		this.userId = userId;;
	}

}
