package entity;

public class Subscriber extends mainEntity {
	public Subscriber(int id, String firstName, String lastName, String phoneNumber, String email, String creditCardNumber,
			int subscriberNumber) {
		super(id);
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		CreditCardNumber = creditCardNumber;
		SubscriberNumber = subscriberNumber;
	}
	private String firstName, lastName, phoneNumber, email, CreditCardNumber;
	private int SubscriberNumber;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCreditCardNumber() {
		return CreditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		CreditCardNumber = creditCardNumber;
	}
	public int getSubscriberNumber() {
		return SubscriberNumber;
	}
	public void setSubscriberNumber(int subscriberNumber) {
		SubscriberNumber = subscriberNumber;
	}

	

}
