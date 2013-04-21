package com.gmapssimple;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

public class ShowMap extends MapActivity {

	// Button bInsert;
	MapView mapV;
	MapController mControl;
	GeoPoint GeoP;
	// MyLocationOverlay compass;
	MyLocationOverlay myLocation;
	TextView tv;
	long start;
	long stop;
	int x;
	int y;
	GeoPoint touchedPoint;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Button bInsert = (Button) findViewById(R.id.insertPOI);
		// bInsert.setOnClickListener(new View.OnClickListener() {

		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// setContentView(R.layout.sqliteexample);

		// }
		// });

		// button upload image
		Button bUpImage = (Button) findViewById(R.id.UpImage);
		bUpImage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.gmapssimple.UPLOADIMAGE"));

			}
		});

		mapV = (MapView) findViewById(R.id.mapView);
		mControl = mapV.getController();
		mControl.setZoom(11);
		mapV.displayZoomControls(true);
		mapV.setBuiltInZoomControls(true);
		Touchy t = new Touchy();
		List<Overlay> overlayList = mapV.getOverlays();
		overlayList.add(t);

		// ****COMPASS***
		// compass= new MyLocationOverlay(this, mapV);
		// mapV.getOverlays().add(compass);

		// ****USERS CURRENT LOCATION****
		myLocation = new MyLocationOverlay(this, mapV);
		mapV.getOverlays().add(myLocation);
		myLocation.enableMyLocation();

		myLocation.runOnFirstFix(new Runnable() {
			public void run() {
				mControl.animateTo(myLocation.getMyLocation());
			}
		});

		// DemoOverlay demoOverlay = new DemoOverlay();
		// mapV.getOverlays().add(demoOverlay);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// compass.disableCompass();
		myLocation.disableMyLocation();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// compass.enableCompass();
		//myLocation.enableMyLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = mapV.getProjection().fromPixels(x, y);

			}

			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}

			if (stop - start > 1500) {
				Geocoder geocoder = new Geocoder(getBaseContext(),
						Locale.getDefault());
				try {
					List<Address> address = geocoder.getFromLocation(
							touchedPoint.getLatitudeE6() / 1E6,
							touchedPoint.getLongitudeE6() / 1E6, 1);
					if (address.size() > 0) {
						String display = "";
						for (int i = 0; i < address.get(0)
								.getMaxAddressLineIndex(); i++) {

							display += address.get(0).getAddressLine(i) + "\n";

						}
						Toast t = Toast.makeText(getBaseContext(), display,
								Toast.LENGTH_LONG);
						t.show();

					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {

				}
			}

			return false;
		}

	}

}
