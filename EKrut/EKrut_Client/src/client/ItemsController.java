package client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import common.Message;
import common.TaskType;
import controllerGui.HostClientController;
import entity.ImgEntity;
import entity.ItemEntity;
import utils.AppConfig;

public class ItemsController {

	private static ArrayList<ItemEntity> items = new ArrayList<>();
	private static ClientController chat = HostClientController.chat; // define the chat for the controller

	/* request the DB to load the items */
	public static void requestItemsFromServer() {
		chat.acceptObj(new Message(TaskType.RequestItemsFromServer, null));
	}

	/* add the item to array list */
	public static void getItemsFromServer(ItemEntity item) {
		item.setImg_relative_path(AppConfig.PRODUCTS_PATH_CLIENT);
		// System.out.println(item.toString());
		items.add(item);
		convertStreamToImg(item.getItemImg());
	}

	/* convert bytes to image and saves the image */
	private static void convertStreamToImg(ImgEntity img) {
		int fileSize = img.getSize();

		byte[] mybytearray = new byte[fileSize];

		try {
			String LocalfilePath = AppConfig.PRODUCTS_PATH_CLIENT + img.getImgName();
			File newFile = new File(LocalfilePath);
			// save in folder
			FileOutputStream fos = new FileOutputStream(newFile.getPath());
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(img.mybytearray, 0, mybytearray.length);
			bos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
