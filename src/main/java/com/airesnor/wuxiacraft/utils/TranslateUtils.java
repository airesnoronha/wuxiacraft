package com.airesnor.wuxiacraft.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TranslateUtils  {

	public static final String enUS = "assets/wuxiacraft/lang/en_us.lang";

	public static String translateKey(String key) {
		InputStreamReader stream = new InputStreamReader(Objects.requireNonNull(TranslateUtils.class.getClassLoader().getResourceAsStream(enUS))
				, StandardCharsets.UTF_8);
		BufferedReader reader = new BufferedReader(stream);
		String returnValue = key;
		try {
			String line = reader.readLine();
			while (line != null) {
				if (line.startsWith(key)) {
					returnValue = line.substring(key.length()+1);
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnValue;
	}

}