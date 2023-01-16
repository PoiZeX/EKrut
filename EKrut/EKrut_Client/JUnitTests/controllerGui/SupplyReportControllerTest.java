package controllerGui;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Store.NavigationStoreController;
import client.ChatClient;
import client.ClientController;
import common.Message;
import common.TaskType;
import entity.UserEntity;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import entity.SupplyReportEntity;

class SupplyReportControllerTest {
	private Field actualResult;
	
	private int machineID;
	private String reportRegion;
	private String reportYear;
	private String reportMonth;
	
	private ClientController chat;
	private SupplyReportController supplyReportController;
	
	private Method initDetailsMethod;
	
	private void setMachineID(int mid) {machineID = mid;}
	private void setReportYear(String year) {reportYear = year;}
	private void setReportMonth(String month) {reportMonth = month;}
	private void setReportRegion(String region) {reportRegion = region;}
	
	private SupplyReportEntity setExpectedResult (
			int id,
			int machine_id,
			int min_stock,
			String item_id,
			String times_under_min,
			String end_stock,
			String month,
			String year,
			String region
		) 
	{
		return new SupplyReportEntity (
			id, 
			machine_id, 
			min_stock, 
			item_id, 
			times_under_min,
			end_stock, 
			month, 
			year, 
			region
		);
	}
	
	@BeforeEach
	void setUp() throws Exception {
		
		chat = mock(ClientController.class); 
		supplyReportController = new SupplyReportController(chat);
		actualResult = SupplyReportController.class.getDeclaredField("currentReport");
		actualResult.setAccessible(true);
		
	    initDetailsMethod = SupplyReportController.class.getDeclaredMethod("initDetails", int.class);
	    initDetailsMethod.setAccessible(true);
	    
	}
	
	@Test
	void testSuccessfulSupplyReport() throws Exception {
		setMachineID(1);
		setReportYear("2022");
		setReportMonth("12");
		setReportRegion("North");
		SupplyReportEntity expectedResult = setExpectedResult (
				63,
				1,
				7,
				"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26",
				"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0",
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10",
				"12",
				"2022",
				"1"
			);
		when(chat.acceptObj(new Message(
				TaskType.RequestReport,
				new String[] {"supply", reportRegion, reportMonth, reportYear, String.valueOf(machineID)}))
			).thenReturn(true);
		SupplyReportController.setReport(reportYear, reportMonth, reportRegion);
		initDetailsMethod.invoke(supplyReportController, machineID);
		assertEquals(actualResult, expectedResult);
	}

}
