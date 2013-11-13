package com.gmapssimple;

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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class PinpointDetails extends Activity {
	TextView tv, tv2, tv3;
	EditText etWiki;
	String wikiLink, value1, tEra, typ;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	SessionManager session;
	List<NameValuePair> nameValuePairs;
	Button description;
	RadioGroup r1, r2;
	RadioButton rb1, rb2, rb3, rb4, rb5, rb6, rb7, rb8, rb9, rb10, rb11, rb12,
			rb13, rb14;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pinpointdetails);
		tv = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		etWiki = (EditText) findViewById(R.id.editText11);
		r1 = (RadioGroup) findViewById(R.id.era);
		r2 = (RadioGroup) findViewById(R.id.type);

		rb1 = (RadioButton) findViewById(R.id.arc);
		rb2 = (RadioButton) findViewById(R.id.clas);
		rb3 = (RadioButton) findViewById(R.id.hellenist);
		rb4 = (RadioButton) findViewById(R.id.rom);
		rb5 = (RadioButton) findViewById(R.id.byz);
		rb6 = (RadioButton) findViewById(R.id.ottom);
		rb7 = (RadioButton) findViewById(R.id.mod);
		rb8 = (RadioButton) findViewById(R.id.radio0);
		rb9 = (RadioButton) findViewById(R.id.radio1);
		rb10 = (RadioButton) findViewById(R.id.radio2);
		rb11 = (RadioButton) findViewById(R.id.radio3);
		rb12 = (RadioButton) findViewById(R.id.radio4);
		rb13 = (RadioButton) findViewById(R.id.radio5);
		rb14 = (RadioButton) findViewById(R.id.radio6);

		session = new SessionManager(getApplicationContext());
		if (session.getLan()) {
			tv.setText("Πρόσθεσε ένα σύνδεσμο ή μια περιγραφή σχετική με τη εικόνα");
			tv2.setText("Επέλεξε το είδος του μνημείου");
			tv3.setText("Επέλεξε τη χρονολογική περίοδο στην οποία ανήκει η εικόνα");
			rb1.setText("Αρχαϊκή (700-480 ΠΧ)");
			rb2.setText("Κλασσική (480-323 ΠΧ)");
			rb3.setText("Ελληνιστική (323-146 ΠΧ)");
			rb4.setText("Ρωμαϊκή (146 ΠΧ-4ος αι.)");
			rb5.setText("Βυζαντινή (4ος αι.-1453)");
			rb6.setText("Ενετική, γαλλική και αγγλική κατοχή (15ος αι.-1864)");
			rb7.setText("Σύγχρονη");
			rb8.setText("Μουσείο");
			rb9.setText("’γαλμα");
			rb10.setText("Παλάτι-Κάστρο");
			rb11.setText("Μνημείο");
			rb12.setText("Αρχαιολογικός Χώρος");
			rb13.setText("Εκκλησία");
			rb14.setText("Ιστορική Εικόνα");

		}

		// tv.setText(value1);

		description = (Button) findViewById(R.id.des);
		description.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				wikiLink = etWiki.getText().toString();
				// put inside asyntask
				if (wikiLink == "") {
					if (session.getLan())
						Toast.makeText(getApplicationContext(),
								"Η εισαγωγή περιγραφής είναι υποχρεωτική.",
								Toast.LENGTH_LONG).show();
					else
						Toast.makeText(getApplicationContext(),
								"You have to enter a link", Toast.LENGTH_LONG)
								.show();
				} else {
					asyncTask7(SignIn.add + "insert_wikiLink.php");
					boolean pathMode = session.getPath();
					if (pathMode) {
						startActivity(new Intent(PinpointDetails.this,
								ShowMap.class));
						finish();
					} else {
						startActivity(new Intent(PinpointDetails.this,
								OpenMap.class));
						finish();
					}
				}
			}
		});

		r2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.radio0:
					// Toast t =
					// Toast.makeText(getBaseContext(),"1",Toast.LENGTH_LONG);
					// t.show();
					typ = "museum";
					break;
				case R.id.radio1:
					typ = "statue";
					// t = Toast.makeText(getBaseContext(),"2" ,
					// Toast.LENGTH_LONG);
					// t.show();

					break;
				case R.id.radio2:
					typ = "palace";
					// t = Toast.makeText(getBaseContext(),"3" ,
					// Toast.LENGTH_LONG);
					// t.show();

					break;
				case R.id.radio3:
					typ = "monument";
					// t = Toast.makeText(getBaseContext(),"4" ,
					// Toast.LENGTH_LONG);
					// t.show();

					break;

				case R.id.radio4:
					typ = "archeological_site";
					// t = Toast.makeText(getBaseContext(),"3" ,
					// Toast.LENGTH_LONG);
					// t.show();

					break;

				case R.id.radio5:
					typ = "church";
					// t = Toast.makeText(getBaseContext(),"3" ,
					// Toast.LENGTH_LONG);
					// t.show();

					break;

				case R.id.radio6:
					typ = "old_pic";
					// t = Toast.makeText(getBaseContext(),"3" ,
					// Toast.LENGTH_LONG);
					// t.show();

					break;

				}

			}
		});

		r1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {

				case R.id.arc:
					tEra = "archaic";
					// Toast t =
					// Toast.makeText(getBaseContext(),tEra,Toast.LENGTH_LONG);
					// t.show();
					break;

				case R.id.clas:
					tEra = "classical";
					// t =
					// Toast.makeText(getBaseContext(),tEra,Toast.LENGTH_LONG);
					// t.show();
					break;

				case R.id.hellenist:
					tEra = "hellenistic";
					// t =
					// Toast.makeText(getBaseContext(),tEra,Toast.LENGTH_LONG);
					// t.show();
					break;

				case R.id.rom:
					tEra = "roman";
					// Toast.makeText(getApplicationContext(),
					// "Added to your favorite images!",Toast.LENGTH_SHORT).show();
					// t.show();
					break;

				case R.id.byz:
					tEra = "byzantine";
					// Toast.makeText(getApplicationContext(),
					// "Added to your favorite images!",Toast.LENGTH_SHORT).show();
					// t.show();
					break;

				case R.id.ottom:
					tEra = "ottoman";
					// Toast.makeText(getApplicationContext(),
					// "Added to your favorite images!",Toast.LENGTH_SHORT).show();
					// t.show();
					break;

				case R.id.mod:
					tEra = "modern";
					// t = Toast.makeText(getBaseContext(),tEra
					// ,Toast.LENGTH_LONG);
					// t.show();
					break;

				}

			}
		});

	}

	public void asyncTask7(String url) {
		new AsyncTask7().execute(url);
	}

	private class AsyncTask7 extends AsyncTask<String, Void, Void> {
		private final HttpClient httpclient = new DefaultHttpClient();
		String Error = null;

		@Override
		protected Void doInBackground(String... urls) {
			// Log.e("OK1", wikiLink);
			try {
				HashMap<String, String> image = session.getImageId();
				value1 = image.get(SessionManager.KEY_IMAGEID);

				httppost = new HttpPost(urls[0]);
				// httppost = new
				// HttpPost("http://192.168.1.60/login/geoP.php");
				Log.i("era", tEra);
				// if (wikiLink==null){ Toast.makeText(getApplicationContext(),
				// "you have to enter a link",
				// Toast.LENGTH_LONG).show();}

				nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs
						.add(new BasicNameValuePair("wiki_link", wikiLink));
				nameValuePairs.add(new BasicNameValuePair("image_id", value1));
				nameValuePairs.add(new BasicNameValuePair("era", tEra));
				nameValuePairs.add(new BasicNameValuePair("type", typ));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				// Toast.makeText(getApplicationContext(), wikiLink,
				// Toast.LENGTH_LONG).show();
				response = httpclient.execute(httppost);
				entity = response.getEntity();
				// is = entity.getContent();

				// httpclient.getConnectionManager().shutdown();
			} catch (Exception e1) {
				Log.e("log_tag", "Error in http connection " + e1.toString());
				Error = "1";
			}

			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			// if (Error==null)
			// Toast.makeText(getApplicationContext(),
			// wikiLink+value1,Toast.LENGTH_SHORT).show();
		}

	}

}
