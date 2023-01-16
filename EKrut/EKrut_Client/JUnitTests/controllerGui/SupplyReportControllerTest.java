package controllerGui;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.ClientController;
import entity.SupplyReportEntity;

class SupplyReportControllerTest {
	private Field actualResult;
	
	private int machineID;
	private String reportRegion;
	private String reportYear;
	private String reportMonth;
	
	private ClientController chat;
	private SupplyReportController supplyReportController;
	
	private Method initDetailsMethod;
	
	private void setMachineID(int mid) {machineID = mid;}
	private void setReportYear(String year) {reportYear = year;}
	private void setReportMonth(String month) {reportMonth = month;}
	private void setReportRegion(String region) {reportRegion = region;}
	
	private SupplyReportEntity setExpectedResult(
			int id,
			int machine_id,
			int min_stock,
			String item_id,
			String times_under_min,
			String end_stock,
			String month,
			String year,
			String region
		) 
	{
		return new SupplyReportEntity(
			id, 
			machine_id, 
			min_stock, 
			item_id, 
			times_under_min,
			end_stock, 
			month, 
			year, 
			region
		);
	}
	
	@BeforeEach
	void setUp() throws Exception {
		// define mock and inject
		chat = mock(ClientController.class); 
		supplyReportController = new SupplyReportController(chat);
		actualResult = SupplyReportController.class.getDeclaredField("currentReport");
		actualResult.setAccessible(true);
		
	    initDetailsMethod = LoginController.class.getDeclaredMethod("initDetails");
	    initDetailsMethod.setAccessible(true);
	}
	
	@Test
	void testSuccessfulSupplyReport() throws Exception{
		setMachineID(1);
		setReportYear("2022");
		setReportMonth("12");
		setReportRegion("North");
		SupplyReportController.setReport(reportYear, reportMonth, reportRegion);
		initDetailsMethod.invoke(supplyReportController);
		
	}

}
