package common;

public enum MessageType {
	
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
	

	
}
