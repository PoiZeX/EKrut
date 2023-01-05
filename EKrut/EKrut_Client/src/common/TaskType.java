package common;

public enum TaskType {
	// Host - Client E-NUMS //
	ClientConnect,
	ClientDisconnect,
	ServerDisconnect,
	LoadSubscribers,
	
	// Login E-NUMS //
	ValidUserNamePassword,
	InvalidUsernamePassword,
	SetUserLoggedIn,
	UserNotApproved, 
	
	// Receive Answers From Server //
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
	ReceiveChangeUserRoleTypeInDB,
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
	RequestProssecedItemsInMachine,
	RequestUpdateItemsInMachine,
	RequesManagerInfoFromServerDB,
	RequestDeliveryFromServer,  
	
	// Update Database
	RequestChangeUserRoleTypeInDB,
	RequestInsertNewSale,
	RequestUpdateMachineMinAmount,

	RequestItemsCallStatusUpdateFromServer,
	RequestUpdateSales,
	RequestSalesFromServer,
	ReceiveSupplyWorkersFromServer, 
	RequestSupplyWorkers, 
	RequestItemsInMachineUpdateFromServer,
	RequestItemsWithMinAmount,
	InitMachinesSupplyUpdate,
	InitMachinesInRegions,

	// Order
	NewOrderCreation, 
	
	// Common Data Initialization //
	InitRegions,
	InitMachines, 
	
}
