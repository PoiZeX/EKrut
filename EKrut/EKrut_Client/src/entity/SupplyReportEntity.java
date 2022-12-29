package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.CommonFunctions;

public class SupplyReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	// {[1, Bamba], [20, 45, 60]}
	private String item_id, item_name, missing_sev;
	private int min_stock, cur_stock, start_stock;
	private ArrayList<String[]> reportsList;
	private String[] details;
	private int machine_id;
	
	public SupplyReportEntity() {
		super();
	}
	
	public SupplyReportEntity(int id,int machine_id, String item_id, String item_name, String min_stock, String start_stock, String cur_stock, String month, String year, String region, String missing_sev) {
		super(id, month, year, region);
		this.setMachine_id(machine_id);
		details = new String[] {item_id, item_name, min_stock, start_stock, cur_stock, missing_sev};
		parserDetails(item_id, item_name, min_stock, start_stock, cur_stock, missing_sev);
	}
	
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
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
	public int getStart_stock() {
		return start_stock;
	}
	public void setStart_stock(int start_stock) {
		this.start_stock = start_stock;
	}
	public ArrayList<String[]> getReportsList() {
		return reportsList;
	}
	private void parserDetails(String item_id, String item_name, String min_stock, String start_stock, String cur_stock, String missing_sev) {
		for (String col : details) {
			if (CommonFunctions.isNullOrEmpty(col)) {
				reportsList = null;
				return;
			}
		}
		String[] item_id_list = item_id.split(",");
		String[] item_name_list = item_name.split(",");
		String[] min_stock_list = min_stock.split(",");
		String[] start_stock_list = start_stock.split(",");
		String[] cur_stock_list = cur_stock.split(",");
		String[] missing_sev_list = missing_sev.split(",");
		
		int validLength = item_id_list.length;
		int[] lengths = new int[] {item_id_list.length,
				item_name_list.length,
				min_stock_list.length,
				start_stock_list.length,
				cur_stock_list.length};
		for (int length : lengths) {
			if (length != validLength) {
				reportsList = null;
				return;
			}
		}
		reportsList = new ArrayList<String[]>();
		for (int i = 0; i < item_id_list.length; i++) {
			String id = item_id_list[i];
			String name = item_name_list[i];
			String min = min_stock_list[i];
			String start = start_stock_list[i];
			String cur = cur_stock_list[i];
			String sev = missing_sev_list[i];
			reportsList.add(new String[] {id, name, min, start, cur, sev});
		}
	}

	public int getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(int machine_id) {
		this.machine_id = machine_id;
	}
}
