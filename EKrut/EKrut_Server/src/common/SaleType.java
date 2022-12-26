package common;

public enum SaleType {

	onePlusOne("1+1"),
	tenPercent("10%"),
	twentyPercent("20%"),
	thirtyPercent("30%");
	private final String name;
	SaleType(String name) {
		this.name=name;
	}
	public String getName() {
		return name;
	}
}
