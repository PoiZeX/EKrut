package controllerDb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import common.CommonFunctions;
import common.Message;
import common.TaskType;
import entity.ItemEntity;
import entity.SupplyReportEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/***
 * The ItemDBController class is responsible for handling database related
 * operations related to items. It retrieves images and information of all items
 * from database, and sends it to the client
 */
public class ItemDBController {

	/***
	 * This method is responsible for sending images and information of all items to
	 * the client
	 * 
	 * @param client the client that requested to receive the items
	 */
	public static void sendImgToClient(ConnectionToClient client) {
		Statement stmt;
		ItemEntity itemEntity;
		try {
			if (MySqlClass.getConnection() == null)
				return;
			ArrayList<ItemEntity> itemEntitys = new ArrayList<>();
			stmt = MySqlClass.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM items;");
			while (rs.next()) {
				InputStream input = null;
				/*
				 * 1 2 3 4 (int item_id, String name, double price, String item_img_name)
				 */
				itemEntity = new ItemEntity(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getString(4));
				try {
					input = ItemDBController.class.getClass().getResourceAsStream("/styles/products/" + itemEntity.getItemImg().getImgName());
				} catch (Exception e) {}
				byte[] mybytearray = new byte[(int) input.available()];
				BufferedInputStream bis = new BufferedInputStream(input);
				itemEntity.getItemImg().initArray(mybytearray.length);
				itemEntity.getItemImg().setSize(mybytearray.length);
				bis.read(itemEntity.getItemImg().getMybytearray(), 0, mybytearray.length);
				itemEntitys.add(itemEntity);
				bis.close();
			}
			client.sendToClient(new Message(TaskType.ReceiveItemsFromServer, itemEntitys)); // finally send the entity
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/***
	 * get all the items names by their ids
	 * 
	 * @param itemsID
	 * @return
	 */
	public static ArrayList<String> getAllItemsNameById(ArrayList<Integer> itemsID) {
		ArrayList<String> itemsNames = new ArrayList<>();
		try {
			Connection con = MySqlClass.getConnection();
			if (con == null)
				return itemsNames;
			for (int item_id : itemsID) {
				String query = "SELECT name FROM items " + "WHERE item_id = ?";
				PreparedStatement ps = con.prepareStatement(query);
				ps.setInt(1, item_id);
				ResultSet res = ps.executeQuery();
				if (res.next()) {
					itemsNames.add(res.getString(1));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return itemsNames;
	}
}
