package common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Timer;

import controllerDb.CommonDataDBController;
import controllerDb.OrderDBController;
import controllerDb.ReportsDBController;
import javafx.animation.PauseTransition;

/**
 * The class handles the scheduled tasks which needs to be executed
 * automatically
 * 
 * @author דוד
 *
 */
public class ScheduledTasksController {

	/**
	 * TODO: 1. Timer for One Month -> for monthly tasks 2. Timer for 24Hours ->
	 * checks if we missed the 1st of the month (OR in the initialize) 3. Tasks to
	 * do every month
	 */

	public static PauseTransition transitionDay, transitionMonth;

	/**
	 * setup timer
	 */
	public void setupTimer(int interval) {
		transitionDay = new PauseTransition(new javafx.util.Duration(interval)); // one day timer
		// transitionMonth = new PauseTransition(new
		// javafx.util.Duration(31*24*60*60*1000)); // one month timer

		transitionDay.setOnFinished(action -> {
			if (isFirstDayOfMonth())
				tasksMonthlyExecuter();

			// keep playing anyway
			transitionDay.playFromStart();
		});

//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
//		calendar.set(Calendar.HOUR_OF_DAY, 15);
//		calendar.set(Calendar.MINUTE, 40);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MILLISECOND, 0);
//
//		Timer time = new Timer(); // Instantiate Timer Object

		// Start running the task on Monday at 15:40:00, period is set to 8 hours
		// if you want to run the task immediately, set the 2nd parameter to 0
		// time.schedule(new CustomTask(), calendar.getTime(),
		// TimeUnit.HOURS.toMillis(8));
	}

	/**
	 * Checks whether the day is the first in month or not
	 * 
	 * @return
	 */
	private boolean isFirstDayOfMonth() {
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

		// sets month and year for previous
		if(month.equals("01"))
			year = String.valueOf(Integer.parseInt(year) - 1); // for example 01-01-2020, we need to calculate for last year (01-12-2019 -> 31-12-2019)
		month = String.valueOf(Integer.parseInt(month) - 1);
		
		// for every region -> check if there are reports for this date
		for (String region : CommonDataDBController.getRegionsListFromDB()) {
			if (!ReportsDBController.isReportExist("orders", month, year, region))
				 ReportsGenerator.generateReportsDB("orders", month, year); // what about region?
			
			if (!ReportsDBController.isReportExist("clients", month, year, region))
				 ReportsGenerator.generateReportsDB("clients", month, year); // what about region?
		}
		
		if (!ReportsDBController.isReportExist("supply", month, year))
			 ReportsGenerator.generateReportsDB("supply", month, year); 
		
		// make payment for all members (as part of the terms)
		OrderDBController.takeMonthlyMoneyScheduledManager(year, month);
		System.out.println(String.format("Monthly tasks executed for  %s/%s" , month, year));
		
	}
	
	
	

}
