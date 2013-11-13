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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
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
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowMap extends SherlockFragmentActivity implements LocationListener  {

	
	MyLocationOverlay myLocation, myLocation1;
	TextView tv;
	long start;
	long stop;
	int x;
	int y;
	int lat_start, lon_start;
	SessionManager session;
	boolean flag;
	String stringLat;

	ArrayList<HashMap<String, String>> mylist;
	InputStream is;
	Drawable draf, draf1, museum, monument, palace, archeological_site, statue,
			church;
	String d, value, value1;;
	int[] lat,lon;
	String[] bit, typ, imid;
	int[] intLat;

	int jarraylen;
	List<Overlay> overlayList;

	Context context;
	CharSequence text;
	int duration, one;
	
	List<Integer> lat1=new ArrayList<Integer>();
	List<Integer> lon1=new ArrayList<Integer>();
	List<String> type=new ArrayList<String>();
	List<String> imid1=new ArrayList<String>();

	String s = null;

	String[] period = new String[] { "byzantine", "roman" };
	String[] type_dialog = new String[] { "museum", "monument", "statue",
			"church", "palace", "archeological_site" };

	int selectedItem = 0;

	int lat_path;
	int lon_path;
	private GoogleMap map2;
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.path);
		
		map2 = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		
		
		LatLng geop1=new LatLng(37.786029, 20.898077);
		
		 CameraUpdate center=
			        CameraUpdateFactory.newLatLng(geop1);

		 CameraUpdate zoom=CameraUpdateFactory.zoomTo(11);

			map2.moveCamera(center);
			map2.animateCamera(zoom);
		tv = (TextView) findViewById(R.id.textView1);
		session = new SessionManager(getApplicationContext());
		
		
		draf = getResources().getDrawable(R.drawable.markerblue);
		draf1 = getResources().getDrawable(R.drawable.museum);

		
		ActionBar actionBar = getSupportActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true);

		
		// get from the database the pinpoints and shows them on the map
		getURL1(SignIn.add + "selpic.php");
		
		map2.setOnMarkerClickListener(new OnMarkerClickListener()
        {

            public boolean onMarkerClick(Marker marker_touched) {
            	LatLng katiii=marker_touched.getPosition();
            	double lat_t=katiii.latitude;
            	double lon_t=katiii.longitude;
            	stringLat = Double.toString(lat_t*1E6);
    				
                getURL3(SignIn.add + "get_imId.php");
    			Intent i = new Intent(getApplicationContext(),
    					PinpointView.class);
    			i.putExtra("touchedLat", lat_t);
    			i.putExtra("touchedLon", lon_t);
    			startActivity(i);
    			
    			return true;
            }

        });

	}

	// constructors for the asynchronous tasks
	public void createPath(String url) {

		new CreatePath().execute(url);

	}

	public void getURL1(String url) {

		new GetURL1().execute(url);

	}
	
	public void getURL3(String url) {

		new GetURL3().execute(url);

	}

	public void setRouteCoordinates(String url) {
		new SetRouteCoordinates().execute(url);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		;
		// myLocation.disableMyLocation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// myLocation.enableMyLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_main, menu);
		MenuItem sitem = menu.findItem(R.id.startPath);
		if (session.getLan())
			sitem.setTitle("ΑΡΧΗ");

		MenuItem pitem = menu.findItem(R.id.placePin);
		if (session.getLan())
			pitem.setTitle("ΣΥΝΕΧΙΣΕ");

		MenuItem enditem = menu.findItem(R.id.endPath);
		if (session.getLan())
			enditem.setTitle("ΤΕΡΜΑ");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		// when the app icon is tap, go back to OpenMap.class
		case android.R.id.home:
			Intent intent3 = new Intent(this, OpenMap.class);
			intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent3);
			return true;

			// start a new path and place the first pinpoint
		case R.id.startPath:
			
			//Location mCurrentLocation;
		   
		   // mCurrentLocation = mLocationClient.getLastLocation();
			map2.setMyLocationEnabled(true);
	        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

	        // Creating a criteria object to retrieve provider
	        Criteria criteria = new Criteria();

	        // Getting the name of the best provider
	        String provider = locationManager.getBestProvider(criteria, true);
	        
	    
 
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
 
            if(location!=null){
                onLocationChanged(location);
            }

            lat_start = (int) (location.getLatitude()*1E6);
			lon_start = (int)(location.getLongitude()*1E6);

			// set the pathsession true
			session.pathsession(true);

			// sends the username and the current position in the db
			// in order to update the path table and the pic table
			// and gets the corresponding path_id and image_id
			createPath(SignIn.add + "pathuser.php");

			// then navigate to UploadImage.java
			startActivity(new Intent(ShowMap.this, UploadImage.class));
			finish();			

			return true;

			// place pinpoints in a path
		case R.id.placePin:

			boolean path = session.getPath();
			Log.i("path", String.valueOf(path));
			// check if the user has started a path
			if (path) {
				map2.setMyLocationEnabled(true);
		        LocationManager locationManager1 = (LocationManager) getSystemService(LOCATION_SERVICE);

		        // Creating a criteria object to retrieve provider
		        Criteria criteria1 = new Criteria();

		        // Getting the name of the best provider
		        String provider1 = locationManager1.getBestProvider(criteria1, true);
		        
		    
	 
	            // Getting Current Location
	            Location location1 = locationManager1.getLastKnownLocation(provider1);
	 
	            
	            
				
			
				

	            lat_path = (int) (location1.getLatitude()*1E6);
				lon_path = (int) (location1.getLongitude()*1E6);

				// sends the username,the current position and the
				// current path_id
				// in the db in order to update the route and pic table
				// and gets the corresponding image_id
				setRouteCoordinates(SignIn.add + "set_route.php");

				// then navigate to UploadImage.java
				startActivity(new Intent(ShowMap.this, UploadImage.class));
				finish();	
				
				
			} else {
				if (session.getLan())
					Toast.makeText(
							getApplicationContext(),
							"Πρέπει να ξεκινήσετε τη δημιουργία ενός μονοπατιού πρώτα!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(),
							"You have to start a path first!",
							Toast.LENGTH_SHORT).show();
			}

			return true;

			// ending the pathsession
		case R.id.endPath:

			session.finishPath();
			startActivity(new Intent(ShowMap.this, OpenMap.class));

			return true;

		default:
			return super.onOptionsItemSelected(item);
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

				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("start_lat", Integer
						.toString(lat_start)));
				nameValuePairs.add(new BasicNameValuePair("start_lon", Integer
						.toString(lon_start)));
				
				Log.i("latitude", Integer
						.toString(lat_start));
				
				Log.i("lon", Integer
						.toString(lon_start));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response2 = Client.execute(httppost);
				 Log.i("RESPONSE", Integer.toString(lat_start));
				// Log.i("lon", Integer.toString(lon_start));

				HttpEntity entity2 = response2.getEntity();
				String pathId = EntityUtils.toString(entity2);
				Log.i("respPa", pathId);

				JSONObject json = new JSONObject(pathId.trim());
				JSONObject json1 = json.getJSONObject("posts");
				int pathId2 = json1.getInt("path_id");

				Log.i("pathId", Integer.toString(pathId2));
				session.pathIdsession(Integer.valueOf(pathId2));

				JSONObject json2 = new JSONObject(pathId.trim());
				JSONObject json3 = json2.getJSONObject("posts1");
				String im_id = json3.getString("image_id");

				session.createImageIdSession(im_id);
				Log.i("image_id", im_id);

			} catch (ClientProtocolException e) {
				Error = e.getMessage();
				cancel(true);
			} catch (IOException e) {
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
			// if (Error ==null )
			// Toast.makeText(getApplicationContext(),
			// "OK!",Toast.LENGTH_SHORT).show();

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

				nameValuePairs
						.add(new BasicNameValuePair("latitude", stringLat));

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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Error = e.getMessage();
				e.printStackTrace();

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			// if (Error ==null )
			// Toast.makeText(getApplicationContext(),
			// "OK!",Toast.LENGTH_SHORT).show();

		}

	}

	public class GetURL1 extends AsyncTask<String, Void, int[]> {

		private final HttpClient httpclient1 = new DefaultHttpClient();
		private String Error = null;

		// String url = "http://192.168.1.60/login/selpic.php";

		@Override
		public int[] doInBackground(String... urls) {
			try {
				// HttpPost httppost1 = new HttpPost(urls[0]);
				HttpGet httpget = new HttpGet(urls[0]);
				HttpEntity entity1 = httpclient1.execute(httpget).getEntity();
				String response = EntityUtils.toString(entity1);
				JSONObject json = new JSONObject(response.trim());

				JSONArray jArray = json.getJSONArray("posts");
				mylist = new ArrayList<HashMap<String, String>>();
				int jarraylen = jArray.length();
				// getting the attributes of all images:coordinates,image
				// id,type and assigns them to an arraylist
				for (int i = 0; i < jArray.length(); i++) {

					JSONObject e1 = jArray.getJSONObject(i);
					s = e1.getString("post");

					JSONObject jObject = new JSONObject(s);

					
					

					lat = new int[jArray.length()];
					lon = new int[jArray.length()];
					typ = new String[jArray.length()];
					imid = new String[jArray.length()];

					lat[i] = Integer.parseInt(jObject.getString("latitude"));
					lat1.add(lat[i]);

					lon[i] = Integer.parseInt(jObject.getString("longitude"));
					lon1.add(lon[i]);

					typ[i] = jObject.getString("type");
					type.add(typ[i]);
					
					imid[i] = jObject.getString("image_id");
					imid1.add(imid[i]);

					
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
			
			for(int i=0; i<type.size(); i++){
				LatLng point = new LatLng(lat1.get(i)/1E6, lon1.get(i)/1E6);
				
				
				if (type.get(i).equals("museum")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.art_museum_2)));

				}

				if (type.get(i).equals("monument")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.memorial)));

					
					

				}

				if (type.get(i).equals("palace")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.palace_2)));


				}

				if (type.get(i).equals("statue")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.statue_2)));


				}

				if (type.get(i).equals("archeological_site")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.archeological_site)));


				}

				if (type.get(i).equals("church")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.chapel_2)));

				}

				if (type.get(i).equals("old_pic")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.historicalquarter)));

				}
			}
			

		}

	}

	private class SetRouteCoordinates extends AsyncTask<String, Void, Void> {
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
				nameValuePairs.add(new BasicNameValuePair("lat", Integer
						.toString(lat_path)));
				nameValuePairs.add(new BasicNameValuePair("lon", Integer
						.toString(lon_path)));
				nameValuePairs.add(new BasicNameValuePair("path_id", Integer
						.toString(session.getPathId())));
				nameValuePairs
						.add(new BasicNameValuePair("username", username));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				Log.d("meta", "meta");

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String im_id1 = EntityUtils.toString(entity);
				Log.i("placepin", im_id1);
				session.createImageIdSession(im_id1);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				Error = e.getMessage();
			}

			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			// if (Error ==null )
			// Toast.makeText(getApplicationContext(),
			// "OK!",Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}
