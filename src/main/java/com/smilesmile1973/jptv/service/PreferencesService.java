/**
 *
 */
package com.smilesmile1973.jptv.service;

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
public class PreferencesService {

	private static final Logger LOG = LoggerFactory.getLogger(PreferencesService.class);

	/**
	 * The instance, singleton pattern
	 */
	private static PreferencesService instance;

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
	public static final PreferencesService getInstance() {
		if (instance == null) {
			instance = new PreferencesService();
		}
		return instance;
	}

	private PreferencesService() {
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	private String buildProperty(final String key, final String value) {
		final StringBuilder sb = new StringBuilder(key.trim()).append("=").append(value.trim());
		return sb.toString();
	}

	public boolean preferencesFileExists() {
		final File file = new File(CONFIG_FILE);
		return file.exists();
	}

	/**
	 * Fill the cache {@link #PREFERENCES}.
	 *
	 * This method read a property from the {@link #PREFERENCES}. If the
	 * configuration file doesn't exist, then an empty value is returned, otherwise
	 * the value is returned.
	 * If the property file exists but not the property, an empty value is returned.
	 *
	 * @param key
	 * @return returns the property or an empty {@link String}.
	 */
	public String readProperty(final String key) throws IOException {
		final File file = new File(CONFIG_FILE);
		if (file.exists() && PREFERENCES.isEmpty()) {
			final Properties properties = new Properties();
			final InputStream in = new FileInputStream(file);
			properties.load(in);
			final Set<Entry<Object, Object>> entries = properties.entrySet();
			for (final Entry<Object, Object> entry : entries) {
				PREFERENCES.put(entry.getKey().toString(), entry.getValue().toString());
			}
			in.close();
		}
		return PREFERENCES.get(key) == null ? "" : PREFERENCES.get(key);
	}

	public void removeProperty(final String key) throws IOException {
		final File file = new File(CONFIG_FILE);
		if (!PREFERENCES.isEmpty()) {
			final String value = PREFERENCES.remove(key.trim());
			if (value != null) {
				if (PREFERENCES.isEmpty()) {
					removeConfigurationFile();
				} else {
					try (FileChannel fileChannel = FileChannel.open(Paths.get(file.getAbsolutePath()),
							StandardOpenOption.WRITE)) {

						fileChannel.truncate(0).close();
					}
					final Set<Entry<String, String>> entries = PREFERENCES.entrySet();
					final PrintWriter printWriter = new PrintWriter(file);
					for (final Entry<String, String> entry : entries) {
						printWriter.write(this.buildProperty(key, value));
					}

				}
			}
		}
	}

	/**
	 * This method write a property in the configuration file. If the configuration
	 * file is not present the file will be created.
	 *
	 * @param key   the property key
	 * @param value the value
	 * @throws IOException
	 */
	public void writeProperty(final String key, final String value) throws IOException {
		final File file = new File(CONFIG_FILE);
		LOG.debug(file.getAbsolutePath());
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
			final Set<Entry<String, String>> entries = PREFERENCES.entrySet();
			for (final Entry<String, String> entry : entries) {
				out.println(this.buildProperty(entry.getKey(), entry.getValue()));
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
		final File file = new File(CONFIG_FILE);
		LOG.info("Deletion of the file {}", file.getAbsoluteFile());
		if (!file.delete()) {
			LOG.error("The file {} can not be deleted.", file.getAbsoluteFile());
		}
	}
}
