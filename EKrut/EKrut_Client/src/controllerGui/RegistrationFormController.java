package controllerGui;

import java.util.ArrayList;

import Store.NavigationStoreController;
import client.ClientController;
import common.Message;
import common.TaskType;
import entity.RegistrationFormEntity;
import entity.SupplyReportEntity;
import entity.UserEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;

public class RegistrationFormController{
	private ToggleGroup radioToggleGroup;
	private ArrayList<TextField> dataArray;
    @FXML
    private Button cancelBtn;
    
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
    private ToggleGroup userSearchRadioToggleGroup;

    @FXML
    private TextField userSearchTxtField;

    @FXML
    private Button userSeatchBtn;


    ClientController chat = HostClientController.chat;
	protected static UserEntity reportDetails;	
	protected static boolean RecievedData = false;
    private static int numberOfForms;
    
    public void initialize() {
    	dataArray = new ArrayList<>();
    	dataArray.add(firstnameTxtField);
    	dataArray.add(lastnameTxtField);
    	dataArray.add(idnumberTxtField);
    	dataArray.add(emailTxtField);
    	dataArray.add(phonenumberTxtField);
    	dataArray.add(creditcardTxtField);
    	
    	membershipRadioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
                if (radioBtn != null) {
                    String radioBtnValue = radioBtn.getText();
                    switch (radioBtnValue) {
	                	case "Yes":
	                		membershipGridpaneBox.setVisible(true);
	                		break;
	                	case "No":
	                		membershipGridpaneBox.setVisible(false);
	                		break;
	                	default:
	                		break;
                    }
                }
            }
        });
    	userSearchRadioToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() 
        {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n)
            {
                RadioButton radioBtn = (RadioButton) userSearchRadioToggleGroup.getSelectedToggle();
                if (radioBtn != null) {
                    String radioBtnValue = radioBtn.getText();
                    switch (radioBtnValue) {
	                	case "ID":
	                		userSearchTxtField.setPromptText("Search By ID");
	                		break;
	                	case "Name":
	                		userSearchTxtField.setPromptText("Search By Name");
	                		break;
	                	default:
	                		break;
                    }
                }
            }
        });
    }
    
    @FXML
    void cancelBtnAction(ActionEvent event) {
    	// Go back to the previous page
    }

    @FXML
    void submitBtnAction(ActionEvent event) {
    	int missingTextFields = 0;
    	for (TextField field : dataArray) {
    		if (field.getText().equals("")) {
    			missingTextFields++;
        		field.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    			errorMsgLabel.setText("There are missing fields!");
    		}
    		else {
    			errorMsgLabel.setText("");
    			field.setStyle("-fx-border-color: none;");
    		}
    	}
    	if (missingTextFields == 0) {
        	int formID = ++numberOfForms;
        	RadioButton radioBtn = (RadioButton) membershipRadioToggleGroup.getSelectedToggle();
    		RegistrationFormEntity registrationForm = new RegistrationFormEntity(
    				formID,
    				firstnameTxtField.getText(),
    				lastnameTxtField.getText(),
    				idnumberTxtField.getText(),
    				emailTxtField.getText(),
    				creditcardTxtField.getText(),
    				radioBtn.getText()
    		);
    		if (radioBtn.getText().equals("Yes"))
    			registrationForm.setClubMemberID(formID + 1);
    		
    	}
    }
    
    @FXML
    void clearBtnAction(ActionEvent event) {
    	errorMsgLabel.setText("");
       	for (TextField field : dataArray) {
    		if (field.getText().equals("")) 
        		field.setStyle("-fx-border-color: none;");
    	}
    	for (TextField field : dataArray) {
    		field.clear();
    	}
    }
    
	public static void recieveDataFromServer(UserEntity report) {
		reportDetails = report;
		RecievedData = true;
		return;
	}
	
    @FXML
    void searchUserAction(ActionEvent event) {
    	RadioButton radioBtn = (RadioButton) userSearchRadioToggleGroup.getSelectedToggle();
    	if (userSearchTxtField.getText().equals("")) {
    		userSearchTxtField.setStyle("-fx-border-color: #ff1414; -fx-border-radius: 15;");
    		userSearchMsgLabel.setText("Please enter " + radioBtn.getText());
    	}
    	else {
    		userSearchTxtField.setStyle("-fx-border-color: none;");
    		userSearchMsgLabel.setText("");
    	}
    	String[] userSearchInput = {radioBtn.getText(), userSearchTxtField.getText()};
    	// Send a message to server
    	chat.acceptObj(new Message(TaskType.RequestUserInfoFromServerDB, userSearchInput));
		// wait for answer
		while (RecievedData == false) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(Arrays.asList(reportDetails.getReportsList())); 
		if (reportDetails.getId_num().equals("")) {
			System.out.println("Empty Report");
			userSearchMsgLabel.setText("User not found!");
			return;
		}
		userSearchMsgLabel.setText("User found!");
		firstnameTxtField.setText(reportDetails.getFirst_name());
		lastnameTxtField.setText(reportDetails.getLast_name());
		emailTxtField.setText(reportDetails.getEmail());
		idnumberTxtField.setText(reportDetails.getId_num());
		creditcardTxtField.setText(reportDetails.getCc_num());
		phonenumberTxtField.setText(reportDetails.getPhone_number());
    }
}
