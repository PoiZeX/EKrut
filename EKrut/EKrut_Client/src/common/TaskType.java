package common;

public enum TaskType {
	// Host - Client E-NUMS //
	ClientConnect,
	ClientDisconnect,
	ServerDisconnect,
	LoadSubscribers,
	EditSubscribers,
	
	// Login E-NUMS //
	ValidUserNamePassword,
	InvalidUsernamePassword,
	SetUserLoggedIn,
	UserNotApproved, 	
	
	// Common Data Initialization //
	InitRegions,
	InitMachines,
	   
	//-------------------------------------Users
	// Requests From Server //
	RequestUsersApproval,
	RequestUserFromDB,
	RequestUserFromServerDB, 
	RequestUserInfoFromServerDB,
	RequestUnapprovedUsers,
	RequestPersonalMessages,
	RequestSupplyWorkers,
	// Receive Answers From Server //
	ReceiveUserUpdateInDB,
	ReceiveUsersApproval,
	ReceiveSupplyWorkersFromServer,  
	SendPersonalMessage,
	ReceivePersonalMessages,
	ReceiveUnapprovedUsers, 
	ReceiveUserFromServerDB,
	ReceiveUserInfoFromServerDB,
	// Update Database//
	RequestUserUpdateInDB,
	
	//-------------------------------------Sales
	// Requests From Server //
	RequestSalesFromServer,
	// Receive Answers From Server //
	ReceiveSalesFromServer,
	
	// Update Database//
	RequestInsertNewSale,
	RequestUpdateSales,
	
	//-------------------------------------Machine
	// Requests From Server //
	InitMachinesSupplyUpdate,
	InitMachinesInRegions,
	// Receive Answers From Server //
	// Update Database//
	RequestUpdateMachineMinAmount,
	
	//-------------------------------------Items
	// Requests From Server //
	RequestItemsInMachine,
	RequestItemsFromServer, 
	RequestItemsWithMinAmount,
	RequestProssecedItemsInMachine,
	// Receive Answers From Server //
	ReceiveItemsInMachine,
	ReceiveItemsFromServer,
	// Update Database//
	RequestItemsCallStatusUpdateFromServer,
	RequestUpdateItemsInMachine,
	RequestItemsInMachineUpdateFromServer,
	RequestItemsInMachineRestockFromServer,
	
	//-------------------------------------Deliveries
	// Requests From Server //
	RequestDeliveriesFromServer,
	RequestDeliveryFromServer,
	ReceiveDeliveriesFromServer,
	// Receive Answers From Server //
	ReceiveDeliveryFromServer,
	// Update Database//
	RequestUpdateDeliveries,
	
	//-------------------------------------Order
	// Requests From Server //
	// Receive Answers From Server //
	ReviewOrderServerAnswer,
	isMemberFirstPurchase,
	// Update Database//
	AddNewDelivery,
	NewOrderCreation,
	UpdateItemsWithAnswer,
	
	//-------------------------------------Reports
	// Requests From Server //
	RequestSupplyReport,
	RequestReport,
	RequesManagerInfoFromServerDB,
	// Receive Answers From Server //
	ReceiveManagerInfoFromServerDB,
	ReceiveOrderReport,
	ReceiveSupplyReport,
	ReceiveClientsReport, 
	// Update Database//
	
}
