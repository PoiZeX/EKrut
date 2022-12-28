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
	ReceiveSupplyReport,
	ReceivePersonalMessages,
	ReceiveOrderReport,
	ReceiveUnapprovedUsers, 
	ReceiveUserFromServerDB,
	ReceiveUserInfoFromServerDB,
	
	// Requests From Server //
	RequestUsersApproval,
	RequestUserFromDB,
	RequestUserFromServerDB, 
	RequestSupplyReport,
	RequestItemsFromServer, 
	RequestUnapprovedUsers,
	RequestReport,
	RequestPersonalMessages,
	SendPersonalMessage,
	RequestUpdateDeliveries,
	RequestDeliveriesFromServer,
	RequestItemsInMachine,
	RequestUpdateItemsInMachine,
	RequestUserInfoFromServerDB,
	
	// Update Database
	ReceiveChangeUserRoleTypeInDB,
	RequestChangeUserRoleTypeInDB,
	
	// Common Data Initialization //
	InitRegions,
	InitMachines,
	

	
}
