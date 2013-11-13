package com.gmapssimple;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import com.gmapssimple.OpenMap.CustomPinpoint;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class TopTenPOI extends FragmentActivity {

	MapView mapV;
	Context context;
	MapController mControl;
	
	SessionManager session;
	ArrayList<HashMap<String, String>> mylist;
	int jarraylen;
	int[] lat;
	int[] lon;
	String[] typ;
	int[] intLat;
	String stringLat;
	
	private GoogleMap map2;
	
	List<String> type=new ArrayList<String>();
	List<Integer> lat1=new ArrayList<Integer>();
	List<Integer> lon1=new ArrayList<Integer>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mary);
		
		map2 = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		        .getMap();

		
		 // check if map is created successfully or not
        if (map2 == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();}
		
		
     // Zoom in, animating the camera.
     		
     		
     		LatLng geop1=new LatLng(37.786029, 20.898077);
     		
     		 CameraUpdate center=
     			        CameraUpdateFactory.newLatLng(geop1);

     		map2.moveCamera(center);
     		map2.animateCamera(CameraUpdateFactory.zoomTo(11), 2000, null); 

		session = new SessionManager(getApplicationContext());
		

		getTopTenPoi(SignIn.add + "getTopTen.php");
		
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

	public void getTopTenPoi(String url) {

		try {
			new GetTopTenPOI().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// async task for getting tha favorite POI of the user
	public class GetTopTenPOI extends AsyncTask<String, Void, Void> {

		private final HttpClient httpclient = new DefaultHttpClient();
		String Error = null;
		private HttpPost httppost;

		@Override
		public synchronized Void doInBackground(String... urls) {
			try {
				httppost = new HttpPost(urls[0]);

				HttpResponse response = httpclient.execute(httppost);
				// Log.i("RESPONSE", "OK");

				HttpEntity entity = response.getEntity();
				String inputs = EntityUtils.toString(entity);
				Log.i("respmyPOI", inputs);

				JSONObject json = new JSONObject(inputs);

				JSONArray jArray = json.getJSONArray("posts");

				mylist = new ArrayList<HashMap<String, String>>();
				jarraylen = jArray.length();
				// getting the attributes of all images:coordinates,type and
				// assigns them to an arraylist
				for (int i = 0; i < jArray.length(); i++) {

					JSONObject e1 = jArray.getJSONObject(i);
					String s = e1.getString("post");

					JSONObject jObject = new JSONObject(s);

					lat = new int[jArray.length()];
					lon = new int[jArray.length()];
					// bit = new String[jArray.length()];
					typ = new String[jArray.length()];
					// imid= new String[jArray.length()];

					lat[i] = Integer.parseInt(jObject.getString("latitude"));
					lat1.add(lat[i]);

					lon[i] = Integer.parseInt(jObject.getString("longitude"));
					lon1.add(lon[i]);

					typ[i] = jObject.getString("type");
					type.add(typ[i]);
					
					


				}

			} catch (ClientProtocolException e) {

				cancel(true);
				Error = "1";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				Error = "1";

			}
			return null;
		}

		@Override
		public void onPostExecute(Void unused) {

			if (Error == null){
				Log.i("ok", "ok");
				
				for(int i=0; i<type.size(); i++){
				LatLng point = new LatLng(lat1.get(i)/1E6, lon1.get(i)/1E6);
				
				
				if (type.get(i).equals("museum")) {
					
					map2.addMarker(new MarkerOptions()
			        .position(point)
			        .icon(BitmapDescriptorFactory
			            .fromResource(R.drawable.museum)));

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

	// asynchronous task for getting the image_id (from the db) of an image and
	// create a session of it
	private class GetURL3 extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();
		private String Error = null;

		@Override
		protected Void doInBackground(String... urls) {
			try {
				Log.i("RESPONSE", "ok");
				// Log.i("stringLa", stringLat);
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
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (Error == null)
				Toast.makeText(getApplicationContext(), "OK!",
						Toast.LENGTH_SHORT).show();

		}

	}

	

}
