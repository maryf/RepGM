package com.gmapssimple;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener {

	EditText username;
	EditText pass;
	EditText email;
	InputStream is;
	TextView text;
	String c, a, b, result;
	//HttpClient httpclient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	byte[] data;
	StringBuffer buffer;

	// public static int a = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		username = (EditText) findViewById(R.id.name);
		pass = (EditText) findViewById(R.id.passw);
		// email = (EditText)findViewById(R.id.emailname);
		text = (TextView) findViewById(R.id.text);
		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(this);
	}

	
	
	public void onClick(View v) {
		
		//runOnUiThread(new Runnable() {
		  //  public void run() {
		if (username.getText().length() == 0) {
			text.setText("Enter username");
		}

		else if (pass.getText().length() == 0) {
			text.setText("Enter password");
		} else {
			a = username.getText().toString().trim();
			b = pass.getText().toString().trim();
			asyncTask6(SignIn.add+"regist.php");  }
			finish();
		    //}
	//});
	}

	 public void asyncTask6(String url) {  
       new AsyncTask6().execute(url);  
   }  
	
	
	private class AsyncTask6 extends AsyncTask<String,Void,Void>{
		private final HttpClient httpclient = new DefaultHttpClient();  
		String getVal;
        private String Error = null;
		@Override
		protected Void doInBackground(String... urls) {
			try {
				//text.setText("try");
				
				// c = email.getText().toString().trim();
				
				httppost = new HttpPost(urls[0]);

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", a));
				nameValuePairs.add(new BasicNameValuePair("pass", b));
				// nameValuePairs.add(new BasicNameValuePair("email", c));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = httpclient.execute(httppost);

				entity = response.getEntity();
				getVal = EntityUtils.toString(entity);
		
			} catch (Exception e) {
				Error="1";
				Log.e("log_tag", "Error in http connection " + e.toString());
				
			}
			
			return null;
		
		  
	}
		
		
		protected void onPostExecute(Void unused) {  
        	
	            if (Error !="1" && getVal.equals("ok")) { 
	            	Toast.makeText(getApplicationContext(), "Registration Completed!",Toast.LENGTH_SHORT).show();
	            	startActivity(new Intent("com.gmapssimple.SIGNIN"));
					finish();
	            }
	            else{
	            	Toast.makeText(getApplicationContext(), "please try again with a different username or later",Toast.LENGTH_SHORT).show();
	            }
	           
	        }  
	

}
}
