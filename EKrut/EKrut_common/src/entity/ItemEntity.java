package entity;

import javafx.scene.image.Image;

public class ItemEntity extends MainEntity {

	private int itemId;
	private String name;
	private double price;
	private ImgEntity itemImg;
	private String img_relative_path;
	private transient Image itemImage;

	/**
	 * Instantiates a new item entity.
	 *
	 * @param item_id the item id
	 * @param name the name
	 * @param price the price
	 */
	public ItemEntity(int item_id, String name, double price) {
		super(item_id);
		this.itemId = item_id;
		this.name = name;
		this.price = price;
	}

	/**
	 * Instantiates a new item entity.
	 *
	 * @param item_id the item id
	 * @param name the name
	 * @param price the price
	 * @param item_img_name the item img name
	 */
	/* constructor to get from DB */
	public ItemEntity(int item_id, String name, double price, String item_img_name) {
		this(item_id, name, price);
		this.itemImg = new ImgEntity(item_img_name);
	}

	/**
	 * Instantiates a new item entity.
	 *
	 * @param item_id the item id
	 * @param name the name
	 * @param price the price
	 * @param itemImage the item image
	 */
	/* constructor to send to DB */
	public ItemEntity(int item_id, String name, double price, Image itemImage) {
		this(item_id, name, price);
		this.itemImage = itemImage;
	}

	/**
	 * Instantiates a new item entity.
	 *
	 * @param item_id the item id
	 * @param name the name
	 */
	public ItemEntity(int item_id, String name) {
		super(item_id);
		this.name = name;
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(double price)
	{
		this.price = price;
	}
	
	/**
	 * Gets the item img.
	 *
	 * @return the item img
	 */
	public ImgEntity getItemImg() {
		return itemImg;
	}

	/**
	 * Sets the item img.
	 *
	 * @param itemImg the new item img
	 */
	public void setItemImg(ImgEntity itemImg) {
		this.itemImg = itemImg;
	}

	/**
	 * Gets the img relative path.
	 *
	 * @return the img relative path
	 */
	public String getImg_relative_path() {
		return img_relative_path;
	}

	/**
	 * Sets the img relative path.
	 *
	 * @param img_relative_path the new img relative path
	 */
	public void setImg_relative_path(String img_relative_path) {
		this.img_relative_path = img_relative_path;
	}

	@Override
	public String toString() {
		return "ItemEntity [item_id=" + itemId + ", name=" + name + ", price=" + price + ", itemImg=" + itemImg + ", item_img_name="
				+ ", img_relative_path=" + img_relative_path + "]";
	}

	/**
	 * Gets the item image.
	 *
	 * @return the item image
	 */
	public Image getItemImage() {
		return itemImage;
	}

	/**
	 * Sets the item image.
	 *
	 * @param itemImage the new item image
	 */
	public void setItemImage(Image itemImage) {
		this.itemImage = itemImage;
	}
	
	
}
