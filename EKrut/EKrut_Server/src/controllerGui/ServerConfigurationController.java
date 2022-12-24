package controllerGui;

import utils.ConsoleStream;

import java.awt.Desktop;
import java.awt.Event;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import mysql.MySqlClass;
import server.EchoServer;
import server.ServerUI;
import common.CommonFunctions;
import controllerDb.UsersSimulationDBController;
import entity.ConnectedClientEntity;

public class ServerConfigurationController {

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
	private Button importUsersBtn;

	@FXML
	private TableView<ConnectedClientEntity> connectedClients;

	@FXML
	private TableColumn<ConnectedClientEntity, String> IP;

	@FXML
	private TableColumn<ConnectedClientEntity, String> Host;

	@FXML
	private TableColumn<ConnectedClientEntity, String> Status;

	@FXML
	private TextArea consoleOutput;

	private PrintStream printStream;

	@FXML
	void connectToDB(ActionEvent event) {
		if (CommonFunctions.isNullOrEmpty(txtIP.getText()) || CommonFunctions.isNullOrEmpty(txtPort.getText())
				|| CommonFunctions.isNullOrEmpty(txtDBName.getText())
				|| CommonFunctions.isNullOrEmpty(txtDBUsername.getText())
				|| CommonFunctions.isNullOrEmpty(txtDBPassword.getText())) {
			System.out.println("Error. one or more fields are empty");
			return;
		}
		ServerUI.runServer(this.txtPort.getText(), this.txtDBName.getText(), this.txtDBUsername.getText(),
				this.txtDBPassword.getText());
		connectBtn.setDisable(true);
		disconnectBtn.setDisable(false);
		setDisableTextFieldValues(true);

		while (MySqlClass.isConnectionSuccess == null)
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		if(MySqlClass.isConnectionSuccess == true)
			importUsersBtn.setDisable(false);
		

	}

	@FXML
	public void disconnectFromDB(ActionEvent event) {
		ServerUI.disconnect();
		connectBtn.setDisable(false);
		disconnectBtn.setDisable(true);
		setDisableTextFieldValues(false);
		importUsersBtn.setDisable(true);
	}

	@FXML
	public void initialize() throws Exception { // Setup screen before launching view
		txtIP.setText(getIPValue());
		connectedClients.setItems(EchoServer.getClientList());
		connectTableColumnToObject();

		txtPort.setText("5555");
		txtDBName.setText("jdbc:mysql://localhost/ekrut?serverTimezone=IST");
		txtDBUsername.setText("root");
		txtDBPassword.setText("root");
		disconnectBtn.setDisable(true);
		changeConsoleToUI();

		importUsersBtn.setDisable(true); // disable as default until connect to DB

		// set import action
		importUsersBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				Stage stage = (Stage) importUsersBtn.getScene().getWindow();
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					parseFile(file);
				}
			}
		});

	}

	private String getIPValue() throws Exception {
		String ip = null;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	private void setDisableTextFieldValues(boolean b) {
		txtDBName.setDisable(b);
		txtIP.setDisable(b);
		txtPort.setDisable(b);
		txtDBPassword.setDisable(b);
		txtDBUsername.setDisable(b);
	}

	@FXML
	void changeConsoleToUI() {
		this.printStream = new PrintStream((OutputStream) new ConsoleStream(this.consoleOutput));
		System.setOut(this.printStream);
		System.setErr(this.printStream);
	}

	/*
	 * Making a connection between the ConnectedClient object to the columns
	 * PropertyValueFactory search for a getters like "getIp", "getHost" in entity
	 * object
	 */
	private void connectTableColumnToObject() {
		IP.setCellValueFactory((Callback) new PropertyValueFactory<ConnectedClientEntity, String>("ip"));
		Host.setCellValueFactory((Callback) new PropertyValueFactory<ConnectedClientEntity, String>("host"));
		Status.setCellValueFactory((Callback) new PropertyValueFactory<ConnectedClientEntity, String>("status"));
	}

	@FXML
	public void importUsersFromDB(ActionEvent event) {
		// nothing to do here now
	}

	final FileChooser fileChooser = new FileChooser();
	private Desktop desktop = Desktop.getDesktop();

	/**
	 * Parse file into tuples
	 * 
	 * @param file
	 */
	private void parseFile(File file) {
		String line = "";
		String cvsSplitBy = ",";
		boolean isTitle = true;
		ArrayList<String[]> res = new ArrayList<>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			while ((line = br.readLine()) != null) {
				if(isTitle) {
					isTitle = false;
					continue;
				}
				
				String[] fields = line.split(cvsSplitBy);
				res.add(fields);
				System.out.println(Arrays.toString(fields));
			}
			UsersSimulationDBController.insertTuples(res); 
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	
}
