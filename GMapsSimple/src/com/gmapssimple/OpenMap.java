package com.gmapssimple;

import java.io.IOException;
import java.io.InputStream;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
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

public class OpenMap extends SherlockFragmentActivity {
	MapView mapV;

	ArrayList<HashMap<String, String>> mylist, mylist1;
	InputStream is;
	
	String d, value, value1;
	String[] lat, lon, bit, typ, imid;
	int[] startLonDb;
	int[] startLatDb;
	int[] intLat, intLon;
	String stringLat;
	int touchedLat, touchedLon;

	MapController mControl;
	MyLocationOverlay myLocation;
	TextView tv, tvnew;
	long start;
	long stop;
	int x;
	int y;
	int jarraylen;
	List<Overlay> overlayList;
	GeoPoint touchedPoint;
	boolean enabled2 = false;
	Context context;
	CharSequence text;
	int duration, one;
	SessionManager session;
	String s = null;
	boolean flag = false;
	String[] period = new String[] { "archaic", "classical", "hellenistic",
			"roman", "byzantine", "ottoman", "modern" };
	String[] type_dialog = new String[] { "museum", "monument", "statue",
			"archeological_site", "old_΄picture" };
	String[] dist = new String[] { "1 Km", "5 Km", "10 Km" };
	int selectedItem = 0;
	int[] pathId;
	MyLocationOverlay myLocation1;
	Location curr_loc;
	int[] routeLatDb;
	int[] routeLonDb;
	List<Integer> pathLat = new ArrayList<Integer>();
	
	List<Integer> Lat2 = new ArrayList<Integer>();
	List<Integer> Lon2= new ArrayList<Integer>();
	List<String> type= new ArrayList<String>();
	JSONArray jArray ;
	List<Integer> pathLon = new ArrayList<Integer>();
	private GoogleMap map2;
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//ActionBar actionbar = getActionBar();
		//actionbar.show();
		map2 = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		
		
		
		LatLng geop1=new LatLng(37.786029, 20.898077);
		
		 CameraUpdate center=
			        CameraUpdateFactory.newLatLng(geop1);

		 CameraUpdate zoom=CameraUpdateFactory.zoomTo(11);

			map2.moveCamera(center);
			map2.animateCamera(zoom);
		
	
		// creating an instance of class SessionManager
		session = new SessionManager(getApplicationContext());

		final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			// notify user you are online
			// Toast.makeText(getApplicationContext(),
			// "Online",Toast.LENGTH_SHORT).show();

		} else {
			// notify user you are not online
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			if (session.getLan()) {
				alertDialogBuilder.setTitle("Δεν υπάρχει σύνδεση στο Internet");
				alertDialogBuilder
						.setMessage("Θα θέλατε να το ενεργοποιήσετε τώρα")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent intent = new Intent(
												Settings.ACTION_WIRELESS_SETTINGS);
										startActivity(intent);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing

										dialog.cancel();

										Toast.makeText(
												getApplicationContext(),
												"Δεν μπορείς να λάβεις δεδομένα χωρίς σύνδεση στο Internet",
												Toast.LENGTH_SHORT).show();

									}
								});
			} else {
				alertDialogBuilder.setTitle("Internet connection is disabled");
				alertDialogBuilder
						.setMessage("Would you like to enable it now?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent intent = new Intent(
												Settings.ACTION_WIRELESS_SETTINGS);
										startActivity(intent);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, just close
										// the dialog box and do nothing

										dialog.cancel();

										Toast.makeText(
												getApplicationContext(),
												"Sorry you cannot receive any data without internet connection",
												Toast.LENGTH_SHORT).show();

									}
								});
			}
			// set dialog message

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();

		}

		getURL1(SignIn.add + "selpic.php"); 
		
		map2.setOnMarkerClickListener(new OnMarkerClickListener()
        {
			
            public boolean onMarkerClick(Marker marker_touched) {
            	Log.i("touch", "touch");
            	LatLng katiii=marker_touched.getPosition();
            	double lat_t=katiii.latitude;
            	double lon_t=katiii.longitude;
            	stringLat = Double.toString(lat_t*1E6);
            	
            	int lat_pT=(int) (lat_t*1E6);
            	if (marker_touched.getTitle().equals("path")){
            		Log.i("touchp", "touchp");
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
    			}else
    			{
    				Log.i("touch", "touch");
                	getURL3(SignIn.add + "get_imId.php");
    				Intent i = new Intent(getApplicationContext(),
    						PinpointView.class);
    				i.putExtra("touchedLat", lat_t);
    				i.putExtra("touchedLon", lon_t);
    				startActivity(i);
    			}
    				
          
                return true;
            }

        });

	}

	// constructors for AsyncTasks
	public void getURL1(String url) {

		
			try {
				new GetURL1().execute(url).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 

	}

	public void getMyPaths(String url) {

		new GetMyPaths().execute(url);

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

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.mapmenu, menu);
		// supportInvalidateOptionsMenu();

		// item upload image
		// MenuItem item = menu.add(Menu.NONE, R.id.upload, Menu.NONE, "mary");

		MenuItem pitem = menu.findItem(R.id.myPaths);
		if (session.getLan())
			pitem.setTitle("Τα μονοπάτια μου");
		else
			pitem.setTitle("My Paths");

		MenuItem sitem = menu.findItem(R.id.showPaths);
		if (session.getLan())
			sitem.setTitle("Κοντινά μονοπάτια");
		else
			sitem.setTitle("Find nearby Paths");

		MenuItem slitem = menu.findItem(R.id.sliderIm);
		if (session.getLan())
			slitem.setTitle("Παρουσίαση Εικόνων μιας Χρονολογικής Περιόδου");
		else
			slitem.setTitle("Slider");

		MenuItem mitem = menu.findItem(R.id.myPoi);
		if (session.getLan())
			mitem.setTitle("Αγαπημένα");
		else
			mitem.setTitle("My POI");

		MenuItem titem = menu.findItem(R.id.top10);
		if (session.getLan())
			titem.setTitle("10 Κορυφαίες Εικόνες");
		else
			titem.setTitle("Top 10 Images");

		MenuItem litem = menu.findItem(R.id.logout);
		if (session.getLan())
			litem.setTitle("Αποσύδεση");
		else
			litem.setTitle("Logout");

		TextView tUpload = (TextView) menu.findItem(R.id.upload)
				.getActionView();
		if (session.getLan())
			tUpload.setText("Εικόνα");
		else
			tUpload.setText("Upload");
		Drawable up = getResources().getDrawable(R.drawable.upimage);
		tUpload.setCompoundDrawablesWithIntrinsicBounds(null, up, null, null);
		tUpload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (session.isLoggedIn()) {
					// if the user is logged in takes him to UploadImage.class
					Intent intent = new Intent(getApplicationContext(),
							CreatePin.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					// else the SignIn.class opens so the user can log in
				} else {
					if (session.getLan()) {
						Toast.makeText(getApplicationContext(),
								"Πρέπει να συνδεθείς στο λογαριασμό σου",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(getApplicationContext(),
								SignIn.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(getApplicationContext(),
								"You have to log in first", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(getApplicationContext(),
								SignIn.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}
				}

			}
		});

		TextView tPos = (TextView) menu.findItem(R.id.curPos).getActionView();
		if (session.getLan())
			tPos.setText("Η θέση μου");
		else
			tPos.setText("MyPosition");
		Drawable pos = getResources().getDrawable(R.drawable.mylocation);
		tPos.setCompoundDrawablesWithIntrinsicBounds(null, pos, null, null);
		tPos.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*myLocation = new MyLocationOverlay(getApplicationContext(),
						mapV);
				mapV.getOverlays().add(myLocation);

				// Attempts to enable MyLocation, registering for updates from
				// LocationManager.GPS_PROVIDER and
				// LocationManager.NETWORK_PROVIDER.
				myLocation.enableMyLocation();

				myLocation.runOnFirstFix(new Runnable() {
					public void run() {
						mControl.animateTo(myLocation.getMyLocation());
					}
				});*/
				map2.setMyLocationEnabled(true);
		        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		        // Creating a criteria object to retrieve provider
		        Criteria criteria = new Criteria();

		        // Getting the name of the best provider
		        String provider = locationManager.getBestProvider(criteria, true);
		        
		    
	 
	            // Getting Current Location
	            Location location = locationManager.getLastKnownLocation(provider);
	 
	            //if(location!=null){
	             //   onLocationChanged(location);
	            //}

	           //int lat_start = (int) (location.getLatitude()*1E6);
				//int lon_start = (int)(location.getLongitude()*1E6);
				
				LatLng curL=new LatLng(location.getLatitude(),location.getLongitude());
				
				map2.moveCamera(CameraUpdateFactory.newLatLng(curL));
			}
		});

		TextView tPath = (TextView) menu.findItem(R.id.create_path)
				.getActionView();
		if (session.getLan())
			tPath.setText("Μονοπάτι");
		else
			tPath.setText("Path");
		Drawable dPath = getResources().getDrawable(R.drawable.path);
		tPath.setCompoundDrawablesWithIntrinsicBounds(null, dPath, null, null);
		tPath.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(getApplicationContext(),
						ShowMap.class);
				intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent2);

			}
		});

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.showPaths:

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (session.getLan())
				builder.setTitle("Επέλεξε την απόσταση");
			else
				builder.setTitle("Select the distance");

			int selected = selectedItem;
			builder.setSingleChoiceItems(dist, selected,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							selectedItem = which;
							// Toast.makeText(OpenMap.this,"You have selected "+dist[selectedItem],Toast.LENGTH_SHORT).show();
							// if (selectedItem!=0){
							Intent i = new Intent(OpenMap.this, NearbyP.class);
							i.putExtra("dist", dist[selectedItem]);
							startActivity(i);

							// }
							dialog.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

			// startActivity(new Intent(getApplicationContext(),NearbyP.class));
			break;

		case R.id.sliderIm:

			Dialog();
			break;

		case R.id.myPaths:

			getMyPaths(SignIn.add + "get_mypaths.php");
			break;

		case R.id.myPoi:

			startActivity(new Intent(getApplicationContext(), MyPOI.class));
			break;

		case R.id.top10:

			Dialog1();
			break;

		// log out user
		case R.id.logout:

			session.logoutUser();
			Intent intent1 = new Intent(this, SignIn.class);
			intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent1);
			break;

		// case R.id.menu_preferences:
		// Toast.makeText(OpenMap.this, "Preferences is Selected",
		// Toast.LENGTH_SHORT).show();
		// return true;

		default:
			break;
		}
		return true;
	}

	// creates dialog with the available chronological periods and sends the
	// period to SlideActivity.java
	void Dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (session.getLan())
			builder.setTitle("Επέλεξε μια χρονολογική περίοδο");
		else
			builder.setTitle("Select a time period");

		int selected = selectedItem;
		builder.setSingleChoiceItems(period, selected,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						selectedItem = which;
						// Toast.makeText(OpenMap.this,"You have selected Period "+period[selectedItem],Toast.LENGTH_SHORT).show();
						// if (selectedItem!=0){
						Intent i = new Intent(OpenMap.this, SlideActivity.class);
						i.putExtra("period", period[selectedItem]);
						startActivity(i);
						// }
						dialog.dismiss();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	void Dialog1() {
		AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(this);
		

		// set title
		if (session.getLan()){
			alertDialogBuild.setTitle("Επέλεξε τρόπο προβολής");
			alertDialogBuild
			.setCancelable(false)
			.setPositiveButton("Στο Χάρτη",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							startActivity(new Intent(
									getApplicationContext(),
									TopTenPOI.class));
							finish();
						}
					})
			.setNegativeButton("Λίστα",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							// if this button is clicked, just close
							// the dialog box and do nothing
							Intent i = new Intent(OpenMap.this,
									ListViewActivity.class);
							// i.putExtra("type",type_dialog[selectedItem]
							// );
							startActivity(i);
							dialog.cancel();
							// Toast.makeText(getApplicationContext(),
							// "Sorry you cannot receive any data without internet connection",Toast.LENGTH_SHORT).show();

						}
					});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuild.create();

			// show it
			alertDialog.show();	
		}
		else{
			//Toast.makeText(getApplicationContext(),
					//"ok!",
					//Toast.LENGTH_SHORT).show();

			alertDialogBuild.setTitle("Select View");

		// set dialog message
		alertDialogBuild
				.setCancelable(false)
				.setPositiveButton("Show on Map",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								startActivity(new Intent(
										getApplicationContext(),
										TopTenPOI.class));
								finish();
							}
						})
				.setNegativeButton("Show a list",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								Intent i = new Intent(OpenMap.this,
										ListViewActivity.class);
								// i.putExtra("type",type_dialog[selectedItem]
								// );
								startActivity(i);
								dialog.cancel();
								// Toast.makeText(getApplicationContext(),
								// "Sorry you cannot receive any data without internet connection",Toast.LENGTH_SHORT).show();

							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuild.create();

		// show it
		alertDialog.show();
		}
		

	}

	// asynchronous task making a network call(in a new independent thread) so
	// all the pinpoints can be shown on the map
	public class GetURL1 extends AsyncTask<String, Void, int[]> {

		private final HttpClient httpclient = new DefaultHttpClient();
		private String Error = null;
		JSONArray jArray;

		// String url = "http://192.168.1.60/login/selpic.php";

		// synchronizing the method for avoiding concurrent modifications
		// exception caused by the the simultaneous changes made in Arraylist
		@Override
		public synchronized int[] doInBackground(String... urls) {
			try {
				// HttpPost httppost = new HttpPost(urls[0]);
				HttpGet httpget = new HttpGet(urls[0]);
				HttpEntity entity = httpclient.execute(httpget).getEntity();
				String response = EntityUtils.toString(entity);
				Log.i("response1", response);
				JSONObject json = new JSONObject(response.trim());

				jArray = json.getJSONArray("posts");
				mylist = new ArrayList<HashMap<String, String>>();
				jarraylen = jArray.length();
				// getting the attributes of all images:coordinates,image
				// id,type and assigns them to an arraylist
				for (int i = 0; i < jArray.length(); i++) {

					JSONObject e1 = jArray.getJSONObject(i);
					s = e1.getString("post");

					JSONObject jObject = new JSONObject(s);

					lat = new String[jArray.length()];
					lon = new String[jArray.length()];
					// bit = new String[jArray.length()];
					typ = new String[jArray.length()];
					
					imid = new String[jArray.length()];

					lat[i] = jObject.getString("latitude");

					lon[i] = jObject.getString("longitude");
					
					typ[i] = jObject.getString("type");
					type.add(typ[i]);

					imid[i] = jObject.getString("image_id");


					//Log.i("TABLE LAT", typ[i]);

					intLat = new int[jArray.length()];

					intLat[i] = Integer.parseInt(lat[i]);
					Lat2.add(intLat[i]);
					intLon = new int[jArray.length()];
					intLon[i] = Integer.parseInt(lon[i]);
					Lon2.add(intLon[i]);
					
					
					

				}

			} catch (ClientProtocolException e) {
				Error = e.getMessage();
				cancel(true);
			} catch (JSONException e1) {
				Log.e("log_tag", "Error parsing data " + e1.toString());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
				Error = "1";

			}
			return intLat;
		}

		public void onPostExecute(int[] result) {
			

			if (Error == null) {
				for (int i=0; i<jArray.length(); i++){
					int maryLat=Lat2.get(i);
					int maryLon=Lon2.get(i);
					String type1=type.get(i);
					LatLng point= new LatLng(maryLat/1E6, maryLon/1E6);
					//Log.i("mary",Double.toString(intLat[i]/1E6));
					
					
					
					// checking the type of the pinpoint and assigns the
					// corresponding image
					if (type1.equals("museum")) {
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(point)
				        .title("smt")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.art_museum_2)));
						
					}

					else if (type1.equals("monument")) {
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(point)
				        .title("smt")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.memorial)));
					}

					

						else if (type1.equals("palace")) {
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(point)
				        .title("smt")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.palace_2)));

					}

						else if (type1.equals("statue")) {
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(point)
				        .title("smt")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.statue_2)));

					}

						else if (type1.equals("archeological_site")) {
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(point)
				        .title("smt")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.archeological_site)));

					}

						else if (type1.equals("church")) {
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(point)
				        .title("smt")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.chapel_2)));


					}

						else if (type1.equals("old_pic")) {
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(point)
				        .title("smt")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.historicalquarter)));


					}
						else {
							Log.i("else if", type1);
							
						}
					 


				
				}
				
				
			}
			

		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// compass.disableCompass();
		// myLocation.disableMyLocation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// compass.enableCompass();
		// myLocation.enableMyLocation();
	}

	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay {

		public boolean onTouchEvent(MotionEvent e, MapView m) {
			// when the user touches the screen the coordinates of the touched
			// point are calculated
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

			// Toast.makeText(c, "Overlay Item " + pinpoints.get(index) +
			// " tapped!", Toast.LENGTH_LONG).show();

			// get the index of the touched pinpoint as well as his coordinates
			OverlayItem item = pinpoints.get(index);
			GeoPoint touchedGeoP = item.getPoint();
			String title = item.getTitle();

			touchedLat = touchedGeoP.getLatitudeE6();
			touchedLon = touchedGeoP.getLongitudeE6();

			stringLat = Integer.toString(touchedLat);
			Log.i("title", title + stringLat);

			// checks if the user taps the image which is used to show the start
			// of a path
			if (title == "path") {
				// getPathId
				int pointer = 0;
				for (int i = 0; i < pathLat.size(); i++) {
					if (pathLat.get(i) == touchedLat) {
						pointer = i;
						break;
					}
				}
				// the Direction.java opens to show the selected path
				Intent i = new Intent(getApplicationContext(), Direction.class);
				Log.i("pointer", Integer.toString(pointer));
				Log.i("latprin", Integer.toString(pathLat.get(pointer)));
				i.putExtra("startLatDb", pathLat.get(pointer));
				i.putExtra("startLonDb", pathLon.get(pointer));
				startActivity(i);

			} else {
				getURL3(SignIn.add + "get_imId.php");
				Intent i = new Intent(getApplicationContext(),
						PinpointView.class);
				i.putExtra("touchedLat", touchedLat);
				i.putExtra("touchedLon", touchedLon);
				startActivity(i);
				// finish();
			}
			return true;
		}

	}

	// async task for getting the paths of a user
	public class GetMyPaths extends AsyncTask<String, Void, Void> {

		private final HttpClient httpclient = new DefaultHttpClient();
		String Error = null;

		@Override
		public synchronized Void doInBackground(String... urls) {
			try {
				HttpPost httppost = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				HashMap<String, String> user = session.getUserDetails();
				String username = user.get(SessionManager.KEY_NAME);
				nameValuePairs
						.add(new BasicNameValuePair("username", username));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				Log.i("RESPONSE", "OK");

				HttpEntity entity = response.getEntity();

				String respons = EntityUtils.toString(entity);
				Log.i("respMyPaths", respons);
				JSONObject json = new JSONObject(respons.trim());

				jArray = json.getJSONArray("posts");
				if (jArray.isNull(0)) {
					Error = "2";

				} else {

					// getting the starting coordinates of the paths
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

						Log.i("TABLE STARTLAT", Integer.toString(startLatDb[i]));

						
					}
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
				
				for (int i = 0; i < jArray.length(); i++) {
					LatLng pointS= new LatLng(pathLat.get(i)/1E6, pathLon.get(i)/1E6);
					//Log.i("mary",Double.toString(intLat[i]/1E6));
					
					
					
					
						// creating an instance of ItemizedOverlay class
						// CustomPinpoint
						map2.addMarker(new MarkerOptions()
				        .position(pointS)
				        .title("path")
				        .icon(BitmapDescriptorFactory
				            .fromResource(R.drawable.hiking)));
					
				}
				
				
				
				
			} else if (Error == "2") {
				if (session.getLan())
					Toast.makeText(getApplicationContext(),
							"Δεν έχεις δημιουργήσει ακόμα κάποιο μονοπάτι!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(),
							"You haven' t created a path yet!",
							Toast.LENGTH_SHORT).show();

			}

		}

	}

	// asynchronous task for getting the image_id (from the db) of an image and
	// create a session of it
	private class GetURL3 extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();
		//private String Error = null;

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

}
