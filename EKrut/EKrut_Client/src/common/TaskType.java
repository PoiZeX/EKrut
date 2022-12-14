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

	
	// Common Data Initialization //
	InitRegions,
	InitMachines,  
	
}
