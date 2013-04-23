package com.gmapssimple;

import java.io.InputStream;
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

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class CreatePin extends MapActivity {
	
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	ArrayList<HashMap<String, String>> mylist;
	InputStream is;
	long start;
	long stop;
	int x;
	int y;
	String value,value1;
	GeoPoint touchedPoint, geoP;
	List<Overlay> overlayList;
	MapView map;
	Drawable dr,dr1;
	MapController mControl;
	SessionManager session;
	MyLocationOverlay myLocation; 
	int lat_path;
	int lon_path;
	
	
	
	
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
		dr = getResources().getDrawable(R.drawable.markerblue);
		
		Touchy t = new Touchy();
		overlayList = map.getOverlays();
		overlayList.add(t);
		
		
		
		
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
				
				Toast.makeText(
						getBaseContext(),
						touchedPoint.getLatitudeE6() / 1e6 + ","
								+ touchedPoint.getLongitudeE6(),
						Toast.LENGTH_SHORT).show();

				

			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			//when the user taps the screen more than 2.5 seconds
			if (stop - start > 1500) {
				// TODO Auto-generated method stub
				HashMap<String, String> user = session.getUserDetails();
				//the session of the user's name is created
				value = user.get(SessionManager.KEY_NAME);
				
				
				asyncTask4(SignIn.add+"geoP.php");
				startActivity(new Intent("com.gmapssimple.UPLOADIMAGE"));
				finish();
			}
			return false;

		}
	}


//asynchronous task for inserting attributes on the db - coordinates and user's name -
//on the image with the specified image_id
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
			nameValuePairs.add(new BasicNameValuePair("latitude", Integer
					.toString(touchedPoint.getLatitudeE6())));
			Log.i("lat", Integer
					.toString(touchedPoint.getLatitudeE6()));
			nameValuePairs.add(new BasicNameValuePair("longitude", Integer
					.toString(touchedPoint.getLongitudeE6())));
			nameValuePairs.add(new BasicNameValuePair("username", value));
			Log.i("user", value);
			//nameValuePairs.add(new BasicNameValuePair("image_id", value1));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			response = httpclient.execute(httppost);
			entity = response.getEntity();
			String imageid1 = EntityUtils.toString(entity);
			Log.i("session", imageid1);
			session.createImageIdSession(imageid1);
			
			// httpclient.getConnectionManager().shutdown();
		} catch (Exception e1) {
			// TODO Auto-generated method stub
			Log.e("log_tag",
					"Error in http connection " + e1.toString());
		}
		
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void unused){
		//pinpoint creation
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



}
