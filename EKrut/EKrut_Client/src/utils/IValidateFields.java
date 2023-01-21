package utils;

import javafx.scene.Node;

public interface IValidateFields {
	public boolean isYearValid();

	public boolean isMonthValid();

	public boolean isRegionValid();
	
	public boolean isSelectedReportValid();
	
	void styleSetter(Node n, boolean flag);
}
