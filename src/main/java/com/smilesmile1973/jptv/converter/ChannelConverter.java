package com.smilesmile1973.jptv.converter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.smilesmile1973.jptv.pojo.Channel;

public class ChannelConverter implements Converter<List<String>, Channel> {

	private static ChannelConverter instance;

	private static final Logger LOG = LoggerFactory.getLogger(ChannelConverter.class);

	private static final String GR_LENGTH = "length";

	private static final Pattern PA_LENGTH = Pattern.compile("#EXTINF:(?<length>[-]?[0-9]+)", Pattern.CASE_INSENSITIVE);

	private static final String GR_TVGID = "tvgId";

	private static final Pattern PA_TVGID = Pattern.compile("tvg-id=\"(?<tvgId>[^\\\"]*)", Pattern.CASE_INSENSITIVE);

	private static final String GR_TVLOGO = "tvLogo";

	private static final Pattern PA_TVLOGO = Pattern.compile("tvg-logo=\"(?<tvLogo>[^\\\"]*)",
			Pattern.CASE_INSENSITIVE);

	private static final String GR_TVGNAME = "tvgName";

	private static final Pattern PA_TVGNAME = Pattern.compile("tvg-name=\"(?<tvgName>[^\\\"]*)",
			Pattern.CASE_INSENSITIVE);

	private static final String GR_GROUPTITLE = "groupTitle";

	private static final Pattern PA_GROUPTITLE = Pattern.compile("group-title=\"(?<groupTitle>[^\\\"]*)\"",
			Pattern.CASE_INSENSITIVE);

	private static final String GR_GROUPTITLE2 = "groupTitle2";

	private static final Pattern PA_GROUPTITLE2 = Pattern.compile("group-title=\".*\",(?<groupTitle2>.*)",
			Pattern.CASE_INSENSITIVE);

	public static final ChannelConverter getInstance() {
		if (instance == null) {
			instance = new ChannelConverter();
		}
		return instance;
	}

	private String fetch(String source, Pattern pattern, String groupName) {
		String result = "";
		Matcher matcher = pattern.matcher(source);
		if (matcher.find()) {
			result = matcher.group(groupName);
		} else {
			LOG.error("The pattern \"{}\" doesn't match something in the source \"{}\".", pattern.pattern(), source);
		}
		return result;
	}

	@Override
	public List<String> toSource(Channel target) {
		String[] results = new String[2];
		StringBuilder sb = new StringBuilder();
		sb.append("#EXTINF:").append(target.getLength()).append(" tvg-id=\"").append(target.getTvgId())
				.append("\" tvg-name=\"").append(target.getTvgName()).append("\" tvg-logo=\"")
				.append(target.getTvLogo()).append("\" group-title=\"").append(target.getGroupTitle()).append("\",")
				.append(target.getGroupTitle2());
		results[0] = sb.toString();
		results[1] = target.getChannelURL();
		return Arrays.asList(results);
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
	public Channel toTarget(List<String> sources) {
		Channel result = new Channel();
		Iterator<String> i = sources.iterator();
		while (i.hasNext()) {
			String string = i.next();
			if (string.startsWith("#EXTINF")) {
				String tmp = "";
				tmp = fetch(string, PA_LENGTH, GR_LENGTH);
				if (!Strings.isNullOrEmpty(tmp)) {
					result.setLength(Long.parseLong(tmp));
				}
				result.setTvgId(fetch(string, PA_TVGID, GR_TVGID));
				tmp = fetch(string, PA_TVGNAME, GR_TVGNAME);
				if (StringUtils.isEmpty(tmp)) {
					LOG.debug("No name : {}", string);
				}
				result.setTvgName(tmp);
				result.setTvLogo(fetch(string, PA_TVLOGO, GR_TVLOGO));
				result.setGroupTitle(fetch(string, PA_GROUPTITLE, GR_GROUPTITLE));
				result.setGroupTitle2(fetch(string, PA_GROUPTITLE2, GR_GROUPTITLE2));
			} else if (string.startsWith("http")) {
				result.setChannelURL(string.trim());
			} else if (string.startsWith("#EXTVLCOPT:")) {
				String tmp = string.substring(11);
				LOG.info(tmp);
				result.setOption(tmp);
			} else {
				LOG.info("Not yet parsed : {}", string);
			}
		}
		return result;
	}
}
