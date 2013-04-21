package com.gmapssimple;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.android.maps.MyLocationOverlay;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class UploadImage extends Activity {
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath;
	InputStream is;
	String imageFilePath,imageid1;
	Button but, butt2,  butt3;
	TextView tv, tv2;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	ArrayList<NameValuePair> nameValuePairs;
	String ba1, name,value2;
	SessionManager session;
	
	Bitmap bitmapOrg;
	ByteArrayOutputStream bao;
	byte [] ba ;
	String value1,flag;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.upimage);
		session = new SessionManager(getApplicationContext());
		tv = (TextView) findViewById(R.id.tv);
		tv2 = (TextView) findViewById(R.id.tv2);
		// session.checkLogin();

		// button upload image
		butt2 = (Button) findViewById(R.id.bUpload2);
		butt2.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {

				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(Intent.createChooser(intent, "Message"),
						1);
			}
		});

		

	}
	
	
	public void asyncTask5(String url) {  
        new AsyncTask5().execute(url);  
    }  
	 
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);
				
				Toast.makeText(this.getApplicationContext(), selectedImagePath,
						Toast.LENGTH_SHORT).show();

			}
		}

			
		
		  	// decode in bitmap format the image that the user selects from his gallery
			  	bitmapOrg= BitmapFactory.decodeFile(selectedImagePath);
		     
			  	bao = new ByteArrayOutputStream();
			  	//compress the bitmap and put in in output stream bao, 80 specifies the quality parametre
			  	bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 70, bao);
			  	ba = bao.toByteArray();
			  	//converts byte array to string so it can be saved in the db
			  	ba1=Base64.encodeToString(ba, Base64.DEFAULT);
		  		 	
	 	
		
		
	 	 asyncTask5(SignIn.add+"base.php");
		
		
	        
	    
		
      
		

	}
	//asynchronous task for saving the image into the db
	private class AsyncTask5 extends AsyncTask<String,Void,Void>{
		private final HttpClient httpclient = new DefaultHttpClient();  
		@Override
		protected Void doInBackground(String... urls) {
			try {

				HashMap<String, String> user = session.getUserDetails();
				name = user.get(SessionManager.KEY_NAME);
				// tv2.setText(name);
				
				HashMap<String, String> image = session.getImageId();
				value1 = image.get(SessionManager.KEY_IMAGEID);
				Log.i("imi", value1);
				
				httppost = new HttpPost(urls[0]);
				//httppost = new HttpPost("http://192.168.1.60/login/base.php");

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("bitmap", ba1));
				nameValuePairs.add(new BasicNameValuePair("username", name));
				nameValuePairs.add(new BasicNameValuePair("image_id", value1));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
				entity = response.getEntity();
				//is = entity.getContent();
				flag = EntityUtils.toString(entity);
				Log.i("exists",flag);
				
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				//tv.setText("Connection error");
			}
			// TODO Auto-generated method stub
			return null;
		}
		protected void onPostExecute(Void unused){

			
			//checks if the user has already uploaded the specific image
			if (flag.equals("exists")){
				Toast.makeText(getApplicationContext(), 
						"You have already uploaded this image,please select another one",
						Toast.LENGTH_SHORT).show();
				
				
			}
			
			
			else{
			
					
					startActivity(new Intent(
							"com.gmapssimple.PINPOINTDETAILS"));
					finish();
				}
		
		}
		
	}
	
	//returns the string of the path on the sd card where the selected image is saved 
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };

		//Cursor cursor = managedQuery(uri, projection, null, null, "");
		Cursor cursor=getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {

			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	
	
}