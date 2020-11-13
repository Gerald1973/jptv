package com.smilesmile1973.jptv.pojo;

public class Channel {

	private String groupTitle;
	private long length;
	private String tvgId;
	private String tvgName;
	private String tvLogo;
	private String groupTitle2;
	private String channelURL;

	public String getChannelURL() {
		return channelURL;
	}

	public void setChannelURL(String channelURL) {
		this.channelURL = channelURL;
	}

	public String getTvLogo() {
		return tvLogo;
	}

	public void setTvLogo(String tvLogo) {
		this.tvLogo = tvLogo;
	}

	public String getGroupTitle() {
		return groupTitle;
	}

	public long getLength() {
		return length;
	}

	public String getTvgId() {
		return tvgId;
	}

	public String getTvgName() {
		return tvgName;
	}

	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}

	public void setLength(long setLength) {
		this.length = setLength;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channelURL == null) ? 0 : channelURL.hashCode());
		result = prime * result + ((groupTitle == null) ? 0 : groupTitle.hashCode());
		result = prime * result + ((groupTitle2 == null) ? 0 : groupTitle2.hashCode());
		result = prime * result + (int) (length ^ (length >>> 32));
		result = prime * result + ((tvLogo == null) ? 0 : tvLogo.hashCode());
		result = prime * result + ((tvgId == null) ? 0 : tvgId.hashCode());
		result = prime * result + ((tvgName == null) ? 0 : tvgName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		if (channelURL == null) {
			if (other.channelURL != null)
				return false;
		} else if (!channelURL.equals(other.channelURL))
			return false;
		if (groupTitle == null) {
			if (other.groupTitle != null)
				return false;
		} else if (!groupTitle.equals(other.groupTitle))
			return false;
		if (groupTitle2 == null) {
			if (other.groupTitle2 != null)
				return false;
		} else if (!groupTitle2.equals(other.groupTitle2))
			return false;
		if (length != other.length)
			return false;
		if (tvLogo == null) {
			if (other.tvLogo != null)
				return false;
		} else if (!tvLogo.equals(other.tvLogo))
			return false;
		if (tvgId == null) {
			if (other.tvgId != null)
				return false;
		} else if (!tvgId.equals(other.tvgId))
			return false;
		if (tvgName == null) {
			if (other.tvgName != null)
				return false;
		} else if (!tvgName.equals(other.tvgName))
			return false;
		return true;
	}

	public void setTvgId(String tvgId) {
		this.tvgId = tvgId;
	}

	public void setTvgName(String tvgName) {
		this.tvgName = tvgName;
	}

	public void setGroupTitle2(String groupTitle2) {
		this.groupTitle2 = groupTitle2;
	}

	public String getGroupTitle2() {
		return groupTitle2;
	}
}
