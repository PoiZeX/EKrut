package controllerGui;

import java.util.ArrayList;
import java.util.regex.Pattern;

import Store.DataStore;
import Store.NavigationStoreController;
import client.ClientController;
import common.CommonFunctions;
import common.Message;
import common.RolesEnum;
import common.TaskType;
import controller.SMSMailHandlerController;
import entity.RegistrationFormEntity;
import entity.SupplyReportEntity;
import entity.UserEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import utils.AppConfig;
import utils.TooltipSetter;

public class RegistrationFormController implements IScreen {
	private ArrayList<TextField> dataArray;

    @FXML
    private Button clearBtn;

    @FXML
    private TextField creditcardTxtField;

    @FXML
    private TextField emailTxtField;

    @FXML
    private TextField firstnameTxtField;

    @FXML
    private TextField idnumberTxtField;

    @FXML
    private TextField lastnameTxtField;
    
    @FXML
    private TextField usernameTxtField;
    
    @FXML
    private Label usernameErrorLabel;

    @FXML
    private TextField passwordTxtField;
    
    @FXML
    private Label passwordErrorLabel;

    @FXML
    private GridPane membershipGridpaneBox;

    @FXML
    private ToggleGroup membershipRadioToggleGroup;

    @FXML
    private TextField phonenumberTxtField;

    @FXML
    private RadioButton radioNoMember;

    @FXML
    private RadioButton radioYesMember;

    @FXML
    private Button submitBtn;
    
    @FXML
    private Label errorMsgLabel;
    
    @FXML
    private Label userSearchMsgLabel;

    @FXML
    private TextField userSearchTxtField;

    @FXML
    private Button userSeatchBtn;
    
    @FXML
    private ComboBox<String> regionComboBox;

    ClientController chat = HostClientController.getChat();
	protected static Object recivedData;
	protected static boolean isDataRecived = false;
	private String roleType;
	private boolean creditCardChecker = false;
	/**

	Initializes the fields and sets the options for the ComboBoxes and TextFields.
	Also adds a listener for the membership RadioToggleGroup to handle the visibility of the membership gridpane box.
	And adds a listener for the credit card TextField to check the format of the credit card number.
	*/
	@Override
    public void initialize() {
    	dataArray = new ArrayList<>();
    	dataArray.add(firstnameTxtField);
    	dataArray.add(lastnameTxtField);
    	dataArray.add(idnumberTxtField);
    	dataArray.add(emailTxtField);
    	dataArray.add(phonenumberTxtField); 
    	dataArray.add(creditcardTxtField);
    	dataArray.add(usernameTxtField);
    	dataArray.add(passwordTxtField);
    	dataArray.add(userSearchTxtField);
    	regionComboBox.getItems().addAll("North", "Center", "South");
    	regionComboBox.setStyle("-fx-background-radius: 15px;");
    	roleType = "member";
    	membershipRadioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
                if (radioBtn != null) {
                    String radioBtnValue = radioBtn.getText();
                    switch (radioBtnValue) {
	                	case "Yes":
	                		roleType = RolesEnum.member.toString();
	                		membershipGridpaneBox.setVisible(true);
	                		break;
	                	case "No":
	                		roleType = RolesEnum.registered.toString();
	                		membershipGridpaneBox.setVisible(false);
	                		break;
	                	default:
	                		break;
                    }
                }
            }
        });
    	creditcardTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
    		if (newValue != null) {
	    	    if (!creditcardTxtField.getText().matches("[0-9]{16}")) {
	    	    	creditCardChecker = false;
	    	    	creditcardTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
	    	    }
	    	    else if (newValue.length() == 0) {
	    	    	creditcardTxtField.setStyle("-fx-border-color: none;");
	    	    }
	    	    else {
	    	    	creditCardChecker = true;
	    			creditcardTxtField.setStyle("-fx-border-color: none;");
	    	    }
    		}
    	});
    } 
	/**
		This method is the event handler for the submit button in the JavaFX application.
		When the button is clicked, this method is called and performs several actions:
		Sets the text color of the "errorMsgLabel" to red.
		Initializes a variable "missingTextFields" to 0.
		Loops through a list of "TextField" objects called "dataArray" and checks if any of the fields are empty.
		If a field is empty and it is not the "userSearchTxtField", the method increments the "missingTextFields" variable,
		sets the border color of the field to red, and sets the error message text to "There are missing fields!"
		If the creditCardChecker variable is false, the method sets the error message text to "Invalid Credit Card Number!"
		and sets the border color of the "creditcardTxtField" to red.
		If there are no missing text fields, the method calls the "updateUser" method and pass the recivedData as parameter.
		@param event The action event that triggers this method, when the submit button is clicked.
	*/
    @FXML
    void submitBtnAction(ActionEvent event) {
    	errorMsgLabel.setStyle("-fx-text-fill: #ff1414");
    	int missingTextFields = 0;
    	
    	// Check for empty text fields
    	for (TextField field : dataArray) {
    		if (field.getText() != null && field.getText().equals("")) {
    			if (field != userSearchTxtField) {
        			missingTextFields++;
            		field.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    			}
    		}
    		else {
    			errorMsgLabel.setText("");
    			field.setStyle("-fx-border-color: none;");
    		}
    	}
    	if (missingTextFields > 0) {
			errorMsgLabel.setText("There are missing fields!");
			regionComboBox.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 1;");
			return;
    	}
    	else if (creditCardChecker == false) {
    		errorMsgLabel.setText("Invalid Credit Card Number!");
    		creditcardTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    		return;
    	}
    	else if (missingTextFields == 0) {
    		updateUser((UserEntity)recivedData);
    	}
    }
    /**
	    This method updates the user's details in the database.
	    It takes the user's id number, role type, region, and credit card number,
	    and sends them to the server using the "chat" object's "acceptObj" method, along with a request to update the user in the database.
	    It then waits for a response from the server, and if the response is successful, it displays a message indicating that the user was updated successfully,
	    and sends a message to the region manager with the updated region.
	    If the response is not successful, it displays an error message.
	    @param user The UserEntity object that contains the user's details.
    */
    private void updateUser(UserEntity user) {
		if (!CommonFunctions.isNullOrEmpty(user.getRole_type().toString()) && !user.getRole_type().equals(RolesEnum.user)) 
			roleType = user.getRole_type().toString();
    	String[] detailsToUpdate = {idnumberTxtField.getText(), roleType, regionComboBox.getValue(), creditcardTxtField.getText()};
    	chat.acceptObj(new Message(TaskType.RequestUserUpdateInDB, detailsToUpdate));
    	
		// wait for answer
		while (isDataRecived == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (recivedData instanceof UserEntity && ((UserEntity) recivedData).getId_num().equals(detailsToUpdate[0])) {
			errorMsgLabel.setStyle("-fx-text-fill: #000000");
			errorMsgLabel.setText("User updated successuflly!");
			sendMessageToRegionManager(((UserEntity)recivedData).getRegion());
			roleType = "member";
			clearBtnAction(null);
			errorMsgLabel.setStyle("-fx-text-fill: #000000");
			errorMsgLabel.setText("User updated successuflly!");
			return;
		}
		else {
			errorMsgLabel.setText("A problem occured, please try again!");
		}
    }
    /**
	    This method sends a message to the region manager of the specified region.
	    It first retrieves the region manager's details using the "getRegionManager" method, passing the region as a parameter.
	    Then it sends a message to the manager via SMS or mail, using the "SMSMailHandlerController.SendSMSOrMail" method,
	    with the sender being "System", the recipient being the region manager, the subject being "Need your action" and the message being "New user has been signed up please go to 'Users Approval' to review and approve the request".
	    @param region The region for which the manager needs to be notified.
    */
    private void sendMessageToRegionManager(String region) {
		UserEntity manager = getRegionManager(region);
		
    	// send user to manager approval
    	SMSMailHandlerController.SendSMSOrMail("System", manager, "Need your action", "New user has been signed up\nplease go to 'Users Approval' to review and approve the request");
    }
    /**
	    This method retrieves the region manager's details for the specified region from the server's database.
	    It first sets the "isDataRecived" and "recivedData" variables to their initial values.
	    It then sends a message to the server, using the "chat" object's "acceptObj" method, with the task type being "RequesManagerInfoFromServerDB"
	    and the region as a parameter.
	    It then waits for a response from the server, and if the response is a UserEntity object, it returns the UserEntity.
	    Otherwise, it returns null.
	    @param region The region for which the manager's details need to be retrieved.
	    @return UserEntity object containing the manager's details, or null if the response is not valid.
    */
    private UserEntity getRegionManager(String region) {
    	// reset
    	isDataRecived = false;
    	recivedData = null;
    	
    	chat.acceptObj(new Message(TaskType.RequesManagerInfoFromServerDB, region));
    	
    	// wait for answer
		while (isDataRecived == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(recivedData instanceof UserEntity && (UserEntity)recivedData != null) {
			return (UserEntity)recivedData;
		}
		return null;
    }
    /**
	    This method is the event handler for the clear button in the JavaFX application.
	    When the button is clicked, this method is called and performs several actions:
	    Set the roleType variable to "member"
	    Reset the prompt text of the regionComboBox and clear the current selection.
	    Clear the text of the errorMsgLabel and userSearchMsgLabel.
	    Loops through a list of "TextField" objects called "dataArray" and clear the text in the fields and enable the fields.
	    And reset the style of the fields to none.
	    @param event The action event that triggers this method, when the clear button is clicked.
    */
    @FXML
    void clearBtnAction(ActionEvent event) {
		roleType = "member";
    	regionComboBox.setStyle("-fx-border-color: none;");
    	regionComboBox.setPromptText("Region");
    	regionComboBox.getSelectionModel().clearSelection();
    	errorMsgLabel.setText("");
    	userSearchMsgLabel.setText("");
       	for (TextField field : dataArray) {
        	passwordErrorLabel.setText("");
        	usernameErrorLabel.setText("");
       		field.clear();
       		field.setDisable(false);
    		if (field.getText().equals("")) 
        		field.setStyle("-fx-border-color: none;");
    	}
    }
    
    /** 
     * receive data from server
     * @param user
     */
	public static void receiveDataFromServer(Object obj) {
		recivedData = obj;
		isDataRecived = true;
		return;
	}
	/**
		This method is the event handler for the search button in the JavaFX application.
		When the button is clicked, this method is called and performs several actions:
		Check if the userSearchTxtField is empty, if true set the border color to red and display an error message
		Else, sends a message to the server, using the "chat" object's "acceptObj" method, with the task type being "RequestUserInfoFromServerDB"
		and the user id as a parameter.
		It then waits for a response from the server, and if the response is a UserEntity object, it fills the text fields with the user's details.
		If the user is not found, it displays an error message.
		If the user is not a member, it disables the regionComboBox
		display success message
		@param event The action event that triggers this method, when the search button is clicked.
	*/
    @FXML
    void searchUserAction(ActionEvent event) {
    	if (userSearchTxtField.getText().equals("")) {
    		userSearchTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    		userSearchMsgLabel.setText("User ID is missing");
    		userSearchMsgLabel.setStyle("-fx-text-fill: #ff1414");
    	}
    	else {
    		userSearchTxtField.setStyle("-fx-border-color: none;");
    		userSearchMsgLabel.setText("");
        	String[] userSearchInput = {userSearchTxtField.getText()};
        	
        	// Send a message to server
        	chat.acceptObj(new Message(TaskType.RequestUserInfoFromServerDB, userSearchInput));
        	
    		// wait for answer
			while (isDataRecived == false) {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
    		UserEntity userDetails = ((UserEntity)recivedData);
    		if (userDetails.getId_num().equals("")) {
    			//System.out.println("Empty Report");
    			userSearchMsgLabel.setText("User not found!");
    			userSearchMsgLabel.setStyle("-fx-text-fill: #ff1414");
    			return;
    		}
    		else if (!CommonFunctions.isNullOrEmpty(userDetails.getRole_type().toString()) && !userDetails.getRole_type().equals(RolesEnum.user)) {
    			regionComboBox.setDisable(true);
    		}

    		// Clear errors if found a user
    		errorMsgLabel.setText("");
           	for (TextField field : dataArray) {
            	passwordErrorLabel.setText("");
            	usernameErrorLabel.setText("");
           		field.clear();
           		field.setDisable(true);
        		if (field.getText().equals("")) 
            		field.setStyle("-fx-border-color: none;");
        	}	
           	
           	// success message
    		userSearchMsgLabel.setText(userDetails.getFirst_name() + " was found!");
    		userSearchMsgLabel.setStyle("-fx-text-fill: #000000");
    		
    		// fill fields
    		creditcardTxtField.setText("");
    		firstnameTxtField.setText(userDetails.getFirst_name());
    		lastnameTxtField.setText(userDetails.getLast_name());
    		emailTxtField.setText(userDetails.getEmail());
    		idnumberTxtField.setText(userDetails.getId_num());
    		creditcardTxtField.setText(userDetails.getCc_num());
    		phonenumberTxtField.setText(userDetails.getPhone_number());
    		passwordTxtField.setText(userDetails.getPassword());
    		usernameTxtField.setText(userDetails.getUsername());
    		regionComboBox.setValue(userDetails.getRegion());
       		creditcardTxtField.setDisable(false);  // the only one if need to change
    	}
    }
}
