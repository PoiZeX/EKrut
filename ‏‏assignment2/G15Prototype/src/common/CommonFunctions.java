package common;

import java.net.Inet4Address;

public class CommonFunctions {
	
	public static boolean isNullOrEmpty(String txt) {
		return (txt == null || txt.isEmpty());
	}
	
	public String getIPValue() throws Exception {
		String ip = null;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ip;
	}

}
