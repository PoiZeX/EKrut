package controllerGui;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.SupplyReportEntity;
import javafx.scene.Node;
import utils.IReportsFromDB;
import utils.IValidateFields;
import utils.PopupSetter;

class SupplyReportControllerTest_Client {


	class ReportsFromDBStub implements IReportsFromDB {
		private SupplyReportEntity prevReport;

		@Override
		public SupplyReportEntity getSupplyReportFromDB(int machineID) {
			return currentReport;
		}

		@Override
		public SupplyReportEntity getPrevSupplyReportForMachine(int machineID) {
			return prevReport;
		}

		public void setPrevReport(SupplyReportEntity prevReport) {
			this.prevReport = prevReport;
		}
	}

	private SupplyReportEntity currentReport;
	private ReportsFromDBStub reportsGetterStub;
	private SupplyReportController supplyReportContoller;
	private String  region, month, year;
	

	@BeforeEach
	void setUp() throws Exception {
		supplyReportContoller = new SupplyReportController();
		reportsGetterStub = new ReportsFromDBStub();
		supplyReportContoller.reportsGetterSetter(reportsGetterStub);

		currentReport = new SupplyReportEntity(63, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "1");
	}
	/**
	 * Functionality : Test that when previous month report is valid, start amount is calculated correctly
	 * Input : Valid report and previous report
	 * Result : Supply report { "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10",
						"10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10" }
	 */
	@Test
	void intersectItemsTestValidReportsSucsses() {
		int machineID = 1;
		SupplyReportEntity validPrevReport = new SupplyReportEntity(62, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");
		reportsGetterStub.setPrevReport(validPrevReport);
		supplyReportContoller.setCurrentReport(supplyReportContoller.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportContoller.intersectItems(machineID);
		ArrayList<String> expectedRes = new ArrayList<String>(
				Arrays.asList(new String[] { "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10",
						"10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10" }));
		assertEquals(actualRes, expectedRes);
	}

	/**
	 * Functionality : Test that when previous month report is invalid : empty, start amount is calculated correctly
	 * Input : Valid report and no previous report
	 * Result : an array of all calculated items from previous month all 0's inserted: 
	 * { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" }
	 */
	@Test
	void intersectItemsTestPreviousReportNotExistSucsses() {
		int machineID = 1;
		reportsGetterStub.setPrevReport(new SupplyReportEntity());
		supplyReportContoller.setCurrentReport(supplyReportContoller.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportContoller.intersectItems(machineID);
		ArrayList<String> expectedRes = new ArrayList<String>(
				Arrays.asList(new String[] { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
						"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" }));
		assertEquals(actualRes, expectedRes);
	}

	/**
	 * Functionality : Test that when previous month report is valid but shorter, start amount is calculated correctly
	 * Input : Valid report and valid previous report
	 * Result : an array of all calculated items from previous month where all missing items are 0's inserted
	 */
	@Test
	void intersectItemsTestPreviousReportNotSameItemsAsCurrentReportSucsses() {
		int machineID = 1;
		SupplyReportEntity missingItemsPrevReport = new SupplyReportEntity(61, 1, 7,
				"6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");
		reportsGetterStub.setPrevReport(missingItemsPrevReport);
		supplyReportContoller.setCurrentReport(supplyReportContoller.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportContoller.intersectItems(machineID);
		ArrayList<String> expectedRes = new ArrayList<String>(
				Arrays.asList(new String[] { "0", "0", "0", "0", "0", "10", "10", "10", "10", "10", "10", "10", "10",
						"10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "0" }));
		assertEquals(expectedRes, actualRes);
	}

	/**
	 * Functionality : Test that when previous month report is valid but unmatched indexes, start amount is calculated correctly
	 * Input : Valid report and previous report
	 * Result : an array of all calculated items from previous month, with matching indexes.
	 */
	@Test
	void intersectItemsTestPreviousReportNotSameItemsIdIndexAsCurrentReportSucsses() {
		int machineID = 1;
		SupplyReportEntity misMatchedItems = new SupplyReportEntity(61, 1, 7,
				"2,1,4,3,6,5,9,11,7,10,8,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"7,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");

		reportsGetterStub.setPrevReport(misMatchedItems);
		supplyReportContoller.setCurrentReport(supplyReportContoller.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportContoller.intersectItems(machineID);
		ArrayList<String> expectedRes = new ArrayList<String>(
				Arrays.asList(new String[] { "10", "7", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10",
						"10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10" }));
		assertEquals(expectedRes, actualRes);
	}

	/**
	 * Functionality : Test that the report label is displayed correctly
	 * Input : region month and year
	 * Result : displayed region month and year is correct
	 */
	@SuppressWarnings("static-access")
	@Test
	void getReportDateTestValidFields() {
		region = "North";
		month = "January";
		year = "2022";
		supplyReportContoller.setReport(year, month, region);
		String actualResult = supplyReportContoller.getReportDate();
		String expectedResult = "North - 01/2022";
		assertEquals(expectedResult, actualResult);
	}

	/**
	 * Functionality : Test empty report is detected when getting empty report
	 * Input : invalid report
	 * Result : no report found error message
	 */
	@Test
	void getReportDateTestNullReportMessage() {
		SupplyReportEntity emptyReport = new SupplyReportEntity();
		String actualMsg = supplyReportContoller.checkNullReport(emptyReport);
		String expectedMsg = "No Report Found!";
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that the machine min amount is displayed correctly in the label
	 * Input : valid report
	 * Result : min amount =7
	 */
	@Test
	void getMinAmountTestSuccsess() {
		int machineID = 1;
		SupplyReportEntity misMatchedItems = new SupplyReportEntity(61, 1, 7,
				"2,1,4,3,6,5,9,11,7,10,8,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");
		reportsGetterStub.setPrevReport(misMatchedItems);
		supplyReportContoller.setCurrentReport(supplyReportContoller.getSupplyReportFromDB(machineID));
		String actualResult = supplyReportContoller.getMinAmount();
		String expectedResult = "7";
		assertEquals(expectedResult, actualResult);
	}
	
	/**
	 * Functionality : Test that the machine min amount is displayed correctly in the label
	 * Input : invalid report
	 * Result : min amount is set correctly
	 */
	@Test
	void getMinAmountTestEmptyReport() {
		int machineID = 1;
		currentReport = new SupplyReportEntity();
		supplyReportContoller.setCurrentReport(supplyReportContoller.getSupplyReportFromDB(machineID));
		String actualResult = supplyReportContoller.getMinAmount();
		String expectedResult = "0";
		assertEquals(expectedResult, actualResult);
	}
}
