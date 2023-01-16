package entity;

import java.util.Objects;

/**
 * This class is not represents in DB
 *
 */
public class ItemInCartEntity {
	private int itemId, quantity, originalPrice, priceAfterDiscount;

	public ItemInCartEntity(int itemId, int quantity, int originalPrice) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
		this.originalPrice = originalPrice;
		this.priceAfterDiscount = originalPrice; // if no discount given
	}

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

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getPriceAfterDiscount() {
		return priceAfterDiscount;
	}

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
