package controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.TaskType;
import controllerGui.HostClientController;
import entity.PersonalMessageEntity;
import entity.UserEntity;

public class SMSMailHandlerController {
	private static ClientController chat = HostClientController.chat;

	
	/**
	 * Handle sending SMS or Mail based on <msgType> to specific user
	 * @param msgType
	 * @param to
	 * @param title
	 * @param message
	 * @return
	 */
	public static boolean SendSMSOrMail(String msgType, UserEntity to, String title, String message) {
		// validate 
		if(CommonFunctions.isNullOrEmpty(title) || CommonFunctions.isNullOrEmpty(message))
			return false;
		
		// set new message
		if (msgType.equals("SMS")) {
			String phone_number = to.getPhone_number();
			message = "This message sent via SMS to " + phone_number + ":\n" + message;
		} else if (msgType.equals("Mail")) {
			String email = to.getEmail();
			message = "This message sent via Email to " + email + ":\n" + message;
		}
		else
		{
			if (!msgType.equals("System")) return false;
		}

		// Create current time string
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		
		PersonalMessageEntity msgToSend = new PersonalMessageEntity(to.getId(), formatter.format(now.getTime()), title, message);
		if (chat != null)
			chat.acceptObj(new Message(TaskType.SendPersonalMessage, msgToSend));

		// in the future when ACTUALLY sending SMS, we can detect if it succeeded or failed
		return true;

	}


}
