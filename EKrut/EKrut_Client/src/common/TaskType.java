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
	SetUserLoggedIn,
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
	RequestUsersApproval,
	RecieveUsersApproval,
	
	RequestClientsReport,
	RecieveClientsReport,
	
	// Common Data's Init
	InitRegions,
	
	// personal messages
	RequestPersonalMessages,
	RecievePersonalMessages
}
