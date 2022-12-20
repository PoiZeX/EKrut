package common;

public enum TaskType {
	
	ClientConnect,
	ClientDisconnect,
	ServerDisconnect,
	LoadSubscribers,
	EditSubscribers,
	
	// Login Enums //
	LoginRequest,
	ValidUserNamePassword,
	InvalidUsernamePassword,
	UserIsLoggedIn,
	UserNotApproved, 

	// ----- SERVER SIDE ------ //
	UserFromServerDB, 
	
	// ---- CLIENT SIDE ------ //
	RequestOrderReport,
}
