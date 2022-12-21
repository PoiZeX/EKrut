package utils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import Store.NavigationStoreController;
import common.ScreensNames;
import entity.UserEntity;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PopupWindow {

	@FXML
	private Label headlineLabel;

	@FXML
	private ImageView loadingImage;

	private Stage primaryStage;
	
	public void startEKTPopup(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Login with EKT - waiting");
//        final Popup popup = new Popup();
//        popup.setX(300);
//        popup.setY(200);
//        popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));

		// add popup css here with stylsheet

//        Button show = new Button("Show");
//        show.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                popup.show(primaryStage);
//            }
//        });
//
//        Button hide = new Button("Hide");
//        hide.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                popup.hide();
//            }
//        });

//		ImageView loadingImage = new ImageView();
		Parent root;
		try {
			String path = "/boundary/EKTPopupBoundary.fxml";
			root = FXMLLoader.load(getClass().getResource(path));
			primaryStage.setScene(new Scene(root));
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();

		}
		// start timer


//		
//		Timer timerOvertime = new Timer();
//		int overtimeMillis = 10000;  // define time to simulate automate exit
//		
//		timerOvertime.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				isFinishAndSuccess = -1;
//			}
//		}, overtimeMillis);
//		
//		

//
//	@Override
//	public void start(final Stage primaryStage) {
//		primaryStage.setTitle(title);
//		final Popup popup = new Popup();
//		popup.setX(300);
//		popup.setY(200);
//		popup.getContent().addAll(new Circle(25, 25, 50, Color.AQUAMARINE));
//
//		// add popup css here with stylsheet
//
//		Button show = new Button("Show");
//		show.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				popup.show(primaryStage);
//			}
//		});
//
//		Button hide = new Button("Hide");
//		hide.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				popup.hide();
//			}
//		});
//
//		HBox layout = new HBox(10);
//		layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
//		layout.getChildren().addAll(show, hide);
//		primaryStage.setScene(new Scene(layout));
//		primaryStage.show();
//	}

	}

	public static int isFinishAndSuccess = 0; // 0: not finish, 1: success, -1: failed
	public void initialize() {
		//this.primaryStage = primaryStage;

		headlineLabel.setText("Waiting for EKT connection");
	
		Timer timerSuccess = new Timer();
		int successMillis = 1500; // define time to simulate success

		timerSuccess.schedule(new TimerTask() {
			@Override
			public void run() {
				// simulate: after X seconds:
				// validate proccess
				Image image = new Image(getClass().getResourceAsStream("../styles/icons/EKTloading_success.gif"));
				loadingImage.setImage(image); // get information gif

				// ------------- enter validation here!!! -------------

				// login success
				headlineLabel.setText("Login with EKT success!");
				// wait for 2 seconds
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// set current user
				NavigationStoreController.connectedUser = new UserEntity("", "", "", "", "", "", "", "registered", null, false, false);
				isFinishAndSuccess = 1;
//
//				// close window, set new screen
//				((Stage)headlineLabel.getScene().getWindow()).close(); // close the popup window
			}
		}, successMillis);
	}
}