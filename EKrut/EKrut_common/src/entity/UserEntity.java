package entity;

import java.io.Serializable;
import java.util.Objects;

import enums.RolesEnum;

public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username, password, first_name, last_name, email, phone_number, role_type, region, cc_num, id_num;
	private boolean logged_in, isNotApproved;
	private int id;

	/**
	 * Instantiates a new user entity.
	 */
	public UserEntity() {
		this("", "", "", "", "", "", "", "", "", "", false, false);
	}

	/**
	 * Instantiates a new user entity.
	 *
	 * @param id_num the id num
	 * @param username the username
	 * @param password the password
	 * @param first_name the first name
	 * @param last_name the last name
	 * @param email the email
	 * @param phone_number the phone number
	 * @param cc_num the cc num
	 * @param region the region
	 * @param role_type the role type
	 * @param logged_in the logged in
	 * @param isNotApproved the is not approved
	 */
	public UserEntity(String id_num, String username, String password, String first_name, String last_name,
			String email, String phone_number, String cc_num,  String region, String role_type,boolean logged_in,
			boolean isNotApproved) {
		this.id_num = id_num;
		this.username = username;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone_number = phone_number;
		this.cc_num = cc_num;
		this.region = region;
		this.role_type = role_type;
		this.isNotApproved = isNotApproved;
		this.logged_in = logged_in;
		this.isNotApproved = isNotApproved;
	}

	/**
	 * Gets the cc num.
	 *
	 * @return the cc num
	 */
	public String getCc_num() {
		return cc_num;
	}

	/**
	 * Sets the cc num.
	 *
	 * @param cc_num the new cc num
	 */
	public void setCc_num(String cc_num) {
		this.cc_num = cc_num;
	}

	/**
	 * Gets the id num.
	 *
	 * @return the id num
	 */
	public String getId_num() {
		return id_num;
	}

	/**
	 * Sets the id num.
	 *
	 * @param id_num the new id num
	 */
	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		return Objects.equals(username, other.username);
	}

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

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
	 * Gets the phone number.
	 *
	 * @return the phone number
	 */
	public String getPhone_number() {
		return phone_number;
	}

	/**
	 * Sets the phone number.
	 *
	 * @param phone_number the new phone number
	 */
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	/**
	 * Gets the role type.
	 *
	 * @return the role type
	 */
	public RolesEnum getRole_type() {
		RolesEnum role = Enum.valueOf(RolesEnum.class, role_type);
		return role;
	}

	/**
	 * Sets the role type.
	 *
	 * @param role_type the new role type
	 */
	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}

	/**
	 * Gets the region.
	 *
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Sets the region.
	 *
	 * @param region the new region
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Checks if is logged in.
	 *
	 * @return true, if is logged in
	 */
	public boolean isLogged_in() {
		return logged_in;
	}

	/**
	 * Sets the logged in.
	 *
	 * @param logged_in the new logged in
	 */
	public void setLogged_in(boolean logged_in) {
		this.logged_in = logged_in;
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
	 * Checks if is not approved.
	 *
	 * @return true, if is not approved
	 */
	public boolean isNotApproved() {
		return isNotApproved;
	}

	/**
	 * Sets the not approved.
	 *
	 * @param isApproved the new not approved
	 */
	public void setNotApproved(boolean isApproved) {
		this.isNotApproved = isApproved;
	}

	@Override
	public String toString() {
		return username;
	}

	/**
	 * Full name.
	 *
	 * @return the string
	 */
	public String fullName() {
		return first_name + " " + last_name;
	}

}
