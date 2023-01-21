package entity;



public class RegistrationFormEntity {
	
	private String first_name, last_name, id_number, email, credit_card, is_clubmember;
	
	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * Sets the first name.
	 *
	 * @param first_name the new first name
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * Sets the last name.
	 *
	 * @param last_name the new last name
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	/**
	 * Gets the id number.
	 *
	 * @return the id number
	 */
	public String getId_number() {
		return id_number;
	}

	/**
	 * Sets the id number.
	 *
	 * @param id_number the new id number
	 */
	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the credit card.
	 *
	 * @return the credit card
	 */
	public String getCredit_card() {
		return credit_card;
	}

	/**
	 * Sets the credit card.
	 *
	 * @param credit_card the new credit card
	 */
	public void setCredit_card(String credit_card) {
		this.credit_card = credit_card;
	}

	/**
	 * Gets the checks if is clubmember.
	 *
	 * @return the checks if is clubmember
	 */
	public String getIs_clubmember() {
		return is_clubmember;
	}

	/**
	 * Sets the checks if is clubmember.
	 *
	 * @param is_clubmember the new checks if is clubmember
	 */
	public void setIs_clubmember(String is_clubmember) {
		this.is_clubmember = is_clubmember;
	}

	/**
	 * Gets the form id.
	 *
	 * @return the form id
	 */
	public int getForm_id() {
		return form_id;
	}

	/**
	 * Sets the form id.
	 *
	 * @param form_id the new form id
	 */
	public void setForm_id(int form_id) {
		this.form_id = form_id;
	}

	/**
	 * Gets the clubmember id.
	 *
	 * @return the clubmember id
	 */
	public int getClubmember_id() {
		return clubmember_id;
	}

	/**
	 * Sets the clubmember id.
	 *
	 * @param clubmember_id the new clubmember id
	 */
	public void setClubmember_id(int clubmember_id) {
		this.clubmember_id = clubmember_id;
	}

	private int form_id, clubmember_id;
	
	/**
	 * Instantiates a new registration form entity.
	 *
	 * @param form_id the form id
	 * @param first_name the first name
	 * @param last_name the last name
	 * @param id_number the id number
	 * @param email the email
	 * @param credit_card the credit card
	 * @param is_clubmember the is clubmember
	 */
	public RegistrationFormEntity(int form_id, String first_name, String last_name, String id_number, String email, String credit_card, String is_clubmember) {
		this.form_id = form_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.credit_card = credit_card;
		this.is_clubmember = is_clubmember;
	}
	
	/**
	 * Sets the club member ID.
	 *
	 * @param id the new club member ID
	 */
	public void setClubMemberID(int id) {
		clubmember_id = id;
	}
	
	
	
}
