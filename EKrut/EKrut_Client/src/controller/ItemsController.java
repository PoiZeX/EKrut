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
	private static ClientController chat = HostClientController.getChat(); // define the chat for the controller
	private static ItemsController instance=null;
	public static ItemsController getInstance() {
		if (instance==null)
			instance= new ItemsController();
		return instance;
	}
	/* request the DB to load the items */
	public void requestItemsFromServer() {
		chat.acceptObj(new Message(TaskType.RequestItemsFromServer, null));
	}

	/* add the item to array list */
	public void getItemsFromServer(ArrayList<ItemEntity> items) {
		for (ItemEntity item : items) {
			item.setImg_relative_path(AppConfig.PRODUCTS_PATH_CLIENT);
			InputStream fis = new ByteArrayInputStream(item.getItemImg().mybytearray);
			Image fileImg = new Image(fis);
			item.setItemImage(fileImg);
			allItems.add(item);
		}
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
