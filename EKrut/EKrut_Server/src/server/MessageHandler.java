package server;

import java.io.IOException;
import java.util.ArrayList;

import common.Message;
import common.TaskType;
import controllerDb.*;
import entity.*;
import ocsf.server.ConnectionToClient;

public class MessageHandler {

	@SuppressWarnings("unchecked")
	public static void Handle(Message msg, ConnectionToClient client) throws IOException {
		TaskType task = msg.getTask();
		Object obj = msg.getObject();
		System.out.println("Message received: " + ((Message) msg).getTask().toString() + " from " + client);
		switch (task) {
		case ClientConnect:
			EchoServer.updateClientList(client, "Connect");
			break;
		case ClientDisconnect:
			EchoServer.updateClientList(client, "Disconnect");
			break;
		case SetUserLoggedIn:
			LoginDBController.setUserLoggedIn((UserEntity) obj);
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
		case RequestUserUpdateInDB:
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
		case RequestDeliveryFromServer:
			DeliveryManagementDBController.getDelivery((String[])obj, client);
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
		case InitMachinesInRegions:
		case InitMachinesSupplyUpdate:
			SupplyManagementDBController.getMachinesFromDB((String[]) obj, client);
			break;
		case InitMachines:
			CommonDataDBController.getAllMachinesFromDB(client);
			break;
		case SendPersonalMessage:
			PersonalMessagesDBController.setPersonalMessagesInDB((PersonalMessageEntity) obj);
			break;

		case RequestItemsInMachine:
			SupplyManagementDBController.getMachineItems((int) obj, client);
			break;
		case RequestItemsWithMinAmount:
			SupplyManagementDBController.getMachineItemsWithMinAmount((int) obj, client);
			break;
		case RequestProssecedItemsInMachine:
			SupplyManagementDBController.getProcessedMachineItems((int[]) obj, client);
			break;
		case RequestInsertNewSale:
			MarketingManagerDBController.insertSaleEntities((SaleEntity) obj, client);
			break;
		case RequestUpdateMachineMinAmount:
			SupplyManagementDBController.updateMachineMinAmount((MachineEntity) obj, client);
			break;
		case RequestItemsInMachineUpdateFromServer:
			SupplyManagementDBController.updateItemsInMachine((ArrayList<ItemInMachineEntity>) obj, client);
			break;
		case RequestUpdateSales:
			MarketingManagerDBController.updateSaleEntities((ArrayList<SaleEntity>) obj, client);
			break;
		case RequestSalesFromServer:
			MarketingManagerDBController.getSales((String)obj,client);
			break;
		case RequestSupplyWorkers:
			SupplyManagementDBController.getSupplyWorkers(client);
			break;
			
		case isMemberFirstPurchase:
			OrderDBController.isMemberFirstPurchase((UserEntity)obj, client);
			break;
		case UpdateItemsWithAnswer:
			SupplyManagementDBController.updateItemsInMachine((ArrayList<ItemInMachineEntity>) obj, client);
			break;
		case NewOrderCreation:
			OrderDBController.insertOrderEntity((OrderEntity)obj, client);
			break;
			
		case AddNewDelivery:
			DeliveryManagementDBController.insertDeliveryEntity((DeliveryEntity) obj, client);
			break;
		case RequestActiveSales:
			MarketingManagerDBController.getActiveSalesByRegion((String)obj, client);
			break;
		default:
			System.out.println("Cannot execute task: " + task.toString());
			break;
		}
	}
}
