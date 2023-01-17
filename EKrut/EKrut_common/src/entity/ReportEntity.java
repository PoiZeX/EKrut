package entity;

import java.io.Serializable;

public class ReportEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String month, year, region;
	
	/**
	 * Instantiates a new report entity.
	 */
	public ReportEntity() {
		id = 0;
		month = "";
		year = "";
		region = "";
	}
	
	/**
	 * Instantiates a new report entity.
	 *
	 * @param id the id
	 * @param month the month
	 * @param year the year
	 * @param region the region
	 */
	public ReportEntity(int id, String month, String year, String region) {
		this.id = id;
		this.month = month;
		this.year = year;
		this.region = region;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the month.
	 *
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * Sets the month.
	 *
	 * @param month the new month
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * Gets the year.
	 *
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets the year.
	 *
	 * @param year the new year
	 */
	public void setYear(String year) {
		this.year = year;
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

}
