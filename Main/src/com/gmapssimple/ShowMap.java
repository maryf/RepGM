package com.gmapssimple;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.gmapssimple.OpenMap.CustomPinpoint;
import com.gmapssimple.OpenMap.GetURL1;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

public class ShowMap extends MapActivity {

	
	MapView mapV;
	MapController mControl;
	GeoPoint GeoP,geoP;
	MyLocationOverlay myLocation, myLocation1;
	TextView tv;
	long start;
	long stop;
	int x;
	int y;
	int lat_start,lon_start;
	GeoPoint touchedPoint;
	SessionManager session;
	boolean flag;
	String stringLat;
	
	
	ArrayList<HashMap<String, String>> mylist;
	InputStream is;
	Drawable draf,draf1,museum,monument,palace,archeological_site,statue,church;
	String  d,value,value1;;
	String[] lat,lon ,bit, typ,imid;
	int[] intLat;
	
	
	
	int jarraylen;
	List<Overlay> overlayList;
	
	Context context;
	CharSequence text;
	int duration, one; 
	HashMap<String, String> map;
	
	
	String s = null;
	
	String[] period =new String[] {"byzantine","roman"};
	String[] type_dialog =new String[] {"museum","monument","statue","church","palace","archeological_site"};
	
	int selectedItem=0;
	
	int lat_path;
	int lon_path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.path);
		tv=(TextView)findViewById(R.id.textView1);
		session = new SessionManager(getApplicationContext());
		draf = getResources().getDrawable(R.drawable.markerblue);
		draf1= getResources().getDrawable(R.drawable.museum);
		
		
		museum= getResources().getDrawable(R.drawable.art_museum_2);
		monument= getResources().getDrawable(R.drawable.memorial);
		statue=getResources().getDrawable(R.drawable.statue_2);
		palace= getResources().getDrawable(R.drawable.palace_2);
		archeological_site= getResources().getDrawable(R.drawable.archeological_site);
		church= getResources().getDrawable(R.drawable.chapel_2);
		
		

		
		

		mapV = (MapView) findViewById(R.id.mapView2);
		mControl = mapV.getController();
		mControl.setZoom(11);
		mapV.displayZoomControls(true);
		mapV.setBuiltInZoomControls(true);
		overlayList = mapV.getOverlays();
		
		//get from the database the pinpoints and shows them on the map
		getURL1(SignIn.add+"selpic.php");  


		

		
		
		

	}
//constructors for the asynchronous tasks
public void createPath(String url) {  
		
		new CreatePath().execute(url);
	
	 } 
	
	public void getURL1(String url) {  
		
		new GetURL1().execute(url);
	
	 } 
	
	
	public void setRouteCoordinates(String url) {  
        new SetRouteCoordinates().execute(url);  
    } 

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();;
		//myLocation.disableMyLocation();
	}

	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//myLocation.enableMyLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
      //start a new path and place the first pinpoint
        case R.id.startPath:
        	
        	myLocation = new MyLocationOverlay(getApplicationContext(), mapV);
			mapV.getOverlays().add(myLocation);
			myLocation.enableMyLocation();

			myLocation.runOnFirstFix(new Runnable() {
				public void run() {
					GeoPoint geo=myLocation.getMyLocation();
					mControl.animateTo(geo);
					lat_start=geo.getLatitudeE6();
					lon_start=geo.getLongitudeE6();
					
					//set the pathsession true
					session.pathsession(true);	
					
					//sends the username and the current position in the db 
					//in order to update the path table and the pic table 
					//and gets the corresponding path_id and image_id
					createPath(SignIn.add+"pathuser.php");
					
					//then navigate to UploadImage.java
					startActivity(new Intent("com.gmapssimple.UPLOADIMAGE"));
					finish();
				}
			});
        	
        	
            return true;
 
        
        
        //place pinpoints in a path
        case R.id.placePin:
        	
        	boolean path=session.getPath();
			Log.i("path", String.valueOf(path));
			//check if the user has started a path
			if (path){
			myLocation1 = new MyLocationOverlay(getApplicationContext(), mapV);
			mapV.getOverlays().add(myLocation1);
			myLocation1.enableMyLocation();

			myLocation1.runOnFirstFix(new Runnable() {
				public void run() {
					mControl.animateTo(myLocation1.getMyLocation());
					geoP=myLocation1.getMyLocation();
					lat_path=geoP.getLatitudeE6();
					lon_path=geoP.getLongitudeE6();
					
					//sends the username,the current position and the current path_id 
					//in the db in order to update the route and pic table and gets the corresponding image_id
					setRouteCoordinates(SignIn.add+"set_route.php");
					startActivity(new Intent("com.gmapssimple.UPLOADIMAGE"));
					finish();
					
				}
			});
	 }
			else{
				Toast.makeText(getApplicationContext(), "You have to start a path first!",Toast.LENGTH_SHORT).show();
			}
			
            return true;
 
       
        
        //ending the pathsession
        case R.id.endPath:
        	
        	session.finishPath();
        	
            return true;
 
            
      
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
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
		public void getURL3(String url) {  
			
			new GetURL3().execute(url);
		
		 } 
		@Override
		public boolean onTap(int index) {
			flag=true;
			 
			int k=0;
			//get the index of the touched pinpoint as well as his coordinates
			OverlayItem item = pinpoints.get(index); 
			GeoPoint touchedGeoP=item.getPoint();
			
			int touchedLat=touchedGeoP.getLatitudeE6();
			
			stringLat=Integer.toString(touchedLat);
		
			//get the image_id of the touched pinpoint
			getURL3(SignIn.add+"get_imId.php");  
			
				
			
			startActivity(new Intent(getApplicationContext(),PinpointView.class));
			//finish();

			return true;
		}
		
	
	}
	
	
	
	
	
	private class CreatePath extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();  
        private String Error = null;  
          
        @Override
		protected Void doInBackground(String... urls) {
			try {
				HashMap<String, String> user = session.getUserDetails();
				String username = user.get(SessionManager.KEY_NAME);
				HttpPost httppost = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
				nameValuePairs.add(new BasicNameValuePair("username",username));
				nameValuePairs.add(new BasicNameValuePair("start_lat",Integer.toString(lat_start)));
				nameValuePairs.add(new BasicNameValuePair("start_lon",Integer.toString(lon_start)));
				
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response2 = Client.execute(httppost);
				//Log.i("RESPONSE", Integer.toString(lat_start));
				//Log.i("lon", Integer.toString(lon_start));
				
				HttpEntity entity2 = response2.getEntity();
				String pathId = EntityUtils.toString(entity2);
				Log.i("respPa", pathId);
				
				JSONObject json = new JSONObject(pathId.trim());
				JSONObject json1 = json.getJSONObject("posts");
				int pathId2=json1.getInt("path_id");
					
					Log.i("pathId", Integer.toString(pathId2));
					session.pathIdsession(Integer.valueOf(pathId2));
					

				JSONObject json2 = new JSONObject(pathId.trim());
				JSONObject json3 = json2.getJSONObject("posts1");
				String im_id=json3.getString("image_id");
				
				session.createImageIdSession(im_id);
				Log.i("image_id", im_id);	
				
				
				
				
	    	
				
			
        } catch (ClientProtocolException e) {  
            Error = e.getMessage();  
            cancel(true);  
        }  catch (IOException e) {
		// TODO Auto-generated catch block
        	Error = e.getMessage();
        	e.printStackTrace();
          
	} catch (JSONException e) {
			// TODO Auto-generated catch block
			Error = e.getMessage();
			e.printStackTrace();
		} 

          
        return null;  
		}  
        
        @Override
		protected void onPostExecute(Void unused) {    
			if (Error ==null ) 
            	Toast.makeText(getApplicationContext(), "OK!",Toast.LENGTH_SHORT).show();
            
	    }  
		
	}
	
	private class GetURL3 extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();  
        private String Error = null;  
         
        @Override
		protected Void doInBackground(String... urls) {
			try {
				Log.i("RESPONSE", "ok");
				HttpPost httppost2 = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
				nameValuePairs.add(new BasicNameValuePair("latitude",stringLat));
				
				httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response2 = Client.execute(httppost2);
				Log.i("RESPONSE", "OK");
				
				
				HttpEntity entity2 = response2.getEntity();
				String inputs = EntityUtils.toString(entity2);
				Log.i("RESPONSE3", inputs);
				session.createImageIdSession(inputs);										
			
			
			
        } catch (ClientProtocolException e) {  
            Error = e.getMessage();  
            cancel(true);  
        }  catch (IOException e) {
        	// TODO Auto-generated catch block
        	Error = e.getMessage();
        	e.printStackTrace();
          
	} 

          
        return null;  
		}  
        
        @Override
		protected void onPostExecute(Void unused) {    
			if (Error ==null ) 
            	Toast.makeText(getApplicationContext(), "OK!",Toast.LENGTH_SHORT).show();
            
	    }  
		
	}

	
	
	
	public class GetURL1 extends AsyncTask<String, Void, int[]> {  
		
		
		
		private final HttpClient httpclient1 = new DefaultHttpClient(); 
		private String Error = null; 
		//String url = "http://192.168.1.60/login/selpic.php";
		
		@Override
		public int[] doInBackground(String... urls) {  
		try {
		//HttpPost httppost1 = new HttpPost(urls[0]);
		HttpGet httpget = new HttpGet(urls[0]);
		HttpEntity entity1 = httpclient1.execute(httpget).getEntity();
		String response = EntityUtils.toString(entity1);
		JSONObject json = new JSONObject(response.trim());

		JSONArray jArray = json.getJSONArray("posts");
		mylist = new ArrayList<HashMap<String, String>>();
		int jarraylen = jArray.length();
		//getting the attributes of all images:coordinates,image id,type and assigns them to an arraylist
		for (int i = 0; i < jArray.length(); i++) {

			map = new HashMap<String, String>();
			JSONObject e1 = jArray.getJSONObject(i);
			s = e1.getString("post");
			
			JSONObject jObject = new JSONObject(s);

			lat = new String[jArray.length()];
			lon = new String[jArray.length()];
			//bit = new String[jArray.length()];
			typ = new String[jArray.length()];
			imid= new String[jArray.length()];
			
			map.put("latitude", jObject.getString("latitude"));
			lat[i] = map.get("latitude");
		
			map.put("longitude", jObject.getString("longitude"));
			lon[i] = map.get("longitude");

			//map.put("bitmap", jObject.getString("bitmap"));
			//bit[i] = map.get("bitmap");
			
			map.put("type", jObject.getString("type"));
			typ[i] = map.get("type");
			
			map.put("image_id", jObject.getString("image_id"));
			imid[i] = map.get("image_id");

			mylist.add(map);

			Log.i("TABLE LAT", lat[i]);

			intLat = new int[jArray.length()];
			
			intLat[i] = Integer.parseInt(lat[i]);
			
			int[] intLon = new int[jArray.length()];
			intLon[i] = Integer.parseInt(lon[i]);

			
			//creates an overlay item and an itemizedoverlay
			//item in order to create the pinpoint and show it on the map
			GeoPoint point = new GeoPoint(intLat[i], intLon[i]);
			OverlayItem overlayItem = new OverlayItem(point, "smth", null);
			//checking the type of the pinpoint and assigns the corresponding image
			if (typ[i].equals("museum")){
				//creating an instance of ItemizedOverlay class CustomPinpoint
				CustomPinpoint itemizedOverlay = new CustomPinpoint(museum,context);
				itemizedOverlay.insertPinpoint(overlayItem);
				overlayList.add(itemizedOverlay);
				mapV.getOverlays().add(itemizedOverlay);
				
			}
			
			
			if (typ[i].equals("monument")){
				//creating an instance of ItemizedOverlay class CustomPinpoint
				CustomPinpoint itemizedOverlay = new CustomPinpoint(monument,context);
				itemizedOverlay.insertPinpoint(overlayItem);
				overlayList.add(itemizedOverlay);
				mapV.getOverlays().add(itemizedOverlay);
				
			}
			
			
			if (typ[i].equals("palace")){
				//creating an instance of ItemizedOverlay class CustomPinpoint
				CustomPinpoint itemizedOverlay = new CustomPinpoint(palace,context);
				itemizedOverlay.insertPinpoint(overlayItem);
				overlayList.add(itemizedOverlay);
				mapV.getOverlays().add(itemizedOverlay);
				
			}
			
			
			if (typ[i].equals("statue")){
				//creating an instance of ItemizedOverlay class CustomPinpoint
				CustomPinpoint itemizedOverlay = new CustomPinpoint(statue,context);
				itemizedOverlay.insertPinpoint(overlayItem);
				overlayList.add(itemizedOverlay);
				mapV.getOverlays().add(itemizedOverlay);
				
			}
			
			
			if (typ[i].equals("archeological_site")){
				//creating an instance of ItemizedOverlay class CustomPinpoint
				CustomPinpoint itemizedOverlay = new CustomPinpoint(archeological_site,context);
				itemizedOverlay.insertPinpoint(overlayItem);
				overlayList.add(itemizedOverlay);
				mapV.getOverlays().add(itemizedOverlay);
				
			}
			
			
			if (typ[i].equals("church")){
				//creating an instance of ItemizedOverlay class CustomPinpoint
				CustomPinpoint itemizedOverlay = new CustomPinpoint(church,context);
				itemizedOverlay.insertPinpoint(overlayItem);
				overlayList.add(itemizedOverlay);
				mapV.getOverlays().add(itemizedOverlay);
				
			}
			
			
			

			overlayList = mapV.getOverlays();
			
		}
		
		
		
		} catch (ClientProtocolException e) {  
            Error = e.getMessage();  
            cancel(true);  
        } catch (JSONException e) {
        	Error = e.getMessage();
        	Log.e("log_tag", "Error parsing data " + e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Error = e.getMessage();
			e.printStackTrace();
	}
	return intLat;  
	}
		
	@Override	
	public void onPostExecute(int[] result) {  
		
		
			mapV.invalidate();
			if (Error ==null ) 
            	Toast.makeText(getApplicationContext(), "OK!",Toast.LENGTH_SHORT).show();

        	
        	
        	
    }
	
}
	
	private class SetRouteCoordinates extends AsyncTask<String,Void,Void>{
		private final HttpClient httpclient = new DefaultHttpClient(); 
		private String Error = null; 
		
		@Override
		protected Void doInBackground(String... urls) {
			
			try {
				
				
				HttpPost httppost = new HttpPost(urls[0]);
				HashMap<String, String> user = session.getUserDetails();
				String username = user.get(SessionManager.KEY_NAME);
				
				Log.d("prin", Integer.toString(session.getPathId()));
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("lat", Integer.toString(lat_path)));
				nameValuePairs.add(new BasicNameValuePair("lon", Integer.toString(lon_path)));
				nameValuePairs.add(new BasicNameValuePair("path_id", Integer.toString(session.getPathId())));
				nameValuePairs.add(new BasicNameValuePair("username", username));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				Log.d("meta", "meta");
				
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
			
				String im_id1 = EntityUtils.toString(entity);
				Log.i("placepin", im_id1);
				session.createImageIdSession(im_id1);
				
				
				
			
			} catch (Exception e) {
				Log.e("log_tag",
						"Error in http connection " + e.toString());
				Error = e.getMessage();
			}
			
			
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Void unused){
			if (Error ==null ) 
            	Toast.makeText(getApplicationContext(), "OK!",Toast.LENGTH_SHORT).show();
		}
		
	}
	

}
