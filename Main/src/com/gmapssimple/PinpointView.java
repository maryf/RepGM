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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;




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

		
		maryAsyncTask(SignIn.add+"decode_bitmap.php");
		
	}
	
	
	/*@Override
	public void onBackPressed() {
	 Intent backKey=new Intent(getApplicationContext(),OpenMap.class);
	 startActivity(backKey);
	 finish();
	}*/
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
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	           	return true;
            case R.id.rate2:
            	rate=(String) item.getTitle();
            	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	            //if (item.isChecked()) item.setChecked(false);
	            //else item.setChecked(true);
            	return true;
            case R.id.rate3:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	return true;
            case R.id.rate4:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	return true;
            case R.id.rate5:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	return true;
            case R.id.rate6:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	return true;
            case R.id.rate7:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	return true;
            case R.id.rate8:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	return true;
            case R.id.rate9:
	        	rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	return true;
            case R.id.rate10:
	        	String rate=(String) item.getTitle();
	        	maryAsyncTask1(SignIn.add+"set_rate.php",rate);
	        	
	        	return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	           
	    }
	}
	private class MaryAsyncTask extends AsyncTask<String,Void,String>{
		private final HttpClient Client = new DefaultHttpClient(); 
		String theString = null;
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute(){

			dialog=ProgressDialog.show(PinpointView.this,"","Loading Image");
			//dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			
		}
   		
		@Override
		protected String doInBackground(String... urls) {
			
	   		try{
	   			HttpParams httpParameters = new BasicHttpParams();
	   			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
	   			HttpConnectionParams.setSoTimeout(httpParameters, 10000);
   				Log.i("OK1111", "ok");	   	   
	   			httppost1 = new HttpPost(urls[0]);
	   			
	   			//httppost = new HttpPost("http://192.168.1.60/login/decode_bitmap.php");
	   			
	   			nameValuePairs1 = new ArrayList<NameValuePair>();
	   			nameValuePairs1.add(new BasicNameValuePair("image_id",valuefs));
	   			
	   			
	   			
	   			httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
	   			response1 = Client.execute(httppost1);
	   			entity1 = response1.getEntity();
	   			
				String is = EntityUtils.toString(entity1);
	   			
	   			
				
				Log.i("response",is);
	   			
	   			JSONObject jsonResponse = new JSONObject(is);
	   			
	   			
	   			frat = jsonResponse.getString("final_rating");// 
	   			link = jsonResponse.getString("wiki_link");
	   			theString = jsonResponse.getString("bitmap");
	   			typ = jsonResponse.getString("type");
	   			erar = jsonResponse.getString("era");
	   			

					
	   					
	   		is=null;
	   		}catch (Exception e2) {
				Log.e("log_rating",
						"Error in http connection " + e2.toString());
				
			}
			
			bitmap3=theString;
			
			return bitmap3;
		}
		
		
		@Override
		protected void onPostExecute(String bitm){

			ImageView imageView = (ImageView) findViewById(R.id.imageView1);
			try{
			byte[] decodedString = Base64.decode(bitm, Base64.DEFAULT);
			Log.i("SIZE", Integer.toString(decodedString.length));
			
			//scale for avoiding out of memory exception
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,o);
			
			
			int scale = 1;
	        if (o.outHeight > 1000 || o.outWidth > 1000) {
	            scale = (int)Math.pow(2, (int) Math.round(Math.log(1000 / 
	               (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
	        }
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	       
	        bitmap4=BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length,o2);

			 imageView.setImageBitmap(bitmap4);
			 bitmap4=null;
			}catch(OutOfMemoryError e){
				 Log.e("EWN", "Out of memory error catched");
	        
			}
			
			 TextView tv1 = (TextView) findViewById(R.id.textType);
			 TextView tv2 = (TextView) findViewById(R.id.textEra);
			 TextView tv3 = (TextView) findViewById(R.id.textRatings);
			 TextView tv4 = (TextView) findViewById(R.id.textLink);
			 tv1.setText("Category : " +typ );
			 tv2.setText("Era : " +erar );
			 tv3.setText("Description/Link : " + link );
			 tv4.setText("Ratings : " +frat );
			 //create clickable link
			 Linkify.addLinks(tv3, Linkify.ALL);
			 dialog.dismiss();
			
		}
		
	}
	
	
	
	private class MaryAsynctask1 extends AsyncTask <String,Void,Void>{
		private final HttpClient httpClient = new DefaultHttpClient();  
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
				String is = EntityUtils.toString(entity);
				Log.i("response", is);
				//InputStream instream = entity.getContent();
				
				
			}
		}

	} catch (Exception e1) {
		Log.e("log_rating",
				"Error in http connection " + e1.toString());
		Error="1";
	}
	return null;	
	
}
	
	@Override
	protected void onPostExecute(Void unused){
		if (Error==null)
        	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();

	}
	}
	
	

	

}
