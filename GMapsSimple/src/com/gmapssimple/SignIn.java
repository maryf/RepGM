package com.gmapssimple;

import java.io.IOException;
import java.util.ArrayList;

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
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignIn extends Activity {
	EditText etUser, etPass;
	Button bLogin;
	String username1, password1, is1;
	HttpPost httppost;
	ArrayList<NameValuePair> nameValuePairs;
	HttpResponse response;
	HttpEntity entity;

	SharedPreferences pref;
	String retUser;
	String retPass;
	TextView t1, t2, t3;
	SessionManager session;
	// static variable defining the address to whom the call should be made
	// public static String add ="http://192.168.1.60/login/";

	public static String add = "http:// /";

	// private Object GetURL;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);

		session = new SessionManager(getApplicationContext());

		t1 = (TextView) findViewById(R.id.textView1);
		t2 = (TextView) findViewById(R.id.textView2);
		t3 = (TextView) findViewById(R.id.textView3);
		
		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPass);
		
		Button bregister = (Button) findViewById(R.id.reg);
		
		
		bregister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SignIn.this, Register.class));
				finish();

			}
		});

		bLogin = (Button) findViewById(R.id.bSubmit);
		
			
		bLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				if (etUser.getText().length() == 0) {
					t3.setText("Please enter your username");
					if (session.getLan())
						t3.setText("Παρακαλώ εισάγετε το Username σας");
					t3.setTextColor(Color.BLUE);
				}

				else if (etPass.getText().length() == 0) {
					t3.setText("Please enter your password");
					if (session.getLan())
						t3.setText("Παρακαλώ εισάγετε τον Κωδικό Πρόσβασης");
					//text.setTextColor(Color.BLUE);
				} 
				else{
				username1 = etUser.getText().toString();
				password1 = etPass.getText().toString();
				getURL(add + "index.php");
				}
			}
		});
		
		
		if (session.getLan()){
			etUser.setHint("Όνομα Χρήστη");
			etPass.setHint("Κωδικός Πρόσβασης");
			bregister.setText("Εγγραφή");
			bLogin.setText("Σύνδεση");
			t2.setText("Δεν έχεις εγγραφεί ακόμα?");
		}
		else{
			etUser.setHint("Username");
			etPass.setHint("Password");
			bregister.setText("Register");
			bLogin.setText("Login");
			t2.setText("Not a member yet?");
		}
		

	}

	public void getURL(String url) {
		new GetURL().execute(url);
	}

	

	// asynchronous task that sends the user's name and the password to the db
	// for configuration
	private class GetURL extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();
		// private String Content;
		private String Error = null;

		protected void onPreExecute() {

		}

		protected Void doInBackground(String... urls) {
			try {
				httppost = new HttpPost(urls[0]);

				// t1.setText("ok1");

				nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs
						.add(new BasicNameValuePair("username", username1));
				nameValuePairs.add(new BasicNameValuePair("pass", password1));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				response = Client.execute(httppost);
				Log.i("status", Integer.toString(response.getStatusLine()
						.getStatusCode()));
				if (response.getStatusLine().getStatusCode() == 200) {
					// t1.setText("ok2");
					entity = response.getEntity();
					is1 = EntityUtils.toString(entity);
					Log.i("resp", is1);

					if (entity != null) {

						if (is1.equals("fail")) {
							Error = "0";
						} else if (is1.equals("success")) {
							session.createLoginSession(username1, password1);

						}

					}

				}

			} catch (ClientProtocolException e) {
				// Log.i("Error", Error);
				Error = e.getMessage();
				cancel(true);
			} catch (IOException e) {
				// Log.i("Error1", Error);
				e.getMessage();
				Error = "2";
				cancel(true);
			} catch (Exception e) {
				// Log.i("Error2", Error);
				Error = "0";
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(Void unused) {

			// Log.i("Error", Error);

			if (Error == null) {
				if (session.getLan()) {
					Toast.makeText(getApplicationContext(),
							"Συνδεθήκατε ως: " + username1, Toast.LENGTH_SHORT)
							.show();
					startActivity(new Intent(SignIn.this, OpenMap.class));
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"You have been logged in as: " + username1,
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(SignIn.this, OpenMap.class));
					finish();
				}
			} else if (Error == "0") {
				if (session.getLan())
					Toast.makeText(getBaseContext(),
							"Λάθος όνομα χρήστη ή κωδικός πρόσβασης!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getBaseContext(),
							"Wrong username or password!", Toast.LENGTH_SHORT)
							.show();

			} else if (Error == "2") {
				if (session.getLan())
					Toast.makeText(getBaseContext(),
							"Δεν έχετε συνδεθεί στο Internet!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(
							getBaseContext(),
							"No network connection found, please connect to Internet",
							Toast.LENGTH_SHORT).show();

			}

		}
	}

}
