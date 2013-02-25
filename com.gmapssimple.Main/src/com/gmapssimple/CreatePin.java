package com.gmapssimple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CreatePin extends MapActivity {
	
	//HttpClient httpclient;
	HttpPost httppost, httppost1;
	HttpResponse response, response1;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	ArrayList<HashMap<String, String>> mylist;
	InputStream is;
	long start;
	long stop;
	int x;
	int y;
	String value,value1,imageid;
	GeoPoint touchedPoint;
	List<Overlay> overlayList;
	MapView map;
	Drawable dr,dr1;
	MapController mControl;
	SessionManager session;
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openmap);
		
		session = new SessionManager(getApplicationContext());
		map = (MapView) findViewById(R.id.mapView1);
		mControl = map.getController();
		mControl.setZoom(11);
		map.displayZoomControls(true);
		map.setBuiltInZoomControls(true);
		Touchy t = new Touchy();
		overlayList = map.getOverlays();
		overlayList.add(t);
		
		dr = getResources().getDrawable(R.drawable.markerblue);
		
		
	}


	public void asyncTask4(String url) {  
        new AsyncTask4().execute(url);  
    }  

class Touchy extends Overlay {

		

		public boolean onTouchEvent(MotionEvent e, MapView m) {

			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();

				touchedPoint = map.getProjection().fromPixels(x, y);
				// Toast t=Toast.makeText(getBaseContext(), x/ 1E6 ,
				// Toast.LENGTH_LONG);
				// t.show();
				// touchedPoint=new GeoPoint((int)(x * 1E6), (int)(y * 1E6));
				Toast.makeText(
						getBaseContext(),
						touchedPoint.getLatitudeE6() / 1e6 + ","
								+ touchedPoint.getLongitudeE6(),
						Toast.LENGTH_SHORT).show();

				

			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}

			if (stop - start > 1500) {
				// TODO Auto-generated method stub
				HashMap<String, String> user = session.getUserDetails();
				value = user.get(SessionManager.KEY_NAME);
				HashMap<String, String> image = session.getImageId();
				value1 = image.get(SessionManager.KEY_IMAGEID);
				Toast.makeText(
						getBaseContext(),
						value1+value,
						Toast.LENGTH_SHORT).show();

				
				asyncTask4("http://10.0.2.2/login/geoP.php");
				startActivity(new Intent("com.gmapssimple.PINPOINTDETAILS"));
				finish();
				//finish();
			}
			return false;

		}
	}



private class AsyncTask4 extends AsyncTask<String,Void,Void>{
	 private final HttpClient httpclient = new DefaultHttpClient(); 
     
	@Override
	protected Void doInBackground(String... urls) {
		
		try {
			
			
			httppost = new HttpPost(urls[0]);
			//httppost = new HttpPost("http://192.168.1.60/login/geoP.php");
			String a = Integer.toString(x);
			String b = Integer.toString(y);

			nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("lat", Integer
					.toString(touchedPoint.getLatitudeE6())));
			nameValuePairs.add(new BasicNameValuePair("long", Integer
					.toString(touchedPoint.getLongitudeE6())));
			nameValuePairs.add(new BasicNameValuePair("username", value));
			nameValuePairs.add(new BasicNameValuePair("image_id", value1));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Toast.makeText(this, responseBody,
			// Toast.LENGTH_LONG).show();
			response = httpclient.execute(httppost);
			entity = response.getEntity();
			is = entity.getContent();
			
			// httpclient.getConnectionManager().shutdown();
		} catch (Exception e1) {
			Log.e("log_tag",
					"Error in http connection " + e1.toString());
			
		}
		
		
		// TODO Auto-generated method stub
		return null;
	}
	
	
	protected void onPostExecute(Void unused){
		OverlayItem overlayItem = new OverlayItem(touchedPoint,
				"What s up", "2on string");
		CustomPinpoint custom = new CustomPinpoint(dr, getBaseContext());

		// int k=custom.size();
		// custom.createItem(k-1);
		custom.insertPinpoint(overlayItem);

		overlayList.add(custom);
	}
	
}

class CustomPinpoint extends ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
	public Context c,mContext;

	public CustomPinpoint(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
		mContext = getApplicationContext();
		// TODO Auto-generated constructor stub
	}

	public CustomPinpoint(Drawable defaultMarker, Context context) {
		//super(boundCenter(defaultMarker));
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

	
}
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
}

@Override
protected boolean isRouteDisplayed() {
	// TODO Auto-generated method stub
	return false;
}


/*private static String convertStreamToString(InputStream is) {

	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	StringBuilder sb = new StringBuilder();

	String line = null;
	try {
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return sb.toString();
}*/

}
