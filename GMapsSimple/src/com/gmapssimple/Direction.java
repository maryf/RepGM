package com.gmapssimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class Direction extends FragmentActivity {
	List<GeoPoint> path_start = new ArrayList<GeoPoint>();
	List<GeoPoint> path_end = new ArrayList<GeoPoint>();
	GeoPoint p1, p2;
	// String lat1,lon1,lat2,lon2;
	List<NameValuePair> nameValuePairs;
	MapView map;
	MapController mControl;
	SessionManager session;
	ArrayList<HashMap<String, String>> mylist;
	List<Overlay> overlayList;
	List<LatLng> path = new ArrayList<LatLng>();

	Drawable draf, start;

	int jarraylen, len;
	HashMap<String, String> map1;
	String[] lat, lon;
	String[] routeLatDb;
	String[] routeLonDb;
	String s;
	int latPath;
	int lonPath;
	String stringLat;
	
	GoogleMap map2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytrek);
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

		draf = getResources().getDrawable(R.drawable.markerblue);
		start = getResources().getDrawable(R.drawable.s);
		
		session = new SessionManager(getApplicationContext());

		Bundle extras = getIntent().getExtras();
		latPath = extras.getInt("startLatDb");
		lonPath = extras.getInt("startLonDb");
		Log.i("sendlatitude", Integer.toString(latPath));
		// gets the pinpoints that a user has uploaded from the AsyncTaskTrek in
		// the list "path"
		// and then the getUrl draws the route between those points
		getPathRoute(SignIn.add + "get_pathid.php");
		
		
		
		
		
		
		map2.addMarker(new MarkerOptions()
        .position(path.get(0))
        .title("smt").icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.s)));
		// asyncTaskTrek(SignIn.add+"mytrek.php");
		//Log.i("ok1", path.toString());
		int j = 1;
		for (int i = 0; i < path.size() - 1; i++) {
			LatLng geo = path.get(i);
			
			LatLng geo2 = path.get(j++);

			
			GMapV2Direction md = new GMapV2Direction();

			Document doc = md.getDocument(geo, geo2,
			                    GMapV2Direction.MODE_DRIVING);

			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			            PolylineOptions rectLine = new PolylineOptions().width(3).color(
			                    Color.RED);

			            for (int k = 0; k < directionPoint.size(); k++) {
			                rectLine.add(directionPoint.get(k));
			            }
			            Polyline polylin = map2.addPolyline(rectLine);
			            
							
							

								
								// creating an instance of ItemizedOverlay class
								// CustomPinpoint
								map2.addMarker(new MarkerOptions()
						        .position(geo2)
						        .title("smt"));


		}
		
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

	public void getPathRoute(String url) {
		try {
			new GetPathRoute().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getURL3(String url) {
		try {
			new GetURL3().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	
	

	private class GetURL3 extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();
		private String Error = null;

		@Override
		protected Void doInBackground(String... urls) {
			try {
				Log.i("RESPONSE", "ok");
				// Log.i("stringLa", stringLat);
				HttpPost httppost = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs
						.add(new BasicNameValuePair("latitude", stringLat));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = Client.execute(httppost);
				Log.i("RESPONSE", "OK");

				HttpEntity entity = response.getEntity();
				String inputs = EntityUtils.toString(entity);
				Log.i("RESPONSE3", inputs);
				session.createImageIdSession(inputs);

			} catch (ClientProtocolException e) {
				Error = e.getMessage();
				cancel(true);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();

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

	public class GetPathRoute extends AsyncTask<String, Void, Void> {

		private final HttpClient httpclient1 = new DefaultHttpClient();

		Location routeCoordLoc = new Location("");;
		float distance = 0;
		int[] nearPathId = null;

		@Override
		public Void doInBackground(String... urls) {
			try {
				HttpPost httppost = new HttpPost(urls[0]);

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("lat", Integer
						.toString(latPath)));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient1.execute(httppost);

				HttpEntity entity = response.getEntity();
				String respon = EntityUtils.toString(entity);
				Log.i("resp", respon);

				// tv.setText("ok!");
				JSONObject json = new JSONObject(respon.trim());
				//Log.i("ok", "okmetajsonobj");

				JSONArray jArray = json.getJSONArray("posts");
				LatLng startPoint = new LatLng(latPath/1E6, lonPath/1E6);
				path.add(startPoint);
				

				// getting the route table:path_id,coordinates
				for (int i = 0; i < jArray.length(); i++) {
					HashMap<String, String> map1;
					map1 = new HashMap<String, String>();
					JSONObject e1 = jArray.getJSONObject(i);
					s = e1.getString("post");

					JSONObject jObject = new JSONObject(s);

					routeLatDb = new String[jArray.length()];
					routeLonDb = new String[jArray.length()];
					// pathId = new int[jArray.length()];

					
					routeLatDb[i] =jObject.getString("lat");

					
					routeLonDb[i] = jObject.getString("lon");
					
					// map1.put("path_id", jObject.getInt("path_id"));
					// pathId[i] = map1.get("path_id");

					Log.i("TABLE STARTLAT", routeLatDb[i]);

					
					LatLng k = new LatLng(Integer.parseInt(routeLatDb[i])/1E6,
							Integer.parseInt(routeLonDb[i])/1E6);
					
					Log.i("trexei2", "trexei");
					path.add(k);
					Log.i("trexei3", "trexei");
					Log.i("trek", path.toString());

				}

			} catch (ClientProtocolException e) {

				cancel(true);
			} catch (JSONException e1) {
				Log.e("log_tag", "Error parsing data " + e1.toString());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();

			}
			return null;
		}

		@Override
		public void onPostExecute(Void unused) {

			

		}

	}

}
