package controllerDb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.ResolverStyle;
import java.util.ArrayList;

import common.CommonFunctions;
import common.Message;
import common.TaskType;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.OrderReportEntity;
import entity.UserEntity;
import entity.ItemInMachineEntity.Call_Status;
import entity.MachineEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class SupplyManagmentDBController {
	static boolean pros = false;
	/*----------------------------------GETDATA--------------------------------*/
	/**get items in machine for supply mangment*/
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
					.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.name "
							+" FROM  ekrut.item_in_machine, ekrut.items"
							+" WHERE item_in_machine.machine_id=(?) AND (item_in_machine.current_amount < (?) OR  item_in_machine.call_status!=(?)) AND item_in_machine.item_id=items.item_id ;");
			ps.setInt(1,machineId);
			ps.setInt(2,minAmount);		
			ps.setString(3, ItemInMachineEntity.Call_Status.NotOpened.toString());
			ResultSet res = ps.executeQuery();
			while (res.next()) {
			
				//   1                  2              3          4          5        7                      
				//machine_id, item_id, current_amount,  call_status, times_under_min,name
				//public ItemInMachineEntity(int machineID, int item_id, int currentAmount,Call_Status callStatus ,times_under_min) 
				item= new ItemInMachineEntity(res.getInt(1),res.getInt(2), res.getInt(3),ItemInMachineEntity.Call_Status.valueOf( res.getString(4)),res.getInt(5),res.getString(7));
				
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
	/**get items in machine for supply update*/
	public static void getProcessedMachineItems(int [] arr, ConnectionToClient client) {
		ItemInMachineEntity item;
		int machineId =arr[0];
		int userId =arr[1];
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
					.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.name "
							+" FROM  ekrut.item_in_machine, ekrut.items"
							+" WHERE item_in_machine.machine_id=(?) AND  item_in_machine.call_status=(?) AND item_in_machine.item_id=items.item_id AND item_in_machine.worker_id=(?)  ;");
			ps.setInt(1,machineId);
			ps.setString(2, ItemInMachineEntity.Call_Status.Processed.toString());
			ps.setInt(3, userId);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
			
				//   1                  2              3          4          5        6                      
				//machine_id, item_id, current_amount,  call_status, times_under_min,name
				//public ItemInMachineEntity(int machineID, int item_id, int currentAmount,Call_Status callStatus ,times_under_min) 
				item= new ItemInMachineEntity(res.getInt(1),res.getInt(2), res.getInt(3),ItemInMachineEntity.Call_Status.valueOf( res.getString(4)),res.getInt(5),res.getString(7));
				item.setWorkerId(res.getInt(6));
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
	/** get supply workers from db */
	public static void getSupplyWorkers(ConnectionToClient client) {
		ArrayList<UserEntity> supplyWorkers= new ArrayList<>() ;
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE role_type=?;");
			ps.setString(1, "supplyWorker");
			ResultSet res = ps.executeQuery();
			while(res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(11), res.getString(9),
						res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));
				if (res.getString(10) != null) // region column
					user.setRegion(res.getString(10));
				supplyWorkers.add(user);
			}
			try {
				client.sendToClient(new Message(TaskType.ReceiveSupplyWorkersFromServer, supplyWorkers)); // finally send the entity
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	/*-----------------------------------UPDAET--------------------------------*/
	public static void updateMachineMinAmount(MachineEntity obj, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			MachineEntity machine =obj;
			PreparedStatement ps=con.prepareStatement("UPDATE ekrut.machines SET min_amount=(?) WHERE machine_id=(?);");{
			ps.setInt(1, machine.getMinamount());
			ps.setInt(2,  machine.machineId);
			ps.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
    /** updateItemsInMachineUpdate method : 
     * input - array list  of ItemInMachineEntity can update the fields of : 
     *  int currentAmount,Call_Status callStatus,int timeUnderMin */
	public static void updateItemsInMachineUpdate(ArrayList<ItemInMachineEntity> itemsInMachine,ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			for (ItemInMachineEntity item : itemsInMachine) {
				PreparedStatement ps=con.prepareStatement("UPDATE ekrut.item_in_machine SET current_amount=(?), call_status=(?), "
						+ "worker_id=(?),times_under_min=(?) WHERE  machine_id=(?) AND item_id=(?) ;");
				ps.setInt(1, item.getCurrentAmount());
				ps.setString(2, item.getCallStatus().toString());
				ps.setInt(3, item.getWorkerId());
				ps.setInt(4, item.getTimeUnderMin());
				ps.setInt(5, item.getMachineId());
				ps.setInt(6, item.getId());
				
				ps.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


}
