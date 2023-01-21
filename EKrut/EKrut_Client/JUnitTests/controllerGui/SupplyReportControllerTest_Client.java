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
	void Test_Bad_Month() {
		reportType = "clientsReport";
		region = "North";
		month = "BlaBla";
		year = "2022";
		selectionClass.setDetails(reportType, region, month, year);
		String expectedMsg = "Please Select Valid Month";
		String actualMsg = selectionClass.validateFields();
		assertEquals(expectedMsg, actualMsg);
	}

}
