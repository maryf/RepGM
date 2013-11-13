package com.gmapssimple;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.facebook.Session;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

	EditText username, pass, email, conPass;

	InputStream is;
	TextView text;
	String a, b, c, d, result;
	// HttpClient httpclient;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	List<NameValuePair> nameValuePairs;
	byte[] data;
	StringBuffer buffer;

	SessionManager session;

	// public static int a = 0;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		session = new SessionManager(getApplicationContext());

		username = (EditText) findViewById(R.id.name);
		pass = (EditText) findViewById(R.id.passw);
		email = (EditText) findViewById(R.id.email);
		conPass = (EditText) findViewById(R.id.conPass);
		text = (TextView) findViewById(R.id.text);
		Button register = (Button) findViewById(R.id.register);

		if (session.getLan()) {
			username.setHint("Όνομα χρήστη");
			pass.setHint("Κωδικός πρόσβασης");
			conPass.setHint("Επιβεβαίωση κωδικού πρόσβασης");
			register.setText("Εγγραφή");
		}
		
		
		register.setOnClickListener(this);
	}

	/*
	 * public static boolean isEmailValid(String email) { boolean isValid =
	 * false;
	 * 
	 * String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	 * CharSequence inputStr = email;
	 * 
	 * Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	 * Matcher matcher = pattern.matcher(inputStr); if (matcher.matches()) {
	 * isValid = true; } return isValid; }
	 */

	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public void onClick(View v) {

		// runOnUiThread(new Runnable() {
		// public void run() {
		a = username.getText().toString().trim();
		b = pass.getText().toString().trim();
		c = email.getText().toString().trim();
		d = conPass.getText().toString().trim();
		if (username.getText().length() == 0) {
			text.setText("Please enter a username");
			if (session.getLan())
				text.setText("Παρακαλώ συμπληρώστε το πεδίο: Όνομα χρήστη");
			text.setTextColor(Color.BLUE);
		}

		else if (pass.getText().length() == 0) {
			text.setText("Please enter a password");
			if (session.getLan())
				text.setText("Παρακαλώ συμπληρώστε το πεδίο:"
						+ " Κωδικός Πρόσβασης");
			//text.setTextColor(Color.BLUE);
		} else if (email.getText().length() == 0) {
			text.setText("Please enter an email address");
			if (session.getLan())
				text.setText("Παρακαλώ συμπληρώστε το πεδίο: email");
			//text.setTextColor(Color.BLUE);
		} else if (conPass.getText().length() == 0) {
			text.setText("Please re-enter the password");
			if (session.getLan())
				text.setText("Παρακαλώ επιβεβαιώστε τον κωδικό πρόσβασης");
			//text.setTextColor(Color.BLUE);
		}else if (!isEmailValid(c)) {
			Toast.makeText(getApplicationContext(),
					"Invalid email address", Toast.LENGTH_SHORT).show();
			if (session.getLan())
				Toast.makeText(getApplicationContext(),
						"Μη αποδεκτή διέυθυνση email", Toast.LENGTH_SHORT).show();
		}  
		
		else {
			
			if (b.equals(d))
				asyncTask6(SignIn.add + "regist.php");
			else {
				if (session.getLan())
					Toast.makeText(
							getApplicationContext(),
							"Οι κωδικοί πρόσβασης που έχετε εισαγάγει δεν ταυτίζονται!",
							Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(),
							"Passwords missmatch!", Toast.LENGTH_LONG).show();
				text.setText("Please reenter a password");
				if (session.getLan())
					text.setText("Παρακαλώ συμπληρώστε ξανά το πεδίο:"
							+ " Κωδικός Πρόσβασης");

			}
		}
		// finish();

	}

	public void asyncTask6(String url) {
		new AsyncTask6().execute(url);
	}

	private class AsyncTask6 extends AsyncTask<String, Void, Void> {
		private final HttpClient httpclient = new DefaultHttpClient();
		String getVal;
		private String Error = null;

		@Override
		protected Void doInBackground(String... urls) {
			try {
				// text.setText("try");

				// c = email.getText().toString().trim();

				httppost = new HttpPost(urls[0]);

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("username", a));
				nameValuePairs.add(new BasicNameValuePair("pass", b));
				nameValuePairs.add(new BasicNameValuePair("email", c));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				
			        
				response = httpclient.execute(httppost);

				entity = response.getEntity();
				getVal = EntityUtils.toString(entity);
				Log.i("response", getVal);
				if (getVal.equals("already_exists"));
				Error = "3";

			} catch (Exception e) {
				Error = "1";
				Log.e("log_tag", "Error in http connection " + e.toString());

			}

			return null;

		}

		protected void onPostExecute(Void unused) {

			if (getVal.equals("ok")) {
				if (session.getLan()) {
					Toast.makeText(getApplicationContext(),
							"Η εγγραφή σου ολοκληρώθηκε επιτυχώς!",
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(Register.this, SignIn.class));
					finish();
				} else {
					Toast.makeText(getApplicationContext(),
							"Registration Completed!", Toast.LENGTH_SHORT)
							.show();
					startActivity(new Intent(Register.this, SignIn.class));
					finish();
				}
			} else if (Error == "1")
				if (session.getLan())
					Toast.makeText(getBaseContext(),
							"Δεν έχετε συνδεθεί στο Internet!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(
							getBaseContext(),
							"No network connection found, please connect to Internet",
							Toast.LENGTH_SHORT).show();

			else if (Error == "3")
				if (session.getLan())
					Toast.makeText(
							getBaseContext(),
							"Αυτό το όνομα χρήστη χρησιμοποιείται ήδη, παρακαλώ επιλέξτε κάποιο άλλο!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(
							getBaseContext(),
							"This username already exists, please select another one!",
							Toast.LENGTH_SHORT).show();

		}

	}
}
