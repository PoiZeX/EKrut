package server;

import java.io.IOException;
import java.util.ArrayList;

import java.util.Map;

import common.Message;

import controllerDb.*;
import entity.*;
import enums.TaskType;
import ocsf.server.ConnectionToClient;

/**
 * The Class MessageHandler.
 */
public class MessageHandler {
	
	/**
	 * Handle.
	 *
	 * @param msg the msg
	 * @param client the client
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("unchecked")
	public static void Handle(Message msg, ConnectionToClient client) throws IOException {
		TaskType task = msg.getTask();
		Object obj = msg.getObject();
		ItemInMachineDBController itemInMachineDBController = new ItemInMachineDBController();
		System.out.println("Message received: " + ((Message) msg).getTask().toString() + " from " + client);
		switch (task) {
		case ClientConnect:
			EchoServer.updateClientList(client, "Connect");
			break;
		case ClientDisconnect:
			EchoServer.updateClientList(client, "Disconnect");
			break;
//---------------------------------------LOGIN--------------------------------------------------------
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
//------------------------------------PERSONAL_MESSAGEES------------------------------------------------------
		case RequestPersonalMessages:
			PersonalMessagesDBController.getAllPersonalMessages((UserEntity) obj, client);
			break;
		case SendPersonalMessage:
			PersonalMessagesDBController.setPersonalMessagesInDB((PersonalMessageEntity) obj);
			break;
//---------------------------------------REGISTRATION--------------------------------------------------------
		case RequestUserInfoFromServerDB:
			UsersManagementDBController.getUserByUsernameOrIDFromDB((String[]) obj, client);
			break;
		case RequestUserUpdateInDB:
			UsersManagementDBController.updateUserInDB((String[]) obj, client);
			break;
		case RequesManagerInfoFromServerDB:
			UsersManagementDBController.getRegionManagerFromDB((String) obj, client);
			break;
		// *********************//
		case RequestUnapprovedUsers:
			UsersManagementDBController.getUnapprovedUsersEntity(client);
			break;
		case RequestUsersApproval:
			UsersManagementDBController.setUnapprovedUsersEntity((ArrayList<UserEntity>) obj, client);
			break;
//-------------------------------------------COMMON_DATA--------------------------------------------------
		case InitRegions:
			CommonDataDBController.getAllRegionsFromDB(client);
			break;
		case InitMachines:
			CommonDataDBController.getAllMachinesFromDB(client);
			break;
		case InitUsers:
			UsersManagementDBController.getAllUsersFromDB(client);
			break;
//----------------------------------------DELIVERY---------------------------------------------------------
		case RequestDeliveriesFromServer:
			DeliveryManagementDBController.getTable((String) obj, client);
			break;
		case RequestDeliveryFromServer:
			DeliveryManagementDBController.getDelivery((String[]) obj, client);
			break;
		case RequestUpdateDeliveries:
			DeliveryManagementDBController.updateDeliveryEntities((ArrayList<DeliveryEntity>) obj, client);
			break;
		case RequestItemsFromServer:
			ItemDBController itemDB = new ItemDBController();
			itemDB.sendImgToClient(client);
			break;

//-------------------------------------------------SALES--------------------------------------------------------
		case RequestUpdateSales:
			MarketingManagementDBController.updateSaleEntities((ArrayList<SaleEntity>) obj, client);
			break;
		case RequestInsertNewSale:
			MarketingManagementDBController.insertSaleEntities((SaleEntity) obj, client);
			break;
		case RequestSalesFromServer:
			MarketingManagementDBController.getSales((String) obj, client);
			break;
		case RequestActiveSales:
			MarketingManagementDBController.getActiveSalesByRegion((String) obj, client);
			break;

//----------------------------------------SUPPLY--------------------------------------------------------
		case InitMachinesInRegions:
		case InitMachinesSupplyUpdate:
			MachineDBController.getMachinesFromDB((String[]) obj, client);
			break;
		case RequestItemsWithMinAmount:
			itemInMachineDBController.getMachineItemsWithMinAmount((int) obj, client);
			break;
		case RequestProssecedItemsInMachine:
			itemInMachineDBController.getProcessedMachineItems((int[]) obj, client);
			break;
		case RequestUpdateMachineMinAmount:
			MachineDBController.updateMachineMinAmount((MachineEntity) obj, client);
			break;
		case RequestItemsInMachineCallStatusUpdate:
			SupplyManagementDBController.updateCallsStatus((ArrayList<ItemInMachineEntity>) obj, client);
			break;
		case RequestItemsInMachineRestockFromServer:
			SupplyManagementDBController.restockItemsInMachine((ArrayList<ItemInMachineEntity>) obj, client);
			break;
		case RequestSupplyWorkers:
			UsersManagementDBController.getSupplyWorkers(client);
			break;
		case RequestAllItemsNameById:
			client.sendToClient(new Message(TaskType.ReceiveAllItemsNameById,
					ItemDBController.getAllItemsNameById((ArrayList<Integer>) obj)));
			break;
//-------------------------------------------ORDERES-------------------------------------------------------			
		case RequestItemsInMachine:
			itemInMachineDBController.getMachineItems((int) obj, client);
			break;
		case isMemberFirstPurchase:
			OrderDBController.isMemberFirstPurchase((UserEntity) obj, client);
			break;
		case UpdateItemsWithAnswer:
			ItemInMachineDBController.decreaseItemsAmountInMachine((Map<ItemInMachineEntity, Integer>) obj, client);
			break;
		case NewOrderCreation:
			OrderDBController.insertOrderEntity((OrderEntity) obj, client);
			break;
		case updatePickupStatus:
			PickupDBController.updatePickupStatus((int) obj, client);
			break;
		case RequestPickupFromServer:
			PickupDBController.isPickupValid((String[]) obj, client);
			break;
		case UpdateItemsUnderMin:
			ItemInMachineDBController.increaseItemsUnderMin((ArrayList<int[]>) obj, client);
			break;
		case RequestUserByOrderIdFromServer:
			CommonDataDBController.getUserByOrderId((int) obj, client);
			break;
		case InsertNewPickup:
			PickupDBController.insertPickupEntity((PickupEntity) obj, client);
			break;
		case AddNewDelivery:
			DeliveryManagementDBController.insertDeliveryEntity((DeliveryEntity) obj, client);
			break;

		default:
			System.out.println("Cannot execute task: " + task.toString());
			break;
		}
	}

}
