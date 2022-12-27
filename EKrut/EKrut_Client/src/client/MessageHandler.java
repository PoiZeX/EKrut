package client;

import ocsf.client.*;
import common.ChatIF;
import common.CommonData;
import common.Message;
import common.TaskType;
import controller.ItemsController;
import controllerGui.LoginController;
import controllerGui.MarketingWorkerController;
import controllerGui.OrdersReportController;
import controllerGui.PersonalMessagesController;
import controllerGui.SupplyManagmentController;
import controllerGui.SupplyReportController;
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

	public static void Handle(ChatClient thisClient,Message msg) {
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
		case ReceiveOrderReport:
			OrdersReportController.recieveDataFromServer((OrderReportEntity) obj);
			break;
		case ReceiveClientsReport:
			ClientsReportController.recieveDataFromServer((ClientsReportEntity) obj);
			break;
		case RequestSupplyReport:
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
			SupplyManagmentController.recevieItemsInMachine((ArrayList<ItemInMachineEntity>) obj);
			break;
		case ReceiveSalesFromServer:
			MarketingWorkerController.getSalesEntityFromServer((SaleEntity) obj);
			break;
		
			
		default:
			break;
		}
	}

}
