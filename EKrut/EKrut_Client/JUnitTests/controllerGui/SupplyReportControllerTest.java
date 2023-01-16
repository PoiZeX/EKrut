package controllerGui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.ClientController;
import entity.SupplyReportEntity;

class SupplyReportControllerTest {
	private Field actualResult;

	private int machineID;
//	private String reportRegion;
//	private String reportYear;
//	private String reportMonth;

	private ClientController chat;
	private SupplyReportController supplyControllerActual, supplyControllerMock;
	private SupplyReportEntity badReport, goodReportDecember2022;
	private Method initDetailsMethod;

	@BeforeEach
	void setUp() throws Exception {
		chat = mock(ClientController.class);
		supplyControllerMock = mock(SupplyReportController.class);
		supplyControllerActual = new SupplyReportController();
		actualResult = SupplyReportController.class.getDeclaredField("currentReport");
		actualResult.setAccessible(true);
		initDetailsMethod = SupplyReportController.class.getDeclaredMethod("getSupplyReportFromDB", int.class);
		initDetailsMethod.setAccessible(true);

		badReport = new SupplyReportEntity();
		goodReportDecember2022 = setExpectedResult(63, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "1");
	}

	@Test
	void testSuccessfulSupplyReport() throws Exception {
		when(supplyControllerMock.getSupplyReportFromDB(machineID)).thenReturn(goodReportDecember2022);
		SupplyReportEntity actualResult = (SupplyReportEntity) initDetailsMethod.invoke(supplyControllerMock,
				machineID);
		assertEquals(actualResult,
				setExpectedResult(63, 1, 7, "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
						"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
						"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022",
						"North"));
	}

	@Test
	void testBadSupplyReport() throws Exception {

	}
	// ---- HELPER METHODS ---- //
//	private void setMachineID(int mid) {
//		machineID = mid;
//	}
//
//	private void setReportYear(String year) {
//		reportYear = year;
//	}
//
//	private void setReportMonth(String month) {
//		reportMonth = month;
//	}
//
//	private void setReportRegion(String region) {
//		reportRegion = region;
//	}

	private SupplyReportEntity setExpectedResult(int id, int machine_id, int min_stock, String item_id,
			String times_under_min, String end_stock, String month, String year, String region) {

		return new SupplyReportEntity(id, machine_id, min_stock, item_id, times_under_min, end_stock, month, year,
				region);
	}

}
