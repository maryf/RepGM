package com.gmapssimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;


public class PolylineP extends MapActivity {
	List<GeoPoint> path_start = new ArrayList<GeoPoint>();
	ArrayList<GeoPoint> path_end = new ArrayList<GeoPoint>();
	GeoPoint p1,p2;
	String lat1,lon1,lat2,lon2;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	MapView map;
	MapController mControl;
	SessionManager session;
	ArrayList<HashMap<String, String>> mylist;
	List<GeoPoint> path = new ArrayList<GeoPoint>();

	int jarraylen,len;
	HashMap<String, String> map1;
	String[] lat,lon;
	
	String enc_points;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytrek);
		map = (MapView) findViewById(R.id.mapView2);
		mControl = map.getController();
		mControl.setZoom(11);
		map.displayZoomControls(true);
		map.setBuiltInZoomControls(true);
		
		session = new SessionManager(getApplicationContext());
		
		
		getURL("http://maps.googleapis.com/maps/api/directions/json?origin=38.2088258,21.7853143&destination=38.2505451,22.0810942&sensor=false&travel_mode=transit");

		
		//map.getOverlays().add(new RoutePathOverlay(path_start));
}
	  
	public void getURL(String url) {  
        new GetURL().execute(url);  
    } 
	
	
	
	private List<GeoPoint> decodePoly(String encoded) {

		List<GeoPoint> poly = new ArrayList<GeoPoint>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
				 (int) (((double) lng / 1E5) * 1E6));
			poly.add(p);
		}

		return poly;
	}
	
	private class GetURL extends AsyncTask<String, Void, Void> {  
        private final HttpClient Client = new DefaultHttpClient();  
        //private String Content;  
         
        
          
        protected void onPreExecute() {  
             
        }  
  
        protected Void doInBackground(String... urls) {  
            try {  
               HttpPost httppost = new HttpPost(urls[0]);  
                
			HttpResponse response = Client.execute(httppost);
					if (response.getStatusLine().getStatusCode() == 200) {
						//t1.setText("ok2");
						HttpEntity entity = response.getEntity();
						 String is1 = EntityUtils.toString(entity);
						if (entity != null) {
							//Log.i("jsonResponse", is1);
							
							//JSONObject json = new JSONObject(is1.trim());
							JSONObject json = new JSONObject(is1);

							
							JSONArray jArray = null;
							jArray =json.getJSONArray("routes");
							//ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
							//int jarraylen = jArray.length();
							
							JSONObject legs =jArray.getJSONObject(0);
							JSONArray	legs1=legs.getJSONArray("legs");
							
							
							JSONObject step = legs1.getJSONObject(0);
							JSONArray step1=step.getJSONArray("steps");
					
							
							for (int i=0; i<step1.length(); i++){
							
							
							JSONObject s = step1.getJSONObject(i);						
							
							JSONObject start= s.getJSONObject("start_location");
							
							
							double sLat = start.getDouble("lat");
							double sLon = start.getDouble("lng");
							
						
							
							JSONObject stop= s.getJSONObject("end_location");
							
							double stopLat =stop.getDouble("lat");
							double stopLon = stop.getDouble("lng");
							
							
							
							//Log.i("ENd", stopLat);
							p1=new GeoPoint((int) (sLat * 1E6),(int) (sLon * 1E6));
							path_start.add(p1);
							
							p2=new GeoPoint((int)(stopLat * 1E6),(int) (stopLon * 1E6));
							path_start.add(p2);
							//map.getOverlays().add(new RoutePathOverlay(path_start));
							//Log.i("ENd", p1.toString());
							
							
							
							
							
							
							
							
							JSONObject poly= s.getJSONObject("overview_polyline");
							enc_points =poly.getString("points");
							List<GeoPoint> mary = decodePoly(enc_points);
							//path_end.add(mary);
							
							
							
							}
							Log.i("ENd", path_start.toString());
							// map.getOverlays().add(new RoutePathOverlay(path_start));
							
						}
					}

					

				
				
				//ResponseHandler<String> responseHandler = new BasicResponseHandler();  
                //Content = Client.execute(httpget, responseHandler);  
            } catch (ClientProtocolException e) {  
                //Error = e.getMessage();  
                cancel(true);  
            } catch (IOException e) {  
               // Error = e.getMessage();  
                cancel(true);  
            } catch (Exception e) {
				e.printStackTrace();
			}
            map.getOverlays().add(new RoutePathOverlay(path_start));
              
            return null;  
        
          }
        protected void onPostExecute(Void unused) {  
        	
        	
        	//map.getOverlays().add(new RoutePathOverlay(path_start));

           
            //if (Error =="1" ) { 
            	//Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
            //}
        
        }  
    }
	
	
	
	
	
	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
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
	
	

}
