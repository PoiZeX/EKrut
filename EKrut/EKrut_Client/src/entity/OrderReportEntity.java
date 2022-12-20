package entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import common.CommonFunctions;

public class OrderReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String month,year,region,description;
	private Map<String,Double[]> reportsList; // {[Karmiel, Sales:30,Sum:40]}
	


	public OrderReportEntity(int id, String description, String month, String year, String region) {
		this.id = id;
		this.month = month;
		this.year = year;
		this.region = region;
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
		reportsList = new HashMap<String, Double[]>();
		if (description.equals("noreport") || CommonFunctions.isNullOrEmpty(description))
			return;
		else if (description.equals("nosales")) {
			reportsList.put("No Sales", new Double[] {100.0,100.0});
		}
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



