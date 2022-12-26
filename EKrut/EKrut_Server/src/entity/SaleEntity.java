package entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


public class SaleEntity implements Serializable {
	//enum for sale status
	public enum SaleStatus{Active, NotActive;
	} ;
	
	private static final long serialVersionUID = 1L;
	//private int saleId;
	private String saleType,region;
	private SaleStatus saleStatus;
	private LocalDate startDate, endDate;
	private LocalTime startTime, endTime;
	
	

	public SaleEntity(String region, String saleType, LocalDate startDate,
			LocalDate endDate, LocalTime startTime, LocalTime endTime, SaleStatus saleStatus) {
		super();
		//this.saleId = saleId;
		this.saleType = saleType;
		this.region = region;
		this.saleStatus = saleStatus;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public SaleEntity(String region, String saleType, LocalDate startDate,
			LocalDate endDate, LocalTime startTime, LocalTime endTime) {
		super();
		//this.saleId = saleId;
		this.saleType = saleType;
		this.region = region;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.saleStatus = SaleStatus.NotActive;
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
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
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
	public String getStartDateToString() {
		return startDate.toString();
	}

	public String getEndDateToString() {
		return endDate.toString();
	}
}
