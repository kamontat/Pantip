package com.kamontat.model;

import java.io.Serializable;

/**
 * Created by kamontat on 2/9/2017 AD.
 */
public class Topic implements Serializable {
	public static final long serialVersionUID = 1L;

	public static final String ID_JSON = "topic_id";
	public static final String TITLE_JSON = "disp_topic";
	public static final String SHORT_BODY_JSON = "disp_msg";

	private int topicID;
	private String title;
	private String shortBody;

	public Topic(int topicID, String title, String body) {
		this.topicID = topicID;
		this.title = title;
		this.shortBody = body;
	}

	public String getShortBody() {
		return shortBody;
	}

	public String getTopicLink() {
		return "http://pantip.com/topic/" + topicID;
	}

	@Override
	public String toString() {
		return topicID + " - " + title;
	}
}
