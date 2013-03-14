package com.gmapssimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class MyTrek extends MapActivity {
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	MapView map;
	MapController mControl;
	SessionManager session;
	ArrayList<HashMap<String, String>> mylist;
	List<GeoPoint> path = new ArrayList<GeoPoint>();

	int jarraylen;
	HashMap<String, String> map1;
	String[] lat,lon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytrek);
		map = (MapView) findViewById(R.id.mapView2);
		mControl = map.getController();
		mControl.setZoom(11);
		map.displayZoomControls(true);
		map.setBuiltInZoomControls(true);
		session = new SessionManager(getApplicationContext());
		asyncTaskTrek(SignIn.add+"mytrek.php");
		
	}
	public void asyncTaskTrek(String url) {  
        new AsyncTaskTrek().execute(url);  
    }  
	 
	
	private class AsyncTaskTrek extends AsyncTask <String,Void,Void>{
		private final HttpClient httpClient = new DefaultHttpClient();  
       // private String Content;  
        //private String Error = null;  
	String ab = null;
	
	@Override
	protected Void doInBackground(String... urls) {
		// TODO Auto-generated method stub
		
	try{
		HashMap<String, String> user = session.getUserDetails();
		 String name = user.get(SessionManager.KEY_NAME);

    	
		httppost = new HttpPost(urls[0]);
		//httppost = new HttpPost("http://192.168.1.60/login/set_rate.php");
		
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", name));

	
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		entity = httpClient.execute(httppost).getEntity();
		String response = EntityUtils.toString(entity);
	
		
		JSONObject json = new JSONObject(response.trim());

		JSONArray jArray = json.getJSONArray("posts");
		mylist = new ArrayList<HashMap<String, String>>();
		jarraylen=jArray.length();
		
		for (int i = 0; i < jArray.length(); i++) {

			map1 = new HashMap<String, String>();
			JSONObject e1 = jArray.getJSONObject(i);
			String s = e1.getString("post");
			
			JSONObject jObject = new JSONObject(s);

			lat = new String[jArray.length()];
			lon = new String[jArray.length()];
			
			
			map1.put("latitude", jObject.getString("latitude"));
			lat[i] = map1.get("latitude");
		
			map1.put("longitude", jObject.getString("longitude"));
			lon[i] = map1.get("longitude");

			
			//GeoPoint k=new GeoPoint(lat[i],lon[i]);

			mylist.add(map1);
			//path.add(arg0, arg1)

			Log.i("TABLE LAT", lat[i]);

			int[] intLat = new int[jArray.length()];
			
			intLat[i] = Integer.parseInt(lat[i]);
			
			
			int[] intLon = new int[jArray.length()];
			intLon[i] = Integer.parseInt(lon[i]);
			GeoPoint k=new GeoPoint(intLat[i],intLon[i]);
			path.add(k);
			Log.i("trek", path.toString());
			
			

		}

	} catch (Exception e1) {
		Log.e("log_rating",
				"Error in http connection " + e1.toString());
		
	}
	
	
	//add your points somehow...
	map.getOverlays().add(new RoutePathOverlay(path));
	return null;	
	
}
	}
	
	
	 
	public class RoutePathOverlay extends Overlay {
	 
	        private int _pathColor;
	        private final List<GeoPoint> _points;
	        private boolean _drawStartEnd;
	 
	        public RoutePathOverlay(List<GeoPoint> points) {
	                this(points, Color.BLUE, true);
	        }
	 
	        public RoutePathOverlay(List<GeoPoint> points, int pathColor, boolean drawStartEnd) {
	                _points = points;
	                _pathColor = pathColor;
	                _drawStartEnd = drawStartEnd;
	        }
	 
	        private void drawOval(Canvas canvas, Paint paint, Point point) {
	                Paint ovalPaint = new Paint(paint);
	                ovalPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	                ovalPaint.setStrokeWidth(2);
	                int _radius = 6;
	                RectF oval = new RectF(point.x - _radius, point.y - _radius, point.x + _radius, point.y + _radius);
	                canvas.drawOval(oval, ovalPaint);               
	        }
	 
	        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	                Projection projection = mapView.getProjection();
	                if (shadow == false && _points != null) {
	                        Point startPoint = null, endPoint = null;
	                        Path path = new Path();
	                        //We are creating the path
	                        for (int i = 0; i < _points.size(); i++) {
	                                GeoPoint gPointA = _points.get(i);
	                                Point pointA = new Point();
	                                projection.toPixels(gPointA, pointA);
	                                if (i == 0) { //This is the start point
	                                        startPoint = pointA;
	                                        path.moveTo(pointA.x, pointA.y);
	                                } else {
	                                        if (i == _points.size() - 1)//This is the end point
	                                                endPoint = pointA;
	                                        path.lineTo(pointA.x, pointA.y);
	                                }
	                        }
	 
	                        Paint paint = new Paint();
	                        paint.setAntiAlias(true);
	                        paint.setColor(_pathColor);
	                        paint.setStyle(Paint.Style.STROKE);
	                        paint.setStrokeWidth(4);
	                        paint.setAlpha(90);
	                        if (getDrawStartEnd()) {
	                                if (startPoint != null) {
	                                        drawOval(canvas, paint, startPoint);
	                                }
	                                if (endPoint != null) {
	                                        drawOval(canvas, paint, endPoint);
	                                }
	                        }
	                        if (!path.isEmpty())
	                                canvas.drawPath(path, paint);
	                }
	                return super.draw(canvas, mapView, shadow, when);
	        }
	 
	        public boolean getDrawStartEnd() {
	                return _drawStartEnd;
	        }
	 
	        public void setDrawStartEnd(boolean markStartEnd) {
	                _drawStartEnd = markStartEnd;
	        }
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
