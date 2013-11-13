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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

public class MyPOI extends FragmentActivity {
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	SessionManager session;

	int jarraylen;
	int[] lat,lon;
	String[] typ;
	Context context;
	int[] intLat;
	Drawable museum, monument, statue, palace, archeological_site, church,
			old_pic;
	String stringLat;
	GoogleMap map2;
	List<Integer> lat1=new ArrayList<Integer>();
	List<Integer> lon1=new ArrayList<Integer>();
	List<String> type=new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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

		session = new SessionManager(getApplicationContext());
		// asyncTaskTrek(SignIn.add+"mytrek.php");

		getMyPoi(SignIn.add + "get_mypoi.php");
		
		
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

	public void getMyPoi(String url) {

		new GetMyPoi().execute(url);

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
			// if (Error ==null )
			// Toast.makeText(getApplicationContext(),
			// "OK!",Toast.LENGTH_SHORT).show();

		}

	}

	// async task for getting tha favorite POI of the user
	public class GetMyPoi extends AsyncTask<String, Void, Void> {

		private final HttpClient httpclient = new DefaultHttpClient();
		String Error = null;

		@Override
		public synchronized Void doInBackground(String... urls) {
			try {
				httppost = new HttpPost(urls[0]);
				HashMap<String, String> user = session.getUserDetails();
				String name = user.get(SessionManager.KEY_NAME);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("username", name));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = httpclient.execute(httppost);
				Log.i("RESPONSE", "OK");

				HttpEntity entity = response.getEntity();
				String inputs = EntityUtils.toString(entity);
				// Log.i("respmyPOI", inputs);

				JSONObject json = new JSONObject(inputs.trim());

				JSONArray jArray = json.getJSONArray("posts");
				if (jArray.isNull(0)) {
					Error = "2";

				} else {

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
				}

			} catch (ClientProtocolException e) {

				cancel(true);
				Error = "1";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-gener9ated catch block
				e2.printStackTrace();
				Error = "1";

			}
			return null;
		}

		@Override
		public void onPostExecute(Void unused) {

			if (Error == null){
				Toast.makeText(getApplicationContext(), "Points Loaded!",
						Toast.LENGTH_SHORT).show();
			
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
			
		
				
			else if (Error == "2") {
				if (session.getLan())
					Toast.makeText(
							getApplicationContext(),
							"Δεν έχεις καταχωρήσει ακόμα κανένα Αγαπημένο σημείο!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(),
							"You haven no favorite POI", Toast.LENGTH_SHORT)
							.show();

			}

		}

	}

}
