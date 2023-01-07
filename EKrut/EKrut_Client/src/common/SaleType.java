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
}
