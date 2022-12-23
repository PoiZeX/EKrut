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
<<<<<<< Updated upstream
	RecieveUserFromServerDB,
	
	// ---- CLIENT SIDE ------ //
	// Registration
	RequestRegistrationForm,
=======
>>>>>>> Stashed changes
	RequestSupplyReport,
	RequestItemsFromServer, 
<<<<<<< Updated upstream
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
=======
	RequestUnapprovedUsers,
	RequestReport,
	RequestPersonalMessages,
	RequestUpdateDeliveries,
	RequestUsersApproval,
	RequestDeliveriesFromServer ,
	// Common Data Initialization //
	InitRegions

>>>>>>> Stashed changes
}
