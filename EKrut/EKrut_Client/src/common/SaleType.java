package common;

public enum SaleType {

	onePlusOne("1+1",0),
	tenPercent("10%",10),
	twentyPercent("20%",20),
	thirtyPercent("30%",30);
	
	
	private final String name;
	private final int precentage;
	
	SaleType(String name,int d) {
		this.name=name;
		this.precentage=d;
	}
	public int getPrecentage() {
		return precentage;
	}
	public String getName() {
		return name;
	}
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
