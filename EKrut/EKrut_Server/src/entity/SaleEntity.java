package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


public class SaleEntity implements Serializable {
	//enum for sale status
	public enum SaleStatus{Active, NotActive;} ;
	
	private static final long serialVersionUID = 1L;
	private int SaleID;
	private String saleType, region, days ;
	private SaleStatus saleStatus;
	private LocalTime startTime, endTime;
	
	

	public SaleEntity(int id, String region, String saleType,String days, LocalTime startTime, LocalTime endTime, SaleStatus saleStatus) {
		super();
		this.SaleID=id;
		this.saleType = saleType;
		this.region = region;
		this.saleStatus = saleStatus;
		this.days=days;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public SaleEntity(String region, String saleType,String days, LocalTime startTime, LocalTime endTime) {
		super();
		this.saleType = saleType;
		this.region = region;
		this.startTime = startTime;
		this.days=days;
		this.endTime = endTime;
		this.saleStatus = SaleStatus.NotActive;
	}
	
	public int getSaleID() {
		return SaleID;
	}
	public String getSaleType() {
		return saleType;
	}
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public SaleStatus getSaleStatus() {
		return saleStatus;
	}
	public void setSaleStatus(SaleStatus saleStatus) {
		this.saleStatus = saleStatus;
	}
	public LocalTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	public LocalTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}

}
