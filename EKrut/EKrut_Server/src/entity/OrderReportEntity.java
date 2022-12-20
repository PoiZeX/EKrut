package entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OrderReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String month,year,region;
	private Map<String,Double[]> reportsList; // {[Karmiel, Sales:30,Sum:40]}
	


	public OrderReportEntity(int id, String description, String month, String year, String region) {
		this.id = id;
		this.month = month;
		this.year = year;
		this.region = region;
		parserDetails(description);
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


	public Map<String, Double[]> getReportsList() {
		return reportsList;
	}


	public void setReportsList(Map<String, Double[]> reportsList) {
		this.reportsList = reportsList;
	}
	

	private void parserDetails(String description) {
		if (description.equals(""))
			return;
		reportsList = new HashMap<String, Double[]>();
		String[] details = description.split(",");
		for (int i = 0; i < details.length; i += 3) {
		  String name = details[i];
		  String sum = details[i + 1];
		  String sales = details[i + 2];
		  Double[] pair = new Double[] {Double.parseDouble(sum),Double.parseDouble(sales)};
		  reportsList.put(name,pair);
		}
	}
	
	
}



