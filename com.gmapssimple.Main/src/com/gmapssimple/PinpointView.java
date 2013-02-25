package com.gmapssimple;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;
import org.apache.commons.io.IOUtils;




public class PinpointView extends Activity {
    Button brating;
    String rate,rate_db;;
    //HttpClient httpclient;
	HttpPost httppost,httppost1;
	HttpResponse response,response1;
	HttpEntity entity,entity1;
	List<NameValuePair> nameValuePairs,nameValuePairs1;
	ArrayList<HashMap<String, String>> mylist;
	String erar,link,bit, typ,imid,frat;
	String valuefs;
	String bitmap3;
	String s = null;
	SessionManager session;
	Bitmap bitmap4;
	int jarraylen;
	HashMap<String, String> map1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pipnpointview);
		//TextView tvsimple = (TextView) findViewById(R.id.simple);
		//tvsimple.setText("simple descr");
		session = new SessionManager(getApplicationContext());
		
		
		
		//Bundle extras = getIntent().getExtras();
		//value = extras.getString("image_id");
		//tvsimple.setText(value);
		
		
		brating= (Button ) findViewById(R.id.rating);
		registerForContextMenu(brating); 
		brating.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				openContextMenu(v);
			    // TODO Auto-generated method stub
				}
		});
		
		
		HashMap<String, String> imageId = session.getImageId();
		 valuefs = imageId.get(SessionManager.KEY_IMAGEID);
		 //tvsimple.setText(valuefs);
		Log.i("bitmapMary0", "[" + valuefs+ "]");

		
		maryAsyncTask("http://10.0.2.2/login/decode_bitmap.php");
		
	}
	public void maryAsyncTask(String url) {  
        new MaryAsyncTask().execute(url);  
    }  
	public void maryAsyncTask1(String url, String rate2) {  
        new MaryAsynctask1().execute(url,rate2);  
    }  
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	}
	
	@Override
	public  boolean onContextItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.rate1:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	           	return true;
            case R.id.rate2:
            	rate=(String) item.getTitle();
            	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	            //if (item.isChecked()) item.setChecked(false);
	            //else item.setChecked(true);
            	return true;
            case R.id.rate3:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	return true;
            case R.id.rate4:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	return true;
            case R.id.rate5:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	return true;
            case R.id.rate6:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	return true;
            case R.id.rate7:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	return true;
            case R.id.rate8:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	return true;
            case R.id.rate9:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	return true;
            case R.id.rate10:
	        	String rate=(String) item.getTitle();
	        	maryAsyncTask1("http://10.0.2.2/login/set_rate.php",rate);
	        	
	        	return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	           
	    }
	}
	private class MaryAsyncTask extends AsyncTask<String,Void,String>{
		private final HttpClient Client = new DefaultHttpClient();  
        private String Content;  
        private String Error = null;  
        //String abc = null;
   	 	String theString = null;
   		//Bitmap bitmap = null;
		@Override
		protected String doInBackground(String... urls) {
			
	   		try{
   				Log.i("OK1111", "ok");	   	   
	   			httppost1 = new HttpPost(urls[0]);
	   			
	   			//httppost = new HttpPost("http://192.168.1.60/login/decode_bitmap.php");
	   			
	   			nameValuePairs1 = new ArrayList<NameValuePair>();
	   			nameValuePairs1.add(new BasicNameValuePair("image_id",valuefs));
	   			
	   			
	   			
	   			httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
	   			response1 = Client.execute(httppost1);
	   			entity1 = response1.getEntity();
	   			//HttpGet httpget = new HttpGet(urls[0]);
				// get the response entity
				//entity1 = Client.execute(httpget).getEntity();
				String is = EntityUtils.toString(entity1);
	   			
	   			//response1 = Client.execute(httppost1); 
	   			
	   			
	   			JSONObject jsonResponse = new JSONObject(is);
	   			
	   			
	   			frat = jsonResponse.getString("final_rating");// 
	   			link = jsonResponse.getString("wiki_link");
	   			theString = jsonResponse.getString("bitmap");
	   			typ = jsonResponse.getString("type");
	   			erar = jsonResponse.getString("era");
	   			Log.i("response",response.toString());

				
				
	   			
	   			
				/*Log.i("OK4", "ok");	 
	   			if (response1.getStatusLine().getStatusCode() == 200) {
	   				Log.i("OK5", "ok");	 
	   				//Log.i("bitmapMary", "[" + response + "]");
	   				entity1 = response1.getEntity();
	   				//BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
	   				if (entity1 != null) {
	   					Log.i("OK6", "ok");	 
	   					//Log.i("ratingmary", "[" + response + "]"); 
	   					
	   					InputStream is = entity1.getContent();
	   					
	   				
	   					
	   					}*/
	   					
	   					
	   					
	   					
	   					//Log.i("ratingmary",  "is");
	   					//StringWriter writer = new StringWriter(88480);
	   					//IOUtils.copy(is, writer);
	   					//theString = writer.toString();
	   					//Log.i("ratingmary", theString); 
	   					
	   					
	   			
	   		}catch (Exception e2) {
				Log.e("log_rating",
						"Error in http connection " + e2.toString());
				
			}
			
			bitmap3=theString;
			//Log.i("theString",theString);
			return bitmap3;
		}
		protected void onPostExecute(String bitm){

			ImageView imageView = (ImageView) findViewById(R.id.imageView1);
			byte[] decodedString = Base64.decode(bitm, Base64.DEFAULT);
			bitmap4=BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			 imageView.setImageBitmap(bitmap4);
			 
			 TextView tv1 = (TextView) findViewById(R.id.textType);
			 TextView tv2 = (TextView) findViewById(R.id.textEra);
			 TextView tv3 = (TextView) findViewById(R.id.textRatings);
			 TextView tv4 = (TextView) findViewById(R.id.textLink);
			 tv1.setText("Category:" +typ );
			 tv2.setText("Era:" +erar );
			 tv3.setText("Description/Link:" + link );
			 tv4.setText("Ratings:" +frat );
			 Linkify.addLinks(tv3, Linkify.ALL);
			
		}
		
	}
	private class MaryAsynctask1 extends AsyncTask <String,Void,Void>{
		private final HttpClient httpClient = new DefaultHttpClient();  
        private String Content;  
        private String Error = null;  
	String ab = null;
	
	@Override
	protected Void doInBackground(String... urls) {
		// TODO Auto-generated method stub
		
	try{
		Log.i("OK1", "ok");
    	String rating=urls[1];
    	Log.i("OK1", rating);
		httppost = new HttpPost(urls[0]);
		//httppost = new HttpPost("http://192.168.1.60/login/set_rate.php");
		Log.i("OK2", "ok");
		nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("image_id", valuefs));
		nameValuePairs.add(new BasicNameValuePair("rating1", rating));
		Log.i("OK3", "ok");
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		response = httpClient.execute(httppost); 
		Log.i("OK4", "ok");
		if (response.getStatusLine().getStatusCode() == 200) {
			Log.i("OK5", "ok");
			
			entity = response.getEntity();
			if (entity != null) {
				Log.i("OK6", "ok");
			
				InputStream instream = entity.getContent();
				
				//ab=convertStreamToString(instream);
				//Toast.makeText(PinpointView.this, rating + " is Selected current rate", Toast.LENGTH_SHORT).show();
				
			}
		}

	} catch (Exception e1) {
		Log.e("log_rating",
				"Error in http connection " + e1.toString());
		
	}
	return null;	
	
}
	}
	
	

	private static byte[] convertStreamToString(InputStream is) {
		
		
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] tmp = new byte[4096];
		int ret = 0;

		try {
			while((ret = is.read(tmp,0,tmp.length)) > 0)
			{
			    bos.write(tmp, 0, ret);
			   
			}
		
			  
			    int pos = 0;
			    do {
			       pos += is.read(tmp, pos, tmp.length-pos);
			    } while (pos < tmp.length);
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		byte[] myArray = bos.toByteArray();
		

	
	return myArray;
}

}
