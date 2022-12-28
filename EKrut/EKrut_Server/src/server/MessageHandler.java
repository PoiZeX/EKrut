package server;

import java.io.IOException;
import java.util.ArrayList;
import common.Message;
import common.TaskType;
import controllerDb.CommonDataDBController;
import controllerDb.DeliveryManagementDBController;
import controllerDb.ItemDBController;
import controllerDb.LoginDBController;
import controllerDb.PersonalMessagesDBController;
import controllerDb.ReportsDBController;
import controllerDb.UsersManagementDBController;
import entity.DeliveryEntity;
import entity.PersonalMessageEntity;
import entity.SubscriberEntity;
import entity.UserEntity;
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
		case SetUserLoggedIn:
			LoginDBController.setUserLoggedIn((UserEntity) obj);
			client.sendToClient("success logged in");
			break;
		case LoadSubscribers:
			SubscribersDbController.getTable(client);
			break;
		// There are similiar, should think about merging //
		case RequestReport:
			ReportsDBController.getReportEntity((String[]) obj, client);
			break;
		case RequestUserFromServerDB:
			LoginDBController.getUserEntity((String[]) obj, client);
			break;
		// Registartion Form //
		case RequestUserInfoFromServerDB:
			UsersManagementDBController.getUserByRoleFromDB((String[]) obj, client);
			break;
		case RequestChangeUserRoleTypeInDB:
			UsersManagementDBController.updateUserRoleType((String[]) obj, client);
			break;
		case RequesManagerInfoFromServerDB:
			UsersManagementDBController.getRegionManagerFromDB((String) obj, client);
			break;
		// *********************//
		case RequestUnapprovedUsers:
			UsersManagementDBController.getUnapprovedUsersEntity(client);
			break;
		case RequestItemsFromServer:
			ItemDBController.sendImgToClient(client);
			break;
		case RequestDeliveriesFromServer:
			DeliveryManagementDBController.getTable(client);
			break;
		case RequestUpdateDeliveries:
			DeliveryManagementDBController.updateDeliveryEntities((ArrayList<DeliveryEntity>) obj, client);
			break;
		case RequestUsersApproval:
			UsersManagementDBController.setUnapprovedUsersEntity((ArrayList<UserEntity>) obj, client);
			break;

		case InitRegions:
			CommonDataDBController.getAllRegionsFromDB(client);
			break;

		case RequestPersonalMessages:
			PersonalMessagesDBController.getAllPersonalMessages((UserEntity) obj, client);
			break;
		case InitMachines:
			CommonDataDBController.getAllMachinesFromDB(client);
			break;
		case SendPersonalMessage:
			PersonalMessagesDBController.setPersonalMessagesInDB((PersonalMessageEntity) obj);
			break;

		default:
			System.out.println("Cannot execute task: " + task.toString());
			break;
		}
	}
}
