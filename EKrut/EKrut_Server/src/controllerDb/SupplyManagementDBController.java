package controllerDb;

import java.awt.ItemSelectable;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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

public class SupplyManagementDBController {
	private static boolean pros = false;
	private static Connection conn = MySqlClass.getConnection();

	/**
	 * get machine minimum amount
	 * 
	 * @param machineId
	 * @return
	 */
	public static int getMachineMinAmount(int machineId) {
		int minAmount = -1;

		try {
			if (conn == null)
				return minAmount;
			PreparedStatement ps1 = conn.prepareStatement(
					"SELECT ekrut.machines.min_amount FROM ekrut.machines WHERE machines.machine_id=(?);");
			ps1.setInt(1, machineId);
			ResultSet res1;

			res1 = ps1.executeQuery();

			if (res1.next())
				minAmount = (res1.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return minAmount;
	}

	/*----------------------------------GETDATA----------------------------*/

	/**
	 * get machine items according to minimum amount
	 * 
	 * @param machineId
	 * @param client
	 */
	public static void getMachineItemsWithMinAmount(int machineId, ConnectionToClient client) {
		PreparedStatement ps;
		try {
			int minAmount = getMachineMinAmount(machineId);
			ps = conn.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.item_id, ekrut.items.name "
					+ " FROM  ekrut.item_in_machine, ekrut.items"
					+ " WHERE item_in_machine.machine_id=? AND (item_in_machine.current_amount < ? OR  item_in_machine.call_status!=?) AND item_in_machine.item_id=items.item_id ;");
			ps.setInt(1, machineId);
			ps.setInt(2, minAmount);
			ps.setString(3, ItemInMachineEntity.Call_Status.NotOpened.toString());

			handleGetItems(ps.executeQuery(), client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * handle the process of items and send to client
	 * 
	 * @param res
	 * @param client
	 */
	private static void handleGetItems(ResultSet res, ConnectionToClient client) {
		ItemInMachineEntity item;

		ArrayList<ItemInMachineEntity> itemsInMachine = new ArrayList<ItemInMachineEntity>();
		try {
			while (res.next()) {
				// 1 2 3 4 5 6 7 8 9 10
				// machine_id, item_id, current_amount, call_status, times_under_min, worker_id,
				// item_id, name, price, item_img_name

				if (res.getMetaData().getColumnCount() >= 9) {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6),
							res.getString(8), res.getDouble(9), res.getString(10));

					String LocalfilePath = "../EKrut_Server/src/styles/products/" + item.getItemImg().getImgName();
					File newFile = new File(LocalfilePath);
					byte[] mybytearray = new byte[(int) newFile.length()];
					FileInputStream fis = new FileInputStream(newFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					item.getItemImg().initArray(mybytearray.length);
					item.getItemImg().setSize(mybytearray.length);
					bis.read(item.getItemImg().getMybytearray(), 0, mybytearray.length);
				}
				// (machineId, item_id, currentAmount, callStatus, timeUnderMin, workerId, name,
				// price, item_img_nam)
				else {
					item = new ItemInMachineEntity(res.getInt(1), res.getInt(2), res.getInt(3),
							ItemInMachineEntity.Call_Status.valueOf(res.getString(4)), res.getInt(5), res.getInt(6),
							res.getString(8), 0, "");
				}

				itemsInMachine.add(item);
			}

			client.sendToClient(new Message(TaskType.ReceiveItemsInMachine, itemsInMachine)); // finally send the

		} catch (Exception e) {
			e.printStackTrace();
		}
//		String LocalfilePath= "../EKrut_Server/src/styles/products/"+itemEntity.getItemImg().getImgName();
//		  try{
//			  	  
//			      File newFile = new File (LocalfilePath);	
//			      byte [] mybytearray  = new byte [(int)newFile.length()];
//			      FileInputStream fis = new FileInputStream(newFile);
//			      BufferedInputStream bis = new BufferedInputStream(fis);
//			      itemEntity.getItemImg().initArray(mybytearray.length);
//			      itemEntity.getItemImg().setSize(mybytearray.length);
//			      bis.read(itemEntity.getItemImg().getMybytearray(),0,mybytearray.length);
//			      client.sendToClient(new Message(TaskType.ReceiveItemsFromServer, itemEntity)); // finally send the entity
//			    } 
//			catch (Exception e) {System.out.println("Error send item to Client");}
//	 }
	}

	/**
	 * get all machine items by machine id
	 * 
	 * @param machineId
	 * @param client
	 */
	public static void getMachineItems(int machineId, ConnectionToClient client) {
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement(
					"SELECT  ekrut.item_in_machine.*, ekrut.items.* " + "FROM  ekrut.item_in_machine, ekrut.items"
							+ " WHERE item_in_machine.machine_id=?  AND item_in_machine.item_id=items.item_id;");
			ps.setInt(1, machineId);
			handleGetItems(ps.executeQuery(), client);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * get items which call status = processed
	 * 
	 * @param arr
	 * @param client
	 */
	public static void getProcessedMachineItems(int[] arr, ConnectionToClient client) {
		ItemInMachineEntity item;
		int machineId = arr[0];
		int userId = arr[1];
		int minAmount = -1;
		ArrayList<ItemInMachineEntity> itemsInMachine = new ArrayList<ItemInMachineEntity>();
		try {
			minAmount = getMachineMinAmount(machineId);

			PreparedStatement ps = conn.prepareStatement("SELECT  ekrut.item_in_machine.*, ekrut.items.name "
					+ " FROM  ekrut.item_in_machine, ekrut.items"
					+ " WHERE item_in_machine.machine_id=(?) AND  item_in_machine.call_status=(?) AND item_in_machine.item_id=items.item_id AND item_in_machine.worker_id=(?)  ;");
			ps.setInt(1, machineId);
			ps.setString(2, ItemInMachineEntity.Call_Status.Processed.toString());
			ps.setInt(3, userId);
			handleGetItems(ps.executeQuery(), client);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get supply workers from db
	 * 
	 * @param client
	 */
	public static void getSupplyWorkers(ConnectionToClient client) {
		ArrayList<UserEntity> supplyWorkers = new ArrayList<>();
		UserEntity user = new UserEntity();
		try {
			if (MySqlClass.getConnection() == null)
				return;
			Connection conn = MySqlClass.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ekrut.users WHERE role_type=?;");
			ps.setString(1, "supplyWorker");

			ResultSet res = ps.executeQuery();
			while (res.next()) {
				user = new UserEntity(res.getString(2), res.getString(3), res.getString(4), res.getString(5),
						res.getString(6), res.getString(7), res.getString(8), res.getString(9), res.getString(10),
						res.getString(11), res.getBoolean(12), res.getBoolean(13));
				user.setId(res.getInt(1));

				supplyWorkers.add(user);
			}
			try {
				client.sendToClient(new Message(TaskType.ReceiveSupplyWorkersFromServer, supplyWorkers)); // finally
																											// send the
																											// entity
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/*-----------------------------------UPDATE--------------------------------*/
	/**
	 * 
	 * @param obj
	 * @param client
	 */
	public static void updateMachineMinAmount(MachineEntity obj, ConnectionToClient client) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			MachineEntity machine = obj;
			PreparedStatement ps = con
					.prepareStatement("UPDATE ekrut.machines SET min_amount=(?) WHERE machine_id=(?);");
			{
				ps.setInt(1, machine.getMinamount());
				ps.setInt(2, machine.machineId);
				ps.executeUpdate();

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * updateItemsInMachineUpdate method : input - array list of ItemInMachineEntity
	 * can update the fields of : int currentAmount,Call_Status callStatus,int
	 * timeUnderMin
	 * 
	 * @param itemsInMachine
	 * @param client
	 */
	private static Connection con = MySqlClass.getConnection();

	/**
	 * Update items in machine with roll back option if update failed
	 * 
	 * @param itemsInMachine
	 * @param client
	 */
	public static void updateItemsInMachine(ArrayList<ItemInMachineEntity> itemsInMachine, ConnectionToClient client) {
		try {
			ItemInMachineEntity[] itemsArray = new ItemInMachineEntity[itemsInMachine.size()];
			// ItemInMachineEntity[itemsInMachine.size()]);
			itemsInMachine.toArray(itemsArray);

			int[] originalAmount = new int[itemsArray.length]; // amount of items

			Statement stmt;
			if (con == null)
				return;
			stmt = con.createStatement();

			ItemInMachineEntity item = null;
			for (int i = 0; i < itemsArray.length; i++) {
				originalAmount[i] = getItemInMachineQuantity(itemsArray[i]);
				item = updateSingleItemInMachine(itemsArray[i]);
				if (item != null || originalAmount[i] == -1) {
					RollBack(originalAmount, itemsArray, i);
					client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));
					return;
				}
			}
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Roll back when update failed
	 * 
	 * @param originalAmount
	 * @param itemsInMachine
	 * @param failedIndex
	 */
	private static void RollBack(int[] originalAmount, ItemInMachineEntity[] itemsInMachine, int failedIndex) {
		for (int i = failedIndex; i >= 0; i--) {
			itemsInMachine[i].setCurrentAmount(originalAmount[i]);
			updateSingleItemInMachine(itemsInMachine[i]);
		}
	}

	/**
	 * get quantity of item
	 * 
	 * @param item
	 * @return
	 */
	private static int getItemInMachineQuantity(ItemInMachineEntity item) {
		Statement stmt;
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return -1;

			stmt = MySqlClass.getConnection().createStatement();
			PreparedStatement ps = con
					.prepareStatement("SELECT current_amount FROM item_in_machine WHERE machine_id=? and item_id=?;");

			ps.setInt(1, item.getMachineId());
			ps.setInt(2, item.getItemId());

			ResultSet res = ps.executeQuery();
			if (res.next())
				return res.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;

	}

	/**
	 * update single item entity
	 * @param item
	 * @return
	 */
	public static ItemInMachineEntity updateSingleItemInMachine(ItemInMachineEntity item) {

		try {
			PreparedStatement ps = con
					.prepareStatement("UPDATE ekrut.item_in_machine SET current_amount=?, call_status=?, "
							+ "worker_id=?,times_under_min=? WHERE  machine_id=? AND item_id=?;");
			ps.setInt(1, item.getCurrentAmount());
			ps.setString(2, item.getCallStatus().toString());
			ps.setInt(3, item.getWorkerId());
			ps.setInt(4, item.getTimeUnderMin());
			ps.setInt(5, item.getMachineId());
			ps.setInt(6, item.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			return item;
		}
		return null;
	}

	/**
	 * Update items in machine with roll back option if update failed
	 * 
	 * @param itemsInMachine
	 * @param client
	 * @throws IOException 
	 */
	public static void decreaseItemsAmountInMachine(Map<ItemInMachineEntity, Integer> itemsInMachine,
			ConnectionToClient client) throws IOException {
		try {
			ArrayList<ItemInMachineEntity> rollBackItems = new ArrayList<ItemInMachineEntity>();

			for (ItemInMachineEntity item : itemsInMachine.keySet()) {
				ItemInMachineEntity updatedItem = null;
				updatedItem = decreaseSingleItemAmount(item, itemsInMachine.get(item));
				if (updatedItem != null) { // has error
					increaseRollBack(itemsInMachine, rollBackItems);
					client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));
					return;
				}
				rollBackItems.add(item);

			}

			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));

		} catch (Exception e) {
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, false));

			e.printStackTrace();
		}
	}

	/**
	 * decrease amount of single item
	 * @param item
	 * @param amountToDecrease
	 * @return
	 */
	public static ItemInMachineEntity decreaseSingleItemAmount(ItemInMachineEntity item, int amountToDecrease) {

		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE item_in_machine SET current_amount=current_amount-? WHERE machine_id=? AND item_id=?;");
			ps.setInt(1, amountToDecrease);
			ps.setInt(2, item.getMachineId());
			ps.setInt(3, item.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			return item;
		}
		return null;
	}

	/**
	 * increase the amount which decreased earlier. Update is NOT good.
	 * @param original
	 * @param successItems
	 */
	private static void increaseRollBack(Map<ItemInMachineEntity, Integer> original,
			ArrayList<ItemInMachineEntity> successItems) {
		for (ItemInMachineEntity item : successItems) {
			increaseSingleItemAmount(item, original.get(item));
		}
	}

	/**
	 * increase amount for single items
	 * @param item
	 * @param amountToIncrease
	 */
	private static void increaseSingleItemAmount(ItemInMachineEntity item, int amountToIncrease) {
		try {
			PreparedStatement ps = con.prepareStatement(
					"UPDATE item_in_machine SET current_amount=current_amount+? WHERE machine_id=? AND item_id=?;");
			ps.setInt(1, amountToIncrease);
			ps.setInt(2, item.getMachineId());
			ps.setInt(3, item.getId());

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param arr
	 * @param client
	 */
	public static void getMachinesFromDB(String[] arr, ConnectionToClient client) {
		try {
			ArrayList<MachineEntity> res = new ArrayList<>();

			if (arr[0].equals("0")) {
				res = getMachineListForSupplyRegionMFromDB(arr[1]);
				client.sendToClient(new Message(TaskType.InitMachinesInRegions, res));
			} else {
				res = getMachineListForSupplyUpdateFromDB(Integer.parseInt(arr[1]));
				client.sendToClient(new Message(TaskType.InitMachinesSupplyUpdate, res));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * get machines list for worker where therse a call open for him
	 * 
	 * @param workerId
	 * @return
	 */
	private static ArrayList<MachineEntity> getMachineListForSupplyUpdateFromDB(int workerId) {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (MySqlClass.getConnection() == null)
				return machines;
			Connection conn = MySqlClass.getConnection();

			PreparedStatement ps = conn.prepareStatement("SELECT  ekrut.machines.* "
					+ "FROM ekrut.machines, ekrut.item_in_machine "
					+ "WHERE item_in_machine.call_status=(?) AND worker_id=(?) AND machines.machine_id=item_in_machine.machine_id ;");
			ps.setString(1, ItemInMachineEntity.Call_Status.Processed.toString());
			ps.setInt(2, workerId);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				machines.add(new MachineEntity(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4),
						res.getInt(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return machines;
	}

	/**
	 * get machines list for worker where there is a call open for him
	 * 
	 * @param regionName
	 * @return
	 */
	private static ArrayList<MachineEntity> getMachineListForSupplyRegionMFromDB(String regionName) {
		ArrayList<MachineEntity> machines = new ArrayList<MachineEntity>();
		try {
			if (MySqlClass.getConnection() == null)
				return machines;
			Connection conn = MySqlClass.getConnection();

			PreparedStatement ps = conn.prepareStatement(
					"SELECT  ekrut.machines.* " + "FROM ekrut.machines " + "WHERE machines.region_name=(?) ;");
			ps.setString(1, regionName);
			ResultSet res = ps.executeQuery();
			while (res.next()) {
				machines.add(new MachineEntity(res.getInt(1), res.getString(2), res.getInt(3), res.getString(4),
						res.getInt(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return machines;
	}

	public static void increaseItemsUnderMin(ArrayList<int[]> machineAnditemsId, ConnectionToClient client) {
		try {
			if (MySqlClass.getConnection() == null)
				return;
			PreparedStatement ps = conn.prepareStatement(
					"UPDATE item_in_machine SET times_under_min=times_under_min+1 WHERE machine_id=? AND item_id=?");
			for (int[] machineItemId : machineAnditemsId) {
				ps.setInt(1, machineItemId[0]); // machine
				ps.setInt(2, machineItemId[1]); // item
				ps.executeUpdate();
			}
			client.sendToClient(new Message(TaskType.ReviewOrderServerAnswer, true));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
