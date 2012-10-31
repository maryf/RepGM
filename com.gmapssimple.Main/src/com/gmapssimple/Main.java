package com.gmapssimple;



import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Menu;



public class Main extends MapActivity  {
	
    //Button bInsert;
	MapController mControl;
	GeoPoint GeoP;
	MapView mapV;
	//MyLocationOverlay compass;
	MyLocationOverlay myLocation;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
   
        
       // Button bInsert = (Button) findViewById(R.id.insertPOI);
       // bInsert.setOnClickListener(new View.OnClickListener() {
			
			//public void onClick(View v) {
		//		// TODO Auto-generated method stub
		//		setContentView(R.layout.sqliteexample);
				
	//	}
	//	});
        
        
     
        
        //button gia Sign in
        Button bsignIn=(Button) findViewById(R.id.signIn);
        bsignIn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity (new Intent ("com.gmapssimple.SIGNIN"));
				
				
		}
		});

        
        //button upload image
        Button bUpImage=(Button) findViewById(R.id.UpImage);
        bUpImage.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity (new Intent ("com.gmapssimple.UPLOADIMAGE"));
				
				
		}
		});
        
        
        
        
        
        
        mapV= (MapView) findViewById(R.id.mapView);
        mControl=mapV.getController();
        mControl.setZoom(11);
        mapV.displayZoomControls(true);
        mapV.setBuiltInZoomControls(true);
        
        double lat=37.782687;
        double longi=20.897387;
        
        GeoP=new GeoPoint ((int) (lat*1E6), (int) (longi*1E6));
       
        
       mControl.animateTo(GeoP);
        
        
       // compass= new MyLocationOverlay(this, mapV); 
       // mapV.getOverlays().add(compass);
        
       // myLocation= new MyLocationOverlay(this, mapV);
       //mapV.getOverlays().add(myLocation);
       // myLocation.enableMyLocation();
        
       // myLocation.runOnFirstFix(new Runnable() {
       //     public void run() {
        //        mControl.animateTo(myLocation.getMyLocation());
      //      }
     //   });
        
       // DemoOverlay demoOverlay = new DemoOverlay();
      //  mapV.getOverlays().add(demoOverlay);
        
      
    }
    
    

   
    

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//compass.disableCompass();
		//myLocation.disableMyLocation();
	}

	@Override
	protected void onResume() {
		 //TODO Auto-generated method stub
		super.onResume();
		//compass.enableCompass();
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
	




}
