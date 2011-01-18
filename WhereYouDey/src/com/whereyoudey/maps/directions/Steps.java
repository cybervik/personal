package com.whereyoudey.maps.directions;

import java.io.UnsupportedEncodingException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * { "travel_mode": "DRIVING", "start_location": { "lat": 42.3585300, "lng":
 * -71.0600700 }, "end_location": { "lat": 42.3580200, "lng": -71.0604300 },
 * "polyline": { "points": "ycpaGl|upLdBfA", "levels": "BB" }, "duration": {
 * "value": 4, "text": "1 min" }, "html_instructions":"Head \u003cb\u003esouthwest\u003c/b\u003e on \u003cb\u003eTremont St\u003c/b\u003e toward \u003cb\u003eSchool St\u003c/b\u003e"
 * , "distance": { "value": 64, "text": "210 ft" } }
 * 
 * @author administrator
 * 
 */
public class Steps {

	private String travelMode;
	private Location startLocation;
	private Location endLocation;
	private PolyLine polyline;
	private Duration duration;
	private String htmlInstructions;
	private Distance distance;

	private JSONObject steps;

	public Steps(JSONObject steps) throws Exception {
		this.steps = steps;
		init();
	}

	private void init() throws Exception {

		travelMode = get("travel_mode");
		htmlInstructions = get("html_instructions");
		cleanHTMLTags();

		startLocation = new Location(steps.getJSONObject("start_location"));
		endLocation = new Location(steps.getJSONObject("end_location"));
		duration = new Duration(steps.getJSONObject("duration"));
		distance = new Distance(steps.getJSONObject("distance"));

	}

	private void cleanHTMLTags() throws UnsupportedEncodingException {

		htmlInstructions = new String(htmlInstructions.getBytes("ascii"));

		char[] cs = htmlInstructions.toCharArray();
		StringBuffer sb = new StringBuffer();
		boolean tag = false;
		for (int i = 0; i < cs.length; i++) {
			switch (cs[i]) {
			case '<':
				if (!tag) {
					tag = true;
					break;
				}
			case '>':
				if (tag) {
					tag = false;
					break;
				}
				// case '&': i += interpretEscape(cs, i, sb); break;
			default:
				if (!tag)
					sb.append(cs[i]);
			}
		}

		htmlInstructions = sb.toString();
		htmlInstructions = StringUtils.unescapeHTML(htmlInstructions);

	}

	private String get(String key) throws JSONException {
		return steps.getString(key);
	}

	public String getTravelMode() {
		return travelMode;
	}

	public Location getStartLocation() {
		return startLocation;
	}

	public Location getEndLocation() {
		return endLocation;
	}

	public PolyLine getPolyline() {
		return polyline;
	}

	public Duration getDuration() {
		return duration;
	}

	public String getHtmlInstructions() {
		return htmlInstructions;
	}

	public Distance getDistance() {
		return distance;
	}
	
	
	
}
