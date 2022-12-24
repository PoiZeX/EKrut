package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import common.CommonFunctions;

public class SupplyReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private String item_id, item_name;
	private int min_stock, cur_stock, start_stock;
	private ArrayList<String[]> reportsList; 
	
	public SupplyReportEntity() {
		super();
	}
	
	public SupplyReportEntity(int id, String item_id, String item_name, String min_stock, String start_stock, String cur_stock, String month, String year) {
		super(id, month, year, "");
		this.item_id = item_id;
		this.item_name = item_name;
		this.min_stock = Integer.parseInt(min_stock);
		this.start_stock = Integer.parseInt(start_stock);
		this.cur_stock = Integer.parseInt(cur_stock);
		parserDetails(item_id, item_name, min_stock, start_stock, cur_stock);
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
	private void parserDetails(String item_id, String item_name, String min_stock, String start_stock, String cur_stock) {
		String[] item_id_list = item_id.split(",");
		String[] item_name_list = item_name.split(",");
		String[] min_stock_list = min_stock.split(",");
		String[] start_stock_list = start_stock.split(",");
		String[] cur_stock_list = cur_stock.split(",");
		reportsList = new ArrayList<String[]>();
		
		if (item_id.equals("noitems"))
			return;
		
		for (int i = 0; i < item_id_list.length; i++) {
			String id = item_id_list[i];
			String name = item_name_list[i];
			String min = min_stock_list[i];
			String start = start_stock_list[i];
			String cur = cur_stock_list[i];
			reportsList.add(new String[] {id, start, cur, min, name});
		}
	}
}
