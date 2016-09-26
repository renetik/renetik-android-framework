package cs.java.lang;

import java.util.Random;

public class CSString {

	static Random random = new Random();
	static String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static String generateRandomStringOfLength(int length) {
		char[] text = new char[length];
		for (int i = 0; i < length; i++)
			text[i] = letters.charAt(random.nextInt(letters.length()));
		return new String(text);
	}

	public static boolean containsCaseInsensitive(String string1, String string2) {
		return string1.toLowerCase().contains(string2.toLowerCase());
	}

}
