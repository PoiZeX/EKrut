package client;

import common.CommonData;
import common.Message;
import common.TaskType;
import controller.ItemsController;
import controller.OrderController;
import controllerGui.LoginController;
import controllerGui.CreateNewSaleController;
import controllerGui.SalesManagementController;
import controllerGui.OrdersReportController;
import controllerGui.PersonalMessagesController;
import controllerGui.RegistrationFormController;
import controllerGui.ReviewOrderController;
import controllerGui.SupplyManagementController;
import controllerGui.SupplyReportController;
import controllerGui.SupplyUpdateController;
import controllerGui.UsersManagementController;

import controllerGui.ViewCatalogController;
import controllerGui.ClientsReportController;
import controllerGui.ConfirmOnlineOrderController;
import controllerGui.DeliveryManagementController;
import java.io.*;
import java.util.ArrayList;
import Store.NavigationStoreController;
import entity.OrderReportEntity;
import entity.PersonalMessageEntity;
import entity.PickupEntity;
import entity.SaleEntity;
import entity.DeliveryEntity;
import entity.ItemEntity;
import entity.ItemInMachineEntity;
import entity.MachineEntity;
import entity.ClientsReportEntity;
import entity.SupplyReportEntity;
import entity.UserEntity;

public class MessageHandler {

	/**
	 * Handle the messages from server and navigates them to right methods
	 * @param thisClient
	 * @param msg
	 */
	public static void Handle(ChatClient thisClient, Message msg) {
		Message msgFromServer = (Message) msg;
		TaskType task = msgFromServer.getTask();
		Object obj = msgFromServer.getObject();

		// ---- Messages ---- //
		switch (task) {
		case ServerDisconnect:
			try {
				thisClient.sendToServer(new Message(TaskType.ClientDisconnect));
			} catch (IOException e) {
				e.printStackTrace();
			}
			NavigationStoreController.closeAllScreens(); // force closing since server is disconnected
			break;
		case InitRegions:
			CommonData.recieveRegions((ArrayList<String>) obj);
			break;
		case ReceiveUserFromServerDB:
			LoginController.validUserFromServer((UserEntity) obj);
			break;
		// Registration Form
		case ReceiveUserInfoFromServerDB:
		case ReceiveUserUpdateInDB:
			RegistrationFormController.receiveDataFromServer(obj);
			break;
		case ReceiveManagerInfoFromServerDB:
			ReviewOrderController.getDataFromServer(obj);
			RegistrationFormController.receiveDataFromServer(obj);
			break;
		//
		case ReceiveOrderReport:
			OrdersReportController.recieveDataFromServer((OrderReportEntity) obj);
			break;
		case ReceiveClientsReport:
			ClientsReportController.recieveDataFromServer((ClientsReportEntity) obj);
			break;
		case ReceiveSupplyReport:
			SupplyReportController.recieveDataFromServer((SupplyReportEntity) obj);
			break;
		case ReceiveUnapprovedUsers:
			UsersManagementController.recieveUnapprovedUsers((ArrayList<UserEntity>) obj);
			break;
		case ReceiveItemsFromServer:
			ItemsController.getItemsFromServer((ItemEntity) obj);
			break;
		case ReceiveDeliveriesFromServer:
			DeliveryManagementController.getDeliveryEntityFromServer((ArrayList<DeliveryEntity>) obj);
			break;
		case ReceiveDeliveryFromServer:
			ConfirmOnlineOrderController.getDeliveryEntityFromServer((DeliveryEntity) obj);
			break;
		case ReceivePersonalMessages:
			PersonalMessagesController.getAllMessagesFromServer((ArrayList<PersonalMessageEntity>) obj);
			break;
		case InitMachines:
			CommonData.recieveMachines((ArrayList<MachineEntity>) obj);
			break;
		case ReceiveItemsInMachine:
			navigateItems((ArrayList<ItemInMachineEntity>) obj);
			break;
		case ReceiveSalesFromServer:
			SalesManagementController.getSalesEntityFromServer((ArrayList<SaleEntity>) obj);
			break;
		case ReceiveSupplyWorkersFromServer:
			SupplyManagementController.recevieSupplyWorkers((ArrayList<UserEntity>) obj);
			break;
		case InitMachinesSupplyUpdate:
			SupplyUpdateController.getAllMachines((ArrayList<MachineEntity>) obj);
			break;
		case InitMachinesInRegions:
			SupplyManagementController.getMachinesInRegion((ArrayList<MachineEntity>) obj);
			break;
		case ReviewOrderServerAnswer:
			ReviewOrderController.getDataFromServer(obj);
			break;

		case ReceiveActiveSales:
			OrderController.setActiveSales((ArrayList<SaleEntity>) obj);
			break;
		case ValidPickupAnswer:
			ConfirmOnlineOrderController.getPickupAnswer((PickupEntity)obj);
			break;
		case ReceiveUserByOrderIdFromServerDB:
			break;
		case InsertSaleAnswer:
			CreateNewSaleController.isSaleExist((Boolean)obj);
			break;
		default:
			break;
		}
	}

	private static void navigateItems(ArrayList<ItemInMachineEntity> obj) {
		Object currentController = NavigationStoreController.getInstance().getController();
		if (currentController instanceof SupplyManagementController) 
			SupplyManagementController.recevieItemsInMachine((ArrayList<ItemInMachineEntity>) obj);
		else if (currentController instanceof SupplyUpdateController)
			SupplyUpdateController.recevieItemsInMachine((ArrayList<ItemInMachineEntity>) obj);
		else
			ViewCatalogController.recevieItemsInMachine((ArrayList<ItemInMachineEntity>) obj);
	}

}
