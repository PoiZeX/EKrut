package controllerGui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import client.ClientController;
import common.Message;
import entity.SupplyReportEntity;
import enums.TaskType;

@RunWith(MockitoJUnitRunner.class)
class SupplyReportControllerTest {
	private Method initDetailsMethod;
	private Field actualResult;
	private SupplyReportController supplyController;
	private ClientController chat;

//	private boolean isSameLength(String compare, String to) {
//		if (compare.length() != to.length())
//			return false;
//		return true;
//	}
//
//	private HashMap<String, String[]> makeItemInSupply(String itemID_string, String timesUnderMin_string,
//			String endStock_string) {
//		// Compare the lengths of the input strings
//		if (!isSameLength(itemID_string, timesUnderMin_string) || !isSameLength(timesUnderMin_string, endStock_string))
//			return null;
//		// Create a hash map to store the item's details
//		HashMap<String, String[]> itemsInSupply = new HashMap<>();
//		String[] itemID_list = itemID_string.split(",");
//		String[] timesUnderMin_list = timesUnderMin_string.split(",");
//		String[] endStock_list = endStock_string.split(",");
//		// Put item deatils into hashmap [id, [under_min, end_stock]]
//		for (int i = 0; i < itemID_list.length; i++)
//			itemsInSupply.put(itemID_list[i], new String[] { timesUnderMin_list[i], endStock_list[i] });
//		return itemsInSupply;
//	}

	@BeforeEach
	void setUp() throws Exception {
		actualResult = SupplyReportController.class.getDeclaredField("currentReport");
		actualResult.setAccessible(true);
		initDetailsMethod = SupplyReportController.class.getDeclaredMethod("initDetails", int.class);
		initDetailsMethod.setAccessible(true);

		supplyController = new SupplyReportController();
		chat = mock(ClientController.class);
		supplyController.setChatService(chat);

	}

	@Test
	void testSuccessfulSupplyReport() throws Exception {
		when(chat.acceptObj(new Message(TaskType.RequestReport, new String[] { "supply", "North", "12", "2022", "1" }))).thenReturn(true);
		supplyController.getSupplyReportFromDB(1);
	}

	@Test
	void testBadSupplyReport() throws Exception {

	}

	private SupplyReportEntity setExpectedResult(int id, int machine_id, int min_stock, String item_id,
			String times_under_min, String end_stock, String month, String year, String region) {

		return new SupplyReportEntity(id, machine_id, min_stock, item_id, times_under_min, end_stock, month, year,
				region);
	}

}
