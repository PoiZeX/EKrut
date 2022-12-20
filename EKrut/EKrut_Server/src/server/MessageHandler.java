package server;

import java.io.IOException;
import java.util.ArrayList;

import common.Message;
import common.TaskType;
import controllerDb.LoginDBController;
import controllerDb.OrderReportDBController;
import controllerDb.UsersManagementDBController;
import entity.SubscriberEntity;
import ocsf.server.ConnectionToClient;

public class MessageHandler {

	@SuppressWarnings("unchecked")
	public static void Handle(Message msg, ConnectionToClient client) throws IOException {
		TaskType task = msg.getTask();
		Object obj = msg.getObject();
		System.out.println("Message received: " + ((Message) msg).getTask().toString() + " from " + client);
		switch (task) {
		case EditSubscribers:
			ArrayList<SubscriberEntity> subscribersLst = (ArrayList<SubscriberEntity>) obj;
			SubscribersDbController.updateSubscribersEntities(client, subscribersLst);
			break;
		case ClientConnect:
			EchoServer.updateClientList(client, "Connect");
			break;
		case ClientDisconnect:
			EchoServer.updateClientList(client, "Disconnect");
			break;
		case LoadSubscribers:
			SubscribersDbController.getTable(client);
			break;
		case RequestOrderReport:
			OrderReportDBController.getOrderReportEntity((String[]) obj, client);
			break;
		case RequestUserFromDB:
			LoginDBController.getUserEntity((String[]) obj, client);
			break;
		case RequestUnapprovedUsers:
			UsersManagementDBController.getUnapprovedUsersEntity(client);
			break;
		default:
			System.out.println("Cannot execute task: " + task.toString());
			break;
		}

	}

}
