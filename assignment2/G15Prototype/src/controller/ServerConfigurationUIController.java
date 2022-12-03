package controller;

import utils.ConsoleStream;

import java.awt.Event;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import server.EKrutServerUI;
import common.CommonFunctions;

public class ServerConfigurationUIController {

	@FXML
	private TextField txtIP;

	@FXML
	private TextField txtPort;

	@FXML
	private TextField txtDBName;

	@FXML
	private PasswordField txtDBPassword;

	@FXML
	private TextField txtDBUsername;

	@FXML
	private Button connectBtn;

	@FXML
	private Button disconnectBtn;

	@FXML
	private TableView<?> consoleConnections;

	@FXML
	private TextArea consoleOutput;

	private PrintStream printStream;


	@FXML
	void connectToDB(ActionEvent event) {	
		if(CommonFunctions.isNullOrEmpty(txtIP.getText()) ||
				CommonFunctions.isNullOrEmpty(txtPort.getText()) ||
				CommonFunctions.isNullOrEmpty(txtDBName.getText()) ||
				CommonFunctions.isNullOrEmpty(txtDBUsername.getText()) ||
				CommonFunctions.isNullOrEmpty(txtDBPassword.getText()))
		{
			System.out.println("Error. one or more fields are empty");  
			return;
		}
		EKrutServerUI.runServer(this.txtPort.getText(), this.txtDBName.getText(), this.txtDBUsername.getText(),
				this.txtDBPassword.getText());
		connectBtn.setDisable(true);
		disconnectBtn.setDisable(false);
		setDisableTextFieldValues(true);
	}

	@FXML
	void disconnectFromDB(ActionEvent event) {
		EKrutServerUI.disconnect();
		connectBtn.setDisable(false);
		disconnectBtn.setDisable(true);
		setDisableTextFieldValues(false);

	}

	@FXML
	public void initialize() throws Exception { // Setup screen before launching view
		this.txtIP.setText(getIPValue());
		this.txtPort.setText("5555");
		this.txtDBName.setText("jdbc:mysql://localhost/ekrut?serverTimezone=IST");
		this.txtDBUsername.setText("root");
		this.txtDBPassword.setText("root");
		this.disconnectBtn.setDisable(true);
		changeConsoleToUI();

	}

	private String getIPValue() throws Exception {
		String ip = null;
		ip = Inet4Address.getLocalHost().getHostAddress();
		return ip;
	}

	private void setDisableTextFieldValues(boolean b) {
		this.txtDBName.setDisable(b);
		this.txtIP.setDisable(b);
		this.txtPort.setDisable(b);
		this.txtDBPassword.setDisable(b);
		this.txtDBUsername.setDisable(b);
	}

	@FXML
	void changeConsoleToUI() {
		this.printStream = new PrintStream((OutputStream) new ConsoleStream(this.consoleOutput));
		System.setOut(this.printStream);
		System.setErr(this.printStream);
	}

}
