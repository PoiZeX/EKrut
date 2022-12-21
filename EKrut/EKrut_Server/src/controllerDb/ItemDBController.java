package controllerDb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import common.Message;
import common.TaskType;
import entity.ImgEntity;
import entity.ItemEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class ItemDBController {
	
	/*send to client item Object */
	public static void sendImgToClient(ConnectionToClient client) {
		Statement stmt;
		ItemEntity itemEntity;

		try {
			if (MySqlClass.getConnection() == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			//TODO get from DB the items -> next step will be to take it from the items in machine 
			ResultSet rs = stmt.executeQuery("SELECT * FROM items;");
			while (rs.next()) 
			{
				
				/*        1          2             3                4                   5                 6
				 * (int item_id, String name, double price, String manufacturer, String description, String item_img_name)*/
				itemEntity = new ItemEntity(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getString(4),rs.getString(5),rs.getString(6));
		
				String LocalfilePath= "../EKrut_Server/src/styles/products/"+itemEntity.getItemImg().getImgName();
				  try{
					  	  
					      File newFile = new File (LocalfilePath);	
					      byte [] mybytearray  = new byte [(int)newFile.length()];
					      FileInputStream fis = new FileInputStream(newFile);
					      BufferedInputStream bis = new BufferedInputStream(fis);
					      itemEntity.getItemImg().initArray(mybytearray.length);
					      itemEntity.getItemImg().setSize(mybytearray.length);
					      bis.read(itemEntity.getItemImg().getMybytearray(),0,mybytearray.length);
					      client.sendToClient(new Message(TaskType.RecieveItemsFromServer, itemEntity)); // finally send the entity
					    } 
					catch (Exception e) {System.out.println("Error send item to Client");}
			 }
			rs.close();
		} catch (SQLException e) {e.printStackTrace();}
	}
}

