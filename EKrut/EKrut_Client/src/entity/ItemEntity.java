package entity;

public class ItemEntity  {
	private int item_id;
	private String name;
	private double price;
	private String manufacturer ,description;
	private ImgEntity itemImg;
	private String item_img_name;
	private String img_relative_path;
	
	


	/*constructor to get from DB*/
	public ItemEntity(int item_id, String name, double price, String manufacturer, String description,
			 String item_img_name) {
		super();
		this.item_id = item_id;
		this.name = name;
		this.price = price;
		this.manufacturer = manufacturer;
		this.description = description;
		this.itemImg = new ImgEntity(item_img_name);
	}
	
	
	/*constructor to send to DB*/
	public ItemEntity(int item_id, String name, double price, String manufacturer, String description,
			 ImgEntity itemImg) {
		super();
		this.item_id = item_id;
		this.name = name;
		this.price = price;
		this.manufacturer = manufacturer;
		this.description = description;
		this.item_img_name = itemImg.getImgName();
	}
	
	public int getItem_id() {
		return item_id;
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
	public String getItem_img_name() {
		return item_img_name;
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
	
}
