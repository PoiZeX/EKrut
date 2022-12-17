package entity;

import java.io.Serializable;

public class UserEntity implements Serializable{

	public UserEntity(String username, String password, String first_name, String last_name, String email,
			String phone_number, String role_type, String region) {
		this(username, password, first_name, last_name, email, phone_number, role_type);

		this.region = region;
	}

	public UserEntity(String username, String password, String first_name, String last_name, String email,
			String phone_number, String role_type) {
		this.username = username;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone_number = phone_number;
		this.role_type = role_type;
		this.logged_in = false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}

	public String getRole_type() {
		return role_type;
	}

	public void setRole_type(String role_type) {
		this.role_type = role_type;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean isLogged_in() {
		return logged_in;
	}

	public void setLogged_in(boolean logged_in) {
		this.logged_in = logged_in;
	}

	public int getId() {
		return id;
	}
	private String username, password, first_name, last_name, email, phone_number, role_type, region;
	private boolean logged_in;
	private int id;

}
