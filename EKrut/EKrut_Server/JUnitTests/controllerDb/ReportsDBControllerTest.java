package controllerDb;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import common.Message;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import entity.SupplyReportEntity;
import entity.UserEntity;
import enums.TaskType;
import mysql.MySqlClass;
import ocsf.server.AbstractServer;
import server.EchoServer;
import server.ServerUI;

class ReportsDBControllerTest {
	private static boolean connectionSuccessful = false;
	private static boolean toConnect = true;
	public static UserEntity connectedUser;

	private SupplyReportEntity setExpectedResult(int id, int machine_id, int min_stock, String item_id,
			String times_under_min, String end_stock, String month, String year, String region) {
		return new SupplyReportEntity(id, machine_id, min_stock, item_id, times_under_min, end_stock, month, year,
				region);
	}

	// Compares two given SupplyReportsEntity
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

	private boolean closeConnection(boolean toClose) throws SQLException {
		if (toClose)
			MySqlClass.getConnection().close();
		return true;
	}
	
	@BeforeEach
	public void setUp() throws Exception {
		if (connectionSuccessful)
			return;
		connectionSuccessful = true;
		//ServerUI.runServer("5555", "jdbc:mysql://localhost/ekrut?serverTimezone=IST", "root", "root0196");
		MySqlClass.connectToDb("jdbc:mysql://localhost/ekrut?serverTimezone=IST", "root", "root0196");
		
	}
	

	
	@Test
	// Functionality: Supply Report request from the server returns an existing Supply Report
	// Input: String "supply", String "1", String "12", String "2022", String "1"
	// Result: SupplyReportEntity
	void testSuccessfulSupplyReport() throws Exception {
		connectionSuccessful = true;
		ReportsDBController.setReport(new String[] { "supply", "1", "12", "2022", "1" });
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = setExpectedResult(63, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "1");
		assertTrue(compareReports(actualResult, expectedResult));
	}
	
	@Test
	// Functionality: Supply Report request with month as String from the server returns an existing Supply Report
	// Input: String "supply", String "1", String "December", String "2022", String "1"
	// Result: SupplyReportEntity
	void testNonNumericMonthSupplyReport() throws Exception {
		ReportsDBController.setReport(new String[] { "supply", "1", "December", "2022", "1" });
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = setExpectedResult(63, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "1");
		assertTrue(compareReports(actualResult, expectedResult));
	}
	
	@Test
	// Functionality: Supply Report request with empty credentials
	// Input: String "supply", String "", String "", String "", String ""
	// Result: SupplyReportEntity
	void testEmptySupplyReport() throws Exception {
		ReportsDBController.setReport(new String[] { "supply", "", "", "", "1" });
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
	}
	@Test
	// Functionality: Supply Report request with empty credentials
	// Input: String "supply", String "", String "", String "", String ""
	// Result: SupplyReportEntity
	void testDisconnectedFromDBSupplyReport() throws Exception {
		
		SupplyReportEntity actualResult = ReportsDBController.getSupplyReportFromDB();
		SupplyReportEntity expectedResult = new SupplyReportEntity();
		assertEquals(actualResult.getId(), expectedResult.getId());
		
	}

}
