package util;

import java.util.regex.Pattern;

public class Validator {

	static final Pattern MAC_PATTERN = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$");

	public static boolean validateMac(String mac) {
		return MAC_PATTERN.matcher(mac).find();

	}

}
