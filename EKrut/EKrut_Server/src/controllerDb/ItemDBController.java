package controllerDb;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import common.Message;
import enums.TaskType;
import entity.ItemEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

/**
 * The Class ItemDBController.
 */
public class ItemDBController {

	/**
	 * Send img to client.
	 *
	 * @param client the client
	 */
	public void sendImgToClient(ConnectionToClient client) {
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
					input = this.getClass().getResourceAsStream("/products/" + itemEntity.getItemImg().getImgName());
					
				} catch (Exception e) {e.printStackTrace();}
				byte[] mybytearray = new byte[(int) input.available()];
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte [] buffer =new byte[1024];
				int n =0;
				while(-1!=(n=input.read(buffer))) {
					output.write(buffer,0,n);
				}
				byte [] imagesBytes=output.toByteArray();
				
				itemEntity.getItemImg().mybytearray=imagesBytes;
				
				
				//itemEntity.getItemImg().initArray(mybytearray.length);
				itemEntity.getItemImg().setSize(mybytearray.length);
			//	bis.read(itemEntity.getItemImg().getMybytearray(), 0, mybytearray.length);
				itemEntitys.add(itemEntity);
			//	bis.close();
			}
			client.sendToClient(new Message(TaskType.ReceiveItemsFromServer, itemEntitys)); // finally send the entity
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the all items name by id.
	 *
	 * @param itemsID the items ID
	 * @return the all items name by id
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
