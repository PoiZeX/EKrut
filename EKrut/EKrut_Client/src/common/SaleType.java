package common;

public enum SaleType {

	onePlusOne("1+1",1.00),
	tenPercent("10%",0.1),
	twentyPercent("20%",0.2),
	thirtyPercent("30%",0.3);
	private final String name;
	private final double precentage;
	SaleType(String name,double d) {
		this.name=name;
		this.precentage=d;
	}
	public double getPrecentage() {
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
