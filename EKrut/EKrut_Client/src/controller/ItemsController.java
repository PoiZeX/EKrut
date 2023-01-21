package controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import client.ClientController;
import common.Message;
import controllerGui.HostClientController;
import entity.ItemEntity;
import entity.ItemInMachineEntity;
import enums.TaskType;
import javafx.scene.image.Image;
import utils.AppConfig;

/**
 * Controller for logic handler of items without GUI
 */
public class ItemsController {

	public static ArrayList<ItemEntity> allItems = new ArrayList<>();
	private static ClientController chat = HostClientController.getChat(); // define the chat for the controller
	private static ItemsController instance = null;

	/**
	 * Singleton implementation
	 * 
	 * @return instance of current controller
	 */
	public static ItemsController getInstance() {
		if (instance == null)
			instance = new ItemsController();
		return instance;
	}

	/**
	 * request the DB to load the items
	 */
	public void requestItemsFromServer() {
		chat.acceptObj(new Message(TaskType.RequestItemsFromServer, null));
	}

	/**
	 * add the item to array list
	 * @param items ArrayList of item entities
	 */
	public void getItemsFromServer(ArrayList<ItemEntity> items) {
		for (ItemEntity item : items) {
			item.setImg_relative_path(AppConfig.PRODUCTS_PATH_CLIENT);
			convertImage(item);
			allItems.add(item);
		}
	}

	/**
	* Convert image from bytes to image
	* @param item itemEntity to get image from, to convert 
	*/
	private void convertImage(ItemEntity item) {
		InputStream fis = new ByteArrayInputStream(item.getItemImg().mybytearray);
		Image fileImg = new Image(fis);
		item.setItemImage(fileImg);
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
