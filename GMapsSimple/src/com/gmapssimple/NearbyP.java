package com.gmapssimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class NearbyP extends FragmentActivity {

	List<Overlay> overlayList;
	MyLocationOverlay myLocation1;
	MapController mControl;
	Location curr_loc;
	MapView mapV;
	List<Integer> pathLat = new ArrayList<Integer>();
	List<Integer> pathLon = new ArrayList<Integer>();
	Drawable draf1;
	int touchedLat, touchedLon;
	int[] startLonDb;
	int[] startLatDb;
	int[] pathId;
	int[] startLonDb1;
	int[] startLatDb1;
	int[] pathId1;
	Location routeCoordLoc = new Location("");
	float distance = 0;
	float distance1 = 0;
	String dist1;
	int dist2;
	int[] nearPathId = null;
	JSONArray jArray, jArray1;
	List<Integer> pathLat1 = new ArrayList<Integer>();
	List<Integer> pathLon1 = new ArrayList<Integer>();

	Boolean exists = false;
	SessionManager session;
	
	private GoogleMap map2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearbyp);

		session = new SessionManager(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		dist1 = extras.getString("dist");
		if (dist1.equals("1 Km")) {
			dist2 = 1000;
			Log.i("dist2", Integer.toString(dist2));
		} else if (dist1.equals("5 Km")) {
			dist2 = 5000;
			Log.i("dist2", Integer.toString(dist2));
		}

		else {
			dist2 = 10000;
			Log.i("dist2", Integer.toString(dist2));
		}

		
		
		map2 = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		        .getMap();

		
		map2.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null); 
		//Zakynthos coordinates
		LatLng geop1=new LatLng(37.786029, 20.898077);
		
		 CameraUpdate center=
			        CameraUpdateFactory.newLatLng(geop1);
			    CameraUpdate zoom=CameraUpdateFactory.zoomTo(11);

		map2.moveCamera(center);
		map2.animateCamera(zoom);

		

		
		draf1 = getResources().getDrawable(R.drawable.hiking);

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		// curr_loc = new Location(LocationManager.GPS_PROVIDER);
		curr_loc = locationManager.getLastKnownLocation(provider);
		// curr_loc.setLatitude(37.785006);
		// curr_loc.setLongitude(20.54684);
		// Log.i("mary", Double.toString(curr_loc.getLatitude()));

		// Initialize the location fields
		if (curr_loc != null) {
			System.out.println("Provider " + provider + " has been selected.");
			getNearbyPath(SignIn.add + "get_route.php");
			// onLocationChanged(location);
		} else {
			//
		}

	}

	

	// ItemizedOverlay class for creating and adding pinpoints on the map an for
	// handling onTap events
	public class CustomPinpoint extends ItemizedOverlay<OverlayItem> {
		private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
		public Context c, mContext;
		// Path path;

		static final int cnt = -1;

		public CustomPinpoint(Drawable defaultMarker) {
			super(boundCenter(defaultMarker));
			mContext = getBaseContext();
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

		@Override
		public boolean onTap(int index) {

			
			

			return true;
		}

	}

	public void getNearbyPath(String url) {

		new GetNearbyPath().execute(url);

	}

	// async task for getting the nearby paths from current position
	public class GetNearbyPath extends AsyncTask<String, Void, Void> {

		private final HttpClient httpclient = new DefaultHttpClient();
		String Error = null;

		@Override
		public synchronized Void doInBackground(String... urls) {
			try {
				// HttpPost httppost = new HttpPost(urls[0]);
				HttpGet httpget = new HttpGet(urls[0]);
				HttpEntity entity = httpclient.execute(httpget).getEntity();
				String response = EntityUtils.toString(entity);
				Log.i("respRoute", response);
				JSONObject json = new JSONObject(response.trim());

				jArray = json.getJSONArray("posts");
				jArray1 = json.getJSONArray("posts1");

				startLatDb = new int[jArray.length()];
				startLonDb = new int[jArray.length()];
				pathId = new int[jArray.length()];

				// getting the path table:path_id,coordinates
				for (int i = 0; i < jArray.length(); i++) {

					JSONObject e = jArray.getJSONObject(i);
					String s = e.getString("post");

					// JSONObject json3 = json2.getJSONObject("posts1");
					JSONObject jObject = new JSONObject(s);

					startLatDb[i] = jObject.getInt("start_lat");
					pathLat.add(startLatDb[i]);

					startLonDb[i] = jObject.getInt("start_lon");
					pathLon.add(startLonDb[i]);

					pathId[i] = jObject.getInt("path_id");

					Log.i("TABLE STARTLAT", Integer.toString(startLatDb[i]));

					
				}

				for (int i = 0; i < jArray1.length(); i++) {
					JSONObject e1 = jArray1.getJSONObject(i);
					String s1 = e1.getString("post1");

					JSONObject jObject1 = new JSONObject(s1);

					startLatDb1 = new int[jArray1.length()];
					startLonDb1 = new int[jArray1.length()];
					pathId1 = new int[jArray1.length()];

					startLatDb1[i] = jObject1.getInt("lat");
					pathLat1.add(startLatDb1[i]);

					startLonDb1[i] = jObject1.getInt("lon");
					pathLon1.add(startLonDb1[i]);

					pathId1[i] = jObject1.getInt("path_id");

					Log.i("TABLE STARTLAT1", Integer.toString(startLatDb1[i]));

					
				}

			} catch (ClientProtocolException e) {

				cancel(true);
				Error = "1";
			} catch (JSONException e1) {
				Log.e("log_tag", "Error parsing data " + e1.toString());
				Error = "1";
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				Error = "1";

			}
			return null;
		}

		@Override
		public void onPostExecute(Void unused) {
			
			if (Error == null) {
				for (int i=0; i<jArray.length(); i++){
				routeCoordLoc.setLatitude(pathLat.get(i) / 1E6);
				routeCoordLoc.setLongitude(pathLon.get(i) / 1E6);
				// Log.i("lat", Double.toString(curr_loc.getLatitude()));
				// Log.i("lon",Double.toString(routeCoordLoc.getLatitude()));
				// Log.i("dist2",Float.toString(curr_loc.distanceTo(routeCoordLoc)));
				distance = curr_loc.distanceTo(routeCoordLoc);

				if (distance < dist2) {
					exists = true;
					// in order not to check it later
					pathId[i] = 0;
					// Log.i("ok", "ok");
					// nearPathId[++j]=pathId[i];
					
					
					
						int maryLat=pathLat.get(i);
						int maryLon=pathLon.get(i);
						
						LatLng pointS= new LatLng(maryLat/1E6, maryLon/1E6);
						//Log.i("mary",Double.toString(intLat[i]/1E6));
						
						
						
						
							// creating an instance of ItemizedOverlay class
							// CustomPinpoint
							map2.addMarker(new MarkerOptions()
					        .position(pointS)
					        .title("path")
					        .icon(BitmapDescriptorFactory
					            .fromResource(R.drawable.hiking)));
							
						

				}
			}
				
			for (int i=0; i<jArray1.length(); i++){	
			Location routeCoordLoc1 = new Location("");
			routeCoordLoc1.setLatitude(startLatDb1[i] / 1E6);
			routeCoordLoc1.setLongitude(startLonDb1[i] / 1E6);
			
			distance1 = curr_loc.distanceTo(routeCoordLoc1);
			
			if (distance1 < dist2) {
				exists = true;
				Log.i("ok1", "ok");
				
				
				Log.i("route", Integer.toString(pathId1[i]));
				for (int m = 0; m < jArray.length(); m++) {

					if (pathId[m] == pathId1[i]) {
						// j=m;
						Log.i("path_id ok!",
								Integer.toString(pathId[m]));
						
						Log.i("indist", Integer.toString(startLatDb[m]));
						int maryLat=pathLat1.get(i);
						int maryLon=pathLon1.get(i);
						
						LatLng pointS= new LatLng(maryLat/1E6, maryLon/1E6);
						//Log.i("mary",Double.toString(intLat[i]/1E6));
						
						
						
						
							// creating an instance of ItemizedOverlay class
							// CustomPinpoint
							map2.addMarker(new MarkerOptions()
					        .position(pointS)
					        .title("path")
					        .icon(BitmapDescriptorFactory
					            .fromResource(R.drawable.hiking)));
						
						break;
					}

				}

				// creates an overlay item and an itemizedoverlay
				// item in order to create the pinpoint and show it on
				// the map

			}
			}
			
			map2.setOnMarkerClickListener(new OnMarkerClickListener()
            {

                public boolean onMarkerClick(Marker marker_touched) {
                	// get the index of the touched pinpoint as well as his coordinates
                	LatLng katiii=marker_touched.getPosition();

                	double lat_t=katiii.latitude;
                	
                	int lat_pT=(int) (lat_t*1E6);

        			// getPathId
        			int pointer = 0;
        			for (int i = 0; i < pathLat.size(); i++) {
        				if (pathLat.get(i) == lat_pT) {
        					pointer = i;
        					break;
        				}
        			}
        			// the Direction.java opens to show the selected path
        			Intent i = new Intent(getApplicationContext(), Direction.class);
        			
        			i.putExtra("startLatDb", pathLat.get(pointer));
        			i.putExtra("startLonDb", pathLon.get(pointer));
        			startActivity(i);
              
                    return true;
                }

            });
			}
			if (exists == false)
				if (session.getLan())
					Toast.makeText(
							getApplicationContext(),
							"Δεν υπάρχουν κοντινά μονοπάτια, μπορείς όμως να δημιουργήσεις ένα!!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(
							getApplicationContext(),
							"Sorry, there are no nearby paths, you can create one!!",
							Toast.LENGTH_SHORT).show();

		}

	}

}
