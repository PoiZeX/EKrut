package entity;

import java.util.LinkedHashMap;
import common.CommonFunctions;
/**
 * The ClientsReportEntity class represents a report containing information about clients of a certain region, in a certain month and year.
 * The report includes descriptions, supply methods, total orders and user status of the clients.
*/
public class ClientsReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private String description, supplyMethods, totalOrders, userStatus;
	private LinkedHashMap<String, Integer> totalSalesArr, supplyMethodsArr, userStatusArr;
	/**

	Initialize a ClientsReportEntity object with no description.
	*/
	public ClientsReportEntity() {
		super();
		this.description = "noreport";
	}
	/**

	Initialize a ClientsReportEntity object with the given id, description, supply methods, total orders, user status, month, year and region.
	@param id id of the report
	@param description description of the report
	@param supplyMethods supply methods of the report
	@param totalOrders total orders of the report
	@param userStatus user status of the report
	@param month month the report generated
	@param year year the report generated
	@param region region the report generated
	*/
	public ClientsReportEntity(int id, String description, String supplyMethods, String totalOrders, String userStatus, String month,
			String year, String region) {
		super(id, month, year, region);
		this.totalOrders = totalOrders;
		setSupplyMethods(supplyMethods);
		setDescription(description);
		setUserStatus(userStatus);
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		parserDescription(description);
	}

	public String getSupplyMethods() {
		return supplyMethods;
	}

	public void setSupplyMethods(String supplyMethods) {
		this.supplyMethods = supplyMethods;
		parserSupplyMethods(supplyMethods);
	}
	private void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
		parseUserStatus(userStatus);
	}

	public String getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(String totalOrders) {
		this.totalOrders = totalOrders;
	}

	public LinkedHashMap<String, Integer> getTotalSalesArr() {
		return totalSalesArr;
	}

	public LinkedHashMap<String, Integer> getUserStatusArr() {
		return userStatusArr;
	}

	public LinkedHashMap<String, Integer> getSupplyMethodsArr() {
		return supplyMethodsArr;
	}

	private void parserDescription(String description) {
		totalSalesArr = new LinkedHashMap<String, Integer>();
		if (CommonFunctions.isNullOrEmpty(description))
			return;
		String[] rangeToSum = description.split(",");
		if (rangeToSum.length % 2 != 0) {
			description = "noreport";
			return;
		}
		for (int i = 0; i <= rangeToSum.length-2; i += 2) {
			totalSalesArr.put(rangeToSum[i], Integer.parseInt(rangeToSum[i + 1]));
		}
	}


	private void parserSupplyMethods(String supplyMethods) {
		supplyMethodsArr = new LinkedHashMap<String, Integer>();
		if (CommonFunctions.isNullOrEmpty(supplyMethods))
			return;
		String[] supplyMethodsSplit = supplyMethods.split(",");
		if (supplyMethodsSplit.length != 2) {
			description = "noreport";
			return;
		}
		supplyMethodsArr.put("On-site", Integer.parseInt(supplyMethodsSplit[0]));
		supplyMethodsArr.put("Pickup", Integer.parseInt(supplyMethodsSplit[1]));
	}


	private void parseUserStatus(String userStatus2) {
		userStatusArr = new LinkedHashMap<>();
		if (CommonFunctions.isNullOrEmpty(userStatus))
			return;
		String[] userStatusDetails = userStatus.split(",");
		if (userStatusDetails.length != 2) {
			description = "noreport";
			return;
		}
		userStatusArr.put("Subscribers", Integer.parseInt(userStatusDetails[0]));
		userStatusArr.put("Registered", Integer.parseInt(userStatusDetails[1]));
		
	}

}
