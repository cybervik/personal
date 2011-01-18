package com.whereyoudey.maps.directions;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Duration {

	private String value;
	private String text;

	public Duration(JSONObject json) throws JSONException {
		value = json.getString("value");
		text = json.getString("text");
		
		System.out.println("value:" + value + " text: " + text);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
