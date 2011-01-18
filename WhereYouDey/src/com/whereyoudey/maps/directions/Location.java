package com.whereyoudey.maps.directions;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Location {

	private String latitude;
	private String longitude;
	
	public Location(JSONObject json) throws JSONException{
		
		latitude = json.getString("lat");
		longitude = json.getString("lng");
		
		//System.out.println(latitude +"=" + longitude);
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	
}
