package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.CustomerStatusEnum;
import common.DeliveryStatusEnum;
import common.Message;
import common.PopupTypeEnum;
import common.ScreensNamesEnum;
import common.TaskType;
import entity.DeliveryEntity;
import entity.PickupEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.AppConfig;
import utils.TooltipSetter;

public class ConfirmOnlineOrderController implements IScreen {

	@FXML
	private Button submitBtn;

	@FXML
	private TextField orderNumTxtField;

	@FXML
	private Label errMsgLbl;

	@FXML
	private ImageView img;
	private TooltipSetter tooltip;
	private String orderNum;
	private static String errorMsg = "";
	private static Boolean isValidOrder = null;
	private String popUpTxt = "";
	private static ClientController chat = HostClientController.getChat();

	/**
	 * initialize the screen according to the app configuration in case of "EK":
	 * order pickup in case of "OL": confirm delivery receipt
	 */
	@Override
	public void initialize() {
		switch (AppConfig.SYSTEM_CONFIGURATION) {
		case "EK":
			setBtnAndPicture(submitBtn, "Collect", "Collect your order now", "../styles/images/pickup.png");
			popUpTxt = "Thank you for collecting your order!\r\nhave fun!";
			break;
		case "OL":
			setBtnAndPicture(submitBtn, "Confirm", "Confirm delivery receipt", "../styles/images/delivery1.png");
			popUpTxt = "The approval was successfully received,\nbon appetit!";
			break;
		default:
			break;
		}

	}
	/**
	 * confirm the delivery or collect the order
	 * 
	 * @param event
	 */
	@FXML
	void confirm(ActionEvent event) {
		orderNum = orderNumTxtField.getText();
		errorMsg = "";
		validateOrderNumber();
		// wait for answer
		while (isValidOrder == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		errMsgLbl.setText(errorMsg);
		errMsgLbl.setDisable(false);
		if (isValidOrder) {
			CommonFunctions.createPopup(PopupTypeEnum.Success, popUpTxt);
			NavigationStoreController.getInstance().refreshStage(ScreensNamesEnum.HomePage);
		}
	}

	/**
	 * Generic method to handle buttons setup according to button text, tooltip and
	 * image
	 */
	private <T extends Button> void setBtnAndPicture(T btn, String btnText, String tooltiptext, String imgPath) {
		Image image = new Image(getClass().getResourceAsStream(imgPath));
		btn.setText(btnText);
		tooltip = new TooltipSetter(tooltiptext);
		btn.setTooltip(tooltip.getTooltip());
		img.setImage(image);
	}

	/**
	 * validation: Check that the customer enter order number Request from server to
	 * get the entity by customer number and order number
	 */
	private void validateOrderNumber() {
		String details[] = new String[2];
		if (CommonFunctions.isNullOrEmpty(orderNum)) {
			errorMsg = ("Please enter your order number\n");
			isValidOrder = false;
			return;
		}
		details[0] = NavigationStoreController.connectedUser.getId() + "";
		details[1] = orderNum;

		switch (AppConfig.SYSTEM_CONFIGURATION) {
		case "EK":
			isValidOrder = null;
			chat.acceptObj(new Message(TaskType.RequestPickupFromServer, details));
			break;
		case "OL":
			isValidOrder = null;
			chat.acceptObj(new Message(TaskType.RequestDeliveryFromServer, details));
			break;
		}
	}

	/**
	 * Validated the details of given deliveryEntity from server: the order exist
	 * for this customer the delivery status is outForDelivery the customerStatus!=
	 * APPROVED Request from server to update the deliveryEntity.customerStatus
	 * 
	 * @param deliveryEntity
	 */
	public static void getDeliveryEntityFromServer(DeliveryEntity deliveryEntity) {

		if (deliveryEntity == null) {
			errorMsg = ("Order doesn't exist\n");
			isValidOrder = false;
		} else if (deliveryEntity.getDeliveryStatus().equals(DeliveryStatusEnum.pendingApproval)) {
			errorMsg = ("Your order is still in process. Cannot be confirmed.\n");
			isValidOrder = false;
		} else if (deliveryEntity.getCustomerStatus().equals(CustomerStatusEnum.APPROVED)) {
			errorMsg = ("You already confirm this order.\n");
			isValidOrder = false;
		} else {
			ArrayList<DeliveryEntity> de = new ArrayList<DeliveryEntity>();
			de.add(deliveryEntity);
			de.get(0).setCustomerStatus(CustomerStatusEnum.APPROVED);
			isValidOrder = true;
			CommonFunctions.SleepFor(200, () -> {
				chat.acceptObj(new Message(TaskType.RequestUpdateDeliveries, de));
			});

		}
	}
	/***
	 * get the pickup status from the user after he types the order code for pickup.
	 * @param pickupEntity
	 */
	public static void getPickupAnswer(PickupEntity pickupEntity) {

		if (pickupEntity == null) {
			errorMsg = ("Order doesn't exist\n");
			isValidOrder = false;
		} else if (pickupEntity.getMachine_id() != AppConfig.MACHINE_ID) {
			errorMsg = "This isn't the correct machine";
			isValidOrder = false;
		} else {
			isValidOrder = true;
			CommonFunctions.SleepFor(200, () -> {
				chat.acceptObj(new Message(TaskType.updatePickupStatus, pickupEntity.getOrderId()));
			});
		}
	}

}
