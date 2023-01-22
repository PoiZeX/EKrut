package entity;

import java.util.ArrayList;
import common.CommonFunctions;

public class SupplyReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private int machine_id, min_stock;
	private String item_id, times_under_min, end_stock;
	private ArrayList<String[]> reportsList;
	private String[] details;

	/**
	 * Instantiates a new supply report entity.
	 */
	public SupplyReportEntity() {
		super();
	}

	/**
	 * Instantiates a new supply report entity.
	 *
	 * @param id the id
	 * @param machine_id the machine id
	 * @param min_stock the min stock
	 * @param item_id the item id
	 * @param times_under_min the times under min
	 * @param end_stock the end stock
	 * @param month the month
	 * @param year the year
	 * @param region the region
	 */
	public SupplyReportEntity(int id, int machine_id, int min_stock, String item_id, String times_under_min,
			String end_stock, String month, String year, String region) {
		super(id, month, year, region);
		this.machine_id = machine_id;
		this.min_stock = min_stock;
		this.item_id = item_id;
		this.times_under_min = times_under_min;
		this.end_stock = end_stock;
		details = new String[] { item_id, end_stock, times_under_min };
		parserDetails(item_id, end_stock, times_under_min);
	}

	/**
	 * Gets the item id.
	 *
	 * @return the item id
	 */
	public String getItem_Id() {
		return item_id;
	}

	/**
	 * Sets the item id.
	 *
	 * @param item_name the new item id
	 */
	public void setItem_Id(String item_name) {
		this.item_id = item_name;
	}

	/**
	 * Gets the min stock.
	 *
	 * @return the min stock
	 */
	public int getMin_stock() {
		return min_stock;
	}

	/**
	 * Sets the min stock.
	 *
	 * @param min_stock the new min stock
	 */
	public void setMin_stock(int min_stock) {
		this.min_stock = min_stock;
	}

	/**
	 * Gets the end stock.
	 *
	 * @return the end stock
	 */
	public String getEnd_stock() {
		return end_stock;
	}

	/**
	 * Sets the end stock.
	 *
	 * @param cur_stock the new end stock
	 */
	public void setEnd_stock(String cur_stock) {
		this.end_stock = cur_stock;
	}

	/**
	 * Gets the reports list.
	 *
	 * @return the reports list
	 */
	public ArrayList<String[]> getReportsList() {
		return reportsList;
	}

	/**
	 * Gets the machine id.
	 *
	 * @return the machine id
	 */
	public int getMachine_id() {
		return machine_id;
	}

	/**
	 * Sets the machine id.
	 *
	 * @param machine_id the new machine id
	 */
	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}

	/**
	 * Gets the times under min.
	 *
	 * @return the times under min
	 */
	public String getTimes_under_min() {
		return times_under_min;
	}

	/**
	 * Sets the times under min.
	 *
	 * @param missing_sev the new times under min
	 */
	public void setTimes_under_min(String missing_sev) {
		this.times_under_min = missing_sev;
	}

	/**
	 * Parser details.
	 *
	 * @param item_name the item name
	 * @param cur_stock the cur stock
	 * @param missing_sev the missing sev
	 */
	private void parserDetails(String item_name, String cur_stock, String missing_sev) {
		for (String col : details) {
			if (CommonFunctions.isNullOrEmpty(col)) {
				reportsList = null;
				return;
			}
		}
		String[] item_name_list = item_name.split(",");
		String[] cur_stock_list = cur_stock.split(",");
		String[] missing_sev_list = missing_sev.split(",");

		int validLength = item_name_list.length;
		int[] lengths = new int[] { item_name_list.length, cur_stock_list.length, missing_sev_list.length };
		for (int length : lengths) {
			if (length != validLength) {
				reportsList = null;
				return;
			}
		}
		reportsList = new ArrayList<String[]>();
		for (int i = 0; i < validLength; i++) {
			String name = item_name_list[i];
			String cur = cur_stock_list[i];
			String sev = missing_sev_list[i];
			reportsList.add(new String[] { name, cur, sev });
		}
	}

}
