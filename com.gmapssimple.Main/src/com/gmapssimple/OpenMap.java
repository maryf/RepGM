package com.gmapssimple;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
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

public class OpenMap extends MapActivity {
	MapView mapV;
	//HttpClient httpclient, httpclient1;
	HttpPost httppost, httppost1;
	HttpPost httppost2;
	HttpResponse response, response1;
	HttpResponse response2;
	HttpEntity entity1;
	HttpEntity entity2;
	//List<NameValuePair> nameValuePairs;
	ArrayList<HashMap<String, String>> mylist;
	InputStream is;
	Drawable draf,draf1;
	String  d,value,value1;;
	String[] lat,lon ,bit, typ,imid;
	int[] intLat;
	String mary,stringLat;
	
	MapController mControl;
	GeoPoint GeoP;
	// MyLocationOverlay compass;
	MyLocationOverlay myLocation;
	TextView tv,tvnew;
	long start;
	long stop;
	int x;
	int y;
	int jarraylen;
	List<Overlay> overlayList;
	GeoPoint touchedPoint;
	Context context;
	CharSequence text;
	int duration, one; 
	HashMap<String, String> map;
	SessionManager session;
	private Object GetURL1;
	private Object GetURL3;
	String s = null;
	boolean flag=false;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapV = (MapView) findViewById(R.id.mapView);
		tv = (TextView) findViewById(R.id.textVie);
		tvnew = (TextView) findViewById(R.id.txtLocation);

		context = getApplicationContext();
		text = "Hello toast!";
		duration = Toast.LENGTH_SHORT;
		
		mControl = mapV.getController();
		GeoPoint geop1=new GeoPoint(38226100,21733333);
		mControl.setZoom(13);
		mapV.displayZoomControls(true);
		mapV.setBuiltInZoomControls(true);
		mControl.animateTo(geop1);
		Touchy t = new Touchy();
		overlayList = mapV.getOverlays();
		overlayList.add(t);
		draf = getResources().getDrawable(R.drawable.markerblue);
		draf1= getResources().getDrawable(R.drawable.museum);
		
		//Bundle extras = getIntent().getExtras();
		//value = extras.getString("username");
		//tv.setText(value);
		session = new SessionManager(getApplicationContext());
		
		
		
		Button bUpImage = (Button) findViewById(R.id.UpImage);
		bUpImage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.gmapssimple.UPLOADIMAGE"));
				finish();

			}
		});
		
		
		
		
		
		
		Button bCurrLoc = (Button) findViewById(R.id.getLocation);
		bCurrLoc.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				myLocation = new MyLocationOverlay(getApplicationContext(), mapV);
				mapV.getOverlays().add(myLocation);
				myLocation.enableMyLocation();

				myLocation.runOnFirstFix(new Runnable() {
					public void run() {
						mControl.animateTo(myLocation.getMyLocation());
					}
				});
			}
		});
		
	getURL1("http://10.0.2.2/login/selpic.php");  
	//HashMap<String, String> imageId = session.getLat();
	 //String valuefs = imageId.get(SessionManager.KEY_IMAGEID);
	 //Toast.makeText(OpenMap.this, valuefs, Toast.LENGTH_SHORT).show();
	
}
	
	public void getURL1(String url) {  
		
			new GetURL1().execute(url);
		
		 } 
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mapmenu, menu);
        return true;
    }
  
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
 
        switch (item.getItemId())
        {
        case R.id.menu_bookmark:
           
            Toast.makeText(OpenMap.this, "Bookmark is Selected", Toast.LENGTH_SHORT).show();
            return true;
 
        case R.id.menu_save:
            Toast.makeText(OpenMap.this, "Save is Selected", Toast.LENGTH_SHORT).show();
            return true;
 
        case R.id.menu_search:
            Toast.makeText(OpenMap.this, "Search is Selected", Toast.LENGTH_SHORT).show();
            return true;
 
        case R.id.menu_share:
            Toast.makeText(OpenMap.this, "Share is Selected", Toast.LENGTH_SHORT).show();
            return true;
 
        case R.id.menu_delete:
            Toast.makeText(OpenMap.this, "Delete is Selected", Toast.LENGTH_SHORT).show();
            return true;
 
        case R.id.menu_preferences:
            Toast.makeText(OpenMap.this, "Preferences is Selected", Toast.LENGTH_SHORT).show();
            return true;
 
        default:
            return super.onOptionsItemSelected(item);
        }
    }    
    public class GetURL1 extends AsyncTask<String, Void, int[]> {  
		
			
			
			private final HttpClient httpclient1 = new DefaultHttpClient();
			private String Content;  
			private String Error = null; 
			//String url = "http://192.168.1.60/login/selpic.php";
			protected void onPreExecute() {  
			}
			
			public int[] doInBackground(String... urls) {  
			try {
			httppost1 = new HttpPost(urls[0]);
			HttpGet httpget = new HttpGet(urls[0]);
			entity1 = httpclient1.execute(httpget).getEntity();
			String response = EntityUtils.toString(entity1);
			//tv.setText("ok!");
			JSONObject json = new JSONObject(response.trim());

			JSONArray jArray = json.getJSONArray("posts");
			mylist = new ArrayList<HashMap<String, String>>();
			jarraylen=jArray.length();
			
			for (int i = 0; i < jArray.length(); i++) {

				map = new HashMap<String, String>();
				JSONObject e1 = jArray.getJSONObject(i);
				s = e1.getString("post");
				
				JSONObject jObject = new JSONObject(s);

				lat = new String[jArray.length()];
				lon = new String[jArray.length()];
				bit = new String[jArray.length()];
				typ = new String[jArray.length()];
				imid= new String[jArray.length()];
				
				map.put("latitude", jObject.getString("latitude"));
				lat[i] = map.get("latitude");
			
				map.put("longitude", jObject.getString("longitude"));
				lon[i] = map.get("longitude");

				map.put("bitmap", jObject.getString("bitmap"));
				bit[i] = map.get("bitmap");
				
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

				
				
				GeoPoint point = new GeoPoint(intLat[i], intLon[i]);
				OverlayItem overlayItem = new OverlayItem(point, "smth", null);
				if (typ[i]=="museum"){
					CustomPinpoint itemizedOverlay = new CustomPinpoint(draf,context);
					itemizedOverlay.insertPinpoint(overlayItem);
					overlayList.add(itemizedOverlay);
					mapV.getOverlays().add(itemizedOverlay);
					
				}
				else{CustomPinpoint itemizedOverlay = new CustomPinpoint(draf,context);
				itemizedOverlay.insertPinpoint(overlayItem);
				overlayList.add(itemizedOverlay);
				mapV.getOverlays().add(itemizedOverlay);}
				
				
				

				overlayList = mapV.getOverlays();
				// overlayList.add();
				//mapV.invalidate();

				// int k=custom.size();
				// custom.createItem(k-1);

			}
			
			
			
			} catch (ClientProtocolException e) {  
                Error = e.getMessage();  
                cancel(true);  
            } catch (JSONException e1) {
			Log.e("log_tag", "Error parsing data " + e1.toString());
			} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
              mary="geia";
		}
		return intLat;  
		}
		public void onPostExecute(int[] result) {  
			//for (int i = 0; i < 2; i++) {
			//tv.setText(result[i]);
		    //}
			
			
				mapV.invalidate();
            	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
            	

            	
            	
            	
        }
		
}
    
    


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// compass.disableCompass();
		//myLocation.disableMyLocation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// compass.enableCompass();
		// myLocation.enableMyLocation();
	}

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private static String convertStreamToString(InputStream is) {

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
	}
	
	

	class Touchy extends Overlay {

		

		public boolean onTouchEvent(MotionEvent e, MapView m) {

			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();

				touchedPoint = mapV.getProjection().fromPixels(x, y);
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
			return false;

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
			 Toast.makeText(c, "Overlay Item " + pinpoints.get(index) +
			" tapped!", Toast.LENGTH_LONG).show();
			 
			 
			int k=0;
			OverlayItem item = pinpoints.get(index); 
			GeoPoint touchedGeoP=item.getPoint();
			//Log.i("GEOPOINT", "[" + item.getPoint()+ "]");
			int touchedLat=touchedGeoP.getLatitudeE6();
			String sdfafwfasd=touchedGeoP.toString();
			Log.i("TOSTRING",sdfafwfasd);
			int touchedLon=touchedGeoP.getLongitudeE6();
			stringLat=Integer.toString(touchedLat);
			
			getURL3("http://10.0.2.2/login/get_imId.php");  
			
				
			
			startActivity(new Intent(getApplicationContext(),PinpointView.class));
			finish();

			return true;
		}

	}
	private class GetURL3 extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();  
		private String Content;  
        private String Error = null;  
        protected void onPreExecute() {  
        
        }  

		protected Void doInBackground(String... urls) {
			try {
				Log.i("RESPONSE", "ok");
				Log.i("stringLa", stringLat);
				httppost2 = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
				nameValuePairs.add(new BasicNameValuePair("latitude",stringLat));
				
				httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response2 = Client.execute(httppost2);
				Log.i("RESPONSE", "OK");
				
					//t1.setText("ok2");
					entity2 = response2.getEntity();
					InputStream is3= entity2.getContent();
					String inputs = convertStreamToString(is3);
					 //String is = EntityUtils.toString(entity2);
					//if (entity2 != null) {
						//t1.setText("okmary");
						//JSONObject jsonResponse = new JSONObject(is);
						//String imageid = jsonResponse.getString("image_id");
												// mySQL
					Log.i("RESPONSE3", inputs);
						session.createImageIdSession(inputs);											// table
																		// field
						
			
					//}

				

			
			
			
        } catch (ClientProtocolException e) {  
            Error = e.getMessage();  
            cancel(true);  
        }  catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
          
	} 

          
        return null;  
		}  
		protected void onPostExecute(Void unused) {    
			if (Error ==null ) 
            	Toast.makeText(getApplicationContext(), "OK!",Toast.LENGTH_SHORT).show();
            
	    }  
		
	}

}