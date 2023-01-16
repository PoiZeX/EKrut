package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.Message;
import common.RolesEnum;
import common.SaleType;
import common.ScreensNamesEnum;
import common.TaskType;
import controllerGui.HostClientController;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.OrderEntity;
import entity.SaleEntity;
import javafx.scene.image.Image;

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

	private static LinkedHashMap<ItemInMachineEntity, Integer> itemsInCartList = new LinkedHashMap<>();
	private static double discounts = 1.0; // in double 0.3 is 30%
	private static OrderEntity currentOrder;
	private static MachineEntity currentMachine = DataStore.getCurrentMachine();
	private static LinkedHashMap<String, ItemInMachineEntity> itemsList = new LinkedHashMap<>(); 																		
	private static ArrayList<SaleEntity> activeSales = null;
	private static boolean onePlusOneSaleExist = false;
	private static boolean percentageSaleExit = false;
	private static ClientController chat = HostClientController.getChat(); 
	private static boolean isDataReceived = false;
	public static boolean isFirstPurchaseDiscountApplied = false;
	private static Object data;
	public static boolean  isMember = NavigationStoreController.connectedUser.getRole_type() == RolesEnum.member ? true : false;


	public OrderController() {

	}

	/**
	 * Add a 20% discount for first purchase of a member
	 */
	public static void addMemberFirstPurchaseDiscount() {
		OrderController.isFirstPurchaseDiscountApplied = true;
	}

	/**
	 * Clear all static data. Be careful using it!
	 */
	public static boolean clearAll() {
		if (itemsList == null)
			return false;
		isFirstPurchaseDiscountApplied = false;
		if (itemsInCartList != null)
			itemsInCartList.clear();
		currentOrder = null;
		if (itemsList != null)
			itemsList.clear();
		return true;
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
	/**

	This method clears the itemsList.
	*/
	public static void clearItemsList() {
		if (!itemsList.isEmpty()) {
			itemsList.clear();
		}
	}
	/**

	This method returns the image of an item from itemsList by its name.
	@param name the name of the item
	@return the Image object of the item
	*/
	public static Image getImageOfItem(String name) {
		return OrderController.itemsList.get(name).getItemImage();
	}
	/**

	This method returns the current order.
	@return the current OrderEntity
	*/
	public static OrderEntity getCurrentOrder() {
		return currentOrder;
	}
	/**

	This method sets the current order by creating a new OrderEntity object with the given user ID and supply method.
	@param user_id the ID of the user who made the order
	@param supplyMethod the method of supply chosen by the user
	*/
	public static void setCurrentOrder(int user_id, String supplyMethod) {
		if (currentOrder == null)
			currentOrder = new OrderEntity(user_id, supplyMethod);
	}


//------------------------------------------------ cart
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
	/**

	Retrieves the amount of a specific item in the machine.
	@param item The item to retrieve the amount of.
	@return The amount of the item in the machine. Returns 0 if the item is not found in the machine.
	*/
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
	public static double getTotalPrice() {
		double totalPrice = 0;
		for (ItemInMachineEntity item : itemsInCartList.keySet())
			totalPrice += (getAmount(item) * item.getPrice());
		return totalPrice;
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
	
	/**
	 * return the cart as list of items
	 * 
	 * @return
	 */
	public static LinkedHashMap<ItemInMachineEntity, Integer> getCart() {
		return itemsInCartList;
	}


//----------------------------------------------------------------------Sales 
	/***
	 * returns if there are any active sales, 
	 * the active sales will not be null only if the user is a member
	 * @return
	 */
	public static boolean isActiveSale() {
		if (activeSales != null && currentOrder != null) {
			if (!activeSales.isEmpty() && !(currentOrder.getSupplyMethod().equals("Delivery"))) {
				return true;
			}
		}
		if(currentOrder != null && isFirstPurchaseDiscountApplied)
			return true;
		return false;
	}
	/**

	This method returns the list of active sales.
	@return an ArrayList of SaleEntity representing the active sales
	*/
	public static ArrayList<SaleEntity> getActiveSales() {
		return activeSales;
	}

	/**
	 * Get a string of all current sales types as string
	 * 
	 * @return
	 */
	public static String getActiveSalesTypeAsString() {
		StringBuilder sb = new StringBuilder();
		activeSales.forEach(sale -> sb.append(sale.getSaleType().toString() + " "));
		return sb.toString();
	}
	/***
	 * request from the data base all the active sales at the moment
	 */
	public static void getActiveSalesFromDB() {
		chat.acceptObj(new Message(TaskType.RequestActiveSales, NavigationStoreController.connectedUser.getRegion()));
		while (!isDataReceived) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/***
	 * get callers from the message handler when receiving active sales
	 * sets the following :
	 * boolean onePlusOneSaleExist, 		
	 * boolean percentageSaleExit, 
	 * ArrayList<SaleEntity> activeSales
	 * @param activesales
	 */
	public static void setActiveSales(ArrayList<SaleEntity> activesales) {
		onePlusOneSaleExist = false;
		percentageSaleExit = false;
		if (activeSales == null) {
			activeSales = new ArrayList<>();
		} else if (!activeSales.isEmpty())
			activeSales.clear();
		activeSales.addAll(activesales);
		for (SaleEntity sale : activeSales) {
			if (sale.getSaleType().equals("1+1"))
				onePlusOneSaleExist = true;
			if (!sale.getSaleType().equals(SaleType.onePlusOne.getName()))
				percentageSaleExit = true;
		}
		calculateDiscountsPercentage();
		isDataReceived = true;
	}
	/**

	This method returns whether a one plus one sale exist or not.
	@return a boolean indicating if a one plus one sale exist
	*/
	public static boolean isOnePlusOneSaleExist() {
		return onePlusOneSaleExist;
	}
	/**

	This method returns whether a percentage sale exist or not.
	@return a boolean indicating if a percentage sale exist
	*/
	public static boolean isPercentageSaleExit() {
		return percentageSaleExit;
	}
//-------------------------------------- discount and price calculation
	/**
	 * Get total discount in NIS 
	 * 
	 * @return 
	 */
	public static double getTotalDiscounts() {
		return getTotalPrice() * (1 - discounts);
	}

	/**
	 * Get total discount in double
	 * 
	 * @return
	 */
	public static void calculateDiscountsPercentage() {
		if (activeSales == null)
			return;
		discounts = 1;
		for (SaleEntity sale : activeSales)
			addDiscount(SaleType.getSaleType(sale.getSaleType()).getPrecentage());
	}
	/**

	Returns the current discounts percentage as a double.
	@return discounts percentage
	*/
	public static double getDiscountsPercentage() {
		return discounts;
	}

	/**
	 * Add discount in %
	 * 
	 * @param discount
	 */
	public static void addDiscount(int discount) {
		discounts *= (1 - (((double) discount) / 100));
	}
	/**

	Returns the percentage of discounts applied to the total amount.
	@return The percentage of discounts applied to the total amount as a double.
	*/
	public static double getItemPriceAfterDiscounts(double itemPrice) {
		return itemPrice * discounts;

	}

	/**
	 * Get total price after discounts applied
	 * 
	 * @return
	 */
	public static double getPriceAfterDiscounts() {
		if (activeSales == null)
			return getTotalPrice();
		return getTotalPrice() - getTotalDiscounts();
	}

	/**
	 * Get the current machine for order
	 * 
	 * @return
	 */
	public static MachineEntity getCurrentMachine() {
		return currentMachine;
	}

	/**
	 * Set the current machine to order from
	 * 
	 * @param currentMachine
	 */
	public static void setCurrentMachine(MachineEntity currentMachine) {
		OrderController.currentMachine = currentMachine;
	}

	/**
	 * Handle the refresh of view-catalog, review-order and clear order-controller
	 * fields
	 */
	public static void refreshOrderToHomePage() {
		OrderController.clearAll();
		NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.HomePage);
	}

}
