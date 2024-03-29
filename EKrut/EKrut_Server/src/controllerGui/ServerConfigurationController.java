package controllerGui;

import utils.ConsoleStream;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;

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

/**
 * The Class ServerConfigurationController.
 */
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
	
	/**
	 * Connect to DB.
	 *
	 * @param event the event
	 */
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
		if (MySqlClass.isConnectionSuccess == true)
			importUsersBtn.setDisable(false);
		fileChooser.setInitialDirectory(
	            new File(System.getProperty("user.home")));
//		fileChooser.setInitialDirectory(new File("./src/mysql/"));
	}
	
	/**
	 * Disconnect from DB.
	 *
	 * @param event the event
	 */
	@FXML
	public void disconnectFromDB(ActionEvent event) {
		ServerUI.disconnect();
		connectBtn.setDisable(false);
		disconnectBtn.setDisable(true);
		setDisableTextFieldValues(false);
		importUsersBtn.setDisable(true);
	}
	
	/**
	 * Initialize.
	 *
	 * @throws Exception the exception
	 */
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
				
				if (file != null && file.getName().endsWith(".csv") ) {
					parseFile(file);
				} else System.out.println("Import failed: Can't open file");
			}
		});

	}

	/**
	 * Gets the IP value.
	 *
	 * @return the IP value
	 * @throws Exception the exception
	 */
	private String getIPValue() throws Exception {
		String ip = null;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

	/**
	 * Sets the disable text field values.
	 *
	 * @param b the new disable text field values
	 */
	private void setDisableTextFieldValues(boolean b) {
		txtDBName.setDisable(b);
		txtIP.setDisable(b);
		txtPort.setDisable(b);
		txtDBPassword.setDisable(b);
		txtDBUsername.setDisable(b);
	}

	/**
	 * Change console to UI.
	 */
	@FXML
	void changeConsoleToUI() {
		this.printStream = new PrintStream((OutputStream) new ConsoleStream(this.consoleOutput));
		System.setOut(this.printStream);
		System.setErr(this.printStream);
	}

	/**
	 * Connect table column to object.
	 */
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

	final FileChooser fileChooser = new FileChooser();

	/**
	 * Parses the file.
	 *
	 * @param file the file
	 */
	private void parseFile(File file) {
		int cnt = 0;
		String line = "";
		String cvsSplitBy = ",";
		final int num_of_fields = 9;
		ArrayList<String[]> res = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			while ((line = br.readLine()) != null) {
				if (line == null || CommonFunctions.isNullOrEmpty(line))
					continue; // skip empty lines
				ArrayList<String> fields = new ArrayList<>(Arrays.asList(line.split(cvsSplitBy)));
				cnt++;

				fields.forEach(item -> {
					fields.set(fields.indexOf(item), item.trim());
				});

				// normalization form
				if (fields.size() != num_of_fields || fields.contains("") ) {
					System.out.println(String.format("Error in line %d", cnt));
					System.out.println("Tuples rules reminder:\n"
							+ "<id_number>, <user_name>, <password>, <first_name>, <last_name>, <email>, <phone_number>, <region>, <role_type>\n");
					continue;
				}
				res.add(fields.toArray(new String[fields.size()]));
				// System.out.println(Arrays.toString(fields));
			}
			System.out.println("Attempting to insert all valid tuples.");
			UsersSimulationDBController.insertTuples(res);
			System.out.println("Import success");
		} catch (IOException e) {
			System.out.println("Import failed: Can't open file");
		} catch (SQLException e) {
			System.out.println("Import failed:\n" + e.toString().split("Exception: ")[1] + "Tuples rules reminder:\n"
					+ "<id_number>, <user_name>, <password>, <first_name>, <last_name>, <email>, <phone_number>, <region>, <role_type>\n");

		}

	}



}