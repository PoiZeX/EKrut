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
	// Registration
	RequestRegistrationForm,
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
	RequestItemsInMachine,
	RequestUpdateItemsInMachine,
	// Common Data's Init
	InitRegions
}
