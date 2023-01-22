package controllerDb;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import entity.SupplyReportEntity;
import entity.UserEntity;
import mysql.MySqlClass;

class ReportsDBControllerTest {
	private static boolean connectionSuccessful = false;
	public static UserEntity connectedUser;
	private Connection nullConnection;

	private SupplyReportEntity setExpectedResult(int id, int machine_id, int min_stock, String item_id,
			String times_under_min, String end_stock, String month, String year, String region) {
		return new SupplyReportEntity(id, machine_id, min_stock, item_id, times_under_min, end_stock, month, year,
				region);
	}

	// Compares two given SupplyReportsEntity
	private boolean compareReports(SupplyReportEntity compareReport, SupplyReportEntity toReport) {
		ArrayList<Boolean> boolArray = new ArrayList<>();
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
	public void setUp() throws Exception {
		// One-time DB Connection
		if (connectionSuccessful)
			return;
		connectionSuccessful = true;
		MySqlClass.connectToDb("jdbc:mysql://localhost/ekrut?serverTimezone=IST", "root", "root0196");
	}

	// Report Creation Tests
	@Test
	// Functionality: Generate a new Supply Report with a valid Machine ID and store
	// it in the DB
	// Input: String "supply", String "12", String "2022", String "3", int 14
	// Result: True
	void testGenerateSupplyReportInDBWithValidMachineID() throws Exception {
		new ReportsDBController();
		// Make sure the report doesn't exist before generating it
		assertFalse(ReportsDBController.isReportExist("supply", "12", "2022", "3", 14));//
		//////////////////////////////////////////////////////////////////////////////////
		ReportsGenerator.generateSupplyReportForMachineID(14);
		assertTrue(ReportsDBController.isReportExist("supply", "12", "2022", "3", 14));

	}

	@Test
	// Functionality: Generate a new Supply Report with a vali d Machine ID and
	// store it in the DB
	// Input: String "supply", String "12", String "2022", String "", int -1
	// Result: Message
	void testGenerateSupplyReportInDBWithUnvalidMachineID() throws Exception {
		new ReportsDBController();
		ReportsGenerator.generateSupplyReportForMachineID(-1);
		UserEntity manager = UsersManagementDBController.getRegionManagerFromDBQuery("North");
		String expectedResult = "Supply report for 12/2022 failed. Please contact EKrut team to check whats wrong";
		String actualResult = PersonalMessagesDBController.getPersonalMessagesFromDB(manager)
				.get(PersonalMessagesDBController.getPersonalMessagesFromDB(manager).size() - 1).getMessage();
		assertEquals(actualResult, expectedResult);
	}

	// Report Viewing Tests
	@Test
	// Functionality: Supply Report request from the server returns an existing
	// Supply Report
	// Input: String "supply", String "3", String "12", String "2022", String "13"
	// Result: SupplyReportEntity
	void testGettingExistingSupplyReport() throws Exception {
		new ReportsDBController();
		ReportsDBController.setReport(new String[] { "supply", "3", "12", "2022", "13" });
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = setExpectedResult(1, 13, 0,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "3");
		assertTrue(compareReports(actualResult, expectedResult));
	}

	@Test
	// Functionality: Supply Report request with month as String from the server
	// returns an existing Supply Report
	// Input: String "supply", String "3", String "December", String "2022", String
	// "13"
	// Result: SupplyReportEntity
	void testGettingNonNumericMonthSupplyReport() throws Exception {
		new ReportsDBController();
		ReportsDBController.setReport(new String[] { "supply", "3", "December", "2022", "13" });
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = setExpectedResult(1, 13, 0,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "3");
		assertTrue(compareReports(actualResult, expectedResult));
	}

	@Test
	// Functionality: Supply Report request with empty credentials
	// Input: String "supply", String "", String "", String "", String ""
	// Result: SupplyReportEntity
	void testGettingEmptySupplyReport() throws Exception {
		new ReportsDBController();
		ReportsDBController.setReport(new String[] { "supply", "", "", "", "1" });
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}

	@Test
	// Functionality: Supply Report request with no connection to DB
	// Input: String "", String "", String "", String "", String ""
	// Result: SupplyReportEntity
	void testDisconnectedFromDBSupplyReport() throws Exception {
		new ReportsDBController(nullConnection);
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}

}
