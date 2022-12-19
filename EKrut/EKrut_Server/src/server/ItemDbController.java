package server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import entity.ImgEntity;
import entity.ItemEntity;
import mysql.MySqlClass;
import ocsf.server.ConnectionToClient;

public class ItemDbController {
	
	/*send to client item Object */
	protected static void sendImgToClient(ConnectionToClient client) {
		Statement stmt;
		ItemEntity itemEntity;

		try {
			if (MySqlClass.getConnection() == null)
				return;

			stmt = MySqlClass.getConnection().createStatement();
			//get from DB
			ResultSet rs = stmt.executeQuery("SELECT * FROM items;");
			while (rs.next()) 
			{
				
				/*        1          2             3                4                   5                 6
				 * (int item_id, String name, double price, String manufacturer, String description, String item_img_name)*/
				itemEntity = new ItemEntity(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getString(4),rs.getString(5),rs.getString(6));
				//crate an image set it in the entity on the item and send it to client
				String LocalfilePath=itemEntity.getItem_img_name();
				
				  try{
					      File newFile = new File (LocalfilePath);
					      		      
					      byte [] mybytearray  = new byte [(int)newFile.length()];
					      FileInputStream fis = new FileInputStream(newFile);
					      BufferedInputStream bis = new BufferedInputStream(fis);			  
					      
					      itemEntity.getItemImg().initArray(mybytearray.length);
					      itemEntity.getItemImg().setSize(mybytearray.length);
					      
					      bis.read(itemEntity.getItemImg().getMybytearray(),0,mybytearray.length);
					      
					      
					    }
					catch (Exception e) {
						System.out.println("Error send (Files)msg) to Client");
					}
				try {
					client.sendToClient(itemEntity); // finally send the entity
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

