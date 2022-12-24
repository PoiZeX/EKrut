package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.CommonFunctions;
import common.Message;
import common.TaskType;
import entity.ItemInMachineEntity;
import entity.OrderReportEntity;
import entity.ItemInMachineEntity.call_Status;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class SupplyManagmentDBController {

	public static void getMachineItems(int machineId, ConnectionToClient client) {
		ItemInMachineEntity item;
		ArrayList<ItemInMachineEntity> itemsInMachine= new ArrayList<ItemInMachineEntity>() ;
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn
					.prepareStatement("SELECT   ekrut.item_in_machine.*, ekrut.items.name, ekrut.items.item_img_name"
							+ " FROM ekrut.items, ekrut.item_in_machine"
							+ " WHERE (item_in_machine.machine_id=? AND items.item_id=item_in_machine.item_id)"
							+ " ;");
			ps.setInt(1,machineId);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
			
			//   1                  2              3                 4          5                6              7            8     9
			//machine_id,         item_id,    current_amount minimum_amount, call_status,   times_under_min, calls_amount, name, item_img_name
			/*(int machineID, int item_id, int currentAmount,int minAmount,call_Status callStatus,
				                                               String name, double price, String manufacturer, String description,String item_img_name,    
				                                                                          int amount_under_min, int amount_calls)*/
				//TODO - add a check for string 
				item= new ItemInMachineEntity(machineId, res.getInt(2),res.getInt(3), res.getInt(4),ItemInMachineEntity.call_Status.NotOpened , res.getString(8), 0.0, null, null, res.getString(9), res.getInt(6),res.getInt(7));
				itemsInMachine.add(item);
			}
			try {
				client.sendToClient(new Message(TaskType.ReceiveItemsInMachine, itemsInMachine)); // finally send the entity
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
