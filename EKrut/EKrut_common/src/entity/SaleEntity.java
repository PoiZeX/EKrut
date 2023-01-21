package entity;

import java.io.Serializable;
import java.time.LocalTime;

public class SaleEntity implements Serializable {
	public enum SaleStatus {
		Active, NotActive;
	};

	private static final long serialVersionUID = 1L;
	private int SaleID;
	private String saleType, region, days;
	private SaleStatus saleStatus;
	private LocalTime startTime, endTime;

	/**
	 * Instantiates a new sale entity.
	 *
	 * @param id the id
	 * @param region the region
	 * @param saleType the sale type
	 * @param days the days
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param saleStatus the sale status
	 */
	public SaleEntity(int id, String region, String saleType, String days, LocalTime startTime, LocalTime endTime,
			SaleStatus saleStatus) {
		super();
		this.SaleID = id;
		this.saleType = saleType;
		this.region = region;
		this.saleStatus = saleStatus;
		this.days = days;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Instantiates a new sale entity.
	 *
	 * @param region the region
	 * @param saleType the sale type
	 * @param days the days
	 * @param startTime the start time
	 * @param endTime the end time
	 */
	public SaleEntity(String region, String saleType, String days, LocalTime startTime, LocalTime endTime) {
		super();

		this.saleType = saleType;
		this.region = region;
		this.startTime = startTime;
		this.days = days;
		this.endTime = endTime;
		this.saleStatus = SaleStatus.NotActive;
	}

	/**
	 * Gets the sale ID.
	 *
	 * @return the sale ID
	 */
	public int getSaleID() {
		return SaleID;
	}

	/**
	 * Gets the sale type.
	 *
	 * @return the sale type
	 */
	public String getSaleType() {
		return saleType;
	}

	/**
	 * Sets the sale type.
	 *
	 * @param saleType the new sale type
	 */
	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	/**
	 * Gets the region.
	 *
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Sets the region.
	 *
	 * @param region the new region
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Gets the sale status.
	 *
	 * @return the sale status
	 */
	public SaleStatus getSaleStatus() {
		return saleStatus;
	}

	/**
	 * Sets the sale status.
	 *
	 * @param saleStatus the new sale status
	 */
	public void setSaleStatus(SaleStatus saleStatus) {
		this.saleStatus = saleStatus;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public LocalTime getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime the new start time
	 */
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public LocalTime getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime the new end time
	 */
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the days.
	 *
	 * @return the days
	 */
	public String getDays() {
		return days;
	}

	/**
	 * Sets the days.
	 *
	 * @param days the new days
	 */
	public void setDays(String days) {
		this.days = days;
	}

}
