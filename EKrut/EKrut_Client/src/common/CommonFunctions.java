package common;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import Store.NavigationStoreController;
import javafx.application.Platform;
import javafx.stage.Stage;

public class CommonFunctions {
	private static Timer timerTimeLimit = new Timer();
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.isEmpty());
	}

	public static void SleepFor(long num) {
		timerTimeLimit.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					NavigationStoreController.getInstance().refreshStage(ScreensNames.UsersManagement);
				});
			}
				
		}, num);
	}
	
	
}
