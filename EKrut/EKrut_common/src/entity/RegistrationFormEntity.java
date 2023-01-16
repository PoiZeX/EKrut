package entity;



public class RegistrationFormEntity {
	
	private String first_name, last_name, id_number, email, credit_card, is_clubmember;
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCredit_card() {
		return credit_card;
	}

	public void setCredit_card(String credit_card) {
		this.credit_card = credit_card;
	}

	public String getIs_clubmember() {
		return is_clubmember;
	}

	public void setIs_clubmember(String is_clubmember) {
		this.is_clubmember = is_clubmember;
	}

	public int getForm_id() {
		return form_id;
	}

	public void setForm_id(int form_id) {
		this.form_id = form_id;
	}

	public int getClubmember_id() {
		return clubmember_id;
	}

	public void setClubmember_id(int clubmember_id) {
		this.clubmember_id = clubmember_id;
	}

	private int form_id, clubmember_id;
	
	public RegistrationFormEntity(int form_id, String first_name, String last_name, String id_number, String email, String credit_card, String is_clubmember) {
		this.form_id = form_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.credit_card = credit_card;
		this.is_clubmember = is_clubmember;
	}
	
	public void setClubMemberID(int id) {
		clubmember_id = id;
	}
	
	
	
}
