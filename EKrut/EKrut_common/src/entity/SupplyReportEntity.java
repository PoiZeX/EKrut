package entity;

import java.util.ArrayList;
import common.CommonFunctions;

public class SupplyReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private int machine_id, min_stock;
	private String item_id, times_under_min, end_stock;
	private ArrayList<String[]> reportsList;
	private String[] details;

	public SupplyReportEntity() {
		super();
	}

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

	public String getItem_Id() {
		return item_id;
	}

	public void setItem_Id(String item_name) {
		this.item_id = item_name;
	}

	public int getMin_stock() {
		return min_stock;
	}

	public void setMin_stock(int min_stock) {
		this.min_stock = min_stock;
	}

	public String getEnd_stock() {
		return end_stock;
	}

	public void setEnd_stock(String cur_stock) {
		this.end_stock = cur_stock;
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

	public String getTimes_under_min() {
		return times_under_min;
	}

	public void setTimes_under_min(String missing_sev) {
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

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SupplyReportEntity)) {
			return false;
		}
		SupplyReportEntity o = (SupplyReportEntity) other;
		return getId() == o.getId() && machine_id == o.getMachine_id() && min_stock == o.getMin_stock()
				&& item_id.equals(o.getItem_Id()) && times_under_min.equals(o.getTimes_under_min())
				&& end_stock.equals(o.getEnd_stock()) && getMonth().equals(o.getMonth())
				&& getYear().equals(o.getYear()) && getRegion().equals(o.getRegion());

	}

}
