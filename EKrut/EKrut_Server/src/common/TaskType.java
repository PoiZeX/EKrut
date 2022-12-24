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
	ReceiveUnapprovedUsers, 
	ReceiveUserFromServerDB,
	// Requests From Server //
	RequestUsersAprroval,
	RequestUserFromDB,
	RequestUserFromServerDB, 
	RequestSupplyReport,
	RequestItemsFromServer, 
	RequestUnapprovedUsers,
	RequestReport,
	RequestPersonalMessages,
	RequestUpdateDeliveries,
	RequestDeliveriesFromServer,
	RequestItemsInMachine,
	RequestUpdateItemsInMachine,
	// Common Data Initialization //
	InitRegions,
	InitMachines
	
}
