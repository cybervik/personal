package com.whereyoudey.maps.directions;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Distance {

	private String value;
	private String distance;

	public Distance(JSONObject json) throws JSONException {
		value = json.getString("value");
		distance = json.getString("text");
		System.out.println("Distance: " + distance);
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return distance;
	}

}
