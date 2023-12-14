package com.smilesmile1973.jptv.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.converter.ChannelConverter;
import com.smilesmile1973.jptv.pojo.Channel;

public class M3UService {

	private static final Logger LOG = LoggerFactory.getLogger(M3UService.class);

	private static M3UService instance;

	public static final M3UService getInstance() {
		if (instance == null) {
			instance = new M3UService();
		}
		return instance;
	}

	private final Map<String, List<Channel>> channels;

	private M3UService() {
		this.channels = new TreeMap<>();
	}

	public Map<String, List<Channel>> buildChannels(final String url) throws Exception {
		this.channels.clear();
		final List<String> strings = this.fetchWebSite(url);
		final Iterator<String> i = strings.iterator();
		List<String> sources = new ArrayList<>();
		while (i.hasNext()) {
			final String string = i.next();
			if (string.startsWith("#EXTM3U")) {
				LOG.info("Header #EXTM3U");
			}
			if (!string.startsWith("http")) {
				sources.add(string);
			} else {
				sources.add(string);
				final Channel channel = ChannelConverter.getInstance().toTarget(sources);
				if (this.channels.get(channel.getGroupTitle()) == null) {
					this.channels.put(channel.getGroupTitle(), new ArrayList<>());
				}
				this.channels.get(channel.getGroupTitle()).add(channel);
				sources = new ArrayList<>();
			}
		}
		return this.channels;
	}

	/**
	 * This method fetch a M3U file for the given url.
	 *
	 * @param url the given url
	 * @return an array of {@link String} where each element is line of the file
	 * @throws Exception
	 */
	public List<String> fetchWebSite(final String url) throws Exception {
		final List<String> results = new ArrayList<>();
		final URL resource = new URL(url);
		final InputStream in = resource.openStream();
		final BufferedInputStream buffer = new BufferedInputStream(in);
		final byte[] bytes = buffer.readAllBytes();
		LOG.info("M3U size: {}", bytes.length);
		buffer.close();
		in.close();
		// =======
		final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		final InputStreamReader in2 = new InputStreamReader(bais);
		final BufferedReader br = new BufferedReader(in2);
		String line = null;
		do {
			line = br.readLine();
			if (line != null) {
				results.add(line);
			}
		} while (line != null);
		return results;
	}

	public Map<String, List<Channel>> getChannels() {
		return this.channels;
	}

	public Channel getFirst() {
		return this.getChannels().entrySet().iterator().next().getValue().get(0);
	}

	public List<Channel> sortGroup(final String group) {
		final List<Channel> tmpChannels = this.channels.get(group);
		tmpChannels.sort(Comparator.comparing(Channel::getTvgName));
		return tmpChannels;
	}

}
