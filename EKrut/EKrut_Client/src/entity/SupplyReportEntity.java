package entity;

public class SupplyReportEntity extends ReportEntity {
	private static final long serialVersionUID = 1L;
	private String item_id,item_name;
	private int min_stock,cur_stock,start_stock;
	
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
}
