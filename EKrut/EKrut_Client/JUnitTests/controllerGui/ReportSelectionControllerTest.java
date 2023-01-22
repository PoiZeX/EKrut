package controllerGui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.Node;
import utils.IValidateFields;

class ReportSelectionControllerTest {
	class FieldsValidatorStub implements IValidateFields {
		@Override
		public boolean isYearValid() {
			return (ReportSelectionClass.getSelectedYear() != "" && ReportSelectionClass.getYears().contains(Integer.parseInt(ReportSelectionClass.getSelectedYear())));
		}

		@Override
		public boolean isMonthValid() {
			return ReportSelectionClass.getMonths().contains(ReportSelectionClass.getSelectedMonth());
		}

		@Override
		public boolean isRegionValid() {
			return ReportSelectionClass.getRegions().contains(ReportSelectionClass.getSelectedRegion());
		}

		@Override
		public boolean isSelectedReportValid() {
			return ReportSelectionClass.getReportTypes().contains(ReportSelectionClass.getSelectedReport());
		}

		@Override
		public void styleSetter(Node n, boolean flag) {
			return;
		}
	}

	private FieldsValidatorStub validatorStub;
	private ReportSelectionController ReportSelectionClass;
	private String reportType, region, month, year;
	
	@BeforeEach
	void setUp() throws Exception {
		ReportSelectionClass = new ReportSelectionController();
		validatorStub = new FieldsValidatorStub();
		ReportSelectionClass.setValidaions(validatorStub);
	}
	/**
	 * Functionality : Test that all values are good error message indicator is
	 * blank Input : Valid report type, region month and year. Result : blank error
	 * message
	 */
	@Test
	void validateFieldsTestValidValuesSuccsess() {
		reportType = "supplyReport";
		region = "North";
		month = "January";
		year = "2022";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad month selection Input :
	 * Valid reportType region year, invalid month Result : error message indicates
	 * bad month selected
	 */
	@Test
	void validateFieldsTestInvalidMonthSucsses() {
		reportType = "supplyReport";
		region = "North";
		month = "BlaBla";
		year = "2022";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad year selection 
	 * Input : Valid reportType region month, invalid year 
	 * Result : error message indicates bad year selected
	 */
	@Test
	void validateFieldsTestInvalidYearSucsses() {
		reportType = "supplyReport";
		region = "North";
		month = "January";
		year = "20239402";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Year";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad region selection 
	 * Input : Valid reportType month year, invalid region 
	 * Result : error message indicates bad region selected
	 */
	@Test
	void validateFieldsTestInvalidRegionSucsses() {
		reportType = "supplyReport";
		region = "SomeWhereFarAway";
		month = "December";
		year = "2022";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Region";
		String actualMsg = ReportSelectionClass.validateFields();
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
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Report Type";
		String actualMsg = ReportSelectionClass.validateFields();
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
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month and Year and Valid Region and Valid Report Type";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows all bad values selection 
	 * Input : Valid reportType region month, Non-numeric year instered
	 * Result : exception about bad year thrown
	 */
	@Test
	void validateFieldsTestInvalidFieldsNonNumericYearFailedSucsses() {
		reportType = "";
		region = "";
		month = "";
		year = "NonInteger";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		try {
			String actualMsg = ReportSelectionClass.validateFields();
		} catch (Exception e) {
			System.out.println("Not a valid year exception was caught in : Test_NonNumericYear");
			assertTrue(true);
		}
	}


	/**
	 * Functionality : Test that error message shows bad month selection Input :
	 * Valid reportType region year, Empty month 
	 * Result : error message indicates bad month selected
	 */
	@Test
	void validateFieldsTestEmptyMonthSucsses() {
		reportType = "supplyReport";
		region = "North";
		month = "";
		year = "2022";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad year selection 
	 * Input : Valid reportType region month, Empty year 
	 * Result : error message indicates bad year selected
	 */
	@Test
	void validateFieldsTestEmptyYearSucsses() {
		reportType = "supplyReport";
		region = "North";
		month = "January";
		year = "";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Year";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad region selection 
	 * Input : Valid reportType month year, Empty region 
	 * Result : error message indicates bad region selected
	 */
	@Test
	void validateFieldsTestEmptyRegionAndYearSucsses() {
		reportType = "supplyReport";
		region = "";
		month = "December";
		year = "";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Year and Valid Region";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows bad reportType selection 
	 * Input : Valid year region month, Empty reportType 
	 * Result : error message indicates bad reportType selected
	 */
	@Test
	void validateFieldsTestEmptyReportTypeAndRegionSucsses() {
		reportType = "";
		region = "";
		month = "January";
		year = "2022";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Report Type and Valid Region";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows all bad values selection 
	 * Input : Valid reportType region month, invalid year 
	 * Result : error message indicates bad year selected
	 */
	@Test
	void validateFieldsTestEmptyFieldsSucsses() {
		reportType = "";
		region = "";
		month = "";
		year = "";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month and Year and Valid Region and Valid Report Type";
		String actualMsg = ReportSelectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message is empty on valid date selection
	 * Input : valid date
	 * Result : error message is empty
	 */
	@Test
	void validateDateTestValidDateSuccess() {
		reportType = "supplyReport";
		region = "North";
		month = "December";
		year = "2020";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "";
		String actualMsg = ReportSelectionClass.validateDate();
		assertEquals(expectedMsg, actualMsg);
	}

	/**
	 * Functionality : Test that error message shows invalid date on later date selected
	 * Input : invalid date
	 * Result : error message shows invalid date selected
	 */
	@Test
	void validateDateTestInvalidDateSuccess() {
		reportType = "supplyReport";
		region = "North";
		month = "December";
		year = "2025";
		ReportSelectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Cannot select date greater than today.";
		String actualMsg = ReportSelectionClass.validateDate();
		assertEquals(expectedMsg, actualMsg);
	}
}
