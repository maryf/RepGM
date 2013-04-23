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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


public class MyPOI extends MapActivity {
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	MapView mapV;
	MapController mControl;
	SessionManager session;
	ArrayList<HashMap<String, String>> mylist;
	List<GeoPoint> path = new ArrayList<GeoPoint>();

	int jarraylen;
	HashMap<String, String> map;
	String[] lat,lon,typ;
	Context context;
	int[] intLat;
	List<Overlay> overlayList;
	Drawable museum,monument,statue,palace,archeological_site,church;
	String stringLat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mytrek);
		mapV = (MapView) findViewById(R.id.mapView2);
		
		context = getApplicationContext();
		
		mControl = mapV.getController();
		mControl.setZoom(11);
		mapV.displayZoomControls(true);
		mapV.setBuiltInZoomControls(true);
		
		overlayList = mapV.getOverlays();
		//overlayList.add(t);
		
		museum= getResources().getDrawable(R.drawable.art_museum_2);
		monument= getResources().getDrawable(R.drawable.memorial);
		statue=getResources().getDrawable(R.drawable.statue_2);
		palace= getResources().getDrawable(R.drawable.palace_2);
		archeological_site= getResources().getDrawable(R.drawable.archeological_site);
		church= getResources().getDrawable(R.drawable.chapel_2);
		
		session = new SessionManager(getApplicationContext());
		//asyncTaskTrek(SignIn.add+"mytrek.php");
		
		getMyPoi(SignIn.add+"get_mypoi.php");

		
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


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	
	//ItemizedOverlay class for creating and adding pinpoints on the map an for handling onTap events
		 public class CustomPinpoint extends ItemizedOverlay<OverlayItem> {
			private ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
			public Context c,mContext;
			//Path path;
			
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
				
				
				
				getURL3(SignIn.add+"get_imId.php");  
				startActivity(new Intent(getApplicationContext(),PinpointView.class));
				//finish();
				
				return true;
			}
			
		
		}
		 
		 

		 //asynchronous task for getting the image_id (from the db) of an image and create a session of it
		private class GetURL3 extends AsyncTask<String, Void, Void> {
			private final HttpClient Client = new DefaultHttpClient(); 
	        private String Error = null;  
	         
	        @Override
			protected Void doInBackground(String... urls) {
				try {
					Log.i("RESPONSE", "ok");
					//Log.i("stringLa", stringLat);
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
	        }  catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
	          
		} 

	          
	        return null;  
			}  
	        
	        @Override
			protected void onPostExecute(Void unused) {    
				if (Error ==null ) 
	            	Toast.makeText(getApplicationContext(), "OK!",Toast.LENGTH_SHORT).show();
	            
		    }  
			
		}
	
	
	//async task for getting tha favorite POI of the user
			public class GetMyPoi extends AsyncTask<String, Void, Void> {  
					
					private final HttpClient httpclient = new DefaultHttpClient();
					String Error=null;
					
					
					@Override
					public synchronized Void doInBackground(String... urls) {  
					try {
					httppost = new HttpPost(urls[0]);
					HashMap<String, String> user = session.getUserDetails();
					String name = user.get(SessionManager.KEY_NAME);
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					
					nameValuePairs.add(new BasicNameValuePair("username",name));
					
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					response = httpclient.execute(httppost);
					Log.i("RESPONSE", "OK");
					
					
					HttpEntity entity = response.getEntity();
					String inputs = EntityUtils.toString(entity);
					//Log.i("respmyPOI", inputs);
					
					JSONObject json = new JSONObject(inputs.trim());
					

					JSONArray jArray = json.getJSONArray("posts");
					if (jArray.isNull(0)){
						Error="2";
		   	        	
					}
					else{
					
					mylist = new ArrayList<HashMap<String, String>>();
					jarraylen=jArray.length();
					//getting the attributes of all images:coordinates,type and assigns them to an arraylist
					for (int i = 0; i < jArray.length(); i++) {

						map = new HashMap<String, String>();
						JSONObject e1 = jArray.getJSONObject(i);
						String s = e1.getString("post");
						
						JSONObject jObject = new JSONObject(s);

						lat = new String[jArray.length()];
						lon = new String[jArray.length()];
						//bit = new String[jArray.length()];
						typ = new String[jArray.length()];
						//imid= new String[jArray.length()];
						
						map.put("latitude", jObject.getString("latitude"));
						lat[i] = map.get("latitude");
					
						map.put("longitude", jObject.getString("longitude"));
						lon[i] = map.get("longitude");

						//map.put("bitmap", jObject.getString("bitmap"));
						//bit[i] = map.get("bitmap");
						
						map.put("type", jObject.getString("type"));
						typ[i] = map.get("type");
						
						//map.put("image_id", jObject.getString("image_id"));
						//imid[i] = map.get("image_id");

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
					}
					
					
					
					} catch (ClientProtocolException e) {  
			         
			            cancel(true); 
			            Error="1";
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
			        } catch (IOException e2) {
			        	// TODO Auto-generated catch block
			        	e2.printStackTrace();
			        	Error="1";
			          
				}
				return null;  
				}
					
				@Override	
				public void onPostExecute(Void unused) {  
					
					
						if (Error ==null ) 
			        	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
						else if (Error=="2"){
				        	Toast.makeText(getApplicationContext(), "You haven no favorite POI",Toast.LENGTH_SHORT).show();

							
						}

			        	
			        	
			        	
			    }
				
			}


}
