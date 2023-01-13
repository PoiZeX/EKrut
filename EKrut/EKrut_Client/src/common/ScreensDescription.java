package common;

/**
 * The class has all description for most screens
 *
 */
public class ScreensDescription {
	
	public static final String HOME_PAGE_DESCRIPTION = "Welcome to EKrut!\n"
			+ "In this page you can navigate to all of your available options by click each button.\n"
			+ "Have a nice ride";
	
	// reports
	public static final String REPORT_SELECTION_DESCRIPTION = "Here you need you select a valid date and report type.\n"
			+ "Region managers has an access to their region only. CEO has access for all regions";
	//public static final String CEOReportSelection_DESCRIPTION = "";
	public static final String ORDERS_REPORT_DESCRIPTION = "";
	public static final String SUPPLY_REPORT_DESCRIPTION = "";
	public static final String CLEINTS_REPORT_DESCRIPTION = "";
	
	// order
	public static final String VIEW_CATALOG_DESCRIPTION = "In this screen you can see all the available items in store\n"
			+ "You can search products by Name, And view your cart at any moment\n"
			+ "You can add as many items as you like, and see the current cart price at any moment.\n\n"
			+ "You can also hover to get more information about products/options\n"
			+ "When you feel like you're done you can choose to procceed to review and payment, or cancel at any moment, Enjoy!";
	public static final String REVIEW_ORDER_DESCRIPTION = "You just one step from happiness!\nAt the left side you can see you order review.\n"
			+ "If some discount were applied, you can hover with the mouse on the red label 'discount' to see full description.\n"
			+ "If you select a shippment, please enter your details at the right side\nWe are exciting for you!";


	// management 
	public static final String USERS_MANAGEMENT_DESCRIPTION = "At this page as a manager you can approve (or not) users.\n"
			+ "To do this, select a user from ths list and click on the right column\nA combo box will appear.\n"
			+ "Do not forget to save!";

	public static final String DELIVERY_MANAGEMENT_DESCRIPTION = "At this page as a manager you can change delivery status.\n"
			+ "To do this, select delivery from the list and click on the right column\nA combo box will appear.\n"
			+ "You can change from 'pendingApproval' to 'outForDelivery' and from 'outForDelivery' to 'done' after customer approval.\n "
			+ "Do not forget to save!";
	
	public static final String SALES_MANAGEMENT_DESCRIPTION = "This page is used by the marketing manager and the regional marketing employees.\n"
			+ "Manager - by selecting a region in advance can view all the sales in that region. (active or inactive)\n"
			+ "Regional employee - can activate or inactivate a sale.\n"
			+ "To do this, select sale from the list and click on the right column\nA combo box will appear.";
	
	public static final String CREATE_NEW_SALE_DESCRIPTION = "On this page, the marketing manager can create a new sale by selecting a region, days, hours, and a sale template.\r\n"
			+ "The start and end times are for each day.\n"
			+ "The start time must be before the end time.\n"
			+ "The same discount cannot be applied more than once.\n"
			+ "It is possible to multiply different discounts.\n"
			+ "To save the sale, please click the 'Create' button.";
	
	public static final String SUPPLY_MANAGEMENT_DESCRIPTION = "At this page you can be updated with the items stock status \n"
			+ " for the machines in your region, in the Not Opened Table you can see the items whom needed to open a call for\n "
			+ "You need to check the items to open calls for, choose a supply worker for the call and send the calls"
			+ "On the Procceced or Complete table  You can see the prosseced calls and the coompleted\n"
			+ "in order to remove the copmleted you can press the remove completed button."
			+ "In addition you can update the machine min amount, you must click save button in order to save the changes.";
	
	public static final String SUPPLY_UPDATE_DESCRIPTION = "At this page you can see all the machines that required your restock"
			+ "\n on the current amount column you can update the current amount after the restock, in order to save, "
			+ "please press enter after typing\n and in the end press the update button. ";
	
	// other
	public static final String PERSONAL_MESSAGES_DESCRIPTION = "Here you can see all kinds of messages\n"
			+ "System message from time-to-time, or SIMULATION of SMS/Mail were send for you";
	public static final String REGISTRATION_FORM_DESCRIPTION = "";
	public static final String CONFIRM_ONLINE_ORDER_DESCRIPTION = "On this page, you can confirm receipt of delivery or pickup an order from the machine.\n"
			+ "Enter an order number.\n"
			+ "Click 'Confirm'/'Collect' button.\n\n"
			+ "Bon appetite!";


}
