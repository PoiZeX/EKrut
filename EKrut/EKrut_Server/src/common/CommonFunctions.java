package common;

public class CommonFunctions {
	
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.isEmpty());
	}
	
	public static String getNumericMonth(String month) {
		int index, numOfMonths = 12;
		String[] months = {	"january",
							"february",
							"march",
							"april",
							"may",
							"june",
							"july",
							"august",
							"september",
							"october",
							"november",
							"december"
		};
		for (index = 1; index <= numOfMonths; index++) {
			if (month.toLowerCase().equals(months[index - 1]))
				if (index < 10) return("0" + index);
				else if (index >= 10) return ("" + index);
		}
		return "Invalid month";
	}
	
}
