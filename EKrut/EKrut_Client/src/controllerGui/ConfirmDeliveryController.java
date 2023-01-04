package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.CustomerStatus;
import common.DeliveryStatus;
import common.Message;
import common.PopupTypeEnum;
import common.ScreensNames;
import common.TaskType;
import controllerGui.HomePageController;
import entity.DeliveryEntity;
import entity.UserEntity;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import utils.TooltipSetter;

public class ConfirmDeliveryController {

    @FXML
    private Button submitBtn;

    @FXML
    private Label titleLbl;
    
    @FXML
    private TextField orderNumTxtField;
    
    @FXML
    private Label errMsgLbl;
    
    private String config="OL";
    private TooltipSetter tooltip;
    private String orderNum;
    private static String errorMsg="";
    static ClientController chat = HostClientController.chat; // define the chat for the controller
    private static Boolean isValidOrder = null;
    private String popUpTxt="";
    
    public void initialize() {
    	
    	
    	switch(config) {
    	case "EK":
    		setBtnAndTitle(submitBtn, "Collect", "Collect your order now","Order Pickup");
    		popUpTxt="Thank you for collecting your order!\r\nhave fun!";
    		break;
    	case "OL":
    		setBtnAndTitle(submitBtn, "Confirm", "Confirm delivery receipt","Confirm delivery");
    		popUpTxt="The approval was successfully received,\nbon appetit!";
    		break;
    	default:
    		break;
    	}
    	
    }

    @FXML
    void confirm(ActionEvent event) {
    	orderNum=orderNumTxtField.getText();
		validateOrderNumber();
		// wait for answer
		while (isValidOrder == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		if(isValidOrder)
			CommonFunctions.createPopup(PopupTypeEnum.Success, popUpTxt);
		else {
			errMsgLbl.setText(errorMsg);
			errMsgLbl.setDisable(false);
		}

	
    }
    private <T extends Button> void setBtnAndTitle(T btn, String btnText, String tooltiptext,String title) {
		btn.setText(btnText);
		titleLbl.setText(title);
		tooltip = new TooltipSetter(tooltiptext);
		btn.setTooltip(tooltip.getTooltip());
	}
    
    private void validateOrderNumber() {
    	String details[]=new String[2];

    	if (CommonFunctions.isNullOrEmpty(orderNum)) {
			errorMsg=("Please enter your order number\n");
			isValidOrder=false;
			return;
    	}
    	details[0]= NavigationStoreController.connectedUser.getId_num();
    	details[1]=orderNum;
    	switch (titleLbl.getText()) {
    	case "Order Pickup":
    		break;
    	case "Confirm delivery":
    		 isValidOrder = null;
    		chat.acceptObj(new Message(TaskType.RequestDeliveryFromServer,details));
    		break;
    
    	}
    	
    }
    
	public static void getDeliveryEntityFromServer(DeliveryEntity deliveryEntity) {
		
		if(deliveryEntity==null) {
			errorMsg=("Order doesn't exist\n");
			isValidOrder=false;
		}
		else if(deliveryEntity.getDeliveryStatus().equals(DeliveryStatus.pendingApproval)) {
			errorMsg=("Your order is still in process. Cannot be confirmed.\n");
			isValidOrder=false;
		}
		else {
			ArrayList<DeliveryEntity> de=new ArrayList<DeliveryEntity>();
			de.add(deliveryEntity);
			de.get(0).setCustomerStatus(CustomerStatus.APPROVED);
			isValidOrder=true;
			chat.acceptObj(new Message(TaskType.RequestUpdateDeliveries,de));

		}
	}
}
