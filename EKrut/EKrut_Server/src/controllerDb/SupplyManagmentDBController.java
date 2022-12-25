package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import common.CommonFunctions;
import common.Message;
import common.TaskType;
import entity.ItemInMachineEntity;
import entity.OrderReportEntity;
import entity.ItemInMachineEntity.Call_Status;
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
			PreparedStatement ps1=conn.prepareStatement("SELECT   ekrut.machines.min_amount"
					+ " FROM ekrut.machines"+ " WHERE machines.machine_id=(?);");
			ps1.setInt(1,machineId);
			ResultSet res1=ps1.executeQuery();
			int minAmount=0;
			if(res1.next()) {
				 minAmount=(res1.getInt(1));}
			
			PreparedStatement ps = conn
					.prepareStatement("SELECT  *"
							+" FROM  ekrut.item_in_machine"
							+" WHERE item_in_machine.machine_id=(?) AND item_in_machine.current_amount <= (?);");
			ps.setInt(1,machineId);
			ps.setInt(2,minAmount);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
			
			//   1                  2              3                 4          5                6              7            8     9
				//machine_id, item_id, current_amount, minimum_amount, call_status, times_under_min, calls_amount
		//public ItemInMachineEntity(int machineID, int item_id, int currentAmount,Call_Status callStatus) {
				//TODO - add a check for string 
				item= new ItemInMachineEntity(res.getInt(1),res.getInt(2), res.getInt(3),ItemInMachineEntity.Call_Status.NotOpened );
				
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
