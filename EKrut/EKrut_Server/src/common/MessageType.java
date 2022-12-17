package common;

public enum MessageType {
	ClientConnect,
	ClientDisconnect,
	ServerDisconnect,
	LoadSubscribers,
	
	// Login enums
	ValidUserNamePassword,
	InvalidUsernamePassword,
	UserIsLoggedIn,
	UserNotApproved
	
	// -----
	
}
