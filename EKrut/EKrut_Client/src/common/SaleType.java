package common;

public enum SaleType {

	onePlusOne("1+1",0),
	tenPercent("10%",10),
	twentyPercent("20%",20),
	thirtyPercent("30%",30);
	
	
	private final String name;
	private final int precentage;
	/**

	Constructor for the SaleType enum. It Initialize a new SaleType with the given name and percentage
	@param name the name of the SaleType
	@param precentage the percentage of the SaleType
	*/
	SaleType(String name,int d) {
		this.name=name;
		this.precentage=d;
	}
	/**

	This method returns the precentage of this SaleType.
	@return the precentage of this SaleType
	*/
	public int getPrecentage() {
		return precentage;
	}
	/**

	This method returns the name of this SaleType.
	@return the name of this SaleType
	*/
	public String getName() {
		return name;
	}
	/***
	 * check what suitable string will fit the sale type
	 * @param str
	 * @return
	 */
	public static SaleType getSaleType(String str) {
		switch (str){
		case "1+1":
			return SaleType.onePlusOne;
		case "10%":
			return SaleType.tenPercent;
		case "20%":
			return SaleType.twentyPercent;
		case "30%":
			return SaleType.thirtyPercent;
		}
		return null;
	}
}
