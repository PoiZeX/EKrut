package entity;

import java.util.Date;

/**
 * The entity saves a single personal message 
 *
 */
public class PersonalMessageEntity {
	
	public PersonalMessageEntity(int year, int month, int day, String type, String msgprev) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
		this.type = type;
		this.msgprev = msgprev;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMsgprev() {
		return msgprev;
	}
	public void setMsgprev(String msgprev) {
		this.msgprev = msgprev;
	}
	
	
	public String getDate() {
		return year + "-" + month + "-" + day;
	}
	
	public void setDate(int[] date) {
		if(date.length == 3) {
			year = date[0];
			month = date[1];
			day = date[2];
		}
	}
	
	private String type, msgprev;
	private int year, month, day;
	

	

}
