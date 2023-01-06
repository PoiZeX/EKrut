package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import entity.ItemEntity;
import entity.ItemInCartEntity;
import entity.ItemInMachineEntity;

/**
 * The class handles the WHOLE order process
 *
 */
public class OrderController {
	/*
	 * Get all items in machine
	 * Get items description
	 * Get discounts for current time and region
	 * Add items to cart [V]
	 * Change quantity (remove specially) [V]
	 * Make a review 
	 * Validate item quantity (server side before insert)
	 * Insert order
	 * 
	 * 
	 */
	
	//private static HashMap<Integer, ItemInMachineEntity> cart = new HashMap<>();  // itemId and itemEntity
	private static Map <ItemInMachineEntity, Integer> itemsInCartList = new LinkedHashMap<>();
	
	public OrderController() {

	}
	/**
	 * return the amount of a specific item in the cart
	 * @param item
	 * @return amount
	 */
	private static int getAmount(ItemInMachineEntity item) {
		return itemsInCartList.get(item);
	}
	/**
	 * returns the amount of items in the cart
	 * @return cartSize
	 */
	public static int getCartSize() {
		int cartSize = 0;
		for (ItemInMachineEntity item : itemsInCartList.keySet()) 
			cartSize += (getAmount(item));
		return cartSize;
	}
	/**
	 * returns the total price of the cart
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
	 * @return
	 */
	public static Map<ItemInMachineEntity, Integer> getCart() {
		return itemsInCartList;
	}

	/**
	 * Add item to cart 
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
	 * Change the quantity of item in cart, if Quantity equal/less than zero the item will be removed
	 * @param itemId
	 * @param newQuantity 
	 * @return true if success
	 */
	public static boolean changeItemQuantity(ItemInMachineEntity item, int newQuantity)
	{
		if (item == null)
			return false;
		// need to remove?
		if(newQuantity <= 0) 
			return removeItem(item);
		
		// set new positive quantity
		itemsInCartList.put(item, newQuantity);
		return true;
	}
	
	/**
	 * Local method to remove item from cart
	 * @param item
	 * @return
	 */
	private static boolean removeItem(ItemInMachineEntity item) {
		if (!itemsInCartList.containsKey(item)) return false;
		itemsInCartList.remove(item);
		return true; 
	}
}
