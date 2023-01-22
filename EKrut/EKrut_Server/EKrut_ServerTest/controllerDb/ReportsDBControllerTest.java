package controllerDb;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entity.SupplyReportEntity;
import entity.UserEntity;
import mysql.MySqlClass;

class ReportsDBControllerTest {
	private static boolean isConnected = false;
	private Connection nullConnection;
	private String reportRegion, reportMonth, reportYear, reportMachineID;
	final private String SUPPLY = "supply";
	
	// Sets the report's details
	private String[] setReportDetails(String region, String month, String year, String machineID) {
		reportRegion = region;
		reportMonth = month;
		reportYear = year;
		reportMachineID = machineID;
		return new String[] {SUPPLY, reportRegion, reportMonth, reportYear, reportMachineID};
	}
	
	// Sets up and return a new SupplyReportEntity
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

	// Deletes a report by machine_id from the DB
	private boolean deleteReportFromDB(int machineID) {
		String query = "DELETE FROM ekrut.supply_report WHERE machine_id = ?;";
		PreparedStatement statement;
		try {
			statement = MySqlClass.getConnection().prepareStatement(query);
			statement.setInt(1, machineID);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// Deletes the last rows in personal_messages by the amount of machines
	private boolean deleteGeneratedMessagesFromDB(int numberOfRows) {
		String query = "DELETE FROM ekrut.personal_messages ORDER BY id DESC limit ?;";
		PreparedStatement statement;
		try {
			statement = MySqlClass.getConnection().prepareStatement(query);
			statement.setInt(1, numberOfRows);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@BeforeEach
	public void setUp() throws Exception {
		// One-time DB Connection
		if (isConnected)
			return;
		isConnected = true;
		MySqlClass.connectToDb("jdbc:mysql://localhost/ekrut?serverTimezone=IST", "root", "Aa123456");
	}

	// Report Creation Tests
	@Test
	// Functionality: Generate a new Supply Report with a valid Machine ID and store
	// it in the DB
	// Input: String "supply", String "12", String "2022", String "3", int 14
	// Result: True
	void testGenerateSupplyReportInDBWithValidMachineID() throws Exception {
		setReportDetails("3", "12", "2022", "14");
		new ReportsDBController();
		// Make sure the report doesn't exist before generating it
		assertFalse(ReportsDBController.isReportExist(SUPPLY, reportMonth, reportYear, reportRegion, Integer.parseInt(reportMachineID)));
		ReportsGenerator.generateSupplyReportForMachineID(14);
		assertTrue(ReportsDBController.isReportExist(SUPPLY, reportMonth, reportYear, reportRegion, Integer.parseInt(reportMachineID)));
		// Keep automation of UnitTesting by deleting the generated result
		deleteReportFromDB(14);
		// Make sure the result was properly deleted
		assertFalse(ReportsDBController.isReportExist(SUPPLY, reportMonth, reportYear, reportRegion, Integer.parseInt(reportMachineID)));
	}
	
	@Test
	// Functionality: Generate a new Supply Report with an empty Machine
	// it in the DB
	// Input: String "supply", String "12", String "2022", String "3", int 15
	// Result: True
	void testGenerateSupplyReportInDBWithEmptyMachineID() throws Exception {
		setReportDetails("3", "12", "2022", "15");
		new ReportsDBController();
		// Make sure the report doesn't exist before generating it
		assertFalse(ReportsDBController.isReportExist(SUPPLY, reportMonth, reportYear, reportRegion, Integer.parseInt(reportMachineID)));
		ReportsGenerator.generateSupplyReportForMachineID(15);
		assertFalse(ReportsDBController.isReportExist(SUPPLY, reportMonth, reportYear, reportRegion, Integer.parseInt(reportMachineID)));
	}
	
	@Test
	// Functionality: Generate a new Supply Report with a vali d Machine ID and
	// store it in the DB
	// Input: String "supply", String "12", String "2022", String "", int -2
	// Result: Message
	void testGenerateSupplyReportInDBWithUnvalidMachineID() throws Exception {
		new ReportsDBController();
		ReportsGenerator.generateSupplyReportForMachineID(-2);
		UserEntity manager = UsersManagementDBController.getRegionManagerFromDBQuery("North");
		String expectedResult = "Supply report for 12/2022 failed. Please contact EKrut team to check whats wrong";
		String actualResult = PersonalMessagesDBController.getPersonalMessagesFromDB(manager)
				.get(PersonalMessagesDBController.getPersonalMessagesFromDB(manager).size() - 1).getMessage();
		assertEquals(actualResult, expectedResult);
		// Keep automation of UnitTesting by deleting the generated result
		deleteGeneratedMessagesFromDB(CommonDataDBController.getMachineListFromDB().size());
	}

	// Report Viewing Tests
	@Test
	// Functionality: Supply Report request from the server returns an existing
	// Supply Report
	// Input: String "supply", String "3", String "12", String "2022", String "13"
	// Result: SupplyReportEntity
	void testGettingExistingSupplyReport() throws Exception { 
		new ReportsDBController();
		ReportsDBController.setReport(setReportDetails("3", "12", "2022", "13"));
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
		ReportsDBController.setReport(setReportDetails("3", "December", "2022", "13"));
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = setExpectedResult(1, 13, 0,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "3");
		assertTrue(compareReports(actualResult, expectedResult));
	}

	@Test
	// Functionality: Supply Report request with no region, month and year
	// Input: String "supply", String "", String "", String "", String "1"
	// Result: SupplyReportEntity
	void testGettingEmptySupplyReport() throws Exception {
		new ReportsDBController();
		ReportsDBController.setReport(setReportDetails("", "", "", "1"));
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}
	
	@Test
	// Functionality: Supply Report request with an invalid year
	// Input: String "supply", String "", String "", String "a$", String "1"
	// Result: SupplyReportEntity
	void testInvalidYearSupplyReport() throws Exception {
		new ReportsDBController();
		ReportsDBController.setReport(setReportDetails("", "", "$a", "1"));
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}
	
	@Test
	// Functionality: Supply Report request with an invalid month
	// Input: String "supply", String "", String "a$", String "", String "1"
	// Result: SupplyReportEntity
	void testInvalidMonthSupplyReport() throws Exception {
		new ReportsDBController();
		ReportsDBController.setReport(setReportDetails("", "$a", "", "1"));
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}
	
	@Test
	// Functionality: Supply Report request with an invalid region
	// Input: String "supply", String "", String "a$", String "", String "1"
	// Result: SupplyReportEntity
	void testInvalidRegionSupplyReport() throws Exception {
		new ReportsDBController();
		ReportsDBController.setReport(setReportDetails("$a", "", "", "1"));
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}
	
	@Test
	// Functionality: Supply Report request with empty credentials
	// Input: String "supply", String "", String "", String "", String ""
	// Result: NumberFormatException
	void testEmptyCredentialsSupplyReport() throws Exception {
		new ReportsDBController();
		assertThrows(NumberFormatException.class, () -> {ReportsDBController.setReport(setReportDetails("", "", "", ""));});
	}
	
	@Test
	// Functionality: Supply Report request with null credentials
	// Input: null, null, null, null, null
	// Result: NullPointerException
	void testNullCredentialsSupplyReport() throws Exception {
		new ReportsDBController();
		assertThrows(NullPointerException.class, () -> {ReportsDBController.setReport(new String[] { null, null, null, null, null });});
	}

	@Test
	// Functionality: Supply Report request with no connection to DB
	// Input: String "", String "", String "", String "", String ""
	// Result: SupplyReportEntity
	void testDisconnectedFromDBSupplyReport() throws Exception {
		new ReportsDBController(nullConnection); // Stub the Connection to the DB
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}
	
}