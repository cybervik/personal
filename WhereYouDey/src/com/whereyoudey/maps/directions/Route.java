package com.whereyoudey.maps.directions;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

public class Route {

	private String summary;
	private Vector steps; // LEG
	private Distance distance;
	private Duration duration;
	private String startAddress;
	private String endAddress;

	private JSONObject dirJSON;

	public Route(JSONObject dirJSON) throws Exception {
		this.dirJSON = dirJSON;
		init();
	}

	private void init() throws Exception {

		summary = dirJSON.getString("summary");

		JSONObject l = dirJSON.getJSONArray("legs").getJSONObject(0);
		System.out.println(l);

		startAddress = l.getString("start_address");
		endAddress = l.getString("end_address");
		
		duration = new Duration(l.getJSONObject("duration"));
		distance = new Distance(l.getJSONObject("distance"));
		
		JSONArray sA = l.getJSONArray("steps");
		
			steps = new Vector();
			
			for(int i=0;i<sA.length();i++)
				steps.addElement(new Steps(sA.getJSONObject(i)));
				
			
		
	}

	public String getSummary() {
		return summary;
	}

	public Vector getSteps() {
		return steps;
	}

	public Distance getDistance() {
		return distance;
	}

	public Duration getDuration() {
		return duration;
	}

	public String getStartAddress() {
		return startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}
	
	

}
