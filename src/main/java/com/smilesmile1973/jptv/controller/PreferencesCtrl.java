/**
 * 
 */
package com.smilesmile1973.jptv.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This controller is used to manage the configuration file of JPTV
 * 
 * @author smilesmile1973@gmail.com
 *
 */
public class PreferencesCtrl {

	private static final Logger LOG = LoggerFactory.getLogger(PreferencesCtrl.class);

	/**
	 * The instance, singleton pattern
	 */
	private static PreferencesCtrl instance;

	/**
	 * The name of the configuration file
	 */
	public static final String CONFIG_FILE = "jptv.config";

	/**
	 * This property must have the URL of the IPTV server
	 */
	public static final String KEY_IPTV_M3U = "mp3u";

	/**
	 * A cache for the configuration.
	 */
	private static final Map<String, String> PREFERENCES = new LinkedHashMap<>();

	/**
	 * 
	 * @return the instance of this class.
	 */
	public static final PreferencesCtrl getInstance() {
		if (instance == null) {
			instance = new PreferencesCtrl();
		}
		return instance;
	}

	private PreferencesCtrl() {
	}

	private String buildProperty(String key, String value) {
		StringBuilder sb = new StringBuilder(key.trim()).append("=").append(value.trim());
		return sb.toString();
	}

	/**
	 * Fill the cache {@link #PREFERENCES}.
	 * 
	 * This method read a property from the {@link #PREFERENCES}. If the
	 * configuration file doesn't exist, then an empty value is returned, otherwise
	 * the value is returned.
	 * 
	 * @param key
	 * @return
	 */
	public String readProperty(String key) throws IOException {
		File file = new File(CONFIG_FILE);
		if (file.exists() && PREFERENCES.isEmpty()) {
			Properties properties = new Properties();
			InputStream in = new FileInputStream(file);
			properties.load(in);
			Set<Entry<Object, Object>> entries = properties.entrySet();
			for (Entry<Object, Object> entry : entries) {
				PREFERENCES.put(entry.getKey().toString(), entry.getValue().toString());
			}
			in.close();
		}
		return PREFERENCES.get(key) == null ? "" : PREFERENCES.get(key);
	}

	public void removeProperty(String key) throws IOException {
		File file = new File(CONFIG_FILE);
		if (!PREFERENCES.isEmpty()) {
			String value = PREFERENCES.remove(key.trim());
			if (value != null) {
				if (PREFERENCES.isEmpty()) {
					removeConfigurationFile();
				} else {
					try (FileChannel fileChannel = FileChannel.open(Paths.get(file.getAbsolutePath()),
							StandardOpenOption.WRITE)) {

						fileChannel.truncate(0).close();
					}
					Set<Entry<String, String>> entries = PREFERENCES.entrySet();
					PrintWriter printWriter = new PrintWriter(file);
					for (Entry<String, String> entry : entries) {
						printWriter.write(this.buildProperty(key, value));
					}

				}
			}
		}
	}

	/**
	 * This method write a property in the configuration file. If the configuration
	 * file is not present the file wil be created automatically.
	 * 
	 * @param key   the property key
	 * @param value the value
	 * @throws IOException
	 */
	public void writeProperty(String key, String value) throws IOException {
		File file = new File(CONFIG_FILE);
		PREFERENCES.put(key.trim(), value);
		if (file.exists()) {
			try (FileChannel fileChannel = FileChannel.open(Paths.get(file.getAbsolutePath()),
					StandardOpenOption.WRITE)) {
				fileChannel.truncate(0).close();
			}
		} else {
			if (!file.createNewFile()) {
				LOG.error("The creation of the configuration file is not possible");
				System.exit(1);
			}
		}
		try (PrintStream out = new PrintStream(file)) {
			Set<Entry<String, String>> entries = PREFERENCES.entrySet();
			for (Entry<String, String> entry : entries) {
				out.println(buildProperty(entry.getKey(), entry.getValue()));
			}
			out.flush();
		}
	}

	public static final void refreshPreferences() {
		LOG.debug("Entries in PREFERENCES : {}", PREFERENCES.size());
		PREFERENCES.clear();
		LOG.debug("Entries in PREFERENCES : {}", PREFERENCES.size());
	}

	public static final void removeConfigurationFile() {
		File file = new File(CONFIG_FILE);
		LOG.info("Deletion of the file {}", file.getAbsoluteFile());
		if (!file.delete()) {
			LOG.error("The file {} can not be deleted.", file.getAbsoluteFile());
		}
	}
}
