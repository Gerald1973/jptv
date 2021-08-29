package com.smilesmile1973.jptv.pojo;

import java.util.Objects;

public class Channel {

	private String groupTitle;
	private long length;
	private String tvgId;
	private String tvgName;
	private String tvLogo;
	private String groupTitle2;
	private String channelURL;
	private String option;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		return Objects.equals(channelURL, other.channelURL) && Objects.equals(groupTitle, other.groupTitle)
				&& Objects.equals(groupTitle2, other.groupTitle2) && length == other.length
				&& Objects.equals(option, other.option) && Objects.equals(tvLogo, other.tvLogo)
				&& Objects.equals(tvgId, other.tvgId) && Objects.equals(tvgName, other.tvgName);
	}

	public String getChannelURL() {
		return channelURL;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public String getGroupTitle2() {
		return groupTitle2;
	}

	public long getLength() {
		return length;
	}

	public String getOption() {
		return option;
	}

	public String getTvgId() {
		return tvgId;
	}

	public String getTvgName() {
		return tvgName;
	}

	public String getTvLogo() {
		return tvLogo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(channelURL, groupTitle, groupTitle2, length, option, tvLogo, tvgId, tvgName);
	}

	public void setChannelURL(String channelURL) {
		this.channelURL = channelURL;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public void setGroupTitle2(String groupTitle2) {
		this.groupTitle2 = groupTitle2;
	}

	public void setLength(long setLength) {
		this.length = setLength;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public void setTvgId(String tvgId) {
		this.tvgId = tvgId;
	}

	public void setTvgName(String tvgName) {
		this.tvgName = tvgName;
	}

	public void setTvLogo(String tvLogo) {
		this.tvLogo = tvLogo;
	}

	@Override
	public String toString() {
		return "Channel [groupTitle=" + groupTitle + ", length=" + length + ", tvgId=" + tvgId + ", tvgName=" + tvgName
				+ ", tvLogo=" + tvLogo + ", groupTitle2=" + groupTitle2 + ", channelURL=" + channelURL + ", option="
				+ option + "]";
	}
}
