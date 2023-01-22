package utils;

import entity.SupplyReportEntity;

public interface IReportsFromDB {
	SupplyReportEntity getSupplyReportFromDB(int machineID);
	SupplyReportEntity getPrevSupplyReportForMachine(int machineID);
}
