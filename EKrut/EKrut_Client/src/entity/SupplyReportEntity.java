package entity;

import java.util.ArrayList;
import common.CommonFunctions;

public class SupplyReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private String item_id, times_under_min;
	private int min_stock, cur_stock;
	private ArrayList<String[]> reportsList;
	private String[] details;
	private int machine_id;

	public SupplyReportEntity() {
		super();
	}

	public SupplyReportEntity(int id, int machine_id, int min_stock, String item_id, String times_under_min,
			String end_stock, String month, String year, String region) {
		super(id, month, year, region);
		this.machine_id = machine_id;
		this.min_stock = min_stock;
		details = new String[] { item_id, end_stock, times_under_min };
		parserDetails(item_id, end_stock, times_under_min);
	}

	public String getItem_name() {
		return item_id;
	}

	public void setItem_name(String item_name) {
		this.item_id = item_name;
	}

	public int getMin_stock() {
		return min_stock;
	}

	public void setMin_stock(int min_stock) {
		this.min_stock = min_stock;
	}

	public int getCur_stock() {
		return cur_stock;
	}

	public void setCur_stock(int cur_stock) {
		this.cur_stock = cur_stock;
	}

	public ArrayList<String[]> getReportsList() {
		return reportsList;
	}

	public int getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}

	public String getMissing_sev() {
		return times_under_min;
	}

	public void setMissing_sev(String missing_sev) {
		this.times_under_min = missing_sev;
	}

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
