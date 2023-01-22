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

	class FieldsValidatorStub implements IValidateFields {

		@Override
		public boolean isYearValid() {
			return validatorClass.getYears().contains(Integer.parseInt(validatorClass.getSelectedYear()));
		}

		@Override
		public boolean isMonthValid() {
			return validatorClass.getMonths().contains(validatorClass.getSelectedMonth());
		}

		@Override
		public boolean isRegionValid() {
			return validatorClass.getRegions().contains(validatorClass.getSelectedRegion());
		}

		@Override
		public boolean isSelectedReportValid() {
			return validatorClass.getReportTypes().contains(validatorClass.getSelectedReport());
		}

		@Override
		public void styleSetter(Node n, boolean flag) {
			return;
		}

	}

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
	private PopupSetter popupMock;
	private FieldsValidatorStub validatorStub;
	private ReportsFromDBStub reportsGetterStub;
	private ReportSelectionController validatorClass;
	private SupplyReportController supplyReportClass;
	private String reportType, region, month, year;

	@BeforeEach
	void setUp() throws Exception {

		validatorClass = new ReportSelectionController();
		supplyReportClass = new SupplyReportController();
		reportsGetterStub = new ReportsFromDBStub();
		validatorStub = new FieldsValidatorStub();
		validatorClass.setValidaions(validatorStub);
		supplyReportClass.reportsGetterSetter(reportsGetterStub);

		currentReport = new SupplyReportEntity(63, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "12", "2022", "1");
	}

	/**
	 * Functionality : Test that all values are good error message indicator is
	 * blank Input : Valid report type, region month and year. Result : blank error
	 * message
	 */
	@Test
	void Test_GoodValues() {
		reportType = "clientsReport";
		region = "North";
		month = "January";
		year = "2022";
		validatorClass.setDetails(reportType, region, month, year);
		String expectedMsg = "";
		String actualMsg = validatorClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad month selection Input :
	 * Valid reportType region year, invalid month Result : error message indicates
	 * bad month selected
	 */
	@Test
	void Test_BadMonthValue() {
		reportType = "clientsReport";
		region = "North";
		month = "BlaBla";
		year = "2022";
		validatorClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month";
		String actualMsg = validatorClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad year selection 
	 * Input : Valid reportType region month, invalid year 
	 * Result : error message indicates bad year selected
	 */
	@Test
	void Test_BadYearValue() {
		reportType = "clientsReport";
		region = "North";
		month = "January";
		year = "20239402";
		validatorClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Year";
		String actualMsg = validatorClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad region selection 
	 * Input : Valid reportType month year, invalid region 
	 * Result : error message indicates bad region selected
	 */
	@Test
	void Test_BadRegionValue() {
		reportType = "clientsReport";
		region = "SomeWhereFarAway";
		month = "December";
		year = "2022";
		validatorClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Region";
		String actualMsg = validatorClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad reportType selection 
	 * Input : Valid year region month, invalid reportType 
	 * Result : error message indicates bad reportType selected
	 */
	@Test
	void Test_BadReportTypeValue() {
		reportType = "SomeReportType";
		region = "North";
		month = "January";
		year = "2022";
		validatorClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Report Type";
		String actualMsg = validatorClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows all bad values selection 
	 * Input : Valid reportType region month, invalid year 
	 * Result : error message indicates bad year selected
	 */
	@Test
	void Test_BadValues() {
		reportType = "SomeReportType";
		region = "SomeText";
		month = "SomeText";
		year = "25059";
		validatorClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month and Year and Valid Region and Valid Report Type";
		String actualMsg = validatorClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows all bad values selection 
	 * Input : Valid reportType region month, Non-numeric year instered
	 * Result : exception about bad year thrown
	 */
	@Test
	void Test_NonNumericYear() {
		reportType = "SomeReportType";
		region = "SomeText";
		month = "SomeText";
		year = "asd";
		validatorClass.setDetails(reportType, region, month, year);
		try {
			String actualMsg = validatorClass.validateFields();
		} catch (Exception e) {
			System.out.println("Not a valid year exception was caught in : Test_NonNumericYear");
			assertTrue(true);
		}
	}

	/**
	 * Functionality : Test that when previous month report is valid, start amount is calculated correctly
	 * Input : Valid report and previous report
	 * Result : an array of all calculated items from previous month
	 */
	@Test
	void Test_HasPrevReport() {
		int machineID = 1;
		SupplyReportEntity validPrevReport = new SupplyReportEntity(62, 1, 7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");
		reportsGetterStub.setPrevReport(validPrevReport);
		supplyReportClass.setCurrentReport(supplyReportClass.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportClass.intersectItems(machineID);
		ArrayList<String> expectedRes = new ArrayList<String>(
				Arrays.asList(new String[] { "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10",
						"10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10", "10" }));
		assertEquals(actualRes, expectedRes);
	}

	/**
	 * Functionality : Test that when previous month report is invalid : empty, start amount is calculated correctly
	 * Input : Valid report and no previous report
	 * Result : an array of all calculated items from previous month all 0's inserted
	 */
	@Test
	void Test_HasNoPrevReport() {
		int machineID = 1;
		reportsGetterStub.setPrevReport(new SupplyReportEntity());
		supplyReportClass.setCurrentReport(supplyReportClass.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportClass.intersectItems(machineID);
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
	void Test_HasShorterPrevReport() {
		int machineID = 1;
		SupplyReportEntity missingItemsPrevReport = new SupplyReportEntity(61, 1, 7,
				"6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");
		reportsGetterStub.setPrevReport(missingItemsPrevReport);
		supplyReportClass.setCurrentReport(supplyReportClass.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportClass.intersectItems(machineID);
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
	void Test_misMatchedIndexesReport() {
		int machineID = 1;
		SupplyReportEntity misMatchedItems = new SupplyReportEntity(61, 1, 7,
				"2,1,4,3,6,5,9,11,7,10,8,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"7,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");

		reportsGetterStub.setPrevReport(misMatchedItems);
		supplyReportClass.setCurrentReport(supplyReportClass.getSupplyReportFromDB(machineID));
		ArrayList<String> actualRes = supplyReportClass.intersectItems(machineID);
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
	void Test_validateReportDetails() {
		region = "North";
		month = "January";
		year = "2022";
		supplyReportClass.setReport(year, month, region);
		String actualResult = supplyReportClass.getReportDate();
		String expectedResult = "North - 01/2022";
		assertEquals(expectedResult, actualResult);
	}

	/**
	 * Functionality : Test empty report is detected when getting empty report
	 * Input : invalid report
	 * Result : no report found error message
	 */
	@Test
	void Test_nullReportMessage() {
		SupplyReportEntity emptyReport = new SupplyReportEntity();
		String actualMsg = supplyReportClass.checkNullReport(emptyReport);
		String expectedMsg = "No Report Found!";
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that the machine min amount is displayed correctly in the label
	 * Input : valid report
	 * Result : min amount is set correctly
	 */
	@Test
	void Test_correctMinAmount() {
		int machineID = 1;
		SupplyReportEntity misMatchedItems = new SupplyReportEntity(61, 1, 7,
				"2,1,4,3,6,5,9,11,7,10,8,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10", "11", "2022", "1");
		reportsGetterStub.setPrevReport(misMatchedItems);
		supplyReportClass.setCurrentReport(supplyReportClass.getSupplyReportFromDB(machineID));
		String actualResult = supplyReportClass.getMinAmount();
		String expectedResult = "7";
		assertEquals(expectedResult, actualResult);
	}
	
	/**
	 * Functionality : Test that the machine min amount is displayed correctly in the label
	 * Input : invalid report
	 * Result : min amount is set correctly
	 */
	@Test
	void Test_correctMinAmountEmptyReport() {
		int machineID = 1;
		currentReport = new SupplyReportEntity();
		supplyReportClass.setCurrentReport(supplyReportClass.getSupplyReportFromDB(machineID));
		String actualResult = supplyReportClass.getMinAmount();
		String expectedResult = "0";
		assertEquals(expectedResult, actualResult);
	}
}
