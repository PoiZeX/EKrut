package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import controllerDb.OrderDBController;
import controllerDb.ReportsGenerator;
import javafx.animation.PauseTransition;

/**
 * The class handles the scheduled tasks which needs to be executed
 * automatically
 * 
 */
public class ScheduledTasksController {

	public static PauseTransition transitionDay, transitionMonth;

	/**
	 * setup timer
	 */
	public void setupTimer(int interval) {
		transitionDay = new PauseTransition(new javafx.util.Duration(interval)); // one day timer

		transitionDay.setOnFinished(action -> {
			if (isFirstDayOfMonth())
				tasksMonthlyExecuter();

			// keep playing anyway
			transitionDay.playFromStart();
		});
		transitionDay.playFromStart();

	}

	/**
	 * Checks whether the day is the first in month or not
	 * 
	 * @return if its the first day or the month or not
	 */
	public static boolean isFirstDayOfMonth() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd");
		LocalDateTime now = LocalDateTime.now();
		if (dtf.format(now).equals("01")) // the first of the month
			return true;
		else
			return false;
	}

	/**
	 * Handles the tasks need to be executed every month (Reports, 'later' payments
	 * etc)
	 */
	public void tasksMonthlyExecuter() {
		DateTimeFormatter monthYear = DateTimeFormatter.ofPattern("MM yyyy");
		LocalDateTime now = LocalDateTime.now();
		String month = now.format(monthYear).split(" ")[0];
		String year = now.format(monthYear).split(" ")[1];

		// specific case
		if (month.equals("01")) {
			year = String.valueOf(Integer.parseInt(year) - 1); // for example 01-01-2020, we need to calculate for last
																// year (01-12-2019 -> 31-12-2019)
			month = "12";
		} else {
			month = String.valueOf(Integer.parseInt(month) - 1);
		}

		String[] reportTypes = new String[] { "orders", "clients", "supply" };
		for (String reportType : reportTypes)
			ReportsGenerator.generateReportsDB(reportType, month, year);

		// make payment for all members (as part of the terms)
		OrderDBController.takeMonthlyMoneyScheduledManager(month, year);
		System.out.println(String.format("Monthly tasks executed for  %s/%s", month, year));

	}

}
