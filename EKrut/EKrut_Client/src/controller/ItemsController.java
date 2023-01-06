package controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import client.ClientController;
import common.Message;
import common.TaskType;
import controllerGui.HostClientController;
import entity.ImgEntity;
import entity.ItemEntity;
import javafx.scene.image.Image;
import utils.AppConfig;

public class ItemsController {

	public static ArrayList<ItemEntity> allItems = new ArrayList<>();
	private static ClientController chat = HostClientController.chat; // define the chat for the controller
	
	/* request the DB to load the items */
	public static void requestItemsFromServer() {
		chat.acceptObj(new Message(TaskType.RequestItemsFromServer, null));
	}

	/* add the item to array list */
	public static void getItemsFromServer(ItemEntity item) {
		item.setImg_relative_path(AppConfig.PRODUCTS_PATH_CLIENT);
		InputStream fis = new ByteArrayInputStream(item.getItemImg().mybytearray);
		Image fileImg = new Image(fis);
		item.setItemImage(fileImg);
		allItems.add(item);
//		convertStreamToImg(item.getItemImg());
	}
	
	

	/* convert bytes to image and saves the image */
	@SuppressWarnings("unused")
	private static void convertStreamToImg(ImgEntity img) {
//		int fileSize = img.getSize();
//		byte[] mybytearray = new byte[fileSize];
//
//		try {
//			String LocalfilePath = AppConfig.PRODUCTS_PATH_CLIENT + img.getImgName();
//			File newFile = new File(LocalfilePath);
//
//			// save in folder
//			FileOutputStream fos = new FileOutputStream(newFile.getPath());
//			BufferedOutputStream bos = new BufferedOutputStream(fos);
//			bos.write(img.mybytearray, 0, mybytearray.length);
//			
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	
	
	/**
	 * Clean products directory
	 */
	public static void deleteAllItemsInDir() {
		for (File file : new File(AppConfig.PRODUCTS_PATH_CLIENT).listFiles())
			if (!file.isDirectory())
				file.delete();
	}
}
