package common;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


import enums.PopupTypeEnum;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CommonFunctions {
	private static Timer timerTimeLimit;
	

	/**
	 * check if the string is null or empty (spaces is empty)
	 * 
	 * @param txt
	 * @return
	 */
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.trim().isEmpty());
	}

	/**
	 * general function to execute lambda <callback> after <num> time
	 * 
	 * @param num
	 * @param callback
	 */
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
	
	/**

	This method takes in a String representing a month and returns a String of the corresponding numerical month (e.g. "january" -> "01").
	If the given month string is not a valid month, the method returns "Invalid month"
	@param month the input month string to be converted to numerical form
	@return a String of the numerical month representation, or "Invalid month" if the given month string is not valid
	*/
	public static String getNumericMonth(String month) {
		int index, numOfMonths = 12;
		String[] months = { "january", "february", "march", "april", "may", "june", "july", "august", "september",
				"october", "november", "december" };
		for (index = 1; index <= numOfMonths; index++) {
			if (month.toLowerCase().equals(months[index - 1]))
				if (index < 10)
					return ("0" + index);
				else if (index >= 10)
					return ("" + index);
		}
		return "Invalid month";
	}
	/**

	This method takes in a String and splits it by uppercase letters, returning a new String with spaces between each of the split parts.

	@param str the input String to be split

	@return a new String with spaces between each of the split parts
	*/
	public static String splitByUpperCase(String str) {
		String goodName = "";

		String[] splitString = str.toString().split("(?<=[^A-Z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][^A-Z])");
		if (splitString.length > 1) {
			for (String s : splitString)
				goodName += s + " ";
		} else
			goodName = splitString[0];
		return goodName;
	}


}
