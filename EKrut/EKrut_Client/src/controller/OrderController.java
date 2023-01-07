package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Store.NavigationStoreController;
import common.RolesEnum;
import common.SaleType;
import entity.ItemEntity;
import entity.ItemInCartEntity;
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
	private static Map<ItemInMachineEntity, Integer> itemsInCartList = new LinkedHashMap<>();
	private static double discounts = 1.0; // in double 0.3 is 30%
	private static OrderEntity currentOrder;
	private static MachineEntity currentMachine;
	private static Map<String, ItemInMachineEntity> itemsList = new LinkedHashMap<>(); // for images i think(???)
	private static ArrayList<SaleEntity> activeSales = null;
	private static boolean isSaleActive =false;

	public OrderController() {

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
	public static Map<String, ItemInMachineEntity> getItemsList() {
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
	public static Map<ItemInMachineEntity, Integer> getCart() {
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
	private static boolean isActiveSale() {
		if (NavigationStoreController.connectedUser.getRole_type().equals(RolesEnum.member) && !activeSales.isEmpty()) {
			isSaleActive=true;
		}
		
		return isSaleActive;
	}
	
	private static ArrayList<SaleEntity> getActiveSales() {
		return activeSales;
	}

	private static void setActiveSales(ArrayList<SaleEntity> activesales) {
		if (NavigationStoreController.connectedUser.getRole_type().equals(RolesEnum.member)) {
			if (activeSales == null) {
				activeSales = new ArrayList<>();
			}
			activeSales.addAll(activesales);
		}
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
		for(SaleEntity sale : activeSales) {
			addDiscount(sale.getSaleType().getPrecentage());
		}
		
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
