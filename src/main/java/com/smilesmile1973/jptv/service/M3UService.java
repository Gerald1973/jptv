package com.smilesmile1973.jptv.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
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

	private Map<String, List<Channel>> channels;

	private M3UService() {
		channels = new TreeMap<>();
	}

	public static final M3UService getInstance() {
		if (instance == null) {
			instance = new M3UService();
		}
		return instance;
	}

	public List<String> fetchWebSite(String url) throws Exception {
		List<String> results = new ArrayList<String>();
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

	public Map<String, List<Channel>> buildChannels(String url) throws Exception {
		List<String> strings = fetchWebSite(url);
		String[] sources = new String[2];
		if (strings != null && !strings.isEmpty() && strings.get(0).equals("#EXTM3U")) {
			for (int i = 1; i < strings.size(); i = i + 2) {
				sources[0] = strings.get(i);
				sources[1] = strings.get(i + 1);
				Channel channel = ChannelConverter.getInstance().toTarget(sources);
				if (channels.get(channel.getGroupTitle()) == null) {
					channels.put(channel.getGroupTitle(), new ArrayList<Channel>());
				}
				channels.get(channel.getGroupTitle()).add(channel);
			}
		}
		return channels;
	}

	public List<Channel> sortGroup(String group) {
		List<Channel> channels = this.channels.get(group);
		channels.sort(Comparator.comparing(Channel::getTvgName));
		return channels;
	}

	public Map<String, List<Channel>> getChannels() {
		return channels;
	}

}
