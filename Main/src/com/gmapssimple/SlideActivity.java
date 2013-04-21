package com.gmapssimple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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



//import com.techipost.imageslider.R;

//import com.gmapssimple.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class SlideActivity extends Activity {
	public int currentimageindex=0;
	Timer timer;
	TimerTask task;
	ImageView slidingimage;
	ArrayList<HashMap<String, String>> mylist;
	String[] bit, typ,imid;
	Bitmap[] bit_array;
	int jarraylen;
	String getPeriod;
	ArrayList<Bitmap> bitmapArray ;
	String Error = null;

	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.slider);
	        
	        Intent intent = getIntent();
	        getPeriod = intent.getStringExtra("period");
	        Log.i("messag", getPeriod);
	        
	        sliderAsTask(SignIn.add+"slide_bitmaps.php");
	        if (Error==null){
	        
	        final Handler mHandler = new Handler();

	        // Create runnable for posting
	        final Runnable mUpdateResults = new Runnable() {
	            public void run() {
	            	
	            	AnimateandSlideShow();
	            	
	            }
	        };
			
	        // delay for 1 sec.
	        int delay = 1000; 
	        
	        // repeat every 3 sec.
	        int period = 3000; 

	        Timer timer = new Timer();

	        timer.scheduleAtFixedRate(new TimerTask() {

	        public void run() {

	        	 mHandler.post(mUpdateResults);

	        }

	        }, delay, period);
	        
			 
	        } 
	        else Toast.makeText(getApplicationContext(), "there are no images in this time period",Toast.LENGTH_SHORT).show();

	    }
	
	
	  public void onClick(View v) {
		    
	        finish();
	        android.os.Process.killProcess(android.os.Process.myPid());
	      }
	    

	    //helper method to start the animation
	     
	    private void AnimateandSlideShow() {
	    	
	    	
	    	slidingimage = (ImageView)findViewById(R.id.ImageView3_Left);
	    	//Log.i("bitarray", Integer.toString(bit_array.length));
	   		slidingimage.setImageBitmap(bit_array[currentimageindex%bit_array.length]);
	   		
	   		currentimageindex++;
	    	
	   		Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.custom_anim);
	    	  
	        
	    	  slidingimage.startAnimation(rotateimage);
	          
	          	 
	        
	    }
	    
	    
	    public void sliderAsTask(String url) {  
			
			
				try {
					new SliderAsTask().execute(url).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		
		 } 
	    
	    
	    
//async task class for getting the images (that belong to a specific time era) from the database    
public class SliderAsTask extends AsyncTask<String, Void, Bitmap[]> {  
		
			
			
			private final HttpClient httpclient1 = new DefaultHttpClient();  
			//String url = "http://192.168.1.60/login/selpic.php";
			//ProgressDialog dialog;
			
			@Override
			protected void onPreExecute(){

				//dialog=ProgressDialog.show(SlideActivity.this,"","Loading Images");
				//dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				
			}
			
			@Override
			public Bitmap[] doInBackground(String... urls) {  
			try {
			HttpPost httppost1 = new HttpPost(urls[0]);
			List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
   			nameValuePairs1.add(new BasicNameValuePair("era",getPeriod));
   			Log.i("ok1", "OK!");
   			
   			
   			httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));;
			
   			
   			HttpResponse response1 = httpclient1.execute(httppost1);
   			HttpEntity entity1 = response1.getEntity();
   		
			String is = EntityUtils.toString(entity1);
			Log.i("strinngresp", is);
			
   			
		
			JSONObject json = new JSONObject(is.trim());
			
			
			//JSONObject jsonResponse = new JSONObject(is);

			JSONArray jArray = json.getJSONArray("posts");
			Log.i("JSON", Boolean.toString(jArray.isNull(0)));
			if (jArray.isNull(0)){
				Error="1";
   	        	
			}
			else{
			Log.i("ok3", "OK!");
			mylist = new ArrayList<HashMap<String, String>>();
			jarraylen=jArray.length();
			bit_array = new Bitmap[jArray.length()];
			//getting the bitmap attribute of the images and assigning them to a list
			for (int i = 0; i < jArray.length(); i++) {

				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject e1 = jArray.getJSONObject(i);
				
				String s = e1.getString("post");
				
				JSONObject jObject = new JSONObject(s);

				
				//bit = new String[jArray.length()];
				typ = new String[jArray.length()];
				imid= new String[jArray.length()];
				bit = new String[jArray.length()];
				
				map.put("bitmap", jObject.getString("bitmap"));
				bit[i] = map.get("bitmap");
				byte[] decodedString = Base64.decode(bit[i], Base64.DEFAULT);
				Log.i("decoded", decodedString.toString());
				
				//BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
				//scale for avoiding out of memory exception
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,o);
				
				
				int scale = 1;
		        if (o.outHeight > 10 || o.outWidth > 10) {
		            scale = (int)Math.pow(2, (int) Math.round(Math.log(500 / 
		               (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
		        }
		        BitmapFactory.Options o2 = new BitmapFactory.Options();
		        o2.inSampleSize = scale;
		       
				bit_array[i]=BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,o2);
				}
			}
			
			
			
			
			} catch (ClientProtocolException e) { 
				Log.i("errorrr",e.getMessage());
                Error = e.getMessage();  
                cancel(true);  
            } catch (JSONException e1) {
			Log.e("log_tag1", "Error parsing data " + e1.toString());
			Error="1";
			} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			Error="1";
              
		}
			return bit_array;
		}
		public void onPostExecute(String[] bitArray) { 
			
			//Log.i("array", bitArray.toString());
			if (Error==null)
        	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
			//dialog.dismiss();
				
        }
		
}
	 

}
