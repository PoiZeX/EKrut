package controllerDb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.Message;
import common.TaskType;
import entity.ItemEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;
/***
 * The ItemDBController class is responsible for handling database related operations related to items.
 * It retrieves images and information of all items from database, and sends it to the client
 */
public class ItemDBController {
	
/***
 *This method is responsible for sending images and information of all items to the client
 *@param client the client that requested to receive the items
*/
	public static void sendImgToClient(ConnectionToClient client) {
		Statement stmt;
		ItemEntity itemEntity;

		try {
			if (MySqlClass.getConnection() == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM items;");
			while (rs.next()) 
			{
				
				/*        1          2             3                4                
				 * (int item_id, String name, double price, String item_img_name)*/
				itemEntity = new ItemEntity(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getString(4));
		
				String LocalfilePath= "../EKrut_Server/src/styles/products/"+itemEntity.getItemImg().getImgName();
				  try{
					  	  
					      File newFile = new File (LocalfilePath);	
					      byte [] mybytearray  = new byte [(int)newFile.length()];
					      FileInputStream fis = new FileInputStream(newFile);
					      BufferedInputStream bis = new BufferedInputStream(fis);
					      itemEntity.getItemImg().initArray(mybytearray.length);
					      itemEntity.getItemImg().setSize(mybytearray.length);
					      bis.read(itemEntity.getItemImg().getMybytearray(),0,mybytearray.length);
					      client.sendToClient(new Message(TaskType.ReceiveItemsFromServer, itemEntity)); // finally send the entity
					    } 
					catch (Exception e) {System.out.println("Error send item to Client");}
			 }
			rs.close();
		} catch (SQLException e) {e.printStackTrace();}
	}
}

