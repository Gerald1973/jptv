package com.smilesmile1973.jptv.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smilesmile1973.jptv.pojo.Channel;

public class ChannelConverter implements Converter<String[], Channel> {

	private static ChannelConverter instance;

	private static final Logger LOG = LoggerFactory.getLogger(ChannelConverter.class);

	private static final String[] RE_LENGTH = { "length", "#EXTINF:(?<length>[-]?[0-9]+)" };

	private static final String[] RE_TVGID = { "tvgId", "tvg-id=\"(?<tvgId>[^\\\"]*)" };

	private static final String[] RE_TVLOGO = { "tvLogo", "tvg-logo=\"(?<tvLogo>[^\\\"]*)" };

	private static final String[] RE_TVGNAME = { "tvgName", "tvg-name=\"(?<tvgName>[^\\\"]*)" };

	private static final String[] RE_GROUPTITLE = { "groupTitle", "group-title=\"(?<groupTitle>[^\\\"]*)\"" };

	private static final String[] RE_GROUPTITLE2 = { "groupTitle2", "group-title=\".*\",(?<groupTitle2>.*)" };

	public static final ChannelConverter getInstance() {
		if (instance == null) {
			instance = new ChannelConverter();
		}
		return instance;
	}

	private String fetch(String source, String[] pattern) {
		String result = "";
		Pattern regEx = Pattern.compile(pattern[1]);
		Matcher matcher = regEx.matcher(source);
		if (matcher.find()) {
			result = matcher.group(pattern[0]);
		} else {
			LOG.error("The pattern \"{}\" doesn't match something in the source \"{}\".", pattern[0], source);
		}
		return result;
	}

	@Override
	public String[] toSource(Channel target) {
		String[] results = new String[2];
		StringBuilder sb = new StringBuilder();
		sb.append("#EXTINF:").append(target.getLength()).append(" tvg-id=\"").append(target.getTvgId())
				.append("\" tvg-name=\"").append(target.getTvgName()).append("\" tvg-logo=\"")
				.append(target.getTvLogo()).append("\" group-title=\"").append(target.getGroupTitle()).append("\",")
				.append(target.getGroupTitle2());
		results[0] = sb.toString();
		results[1] = target.getChannelURL();
		return results;
	}

	/**
	 * This class has to be the parsed result the following lines 1.<br>
	 * #EXTINF:-1 tvg-id="" tvg-name="---●★| BEIN SPORT |★●---"
	 * tvg-logo="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYDBWRrnlPawtN1rZy7ox0_t1ZJxeAympp3leNVwxob2CrptVp1IDyeuQ"
	 * group-title="ARABIC BEIN SPORTS",---●★| BEIN SPORT |★●---<br>
	 * 2.<br>
	 * http://NNNN:8888/NNNN/NNNN/NNNN
	 * 
	 * @author private
	 *
	 */
	@Override
	public Channel toTarget(String[] source) {
		Channel result = new Channel();
		String info = source[0];
		String url = source[1];
		result.setLength(Long.parseLong(fetch(info, RE_LENGTH)));
		result.setTvgId(fetch(info, RE_TVGID));
		result.setTvgName(fetch(info, RE_TVGNAME));
		result.setTvLogo(fetch(info, RE_TVLOGO));
		result.setGroupTitle(fetch(info, RE_GROUPTITLE));
		result.setGroupTitle2(fetch(info, RE_GROUPTITLE2));
		result.setChannelURL(url.trim());
		return result;
	}
}
