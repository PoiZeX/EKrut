package client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import common.MessageType;
import controllerGui.HostClientController;
import entity.ImgEntity;
import entity.ItemEntity;


public class ItemsController {
	
	private static ArrayList<ItemEntity> items= new ArrayList<>();
	private static ClientController chat = HostClientController.chat; // define the chat for the controller
	
	public static void requestItemsFromServer() {
		chat.acceptObj(MessageType.LoadItems);	
	}
//	private void save() {
//	
//		 // get all entities to ArrayList from DB
//	}
	/*convert byts to img and saves the imge*/
	public static void getItemsFromServer(ItemEntity item) {
		item.setImg_relative_path("/EKrut_Client/src/styles/products/");
		items.add(item);
		convertStreamToImg(item.getItemImg());
		
	}
	
	private static boolean convertStreamToImg(ImgEntity img) {
		 int fileSize =img.getSize(); 
		 System.out.println(""+fileSize);
		 img.setImgName("/EKrut_Client/src/styles/products/"+img.getImgName());
		 File newFile= new File("/EKrut_Client/src/styles/products/"+img.getImgName());
		 byte[] mybytearray = new byte[fileSize];
		 
		try {
			 BufferedInputStream bis = new BufferedInputStream(new FileInputStream(img.getImgName()));
			
			 bis.read(mybytearray, 0, fileSize);
			 FileOutputStream fos = new FileOutputStream(newFile.getName());
			 BufferedOutputStream bos = new BufferedOutputStream(fos);
			 bos.write(mybytearray, 0, mybytearray.length);
			 bos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
