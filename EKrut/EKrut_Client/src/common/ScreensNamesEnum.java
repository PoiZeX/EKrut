package common;

/**
 * The enum saves all screens names
 * and their associate description
 * the ScreensDescription will appear on each screens question mark icon
 *
 */
public enum ScreensNamesEnum {
	HostClient(),
	Login(),
	HomePage(ScreensDescription.HOME_PAGE_DESCRIPTION),
	ReportSelection(ScreensDescription.REPORT_SELECTION_DESCRIPTION),
	OrdersReport(ScreensDescription.ORDERS_REPORT_DESCRIPTION),
	SupplyReport(ScreensDescription.SUPPLY_REPORT_DESCRIPTION),
	ClientsReport(ScreensDescription.CLEINTS_REPORT_DESCRIPTION),
	
	// order
	ViewCatalog(ScreensDescription.VIEW_CATALOG_DESCRIPTION),
	ReviewOrder(ScreensDescription.REVIEW_ORDER_DESCRIPTION),
	
	// management
	UsersManagement(ScreensDescription.USERS_MANAGEMENT_DESCRIPTION),
	DeliveryManagement(ScreensDescription.DELIVERY_MANAGEMENT_DESCRIPTION), 
	SalesManagement(ScreensDescription.SALES_MANAGEMENT_DESCRIPTION),
	CreateNewSale(ScreensDescription.CREATE_NEW_SALE_DESCRIPTION),
	SupplyManagement(ScreensDescription.SUPPLY_MANAGEMENT_DESCRIPTION),
	SupplyUpdate(ScreensDescription.SUPPLY_UPDATE_DESCRIPTION),
	
	// other
	PersonalMessages(ScreensDescription.PERSONAL_MESSAGES_DESCRIPTION),
	RegistrationForm(ScreensDescription.REGISTRATION_FORM_DESCRIPTION),
	ConfirmOnlineOrder(ScreensDescription.CONFIRM_ONLINE_ORDER_DESCRIPTION);

	
	private final String description;
	/**
	 * Constructor for screens with description
	 * @param description
	 */
	ScreensNamesEnum(String description) {
		this.description = description;
	}
	
	/**
	 * Constructor to screens without description
	 */
	private ScreensNamesEnum() {
		description = "";
	}
	
	/**
	 * Get the description of screen
	 * @return
	 */
	public String getDescription() {
		return description;
	}

}
