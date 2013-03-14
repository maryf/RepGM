package com.gmapssimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.gmapssimple.OpenMap.CustomPinpoint;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;


public class Direction extends MapActivity {
	List<GeoPoint> path_start = new ArrayList<GeoPoint>();
	List<GeoPoint> path_end = new ArrayList<GeoPoint>();
	GeoPoint p1,p2;
	//String lat1,lon1,lat2,lon2;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	MapView map;
	MapController mControl;
	SessionManager session;
	ArrayList<HashMap<String, String>> mylist;
	List<Overlay> overlayList;
	List<GeoPoint> path = new ArrayList<GeoPoint>();

	Drawable draf;

	int jarraylen,len;
	HashMap<String, String> map1;
	String[] lat,lon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytrek);
		map = (MapView) findViewById(R.id.mapView2);
		mControl = map.getController();
		mControl.setZoom(11);
		map.displayZoomControls(true);
		map.setBuiltInZoomControls(true);
		draf = getResources().getDrawable(R.drawable.markerblue);
		overlayList = map.getOverlays();
		
		session = new SessionManager(getApplicationContext());
		
		//gets the pinpoints that a user has uploaded from the AsyncTaskTrek in the list "path"
		//and then the getUrl draws the route between those points
		asyncTaskTrek(SignIn.add+"mytrek.php");
		Log.i("ok", path.toString());
		int j=1;
		for (int i = 0; i < len-1; i++) {
			GeoPoint geo=path.get(i);
			int la1=geo.getLatitudeE6();
			double lats=la1/1E6;
			//int mary=lats/0.000001;
			String lat1=String.valueOf(lats);
			int lo1=geo.getLongitudeE6();
			double lons=lo1/1E6;
			String lon1 = String.valueOf(lons);
		
			
			
			//if (j<len)
			//{
				GeoPoint geo2=path.get(j++);
				
				int la2=geo2.getLatitudeE6();
				double lats2=la2/1E6;
				
				String lat2=String.valueOf(lats2);
				
				
				int lo2=geo2.getLongitudeE6();
				double lons2=lo2/1E6;
				String lon2 = String.valueOf(lons2);
				
				
				Log.i("lat2", lats+" " + " "+ lons+" " + " "+lats2 +" "+ lons2);
				Log.i("lat1", lat1+" " + " "+ lon1+" " + " "+lat2 +" "+ lon2);
				
				
				
				
				
				
				
				
				getURL("http://maps.googleapis.com/maps/api/directions/json?origin="+lat1+","+lon1+"&destination="+lat2+"," 
						+lon2+"&sensor=false");
				
				
				//getURL("http://maps.googleapis.com/maps/api/directions/json?" +
						//"origin=38.375899,21.685268&destination=38.573721,21.567165&sensor=false&travel_mode=transit");
				
				//map.getOverlays().add(new RoutePathOverlay(path_start)); 
			//}
				 
		}
		//String maryb=String.valueOf(path.get(0).getLatitudeE6()/1E6);
		//String maryb1=String.valueOf(path.get(0).getLongitudeE6()/1E6);
		//String maryb2=String.valueOf(path.get(1).getLatitudeE6()/1E6);
		//String maryb3=String.valueOf(path.get(1).getLongitudeE6()/1E6);
		//String maryb4=String.valueOf(path.get(2).getLatitudeE6()/1E6);
		//String maryb5=String.valueOf(path.get(2).getLongitudeE6()/1E6);
		//Log.i("OKK", maryb+" "+maryb1+" "+maryb2+" "+maryb3+" "+maryb4+" "+maryb5);
		
		
		//getURL("http://maps.googleapis.com/maps/api/directions/json?origin="+maryb+","+maryb1+"&destination="+maryb4+"," 
				//+maryb5+"&waypoints="+maryb2+","+maryb3+"&sensor=false");

}
	  
	public void getURL(String url) {  
        try {
			new GetURL().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
	
	public void asyncTaskTrek(String url) {  
        try {
			new AsyncTaskTrek().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }
	
	
	//gets the direction pinpoints in json format and draw a path based on them
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

							
							JSONArray jArrayDir = null;
							jArrayDir =json.getJSONArray("routes");
							//ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
							//int jarraylen = jArray.length();
							
							JSONObject legs =jArrayDir.getJSONObject(0);
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
            map.invalidate();
            return null;  
        
          }
        protected void onPostExecute(Void unused) {  
        	

        }  
    }
	
	//ItemizedOverlay class for creating and adding pinpoints on the map an for handling onTap events
	 public class CustomPinpoint extends ItemizedOverlay<OverlayItem> {
			private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
			public Context c,mContext;
			//Path path;
			
		    static final int cnt = -1;

			public CustomPinpoint(Drawable defaultMarker) {
				super(boundCenter(defaultMarker));
				mContext = getApplicationContext();
				// TODO Auto-generated constructor stub
			}

			public CustomPinpoint(Drawable defaultMarker, Context context) {
				
				this(defaultMarker);
				c = context;
				// TODO Auto-generated constructor stub
			}

			@Override
			public OverlayItem createItem(int i) {
				// TODO Auto-generated method stub
				return pinpoints.get(i);
			}

			@Override
			public int size() {
				// TODO Auto-generated method stub
				return pinpoints.size();
			}

			public void insertPinpoint(OverlayItem item) {
				pinpoints.add(item);
				this.populate();
			}
			//public void getURL3(String url) {  
				
				//new GetURL3().execute(url);
			
			 //} 
			@Override
			public boolean onTap(int index) {
				//flag=true;
				 Toast.makeText(c, "Overlay Item " + pinpoints.get(index) +
				" tapped!", Toast.LENGTH_LONG).show();
				 
				 
				int k=0;
				//get the index of the touched pinpoint as well as his coordinates
				OverlayItem item = pinpoints.get(index); 
				GeoPoint touchedGeoP=item.getPoint();
				//Log.i("GEOPOINT", "[" + item.getPoint()+ "]");
				int touchedLat=touchedGeoP.getLatitudeE6();
				//String sdfafwfasd=touchedGeoP.toString();
				//Log.i("TOSTRING",sdfafwfasd);
				//int touchedLon=touchedGeoP.getLongitudeE6();
				//stringLat=Integer.toString(touchedLat);
				
				
				//getURL3(SignIn.add+"get_imId.php");  
				
					
				
				startActivity(new Intent(getApplicationContext(),PinpointView.class));
				finish();

				return true;
			}
			
		
		}
	
	//gets the user' s pinpoints from the db and puts them on the map
	public class AsyncTaskTrek extends AsyncTask <String,Void,List<GeoPoint>>{
		private final HttpClient httpClient = new DefaultHttpClient();
        //private String Error = null;  
	String ab = null;
	
	@Override
	public List<GeoPoint> doInBackground(String... urls) {
		// TODO Auto-generated method stub
		
	try{
		
		HashMap<String, String> user = session.getUserDetails();
		 String name = user.get(SessionManager.KEY_NAME);
		 Log.i("lat111", name);
    	
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


			mylist.add(map1);

			int[] intLat = new int[jArray.length()];
			
			intLat[i] = Integer.parseInt(lat[i]);
			
			
			int[] intLon = new int[jArray.length()];
			intLon[i] = Integer.parseInt(lon[i]);
			
			GeoPoint k=new GeoPoint(intLat[i],intLon[i]);
			OverlayItem overlayItem = new OverlayItem(k, "smth", null);
			
			//assigns the corresponding image
			//creating an instance of ItemizedOverlay class CustomPinpoint
			CustomPinpoint itemizedOverlay = new CustomPinpoint(draf,getApplicationContext());
			itemizedOverlay.insertPinpoint(overlayItem);
			overlayList.add(itemizedOverlay);
			map.getOverlays().add(itemizedOverlay);
			

			overlayList = map.getOverlays();
			Log.i("trexei2", "trexei");
			path.add(k);
			Log.i("trexei3", "trexei");
			Log.i("trek", path.toString());
			
			

		}
		len=path.size();
		
	} catch (Exception e1) {
		Log.e("log_rating1",
				"Error in http connection " + e1.toString());
		
	}

	return path;	
	
}	
	@Override
	protected void onPostExecute(List<GeoPoint> path1)  {  
	Log.i("lan", Integer.toString(len));
	
	
	/*int j=1;
	for (int i = 0; i < len; i++) {
		GeoPoint geo=path1.get(i);
		int la1=geo.getLatitudeE6();
		double lats=la1/1E6;
		//int mary=lats/0.000001;
		String lat1=String.valueOf(lats);
		int lo1=geo.getLongitudeE6();
		double lons=lo1/1E6;
		String lon1 = String.valueOf(lons);
		//lon1=Integer.toString(lons);
		
		
		if (j<len)
		{
			GeoPoint geo2=path1.get(j++);
			
			int la2=geo2.getLatitudeE6();
			double lats2=la2/1E6;
			//int mary=lats/0.000001;
			String lat2=String.valueOf(lats2);
			
			
			int lo2=geo2.getLongitudeE6();
			double lons2=lo2/1E6;
			String lon2 = String.valueOf(lons2);
			
			//int late=geo2.getLatitudeE6();
			//lat2=Integer.toString(late);
			//int lone=geo2.getLongitudeE6();
			//lon2=Integer.toString(lone);
			Log.i("lat1", lat1+" " + " "+ lon1+" " + " "+lat2 +" "+ lon2);
			//getURL("http://maps.googleapis.com/maps/api/directions/json?origin=lat1,lon1&destination=lat2,lon2&sensor=false"); 
			//map.getOverlays().add(new RoutePathOverlay(path)); 
	
			
			//new GetURL().execute("http://maps.googleapis.com/maps/api/directions/json?origin=38.2088258,21.7853143&destination=38.2505451,22.0810942&sensor=false&travel_mode=transit"); 
		
		}
	
	}*/

    
} 
	
	
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	//Overlay class that draws the path of a List of GeopPoints
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
                                if (i == 0) { //start point
                                        startPoint = pointA;
                                        path.moveTo(pointA.x, pointA.y);
                                } else {
                                        if (i == _points.size() - 1)//end point
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
