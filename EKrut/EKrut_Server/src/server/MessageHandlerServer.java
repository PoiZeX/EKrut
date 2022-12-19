package server;

import java.io.IOException;
import java.util.ArrayList;

import common.Message;
import common.MessageType;
import controllerDb.LoginDbController;
import entity.SubscriberEntity;
import ocsf.server.ConnectionToClient;

public class MessageHandlerServer {

	public static void HandleMessage(Message msg, ConnectionToClient client) throws IOException {
		MessageType task = msg.getTask();
		Object obj = msg.getObject();
		switch (task) {
		case EditSubscribers:
			ArrayList<SubscriberEntity> subscribersLst = (ArrayList<SubscriberEntity>) obj;
			SubscribersDbController.updateSubscribersEntities(client, subscribersLst);
			return;
		case LoginRequest:
			LoginDbController.getUserEntity((String[]) obj, client);
			return;
		case ClientConnect:
			EchoServer.updateClientList(client, "Connect");
			break;
		case ClientDisconnect:
			EchoServer.updateClientList(client, "Disconnect");
			break;
		case LoadSubscribers:
			SubscribersDbController.getTable(client);
			break;
		default:
			System.out.println("Message received: " + msg + " from " + client);
			break;
		}

	}

}
