package entity;

import java.util.HashMap;
import java.util.Map;

import common.CommonFunctions;

public class OrderReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private int id;
	private String month,year,region,description;
	private Map<String,Double[]> reportsList; // {[Karmiel, Sales:30,Sum:40]}
	

	public OrderReportEntity() {
		super();
		description = "";
	}
	
	public OrderReportEntity(int id, String description, String month, String year, String region) {
		super(id,month,year,region);
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		parserDetails(description);
	}

	public Map<String, Double[]> getReportsList() {
		return reportsList;
	}


	public void setReportsList(Map<String, Double[]> reportsList) {
		this.reportsList = reportsList;
	}
	

	private void parserDetails(String description) {
		reportsList = new HashMap<String, Double[]>();
		if (CommonFunctions.isNullOrEmpty(description) || description.equals("noreport"))
			return;
		else if (description.equals("nosales")) {
			reportsList.put("No Sales", new Double[] {0.0,0.0});
			return;
		}
		String[] details = description.split(",");
		for (int i = 0; i < details.length; i += 3) {
		  String name = details[i];
		  String sum = details[i + 1];
		  String sales = details[i + 2];
		  Double[] pair = new Double[] {Double.parseDouble(sum),Double.parseDouble(sales)};
		  reportsList.put(name,pair); // [Karmiel : [20,30], Tel Aviv : [32,15]]
		}
	}	
}



