package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import common.CommonFunctions;

public class ClientsReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private String description, supplyMethods, totalOrders;
	private LinkedHashMap<String, Integer> totalSalesArr, supplyMethodsArr;

	public ClientsReportEntity() {
		super();
		this.description = "noreport";
	}

	public ClientsReportEntity(int id, String description, String supplyMethods, String totalOrders, String month,
			String year, String region) {
		super(id, month, year, region);
		this.totalOrders = totalOrders;
		setSupplyMethods(supplyMethods);
		setDescription(description);
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

	public String getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(String totalOrders) {
		this.totalOrders = totalOrders;
	}

	public LinkedHashMap<String, Integer> getTotalSalesArr() {
		return totalSalesArr;
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
		if (supplyMethodsSplit.length != 3) {
			description = "noreport";
			return;
		}
		supplyMethodsArr.put("On-site", Integer.parseInt(supplyMethodsSplit[0]));
		supplyMethodsArr.put("Delivery", Integer.parseInt(supplyMethodsSplit[1]));
		supplyMethodsArr.put("Pickup", Integer.parseInt(supplyMethodsSplit[2]));
	}
}
