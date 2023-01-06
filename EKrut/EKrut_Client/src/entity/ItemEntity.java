package entity;

import javafx.scene.image.Image;

public class ItemEntity extends MainEntity {

	private int itemId;
	private String name;
	private double price;
	private ImgEntity itemImg;
	private String img_relative_path;
	private Image itemImage;

	public ItemEntity(int item_id, String name, double price) {
		super(item_id);
		this.itemId = item_id;
		this.name = name;
		this.price = price;
	}

	/* constructor to get from DB */
	public ItemEntity(int item_id, String name, double price, String item_img_name) {
		this(item_id, name, price);
		this.itemImg = new ImgEntity(item_img_name);
	}

	/* constructor to send to DB */
	public ItemEntity(int item_id, String name, double price, Image itemImage) {
		this(item_id, name, price);
		this.itemImage = itemImage;
	}

	public ItemEntity(int item_id, String name) {
		super(item_id);
		this.name = name;
	}

	public int getItemId() {
		return itemId;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	
	public ImgEntity getItemImg() {
		return itemImg;
	}

	public void setItemImg(ImgEntity itemImg) {
		this.itemImg = itemImg;
	}

	public String getImg_relative_path() {
		return img_relative_path;
	}

	public void setImg_relative_path(String img_relative_path) {
		this.img_relative_path = img_relative_path;
	}

	@Override
	public String toString() {
		return "ItemEntity [item_id=" + itemId + ", name=" + name + ", price=" + price + ", itemImg=" + itemImg + ", item_img_name="
				+ ", img_relative_path=" + img_relative_path + "]";
	}

	public Image getItemImage() {
		return itemImage;
	}

	public void setItemImage(Image itemImage) {
		this.itemImage = itemImage;
	}
}
