package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonData;
import common.Message;
import common.SaleType;
import common.ScreensNames;
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

	// private static HashMap<Integer, ItemInMachineEntity> cart = new HashMap<>();
	// // itemId and itemEntity
	private static LinkedHashMap<ItemInMachineEntity, Integer> itemsInCartList = new LinkedHashMap<>();
	private static double discounts = 1.0; // in double 0.3 is 30%
	private static OrderEntity currentOrder;
	private static MachineEntity currentMachine = CommonData.getCurrentMachine();
	private static LinkedHashMap<String, ItemInMachineEntity> itemsList = new LinkedHashMap<>(); // for images i
																									// think(???)
	private static ArrayList<SaleEntity> activeSales = null;
//	private static boolean isSaleActive = false;
	private static boolean onePlusOneSaleExist = false;
	private static boolean percentageSaleExit = false;
	private static ClientController chat = HostClientController.chat; // define the chat for th
	private static boolean isDataReceived = false;
	public static boolean isFirstPurchaseDiscountApplied = false;
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
		isDataReceived = false;
		chat.acceptObj(msg);
		while (!isDataReceived)
			Thread.sleep(100);
	}

	public static void getDataFromServer(Object dataRecived) {
		data = dataRecived;
		isDataReceived = true;
	}

	/**
	 * Add a 20% discount for first purchase of a member
	 */
	public static void addMemberFirstPurchaseDiscount() {
		OrderController.addDiscount(20); // members get 20% on first order
		OrderController.isFirstPurchaseDiscountApplied = true;
	}

	/**
	 * Clear all static data. Be careful using it!
	 */
	public static boolean clearAll() {
		if (itemsList == null)
			return false;
		isFirstPurchaseDiscountApplied = false;
		itemsInCartList.clear();
		currentOrder = null;
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
//------------------------------------------------ cart
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
		if (activeSales != null && currentOrder != null) {
			if (!activeSales.isEmpty() && currentOrder.getMachine_id() != -1) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<SaleEntity> getActiveSales() {
		return activeSales;
	}

	/**
	 * Get a string of all current sales types as string
	 * @return
	 */
	public static String getActiveSalesTypeAsString() {
		StringBuilder sb = new StringBuilder();
		activeSales.forEach(sale -> sb.append(sale.getSaleType().toString() + " "));
		return sb.toString();
	}

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

	public static boolean isOnePlusOneSaleExist() {
		return onePlusOneSaleExist;
	}

	public static boolean isPercentageSaleExit() {
		return percentageSaleExit;
	}

	/**
	 * Get total discount in NIS
	 * 
	 * @return //price 100 discounts 0.3 : give 30
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

	public static double getItemPriceAfterDiscounts(double itemPrice) {
		if (activeSales == null)
			return itemPrice;
		return itemPrice *discounts;

	}

	public static double getPriceAfterDiscounts() {
		if (activeSales == null)
			return getTotalPrice();
		return getTotalPrice() - getTotalDiscounts();
	}

	public static MachineEntity getCurrentMachine() {
		return currentMachine;
	}

	public static void setCurrentMachine(MachineEntity currentMachine) {
		OrderController.currentMachine = currentMachine;
	}
	
	/**
	 * Handle the refresh of view-catalog, review-order and clear order-controller fields
	 */
	public static void refreshOrderToHomePage() {
		OrderController.clearAll();
		// goto homepage
		NavigationStoreController.getInstance().refreshWithoutScreenChange(ScreensNames.ViewCatalog);
		NavigationStoreController.getInstance().refreshStage(ScreensNames.HomePage);
	}

}
