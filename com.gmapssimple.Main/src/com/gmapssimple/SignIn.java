package com.gmapssimple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends Activity {
	EditText etUser, etPass;
	Button bLogin;
	String username1, password1,is1;
	HttpPost httppost;
	ArrayList<NameValuePair> nameValuePairs;
	HttpResponse response;
	HttpEntity entity;
	
	SharedPreferences pref;
	String retUser;
	String retPass;
	TextView t1;
	SessionManager session;
	
	private Object GetURL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);

		session = new SessionManager(getApplicationContext());

		t1 = (TextView) findViewById(R.id.textView1);

		initialise();
		
		

		bLogin = (Button) findViewById(R.id.bSubmit);

		bLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				username1 = etUser.getText().toString();
				password1 = etPass.getText().toString();
				getURL("http://10.0.2.2/login/index.php");  
				}
		});
		
	}
	
	 public void getURL(String url) {  
        new GetURL().execute(url);  
    }  
	/**session.getUserDetails();**/
	private void initialise() {
		// TODO Auto-generated method stub
		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPass);
		Button bregister = (Button) findViewById(R.id.reg);
		bregister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.gmapssimple.REGISTER"));
				finish();

			}
		});
	}
	
	private class GetURL extends AsyncTask<String, Void, Void> {  
        private final HttpClient Client = new DefaultHttpClient();  
        private String Content;  
        private String Error = null;  
        
          
        protected void onPreExecute() {  
             
        }  
  
        protected Void doInBackground(String... urls) {  
            try {  
               httppost = new HttpPost(urls[0]);  
                
				//t1.setText("ok1");

					nameValuePairs = new ArrayList<NameValuePair>();

					nameValuePairs.add(new BasicNameValuePair("username",
							username1));
					nameValuePairs
							.add(new BasicNameValuePair("pass", password1));

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					response = Client.execute(httppost);
					if (response.getStatusLine().getStatusCode() == 200) {
						//t1.setText("ok2");
						entity = response.getEntity();
						 is1 = EntityUtils.toString(entity);
						if (entity != null) {
							
							JSONObject jsonResponse = new JSONObject(is1);
							retUser = jsonResponse.getString("username");
							retPass = jsonResponse.getString("pass");
							//t1.setText(retUser);
							
							if (username1.equals(retUser)
									&& password1.equals(retPass)) {
								
								//t1.setText("prin_session");
								session.createLoginSession(retUser, retPass);
								startActivity(new Intent("com.gmapssimple.OPENMAP"));
								finish();

								//Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
									Error="1";
							} else {

								//Toast.makeText(getBaseContext(),"Invalid Login Details",Toast.LENGTH_SHORT).show();
							}

						}

					}

				
				
				//ResponseHandler<String> responseHandler = new BasicResponseHandler();  
                //Content = Client.execute(httpget, responseHandler);  
            } catch (ClientProtocolException e) {  
                Error = e.getMessage();  
                cancel(true);  
            } catch (IOException e) {  
                Error = e.getMessage();  
                cancel(true);  
            } catch (Exception e) {
				e.printStackTrace();
			}

              
            return null;  
        
          }
        protected void onPostExecute(Void unused) {  
        	
        	
			

           
            if (Error =="1" ) { 
            	Toast.makeText(getApplicationContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();
            }
        
        }  
    }   
    


	
}
