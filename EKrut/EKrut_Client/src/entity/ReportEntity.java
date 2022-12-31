package entity;

import java.io.Serializable;

public class ReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String month, year, region;
	
	public ReportEntity() {
		id = 0;
		month = "";
		year = "";
		region = "";
	}
	
	public ReportEntity(int id, String month, String year, String region) {
		this.id = id;
		this.month = month;
		this.year = year;
		this.region = region;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
