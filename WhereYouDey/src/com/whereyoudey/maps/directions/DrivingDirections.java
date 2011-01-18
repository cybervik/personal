package com.whereyoudey.maps.directions;

import com.whereyoudey.utils.UiUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.json.me.JSONArray;
import org.json.me.JSONObject;
import org.json.me.StringWriter;

public class DrivingDirections {

    private String sourceLat;
    private String sourceLongi;
    private String destLat;
    private String destLongi;
    private String srcLoc;
    private String dstLoc;
    private String status;
    private Vector routes;

    public DrivingDirections() throws Exception {

        init();

    }

    public DrivingDirections(String sourceLat, String sourceLongi,
                             String destLat, String destLongi) throws Exception {
        this.sourceLat = sourceLat;
        this.sourceLongi = sourceLongi;
        this.destLat = destLat;
        this.destLongi = destLongi;

        init();

    }

    public DrivingDirections(String srcLoc, String dstLoc) throws Exception {
        this.srcLoc = srcLoc;
        this.dstLoc = dstLoc;
        init();
    }

    public DrivingDirections(String srcLoc, String destLat, String destLongi)
            throws Exception {
        this.srcLoc = srcLoc;
        this.destLat = destLat;
        this.destLongi = destLongi;
        init();
    }

    private void init() throws Exception {
        routes = new Vector();

        String data = getDirectionFromServer();

        JSONObject object = new JSONObject(data);
        status = object.getString("status");
        System.out.println(status);

        JSONArray routesArray = object.getJSONArray("routes");

        for (int i = 0; i < routesArray.length(); i++) {
            routes.addElement(new Route(routesArray.getJSONObject(i)));
        }
    }

    private String getDirectionFromServer() throws Exception {

        String dirURL = "http://maps.googleapis.com/maps/api/directions/json";
        String getParams = createGetParams();

        String finalURL = dirURL + "?" + getParams;

        finalURL = finalURL.replace(' ', '+');
        HttpConnection conn = (HttpConnection) Connector.open(finalURL);
        conn.setRequestMethod(HttpConnection.GET);
        InputStream input = conn.openInputStream();
        String jsonData = readData(input);
        return jsonData;
    }

    private String readData(InputStream is)
            throws IOException {
        StringBuffer sb = new StringBuffer();
        try {
            int chars, i = 0;
            while ((chars = is.read()) != -1) {
                sb.append((char) chars);
            }
            return sb.toString();
        } catch (Exception e) {
        }
        return null;
    }

    private String createGetParams() {
        StringBuffer sb = new StringBuffer();
        // origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA&sensor=false
        sb.append("origin=" + getSourceLoc());
        sb.append("&");
        sb.append("destination=" + getDstLoc());
        sb.append("&");
        sb.append("sensor=false");



        return sb.toString();


    }

    private String getDstLoc() {
        if (destLat == null) {
            return dstLoc;


        }
        return destLongi + "," + destLat;


    }

    private String getSourceLoc() {
        if (sourceLat == null) {
            return srcLoc;


        }
        return sourceLongi + "," + sourceLat;


    }

    public Vector getRoutes() {
        return routes;


    }

    public void setRoutes(Vector routes) {
        this.routes = routes;


    }

    public String getStatus() {
        return status;


    }

    public void setStatus(String status) {
        this.status = status;


    }
}
