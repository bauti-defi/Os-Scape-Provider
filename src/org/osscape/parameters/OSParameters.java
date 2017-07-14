package org.osscape.parameters;

import org.bot.util.NetUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Ethan on 7/7/2017.
 */
public class OSParameters {
	private static final Map<String, String> PARAMETER_MAP = new HashMap<>();
	public String paramsString = "";
	public Properties prop = new Properties();

	public OSParameters() {
		parse();

	}

	public void parse() {
		PARAMETER_MAP.clear();
		StringBuilder builder = new StringBuilder();
		try {
			String[] hooks = NetUtil.readPage("http://pastebin.com/raw/rnSpKiQ2");
			for (String str : hooks) {

				builder.append(str);
				builder.append("\n");
				if (str.contains("param=")) {
					str = str.replace("param=", "");
					String str3 = str.substring(0, str.indexOf("="));
					String str4 = str.substring(str.indexOf("=") + 1, str.length());
					prop.put(str3, str4);
				}
				if (str.contains("=")) {
					str = str.replaceAll("param=", "");

					final String[] parts = str.split("=");
					if (parts.length == 1) {
						add(parts[0], "");
					} else if (parts.length == 2) {
						add(parts[0], parts[1]);
					} else if (parts.length == 3) {
						add(parts[0], parts[1] + "=" + parts[2]);
					} else if (parts.length == 4) {
						add(parts[0], parts[1] + "=" + parts[2] + "=" + parts[3]);
					}

				}
			}
			paramsString = builder.toString();

		} catch (Exception e) {

		}
	}

	private void add(String key, String val) {
		PARAMETER_MAP.put(key, val);
	}

	public String get(String key) {
		return PARAMETER_MAP.containsKey(key) ? PARAMETER_MAP.get(key) : "";
	}

}
