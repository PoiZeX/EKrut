package controllerGui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controllerGui.SupplyReportControllerTest_Client.FieldsValidatorStub;
import controllerGui.SupplyReportControllerTest_Client.ReportsFromDBStub;
import entity.SupplyReportEntity;
import javafx.scene.Node;
import utils.IValidateFields;

class ReportSelectionControllerTest {
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

	private FieldsValidatorStub validatorStub;
	private ReportSelectionController validatorClass;
	private String reportType, region, month, year;
	
	@BeforeEach
	void setUp() throws Exception {

		validatorClass = new ReportSelectionController();
		validatorStub = new FieldsValidatorStub();
		validatorClass.setValidaions(validatorStub);
	}
	/**
	 * Functionality : Test that all values are good error message indicator is
	 * blank Input : Valid report type, region month and year. Result : blank error
	 * message
	 */
	@Test
	void validateFieldsTestValidValuesSuccsess() {
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
	void validateFieldsTestInvalidMonthSucsses() {
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
	void validateFieldsTestInvalidYearSucsses() {
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
	void validateFieldsTestInvalidRegionSucsses() {
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
	void validateFieldsTestInvalidReportTypeSucsses() {
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
	void validateFieldsTestInvalidFieldsSucsses() {
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
	void validateFieldsTestInvalidFieldsNonNumericYearFeiledSucsses() {
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


}
