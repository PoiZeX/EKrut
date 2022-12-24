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
	RequestUserFromDB,
	RequestUserFromServerDB, 
	RequestSupplyReport,
	RequestItemsFromServer, 
	RequestUnapprovedUsers,
	RequestReport,
	RequestPersonalMessages,
	RequestUpdateDeliveries,
	RequestUsersApproval,
	RequestDeliveriesFromServer ,
	// Common Data Initialization //
	InitRegions

	RequestUsersAprroval,
	RecieveUsersApproval,
	RequestClientsReport,
	RecieveClientsReport,
	InitMachines,
	// Common Data's Init
	InitRegions
	
}
