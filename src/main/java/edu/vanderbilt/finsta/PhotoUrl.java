package edu.vanderbilt.finsta;

import java.time.LocalDateTime;
import java.util.UUID;

public class PhotoUrl {

	private String id = UUID.randomUUID().toString();

	private long time;

	private String url;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
