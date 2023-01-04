package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import entity.ItemInCartEntity;

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
	private HashMap<Integer, ItemInCartEntity> cart = new HashMap<>();  // itemId and itemEntity
	
	public OrderController(){
		
	}
	
	// 1+1
	// 30% 
	
	/**
	 * return the cart as list of items
	 * @return
	 */
	public Collection<ItemInCartEntity> getCart() {
		return cart.values();
	}

	/**
	 * Add item to cart 
	 * @param item
	 * @return true if the item added successfully
	 */
	public boolean addItemToCart(ItemInCartEntity item) {
		if (cart.containsKey(item.getItemId()))
				return false;
		cart.put(item.getItemId(), item);
		return true;
	}
	
	/**
	 * Change the quantity of item in cart, if Quantity equal/less than zero the item will be removed
	 * @param itemId
	 * @param newQuantity 
	 * @return true if success
	 */
	public boolean changeItemQuantity(int itemId, int newQuantity)
	{
		// search for item
		ItemInCartEntity itemToChange = cart.get(itemId);
		if (itemToChange == null)
			return false;
		
		// need to remove?
		if(newQuantity <= 0) 
			return removeItem(itemToChange);
		
		// set new positive quantity
		itemToChange.setQuantity(newQuantity);
		cart.replace(itemToChange.getItemId(), itemToChange);
		return true;
	}
	
	/**
	 * Local method to remove item from cart
	 * @param item
	 * @return
	 */
	private boolean removeItem(ItemInCartEntity item) {
		if(cart.remove(item) == null) return false;
		return true; 
	}
	
}
