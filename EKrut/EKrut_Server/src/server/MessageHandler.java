package server;

import java.io.IOException;
import java.util.ArrayList;

import common.Message;
import common.TaskType;
import controllerDb.CommonDataDBController;
import controllerDb.DeliveryManagementDBController;
import controllerDb.ItemDBController;
import controllerDb.LoginDBController;
import controllerDb.MarketingManagerDBController;
import controllerDb.PersonalMessagesDBController;
import controllerDb.ReportsDBController;
import controllerDb.SupplyManagmentDBController;
import controllerDb.SupplyReportDBController;
import controllerDb.UsersManagementDBController;
import entity.DeliveryEntity;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.SaleEntity;
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
		case RequestSupplyReport:
			SupplyReportDBController.getSupplyReportEntity((String[]) obj, client);
			break;
		case RequestUserFromServerDB:
			LoginDBController.getUserEntity((String[]) obj, client);
			break;
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
			PersonalMessagesDBController.getClientReportEntity((UserEntity) obj, client);
		case InitMachines:
			CommonDataDBController.getAllMachinesFromDB(client);
			break;
		case RequestItemsInMachine:
			SupplyManagmentDBController.getMachineItems((int)obj,client);
			break;
		case RequestProssecedItemsInMachine:
			SupplyManagmentDBController.getProcessedMachineItems((int [])obj, client);
			break;
		case RequestInsertNewSale:
			MarketingManagerDBController.insertSaleEntities((SaleEntity) obj, client);
			break;
		case RequestUpdateMachineMinAmount:
			SupplyManagmentDBController.updateMachineMinAmount((MachineEntity)obj,client);
			break;
		case RequestItemsInMachineUpdateFromServer:
			SupplyManagmentDBController.updateItemsInMachineUpdate((ArrayList<ItemInMachineEntity>) obj, client);
			break;
		case RequestUpdateSales:
			break;
		case RequestSalesFromServer:
			MarketingManagerDBController.getSales(client);
			break;
		case RequestSupplyWorkers:
			SupplyManagmentDBController.getSupplyWorkers(client);
			break;
		default:
			System.out.println("Cannot execute task: " + task.toString());
			break;
		}
	}
}
