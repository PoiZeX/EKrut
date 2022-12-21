package entity;

public class AddressEntity {
	private String street, city;
	private Integer houseNum,aptNum;
	
	public AddressEntity(String street, String city,Integer houseNum, Integer aptNum) {
		this.street = street;
		this.city = city;
		this.aptNum = aptNum;
		this.houseNum=houseNum;
	}
	
	public String getStreet() {
		return street;
	}

	public Integer getHouseNum() {
		return houseNum;
	}

	public String getCity() {
		return city;
	}

	public Integer getAptNum() {
		return aptNum;
	}
	
	@Override
	public String toString() {
		String aptNumToString="";
		if(aptNum!=null)
			aptNumToString="/"+aptNum;
		return "" + street + " " + houseNum + aptNumToString + "";
	}

	

}
