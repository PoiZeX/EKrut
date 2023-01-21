package entity;

import java.util.HashMap;
import java.util.Map;

import common.CommonFunctions;

public class OrderReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private String description;
	private Map <String, Double[]> reportsList;
	
	/**
	 * Instantiates a new order report entity.
	 */
	public OrderReportEntity() {
		super();
		description = "noreport";
	}
	
	/**
	 * Instantiates a new order report entity.
	 *
	 * @param id the id
	 * @param description the description
	 * @param month the month
	 * @param year the year
	 * @param region the region
	 */
	public OrderReportEntity(int id, String description, String month, String year, String region) {
		super(id, month, year, region);
		setDescription(description);
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
		parserDetails(description);
	}

	/**
	 * Gets the reports list.
	 *
	 * @return the reports list
	 */
	public Map<String, Double[]> getReportsList() {
		return reportsList;
	}


	/**
	 * Sets the reports list.
	 *
	 * @param reportsList the reports list
	 */
	public void setReportsList(Map<String, Double[]> reportsList) {
		this.reportsList = reportsList;
	}
	

	/**
	 * Parser details.
	 *
	 * @param description the description
	 */
	private void parserDetails(String description) {
		reportsList = new HashMap<String, Double[]>();
		if (CommonFunctions.isNullOrEmpty(description))
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
			reportsList.put(name,pair);
		}
	}	
}



