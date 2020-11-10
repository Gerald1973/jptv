package com.smilesmile1973.jptv.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class M3UService {
	
	private static final Logger LOG = LoggerFactory.getLogger(M3UService.class);
			
	private static M3UService instance;

	private M3UService() {
	}

	public static final M3UService getInstance() {
		if (instance == null) {
			instance = new M3UService();
		}
		return instance;
	}
	
	public List<String> fetchWebSite(String url) throws Exception {
		List<String> results = new LinkedList<String>();
		URL resource = new URL(url);
		InputStream in = resource.openStream();
		BufferedInputStream buffer = new BufferedInputStream(in);
		byte[] bytes = buffer.readAllBytes();
		LOG.info("M3U size: {}", bytes.length);
		buffer.close();
		in.close();
		//=======
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		InputStreamReader in2 = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(in2);
		String line = null;
		do {
			line = br.readLine();
			if (line != null) {
				results.add(line);
			}
		}while (line != null);
		results.remove(0);
		return results;
	}

}
