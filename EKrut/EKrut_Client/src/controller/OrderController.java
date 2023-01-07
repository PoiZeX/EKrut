package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.RolesEnum;
import common.SaleType;
import common.TaskType;
import controllerGui.HostClientController;
import entity.ItemEntity;
import entity.ItemInCartEntity;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.OrderEntity;
import entity.SaleEntity;
import javafx.scene.image.Image;
import utils.AppConfig;

/**
 * The class handles the WHOLE order process
 *
 */
public class OrderController {
	/*
	 * Get all items in machine Get items description Get discounts for current time
	 * and region Add items to cart [V] Change quantity (remove specially) [V] Make
	 * a review Validate item quantity (server side before insert) Insert order
	 * 
	 * 
	 */

	// private static HashMap<Integer, ItemInMachineEntity> cart = new HashMap<>();
	// // itemId and itemEntity
	private static LinkedHashMap<ItemInMachineEntity, Integer> itemsInCartList = new LinkedHashMap<>();
	private static double discounts = 1.0; // in double 0.3 is 30%
	private static OrderEntity currentOrder;
	private static MachineEntity currentMachine = CommonData.getCurrentMachine();
	private static LinkedHashMap<String, ItemInMachineEntity> itemsList = new LinkedHashMap<>(); // for images i think(???)
	private static ArrayList<SaleEntity> activeSales = null;
	private static boolean isSaleActive =false;
	private static boolean onePlusOneSaleExist =false;
	private static boolean percentageSaleExit=false;
	private static ClientController chat = HostClientController.chat; // define the chat for th
	private static boolean isDataRecived = false;
	private static Object data; 
	
	public OrderController() {

	}
	/**
	 * local function to handle sending and waiting for answer
	 * 
	 * @param msg
	 * @throws Exception
	 */
	private void waitOn(Message msg) throws Exception {
		isDataRecived = false;
		chat.acceptObj(msg);
		while (!isDataRecived)
			Thread.sleep(100);
	}
	public static void getDataFromServer(Object dataRecived) {
		data = dataRecived;
		isDataRecived = true;
	}

	/**
	 * Clear all static data. Be careful using it!
	 */
	public static void clearAll() {
		itemsInCartList.clear();
		discounts = 1.0;
		currentOrder = null;
		itemsList.clear();
	}

	/**
	 * get items list
	 * 
	 * @return
	 */
	public static LinkedHashMap<String, ItemInMachineEntity> getItemsList() {
		return itemsList;
	}

	/**
	 * put an item in itemslist
	 * 
	 * @param name
	 * @param entity
	 */
	public static void putItemInList(ItemInMachineEntity entity) {
		itemsList.put(entity.getName(), entity);
	}

	public static void clearItemsList() {
		if (!itemsList.isEmpty()) {
			itemsList.clear();
		}
	}

	public static Image getImageOfItem(String name) {
		return OrderController.itemsList.get(name).getItemImage();
	}

	public static OrderEntity getCurrentOrder() {
		return currentOrder;
	}

	public static void setCurrentOrder(int user_id, String supplyMethod) {
		if (currentOrder == null)
			currentOrder = new OrderEntity(user_id, supplyMethod);
	}



	/**
	 * return the amount of a specific item in the cart
	 * 
	 * @param item
	 * @return amount
	 */
	private static int getAmount(ItemInMachineEntity item) {
		return itemsInCartList.get(item);
	}

	/**
	 * returns the amount of items in the cart
	 * 
	 * @return cartSize
	 */
	public static int getCartSize() {
		int cartSize = 0;
		for (ItemInMachineEntity item : itemsInCartList.keySet())
			cartSize += (getAmount(item));
		return cartSize;
	}

	public static int getItemAmount(ItemInMachineEntity item) {
		if (!itemsInCartList.containsKey(item))
			return 0;
		return itemsInCartList.get(item);
	}

	/**
	 * returns the total price of the cart
	 * 
	 * @return totalPrice;
	 */
	public static int getTotalPrice() {
		int totalPrice = 0;
		for (ItemInMachineEntity item : itemsInCartList.keySet())
			totalPrice += (getAmount(item) * item.getPrice());
		return totalPrice;
	}

	/**
	 * return the cart as list of items
	 * 
	 * @return
	 */
	public static LinkedHashMap<ItemInMachineEntity, Integer> getCart() {
		return itemsInCartList;
	}

	/**
	 * Add item to cart
	 * 
	 * @param item
	 * @return true if the item added successfully
	 */
	public static boolean addItemToCart(ItemInMachineEntity item, int amount) {
		if (itemsInCartList.containsKey(item))
			return false;
		itemsInCartList.put(item, amount);
		return true;
	}

	/**
	 * Change the quantity of item in cart, if Quantity equal/less than zero the
	 * item will be removed
	 * 
	 * @param itemId
	 * @param newQuantity
	 * @return true if success
	 */
	public static boolean changeItemQuantity(ItemInMachineEntity item, int newQuantity) {
		if (item == null)
			return false;
		// need to remove?
		if (newQuantity <= 0)
			return removeItem(item);

		// set new positive quantity
		itemsInCartList.put(item, newQuantity);
		return true;
	}

	/**
	 * Local method to remove item from cart
	 * 
	 * @param item
	 * @return
	 */
	private static boolean removeItem(ItemInMachineEntity item) {
		if (!itemsInCartList.containsKey(item))
			return false;
		itemsInCartList.remove(item);
		return true;
	}
//----------------------------------------------------------------------Sales 
	public static boolean isActiveSale() {
		if ( activeSales!=null && currentOrder!=null) {
			if(!activeSales.isEmpty() && currentOrder.getMachine_id()!=-1) {
				isSaleActive=true;
		}
		}
		return isSaleActive;
	}
	
	public static ArrayList<SaleEntity> getActiveSales() {
		return activeSales;
	}
	public static void getActiveSalesFromDB() throws Exception {
		while(currentOrder==null)
			Thread.sleep(100);
		if (NavigationStoreController.connectedUser.getRole_type().equals(RolesEnum.member)&& currentOrder.getMachine_id()!=-1) { 
			isDataRecived=false;
			chat.acceptObj(new Message(TaskType.RequestActiveSales, NavigationStoreController.connectedUser.getRegion()));
		while (!isDataRecived)
			Thread.sleep(100);}
	
	}


	public static void setActiveSales(ArrayList<SaleEntity> activesales) {
		
			if (activeSales == null ) {
				activeSales = new ArrayList<>();
			}
			else if (!activeSales.isEmpty())
				activeSales.clear();
			activeSales.addAll(activesales);
			for(SaleEntity sale : activeSales) {
				if (sale.getSaleType().equals("1+1"))
					onePlusOneSaleExist=true;
				if (!sale.getSaleType().equals(SaleType.onePlusOne.getName()))
					percentageSaleExit=true;
					}
			getTotalDiscountsPercentage();
			isDataRecived=true;
	}
	
	public static boolean isOnePlusOneSaleExist() {
		return onePlusOneSaleExist;
	}

	public static boolean isPercentageSaleExit() {
		return percentageSaleExit;
	}
	/**
	 * Get total discount in NIS
	 * 
	 * @return
	 */
	public static double getTotalDiscounts() {
		return getTotalPrice() * discounts / 100;
	}

	/**
	 * Get total discount in double 
	 * 
	 * @return
	 */
	public static double getTotalDiscountsPercentage() {
		if(activeSales==null)
			return getTotalPrice() ;
		for(SaleEntity sale : activeSales) 
			addDiscount(SaleType.getSaleType(sale.getSaleType()).getPrecentage());
		return discounts;
	}

	/**
	 * Add discount in %
	 * 
	 * @param discount
	 */
	public static void addDiscount(double discount) {
		discounts = discounts*discount;
	}
	public static double getItemPriceAfterDiscounts(double itemPrice) {
		if(activeSales==null)
			return itemPrice ;
		for(SaleEntity sale : activeSales) 
			itemPrice= itemPrice*(1-SaleType.getSaleType(sale.getSaleType()).getPrecentage());
		
				
		return itemPrice;
	}
	public static double getPriceAfterDiscounts() {
		if(activeSales==null)
			return getTotalPrice() ;
				
		return getTotalPrice() - getTotalDiscounts();
	}

	public static MachineEntity getCurrentMachine() {
		return currentMachine;
	}

	public static void setCurrentMachine(MachineEntity currentMachine) {
		OrderController.currentMachine = currentMachine;
	}

}
