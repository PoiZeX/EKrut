package common;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

public class CommonFunctions {
	private static Timer timerTimeLimit;
	
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.isEmpty());
	}

	public static void SleepFor(long num, Runnable callback) {
		timerTimeLimit = new Timer();
		timerTimeLimit.schedule(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(() -> {
					try {
						callback.run();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			}
				
		}, num);
	}
	
	
}
