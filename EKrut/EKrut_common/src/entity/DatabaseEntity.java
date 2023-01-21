package entity;

public class DatabaseEntity {
	  private String username;
	  
	  private String password;
	  
	  private String DBAddress;
	  
	  /**
  	 * Instantiates a new database entity.
  	 *
  	 * @param username the username
  	 * @param password the password
  	 * @param DBAddress the DB address
  	 */
  	public DatabaseEntity(String username, String password, String DBAddress) {
	    this.username = username;
	    this.password = password;
	    this.DBAddress = DBAddress;
	  }
	  
	  /**
  	 * Gets the username.
  	 *
  	 * @return the username
  	 */
  	public String getUsername() {
	    return this.username;
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
  	 * Sets the DB address.
  	 *
  	 * @param DBAddress the new DB address
  	 */
  	public void setDBAddress(String DBAddress) {
	    this.DBAddress = DBAddress;
	  }
	  
	  /**
  	 * Gets the DB address.
  	 *
  	 * @return the DB address
  	 */
  	public String getDBAddress() {
	    return this.DBAddress;
	  }
	  
	  /**
  	 * Gets the password.
  	 *
  	 * @return the password
  	 */
  	public String getPassword() {
	    return this.password;
	  }
	  
	  /**
  	 * Sets the password.
  	 *
  	 * @param password the new password
  	 */
  	public void setPassword(String password) {
	    this.password = password;
	  }
}
