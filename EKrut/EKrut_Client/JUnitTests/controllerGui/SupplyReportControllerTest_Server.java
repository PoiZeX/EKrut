package controllerGui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import client.ClientController;
import entity.SupplyReportEntity;

class SupplyReportControllerTest_Server {
	private SupplyReportController supplyReportController;
	private ClientController chatService;

	private boolean compareReports(SupplyReportEntity compareReport, SupplyReportEntity toReport) {
		ArrayList<Boolean> boolArray = new ArrayList<>();
		boolArray.add(compareReport.getId() == toReport.getId()); // ?
		boolArray.add(compareReport.getYear().equals(toReport.getYear()));
		boolArray.add(compareReport.getMonth().equals(toReport.getMonth()));
		boolArray.add(compareReport.getRegion().equals(toReport.getRegion()));
		boolArray.add(compareReport.getMachine_id() == toReport.getMachine_id());
		boolArray.add(compareReport.getReportsList().size() == toReport.getReportsList().size());
		if (boolArray.contains(false))
			return false;

		int numberOfDataSets = compareReport.getReportsList().size();
		int numberOfEqualDataSets = 0;

		// Compare item data sets
		for (String[] compareDataSet : compareReport.getReportsList()) {
			for (String[] toDataSet : toReport.getReportsList()) {
				if (new ArrayList<String>(Arrays.asList(compareDataSet))
						.equals(new ArrayList<String>(Arrays.asList(toDataSet)))) {
					numberOfEqualDataSets++;
				}
			}
		}
		if (numberOfDataSets != numberOfEqualDataSets)
			return false;
		return true;
	}

	@BeforeEach
	void setUp() throws Exception {
		supplyReportController = new SupplyReportController();
		// Handle this differently later, currently you need to run the server
		chatService = new ClientController("localhost", 5555);
		supplyReportController.setChatService(chatService);
	}

	@Test
	// Functionality: Supply Report request from the server returns an existing Supply Report
	// Input: String "2022", String "12", String "1", int 1
	// Result: SupplyReportEntity
	void testSuccessfulSupplyReport() throws Exception {
		SupplyReportController.setReport("2022", "12", "1");
		SupplyReportEntity actualResult = supplyReportController.getSupplyReportFromDB(1);
		SupplyReportEntity expectedResult = setExpectedResult(63, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "1");
		assertTrue(compareReports(actualResult, expectedResult));
	}

	@Test
	// Functionality: Supply Report request from the server with invalid credentials 
	// Input: String "2024", String "12", String "1", int 1
	// Result: null
	void testNullSupplyReport() throws Exception {
		SupplyReportController.setReport("2024", "12", "1");
		SupplyReportEntity actualResult = supplyReportController.getSupplyReportFromDB(1);
		assertNull(actualResult.getReportsList());
	}
	
	@Test
	// Functionality: Supply Report request from the server with no credentials 
	// Input: String "2024", String "12", String "1", int 1
	// Result: null
	void testInvalidSupplyReport() throws Exception {
		SupplyReportController.setReport("", "", "");
		SupplyReportEntity actualResult = supplyReportController.getSupplyReportFromDB(0);
		assertNull(actualResult.getReportsList());
	}
	
	
	

	private SupplyReportEntity setExpectedResult(int id, int machine_id, int min_stock, String item_id,
			String times_under_min, String end_stock, String month, String year, String region) {
		return new SupplyReportEntity(id, machine_id, min_stock, item_id, times_under_min, end_stock, month, year,
				region);
	}

}
