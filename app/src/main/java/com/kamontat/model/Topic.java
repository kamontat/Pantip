package com.kamontat.model;

/**
 * Created by kamontat on 2/9/2017 AD.
 */

public class Topic {
	private int topicID;
	private String title;

	public Topic(int topicID, String title) {
		this.topicID = topicID;
		this.title = title;
	}

	@Override
	public String toString() {
		return topicID + " - " + title;
	}
}
