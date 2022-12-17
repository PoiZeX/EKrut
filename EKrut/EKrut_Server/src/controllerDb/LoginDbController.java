package controllerDb;

import java.io.IOException;

import entity.UserEntity;
import ocsf.server.ConnectionToClient;

public class LoginDbController {

	private static String username, password;

	public static boolean setUser(String[] usernamePassword) {
		if (usernamePassword.length == 2) {
			username = usernamePassword[0];
			password = usernamePassword[1];
			return true;
		}
		return false;
	}

	public static void getUserEntity(String[] usernamePassword, ConnectionToClient client) {
		if (setUser(usernamePassword)) {

			// sql query //

			UserEntity notGoodUser = new UserEntity("", "", "", "", "", "", "");
			UserEntity goodUser = new UserEntity("username", "password", "fname", "lname", "email", "phoneNumber",
					"registered");

			try {
				if (username.equals("username") && password.equals("password")) {
					client.sendToClient((Object)goodUser);
					System.out.println("Server: success");
				}

				else {
					client.sendToClient((Object)notGoodUser); // finally send the entity
					System.out.println("Server: failed");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 

		}
	}
}
