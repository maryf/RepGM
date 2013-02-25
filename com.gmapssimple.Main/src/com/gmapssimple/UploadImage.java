package com.gmapssimple;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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



import android.app.Activity;
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
//import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.gmapssimple.Base64;

public class UploadImage extends Activity {
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath;
	// private String filemanagerstring;
	// private ImageView imageView1;
	InputStream is;
	String imageFilePath,imageid1;
	Button but, butt2, butt3;
	TextView tv, tv2;
	//HttpClient httpclient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	ArrayList<NameValuePair> nameValuePairs;
	String ba1, name,value2;
	SessionManager session;

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

		// metatropi eikonas se bitmap
		
	      Bitmap bitmapOrg= BitmapFactory.decodeFile(selectedImagePath);
	      // tv2.setText("Environment.getExternalStorageDirectory().getPath() ") ;
	 	  ByteArrayOutputStream bao = new ByteArrayOutputStream();
	 	  bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
	 	  byte [] ba = bao.toByteArray();
	 	   ba1=Base64.encodeBytes(ba);
	 	 // tv.setText(ba1);
		
		
		
	 	 asyncTask5("http://10.0.2.2/login/base.php");   
		

	}
	private class AsyncTask5 extends AsyncTask<String,Void,Void>{
		private final HttpClient httpclient = new DefaultHttpClient();  
		@Override
		protected Void doInBackground(String... urls) {
			try {

				HashMap<String, String> user = session.getUserDetails();
				name = user.get(SessionManager.KEY_NAME);
				// tv2.setText(name);
				
				
				httppost = new HttpPost(urls[0]);
				//httppost = new HttpPost("http://192.168.1.60/login/base.php");

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("bitmap", ba1));
				nameValuePairs.add(new BasicNameValuePair("username", name));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
				entity = response.getEntity();
				//is = entity.getContent();
				imageid1 = EntityUtils.toString(entity);
				
				
				// tv2.setText("Step 2, pick the place on the map to pin the picture");
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				//tv.setText("Connection error");
			}
			// TODO Auto-generated method stub
			return null;
		}
		protected void onPostExecute(Void unused){

			
			//Toast.makeText(getApplicationContext(),imageid1,Toast.LENGTH_SHORT).show();
			if (imageid1.equals("exists")){
				Toast.makeText(getApplicationContext(), 
						"You have already uploaded this image,please select another one",
						Toast.LENGTH_SHORT).show();
				
				
			} else{
			
			session.createImageIdSession(imageid1);
			
			startActivity(new Intent(
					"com.gmapssimple.CREATEPIN"));
			finish();
			}
		}
		
	}
	

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