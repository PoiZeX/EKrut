package entity;

public class DatabaseEntity {
	  private String username;
	  
	  private String password;
	  
	  private String DBAddress;
	  
	  public DatabaseEntity(String username, String password, String DBAddress) {
	    this.username = username;
	    this.password = password;
	    this.DBAddress = DBAddress;
	  }
	  
	  public String getUsername() {
	    return this.username;
	  }
	  
	  public void setUsername(String username) {
	    this.username = username;
	  }
	  
	  public void setDBAddress(String DBAddress) {
	    this.DBAddress = DBAddress;
	  }
	  
	  public String getDBAddress() {
	    return this.DBAddress;
	  }
	  
	  public String getPassword() {
	    return this.password;
	  }
	  
	  public void setPassword(String password) {
	    this.password = password;
	  }
}
