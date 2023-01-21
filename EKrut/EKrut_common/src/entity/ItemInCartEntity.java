package entity;

import java.util.Objects;

/**
 * This class is not represents in DB
 *
 */
public class ItemInCartEntity {
	private int itemId, quantity, originalPrice, priceAfterDiscount;

	/**
	 * Instantiates a new item in cart entity.
	 *
	 * @param itemId the item id
	 * @param quantity the quantity
	 * @param originalPrice the original price
	 */
	public ItemInCartEntity(int itemId, int quantity, int originalPrice) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
		this.originalPrice = originalPrice;
		this.priceAfterDiscount = originalPrice; // if no discount given
	}

	/**
	 * Instantiates a new item in cart entity.
	 *
	 * @param itemId the item id
	 * @param quantity the quantity
	 * @param originalPrice the original price
	 * @param priceAfterDiscount the price after discount
	 */
	public ItemInCartEntity(int itemId, int quantity, int originalPrice, int priceAfterDiscount) {
		this(itemId, quantity, originalPrice);
		this.priceAfterDiscount = priceAfterDiscount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(itemId, originalPrice, priceAfterDiscount, quantity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemInCartEntity other = (ItemInCartEntity) obj;
		return itemId == other.itemId && originalPrice == other.originalPrice
				&& priceAfterDiscount == other.priceAfterDiscount && quantity == other.quantity;
	}

	/**
	 * Gets the item id.
	 *
	 * @return the item id
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Sets the item id.
	 *
	 * @param itemId the new item id
	 */
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * Sets the quantity.
	 *
	 * @param quantity the new quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * Gets the original price.
	 *
	 * @return the original price
	 */
	public int getOriginalPrice() {
		return originalPrice;
	}

	/**
	 * Sets the original price.
	 *
	 * @param originalPrice the new original price
	 */
	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	/**
	 * Gets the price after discount.
	 *
	 * @return the price after discount
	 */
	public int getPriceAfterDiscount() {
		return priceAfterDiscount;
	}

	/**
	 * Sets the price after discount.
	 *
	 * @param priceAfterDiscount the new price after discount
	 */
	public void setPriceAfterDiscount(int priceAfterDiscount) {
		this.priceAfterDiscount = priceAfterDiscount;
	}


	@Override
	public String toString() {
		if (originalPrice != priceAfterDiscount)
			return "[Item Id=" + itemId + ", Quantity=" + quantity + ", Original Price=" + originalPrice
					+ ", Price After Discount=" + priceAfterDiscount + "]";
		
		return "[Item Id =" + itemId + ", Quantity=" + quantity + ", Original Price=" + originalPrice + "]";
				
	}

}
