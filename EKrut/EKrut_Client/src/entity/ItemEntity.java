package entity;

public class ItemEntity extends MainEntity {

	
	private int itemId;
	private String name;
	private double price;
	private String manufacturer ,description;
	private ImgEntity itemImg;
	private String img_relative_path;
	
	


	/*constructor to get from DB*/
	public ItemEntity(int item_id, String name, double price, String manufacturer, String description,
			 String item_img_name) {
		super(item_id);
		this.itemId = item_id;
		this.name = name;
		this.price = price;
		this.manufacturer = manufacturer;
		this.description = description;
		this.itemImg = new ImgEntity(item_img_name);
	}
	 
	

	public ItemEntity(int item_id, String name, double price, String manufacturer, String description,
			 ImgEntity itemImg) {
		super(item_id);
		this.itemId = item_id;
		this.name = name;
		this.price = price;
		this.manufacturer = manufacturer;
		this.description = description;
		this.itemImg=itemImg;
	}
	
	public int getItem_id() {
		return itemId;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getDescription() {
		return description;
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
		return "ItemEntity [item_id=" + itemId + ", name=" + name + ", price=" + price + ", manufacturer="
				+ manufacturer + ", description=" + description + ", itemImg=" + itemImg + ", item_img_name="
				 + ", img_relative_path=" + img_relative_path + "]";
	}
}
