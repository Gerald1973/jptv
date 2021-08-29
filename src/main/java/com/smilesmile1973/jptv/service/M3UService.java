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

	private Map<String, List<Channel>> channels;

	private M3UService() {
		channels = new TreeMap<>();
	}

	public Map<String, List<Channel>> buildChannels(String url) throws Exception {
		channels.clear();
		List<String> strings = fetchWebSite(url);
		Iterator<String> i = strings.iterator();
		List<String> sources = new ArrayList<>();
		while (i.hasNext()) {
			String string = i.next();
			if (string.startsWith("#EXTM3U")) {
				LOG.info("Header #EXTM3U");
			}
			if (!string.startsWith("http")) {
				sources.add(string);
			} else {
				sources.add(string);
				Channel channel = ChannelConverter.getInstance().toTarget(sources);
				if (channels.get(channel.getGroupTitle()) == null) {
					channels.put(channel.getGroupTitle(), new ArrayList<>());
				}
				channels.get(channel.getGroupTitle()).add(channel);
				sources = new ArrayList<>();
			}
		}
		return channels;
	}

	public List<String> fetchWebSite(String url) throws Exception {
		List<String> results = new ArrayList<>();
		URL resource = new URL(url);
		InputStream in = resource.openStream();
		BufferedInputStream buffer = new BufferedInputStream(in);
		byte[] bytes = buffer.readAllBytes();
		LOG.info("M3U size: {}", bytes.length);
		buffer.close();
		in.close();
		// =======
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		InputStreamReader in2 = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(in2);
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
		return channels;
	}

	public Channel getFirst() {
		return getChannels().entrySet().iterator().next().getValue().get(0);
	}

	public List<Channel> sortGroup(String group) {
		List<Channel> tmpChannels = this.channels.get(group);
		tmpChannels.sort(Comparator.comparing(Channel::getTvgName));
		return tmpChannels;
	}

}
