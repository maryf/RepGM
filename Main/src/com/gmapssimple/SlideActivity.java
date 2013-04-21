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

	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.slider);
	        
	        Intent intent = getIntent();
	        getPeriod = intent.getStringExtra("period");
	        Log.i("messag", getPeriod);
	        
	        getURL1(SignIn.add+"slide_bitmaps.php");
	        
	        
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
	    
	    
	    public void getURL1(String url) {  
			
			try {
				new GetURL1().execute(url).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.i("interrupted","interrupte");
				e.printStackTrace();
			} catch (ExecutionException e) {
				Log.i("execution","execution");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		 } 
	    
	    
	    
//async task class for getting the images (which belong to a specific time era) from the database    
public class GetURL1 extends AsyncTask<String, Void, String[]> {  
		
			
			
			private final HttpClient httpclient1 = new DefaultHttpClient();
			private String Content;  
			private String Error = null; 
			//String url = "http://192.168.1.60/login/selpic.php";
			protected void onPreExecute() {  
			}
			
			public String[] doInBackground(String... urls) {  
			try {
			HttpPost httppost1 = new HttpPost(urls[0]);
			List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
   			nameValuePairs1.add(new BasicNameValuePair("era",getPeriod));
   			Log.i("ok1", "OK!");
   			
   			
   			httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
			//HttpGet httpget = new HttpGet(urls[0]);
			//HttpEntity entity1 = httpclient1.execute(httpget).getEntity();
			//String response = EntityUtils.toString(entity1);
			
   			
   			HttpResponse response1 = httpclient1.execute(httppost1);
   			HttpEntity entity1 = response1.getEntity();
   		
			String is = EntityUtils.toString(entity1);
   			
   			Log.i("ok2",is);
			//tv.setText("ok!");
   			
   			//HttpResponse response = httpclient1.execute(httppost1);
   			//HttpEntity entity1 = response.getEntity();
   			//HttpGet httpget = new HttpGet(urls[0]);
			// get the response entity
			//entity1 = Client.execute(httpget).getEntity();
			//String is = EntityUtils.toString(entity1);
			JSONObject json = new JSONObject(is.trim());
			
			//JSONObject jsonResponse = new JSONObject(is);

			JSONArray jArray = json.getJSONArray("posts");
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
				
				bit_array[i]=BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
				//bitmapArray = new ArrayList<Bitmap>();
				//bitmapArray.add(bit_array); // Add a bitmap
				
				
				//map.put("type", jObject.getString("type"));
				//typ[i] = map.get("type");
				
				//map.put("image_id", jObject.getString("image_id"));
				//imid[i] = map.get("image_id");
				//Log.i("Toast", bit_array[i].toString());
				
				//mylist.add(map);

				
				
				
			
			}
			
			
			
			} catch (ClientProtocolException e) { 
				Log.i("errorrr",e.getMessage());
                Error = e.getMessage();  
                cancel(true);  
            } catch (JSONException e1) {
			Log.e("log_tag1", "Error parsing data " + e1.toString());
			} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
              
		}
			return bit;
		}
		public void onPostExecute(String[] bitArray) { 
			
			Log.i("array", bit_array.toString());
			
        	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();

				
        }
		
}
	 

}
