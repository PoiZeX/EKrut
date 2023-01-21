package controllerGui;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.scene.Node;
import utils.IValidateFields;

class SupplyReportControllerTest_Client {

	class FieldsValidatorStub implements IValidateFields {

		private ReportSelectionController validatorClass;

		public FieldsValidatorStub(ReportSelectionController validatorClass) {
			this.validatorClass = validatorClass;
		}

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
	private ReportSelectionController selectionClass;
	private String reportType, region, month, year;

	@BeforeEach
	void setUp() throws Exception {

		selectionClass = new ReportSelectionController();
		validatorStub = new FieldsValidatorStub(selectionClass);
		selectionClass.setValidaions(validatorStub);

	}

	@Test
	void Test_GoodValues() {
		reportType = "clientsReport";
		region = "North";
		month = "January";
		year = "2022";
		selectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "";
		String actualMsg = selectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	@Test
	void Test_BadMonthValue() {
		reportType = "clientsReport";
		region = "North";
		month = "BlaBla";
		year = "2022";
		selectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month";
		String actualMsg = selectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	@Test
	void Test_BadYearValue() {
		reportType = "clientsReport";
		region = "North";
		month = "January";
		year = "20239402";
		selectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Year";
		String actualMsg = selectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	@Test
	void Test_BadRegionValue() {
		reportType = "clientsReport";
		region = "SomeWhereFarAway";
		month = "December";
		year = "2022";
		selectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Region";
		String actualMsg = selectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	@Test
	void Test_BadReportTypeValue() {
		reportType = "SomeReportType";
		region = "North";
		month = "January";
		year = "2022";
		selectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Report Type";
		String actualMsg = selectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	@Test
	void Test_BadValues() {
		reportType = "SomeReportType";
		region = "SomeText";
		month = "SomeText";
		year = "25059";
		selectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month and Year and Valid Region and Valid Report Type";
		String actualMsg = selectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

	@Test
	void Test_NonNumericYear() {
		reportType = "SomeReportType";
		region = "SomeText";
		month = "SomeText";
		year = "asd";
		selectionClass.setDetails(reportType, region, month, year);
		try {
			String actualMsg = selectionClass.validateFields();
		} catch (Exception e) {
			System.out.println("Not a valid year exception was caught");
			assertTrue(true);
		}
	}
}
