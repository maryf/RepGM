package com.gmapssimple;

import java.io.IOException;
import java.io.InputStream;
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

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;


import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.maps.Projection;

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
	ArrayList<HashMap<String, String>> mylist,mylist1;
	InputStream is;
	Drawable draf,draf1,museum,monument,statue,palace,archeological_site,church;
	String  d,value,value1;;
	String[] lat,lon ,bit, typ,imid;
	int[] startLonDb  ;
	int[] startLatDb;
	int[] intLat;
	String stringLat;
	
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
	HashMap<String, Integer> map1;
	SessionManager session;
	private Object GetURL1;
	private Object GetURL3;
	String s = null;
	boolean flag=false;
	String[] period =new String[] {"byzantine","roman"};
	String[] type_dialog =new String[] {"museum","monument","statue","archeological_site"};
	int selectedItem=0;
	int[] pathId;
	MyLocationOverlay myLocation1;
	Location curr_loc;
	int[] routeLatDb;
	int[] routeLonDb;
	List<GeoPoint> path = new ArrayList<GeoPoint>();
	List<Integer> pathLat =new ArrayList<Integer>();
	List<Integer> pathLon = new ArrayList<Integer>();

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//ActionBar actionBar = getActionBar();
		//actionBar.show();
		
		mapV = (MapView) findViewById(R.id.mapView);
		//tv = (TextView) findViewById(R.id.textVie);
		//tvnew = (TextView) findViewById(R.id.txtLocation);
		
		
		//toast variables
		context = getApplicationContext();
		text = "Hello toast!";
		duration = Toast.LENGTH_SHORT;
		
		mControl = mapV.getController();
		//new GeopPoint for the city of Patras/Zankynthos
		GeoPoint geop1=new GeoPoint(37786029,20898077);
		
		mControl.setZoom(14);
		mapV.displayZoomControls(true);
		mapV.setBuiltInZoomControls(true);
		mControl.animateTo(geop1);
		
		//overlay item for getting the touched coordinates
		Touchy t = new Touchy();
		overlayList = mapV.getOverlays();
		overlayList.add(t);
		
		//drawables for the various types of images
		draf = getResources().getDrawable(R.drawable.markerblue);
		draf1= getResources().getDrawable(R.drawable.hiking);
		museum= getResources().getDrawable(R.drawable.art_museum_2);
		monument= getResources().getDrawable(R.drawable.memorial);
		statue=getResources().getDrawable(R.drawable.statue_2);
		palace= getResources().getDrawable(R.drawable.palace_2);
		archeological_site= getResources().getDrawable(R.drawable.archeological_site);
		church= getResources().getDrawable(R.drawable.chapel_2);



		//creating an instance of class SessionManager
		session = new SessionManager(getApplicationContext());

		
	
	getURL1(SignIn.add+"selpic.php");  

}
	//method which causes the execution of asynctask GetURL1
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
        
        
        
      //upload image
        case R.id.upload:
        	if (session.isLoggedIn()){
        		//if the user is logged in takes him to UploadImage.class
        		Intent intent = new Intent(this, CreatePin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
				}else{
					//else the SignIn.class opens so the user can log in
					Toast.makeText(getApplicationContext(), "You have to log in first",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(this, SignIn.class);
	                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	                startActivity(intent);
					finish();
				}
        	break;
        	
            
 
        
        
        //users current position
        case R.id.curPos:
        	myLocation = new MyLocationOverlay(getApplicationContext(), mapV);
        	mapV.getOverlays().add(myLocation);
			
			 //Attempts to enable MyLocation, registering for updates from 
			//LocationManager.GPS_PROVIDER and LocationManager.NETWORK_PROVIDER.
			myLocation.enableMyLocation();

			myLocation.runOnFirstFix(new Runnable() {
				public void run() {
					mControl.animateTo(myLocation.getMyLocation());
				}
			});
			break;
 
       
        
        
        case R.id.sliderIm:
        	
        	
        	Dialog();
        	break;
            
 
            
            
        //log out user    
        case R.id.logout:
        	session.logoutUser();
        	Intent intent1 = new Intent(this, SignIn.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
            break;
            
            
        case R.id.create_path:
        	
            
            Intent intent2 = new Intent(this, ShowMap.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent2);
            break;
 
       case R.id.showPaths:

    	   myLocation1 = new MyLocationOverlay(getApplicationContext(), mapV);
			mapV.getOverlays().add(myLocation1);
			myLocation1.enableMyLocation();

			myLocation1.runOnFirstFix(new Runnable() {
				public void run() {
					GeoPoint loc1=myLocation1.getMyLocation();
					mControl.animateTo(loc1);
					Log.i("loc1", Double.toString(loc1.getLatitudeE6()/1E6));
					curr_loc = new Location("");
					curr_loc.setLatitude(loc1.getLatitudeE6()/1E6);
					curr_loc.setLongitude(loc1.getLongitudeE6()/1E6);
					//GeoPoint cur_loc=myLocation.getMyLocation();
					getNearbyPath(SignIn.add+"get_route.php");
			           

				}
			});
			break;
    	             
 
      //  case R.id.menu_preferences:
       //     Toast.makeText(OpenMap.this, "Preferences is Selected", Toast.LENGTH_SHORT).show();
        //    return true;
 
        default:
            break;
        }
        return true;
    }
    
    
    
  //creates dialog with the available chronological periods and sends the period to SlideActivity.java 
  void Dialog() {
	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Select a time period");	  
      
      int selected = selectedItem;	        
      builder.setSingleChoiceItems(
      		period, 
              selected, 
              new DialogInterface.OnClickListener() {
          
          public void onClick(DialogInterface dialog,int which) {
          	selectedItem=which;
          	Toast.makeText(OpenMap.this,"You Select Period "+period[selectedItem],Toast.LENGTH_SHORT).show();
          	//if (selectedItem!=0){
          	Intent i = new Intent(OpenMap.this, SlideActivity.class);
          	i.putExtra("period",period[selectedItem] );
          	startActivity(i);
          	//}
            dialog.dismiss();
          }
      });
      AlertDialog alert = builder.create();
      alert.show();
      
	 
 }
  
  
  void Dialog1() {
	  AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle("Select a time period");	  
      
      int selected = selectedItem;	        
      builder.setSingleChoiceItems(
    		  type_dialog, 
              selected, 
              new DialogInterface.OnClickListener() {
          
          public void onClick(DialogInterface dialog,int which) {
          	selectedItem=which;
          	Toast.makeText(OpenMap.this,"You Select Type "+type_dialog[selectedItem],Toast.LENGTH_SHORT).show();
          	//if (selectedItem!=0){
          	Intent i = new Intent(OpenMap.this, ListViewActivity.class);
          	i.putExtra("type",type_dialog[selectedItem] );
          	startActivity(i);
          	//}
            dialog.dismiss();
          }
      });
      AlertDialog alert = builder.create();
      alert.show();
      
	 
 }
    
    
    //asynchronous task making a network call(in a new independent thread) so 
    //all the pinpoints can be shown on the map
    public class GetURL1 extends AsyncTask<String, Void, int[]> {  
		
			
			
			private final HttpClient httpclient1 = new DefaultHttpClient();
			private String Content;  
			private String Error = null; 
			//String url = "http://192.168.1.60/login/selpic.php";
			protected void onPreExecute() {  
			}
			
			public synchronized int[] doInBackground(String... urls) {  
			try {
			httppost1 = new HttpPost(urls[0]);
			HttpGet httpget = new HttpGet(urls[0]);
			entity1 = httpclient1.execute(httpget).getEntity();
			String response = EntityUtils.toString(entity1);
			JSONObject json = new JSONObject(response.trim());

			JSONArray jArray = json.getJSONArray("posts");
			mylist = new ArrayList<HashMap<String, String>>();
			jarraylen=jArray.length();
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

				Log.i("TABLE LAT", typ[i]);

				intLat = new int[jArray.length()];
				
				intLat[i] = Integer.parseInt(lat[i]);
				
				int[] intLon = new int[jArray.length()];
				intLon[i] = Integer.parseInt(lon[i]);

				
				//creates an overlay item and an itemizedoverlay
				//item in order to create the pinpoint and show it on the map
				GeoPoint point = new GeoPoint(intLat[i], intLon[i]);
				OverlayItem overlayItem = new OverlayItem(point, "smt", null);
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
            } catch (JSONException e1) {
			Log.e("log_tag", "Error parsing data " + e1.toString());
			} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
             
		}
		return intLat;  
		}
		public void onPostExecute(int[] result) {  
			
			
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
	
	
	
	

	class Touchy extends Overlay {

		

		public boolean onTouchEvent(MotionEvent e, MapView m) {
			//when the user touches the screen the coordinates of the touched point are calculated
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();

				touchedPoint = mapV.getProjection().fromPixels(x, y);

				

			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			return false;

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
		@Override
		public boolean onTap(int index) {
			
			 Toast.makeText(c, "Overlay Item " + pinpoints.get(index) +
			" tapped!", Toast.LENGTH_LONG).show();
			 
			 
			int k=0;
			//get the index of the touched pinpoint as well as his coordinates
			OverlayItem item = pinpoints.get(index); 
			GeoPoint touchedGeoP=item.getPoint();
			String title=item.getTitle();
		
			
			int touchedLat=touchedGeoP.getLatitudeE6();
			
			stringLat=Integer.toString(touchedLat);
			Log.i("title", title+stringLat);
			
			//checks if the user taps the image which is used to show the start of a path
			if (title=="path"){
				//getPathId
				int pointer=0;
				for(int i=0; i<pathLat.size(); i++){
					if (pathLat.get(i)==touchedLat){
						pointer=i;
						break;
						}
				}
				//the Direction.java opens to show the selected path
				Intent i = new Intent(getApplicationContext(), Direction.class);
				Log.i("pointer", Integer.toString(pointer));
				Log.i("latprin", Integer.toString(pathLat.get(pointer)));
				i.putExtra("startLatDb", pathLat.get(pointer));
				i.putExtra("startLonDb", pathLon.get(pointer));
				startActivity(i);
				
			}
			else{
			getURL3(SignIn.add+"get_imId.php");  
			startActivity(new Intent(getApplicationContext(),PinpointView.class));
			//finish();
			}
			return true;
		}
		
	
	}
	 
	 //asynchronous task for getting the image_id (from the db) of an image and create a session of it
	private class GetURL3 extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();  
		private String Content;  
        private String Error = null;  
        protected void onPreExecute() {  
        
        }  

		protected Void doInBackground(String... urls) {
			try {
				Log.i("RESPONSE", "ok");
				//Log.i("stringLa", stringLat);
				httppost2 = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
				nameValuePairs.add(new BasicNameValuePair("latitude",stringLat));
				
				httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response2 = Client.execute(httppost2);
				Log.i("RESPONSE", "OK");
				
				
				entity2 = response2.getEntity();
				String inputs = EntityUtils.toString(entity2);
				Log.i("RESPONSE3", inputs);
				session.createImageIdSession(inputs);										
			
			
			
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
	
	
	public void getNearbyPath(String url) {  
		
		try {
			new GetNearbyPath().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	 } 
	
	//async task for getting the nearby paths from current position
	public class GetNearbyPath extends AsyncTask<String, Void, Void> {  
		
		
		
		private final HttpClient httpclient1 = new DefaultHttpClient();
		
		Location routeCoordLoc= new Location("");;
		float distance=0;
		int[] nearPathId=null;
		
		@Override
		public synchronized Void doInBackground(String... urls) {  
		try {
		httppost1 = new HttpPost(urls[0]);
		HttpGet httpget = new HttpGet(urls[0]);
		entity1 = httpclient1.execute(httpget).getEntity();
		String response = EntityUtils.toString(entity1);
		Log.i("respRoute", response);
		JSONObject json = new JSONObject(response.trim());

		JSONArray jArray = json.getJSONArray("posts");
		
		
		//getting the route table:path_id,coordinates
		for (int i = 0; i < jArray.length(); i++) {

			
			JSONObject e1 = jArray.getJSONObject(i);
			s = e1.getString("post");
			
			JSONObject jObject = new JSONObject(s);

			startLatDb = new int[jArray.length()];
			startLonDb = new int[jArray.length()];
			pathId = new int[jArray.length()];
			
			startLatDb[i] = jObject.getInt("start_lat");
			pathLat.add(startLatDb[i]);
			
			startLonDb[i] = jObject.getInt("start_lon");
			pathLon.add(startLonDb[i]);
		
			pathId[i] = jObject.getInt("path_id");

			

			Log.i("TABLE STARTLAT", Integer.toString(startLatDb[i]));

			
			routeCoordLoc.setLatitude(startLatDb[i]/1E6);
			routeCoordLoc.setLongitude(startLonDb[i]/1E6);
			Log.i("lat", Double.toString(curr_loc.getLatitude()));
			Log.i("lon",Double.toString(routeCoordLoc.getLatitude()));
			Log.i("dist2",Float.toString(curr_loc.distanceTo(routeCoordLoc)));
			distance=curr_loc.distanceTo(routeCoordLoc);
			int j=-1 ;
			
			if (distance<1000){
				Log.i("ok", "ok");
				//nearPathId[++j]=pathId[i];
				
				
				//creates an overlay item and an itemizedoverlay
				//item in order to create the pinpoint and show it on the map
				GeoPoint point = new GeoPoint(startLatDb[i], startLonDb[i]);
				Log.i("indist", Integer.toString(startLatDb[i]));
				OverlayItem overlayItem = new OverlayItem(point, "path", null);
				//checking the type of the pinpoint and assigns the corresponding image
			
					//creating an instance of ItemizedOverlay class CustomPinpoint
					CustomPinpoint itemizedOverlay = new CustomPinpoint(draf1,context);
					itemizedOverlay.insertPinpoint(overlayItem);
					overlayList.add(itemizedOverlay);
					mapV.getOverlays().add(itemizedOverlay);

					
				}
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
	public void onPostExecute(Void unused) {  
		
		
			mapV.invalidate();
        	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
        	

        	
        	
        	
    }
	
}
	
	

}
