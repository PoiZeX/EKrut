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
	
	// Receive Answers From Server //
	ReceiveUserUpdateInDB,
	ReceiveUsersApproval,
	ReceiveDeliveriesFromServer,
	ReceiveItemsFromServer,
	ReceiveClientsReport,
	ReceivePersonalMessages,
	ReceiveOrderReport,
	ReceiveSupplyReport,
	ReceiveUnapprovedUsers, 
	ReceiveUserFromServerDB,
	ReceiveUserInfoFromServerDB,
	SendPersonalMessage,
	ReceiveManagerInfoFromServerDB,
	ReceiveDeliveryFromServer,
	ReceiveItemsInMachine,
	ReceiveSalesFromServer,
	// Requests From Server //
	RequestUsersApproval,
	RequestUserFromDB,
	RequestUserFromServerDB, 
	RequestUserInfoFromServerDB,
	RequestSupplyReport,
	RequestItemsFromServer, 
	RequestUnapprovedUsers,
	RequestReport,
	RequestPersonalMessages,
	RequestUpdateDeliveries,
	RequestDeliveriesFromServer,
	RequestItemsInMachine,
	RequestUpdateItemsInMachine,
	RequesManagerInfoFromServerDB,
	RequestDeliveryFromServer,
	// Update Database
	RequestUserUpdateInDB,
	
	RequestInsertNewSale,
	RequestUpdateMachineMinAmount,
	RequestProssecedItemsInMachine,
	RequestItemsCallStatusUpdateFromServer,
	RequestUpdateSales,
	RequestSalesFromServer,
	RequestSupplyWorkers,
	RequestItemsInMachineUpdateFromServer,
	RequestItemsWithMinAmount,
	ReceiveSupplyWorkersFromServer,  
	InitMachinesSupplyUpdate,
	InitMachinesInRegions,
	
	// Order
	ReviewOrderServerAnswer,
	isMemberFirstPurchase,
	AddNewDelivery,
	NewOrderCreation,
	UpdateItemsWithAnswer,
	
	// Common Data Initialization //
	InitRegions,
	InitMachines,
	   
	
	
}
