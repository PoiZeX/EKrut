package common;

public enum TaskType {
	
	ClientConnect,
	ClientDisconnect,
	ServerDisconnect,
	LoadSubscribers,
	EditSubscribers,
	
	// Login Enums //
	RequestUserFromDB,
	ValidUserNamePassword,
	InvalidUsernamePassword,
	UserIsLoggedIn,
	UserNotApproved, 

	// ----- SERVER SIDE ------ //
	RequestUserFromServerDB, 
	RecieveUserFromServerDB,
	
	// ---- CLIENT SIDE ------ //
	RequestSupplyReport,

	// RequestClientReport
	RequestOrderReport,
	RecieveOrderReport,
	
	RequestUnapprovedUsers, 
	RecieveUnapprovedUsers, 
	
	RequestItemsFromServer, 
	RecieveItemsFromServer, 
	RequestDeliveriesFromServer, 
	RecieveDeliveriesFromServer,
	RequestUpdateDeliveries,
	RequestUsersAprroval,
	RecieveUsersApproval,
	RequestClientsReport,
	RecieveClientsReport,
	InitMachines,
	// Common Data's Init
	InitRegions
	
}
