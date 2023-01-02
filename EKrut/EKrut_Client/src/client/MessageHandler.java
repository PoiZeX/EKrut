package client;

import common.CommonData;
import common.Message;
import common.TaskType;
import controller.ItemsController;
import controllerGui.LoginController;
import controllerGui.MarketingWorkerController;
import controllerGui.OrdersReportController;
import controllerGui.PersonalMessagesController;
import controllerGui.RegistrationFormController;
import controllerGui.SupplyManagementController;
import controllerGui.SupplyReportController;
import controllerGui.SupplyUpdateController;
import controllerGui.UsersManagementController;
import controllerGui.ClientsReportController;
import controllerGui.DeliveryManagementController;
import java.io.*;
import java.util.ArrayList;
import Store.NavigationStoreController;
import entity.OrderReportEntity;
import entity.PersonalMessageEntity;
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
		case ReceiveChangeUserRoleTypeInDB:
		case ReceiveManagerInfoFromServerDB:
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
			DeliveryManagementController.getDeliveryEntityFromServer((DeliveryEntity) obj);
			break;
		case ReceivePersonalMessages:
			PersonalMessagesController.getAllMessagesFromServer((ArrayList<PersonalMessageEntity>) obj);
			break;
		case InitMachines:
			CommonData.recieveMachines((ArrayList<MachineEntity>) obj);
			break;
		case ReceiveItemsInMachine:
			SupplyManagementController.recevieItemsInMachine((ArrayList<ItemInMachineEntity>) obj);
			SupplyUpdateController.recevieItemsInMachine((ArrayList<ItemInMachineEntity>) obj);
			break;
		case ReceiveSalesFromServer:
			MarketingWorkerController.getSalesEntityFromServer((SaleEntity) obj);
			break;
		case ReceiveSupplyWorkersFromServer:
			SupplyManagementController.recevieSupplyWorkers((ArrayList<UserEntity>) obj);
			break;
		default:
			break;
		}
	}

}
