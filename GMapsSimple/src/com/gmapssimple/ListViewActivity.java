package com.gmapssimple;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListViewActivity extends Activity {

	ListView list;
	LazyAdapter adapter;
	ArrayList<HashMap<String, String>> mylist;
	String[] bit, typ, imid, username;
	String[] lat;
	String[] rate;
	Bitmap[] bit_array;
	int jarraylen;
	String getType, mary;
	ArrayList<Bitmap> bitmapArray;
	HashMap<String, String> map;
	String is;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		list = (ListView) findViewById(R.id.list);
		sliderAsTask1("http://83.212.123.114/getTopTen.php");
		// sliderAsTask1("http://192.168.1.60/login/getTopTen.php");

	}

	@Override
	public void onDestroy() {
		list.setAdapter(null);
		super.onDestroy();
	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			adapter.imageLoader.clearCache();
			adapter.notifyDataSetChanged();
		}
	};

	public void sliderAsTask1(String url) {

		try {
			new SliderAsTask1().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("error", "1");
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i("error", "2");
		}

	}

	public class SliderAsTask1 extends AsyncTask<String, Void, String> {

		String Error = null;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			dialog = ProgressDialog.show(ListViewActivity.this, "",
					"Loading Image");
			// dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		}

		@Override
		public synchronized String doInBackground(String... urls) {
			try {
				HttpClient httpclient1 = new DefaultHttpClient();
				HttpPost httppost1 = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
				nameValuePairs1.add(new BasicNameValuePair("type", getType));
				Log.i("ok1", "OK!");

				httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
				// HttpGet httpget = new HttpGet(urls[0]);
				// HttpEntity entity1 =
				// httpclient1.execute(httpget).getEntity();
				// String response = EntityUtils.toString(entity1);

				HttpResponse response1 = httpclient1.execute(httppost1);
				HttpEntity entity1 = response1.getEntity();

				is = EntityUtils.toString(entity1);

				// Log.i("ok2",is);
			} catch (ClientProtocolException e) {
				Log.i("errorrr", e.getMessage());
				Error = e.getMessage();
				cancel(true);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();

			}
			return is;
		}

		@Override
		public void onPostExecute(String bitArray) {
			super.onPostExecute(bitArray);
			dialog.dismiss();
			// Log.i("array", bitArray);

			try {
				JSONObject json = new JSONObject(bitArray.trim());

				// JSONObject jsonResponse = new JSONObject(is);

				JSONArray jArray = json.getJSONArray("posts");
				Log.i("ok3", bitArray);
				mylist = new ArrayList<HashMap<String, String>>();
				jarraylen = jArray.length();
				bit_array = new Bitmap[jArray.length()];
				typ = new String[jArray.length()];
				imid = new String[jArray.length()];
				bit = new String[jArray.length()];
				username = new String[jArray.length()];
				lat = new String[jArray.length()];
				rate = new String[jArray.length()];
				// getting the bitmap attribute of the images and assigning them
				// to a list
				for (int i = 0; i < jArray.length(); i++) {

					HashMap<String, String> map = new HashMap<String, String>();
					JSONObject e1 = jArray.getJSONObject(i);
					String s = e1.getString("post");

					JSONObject jObject = new JSONObject(s);

					// bit = new String[jArray.length()];

					map.put("bitmap", jObject.getString("bitmap"));
					bit[i] = map.get("bitmap");

					map.put("latitude", jObject.getString("latitude"));
					lat[i] = map.get("latitude");

					map.put("final_rating", jObject.getString("final_rating"));
					rate[i] = map.get("final_rating");
					// float rate1=Math.round(Float.parseFloat(rate[i]));
					float rate1 = Float.parseFloat(rate[i]);

					BigDecimal bd = new BigDecimal(Float.toString(rate1));
					bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
					bd.floatValue();
					rate[i] = String.valueOf(bd);

					map.put("type", jObject.getString("type"));
					typ[i] = map.get("type");

					map.put("image_id", jObject.getString("image_id"));
					imid[i] = map.get("image_id");

					map.put("username", jObject.getString("username"));
					username[i] = map.get("username");
					Log.i("username", username[i]);
					try {
						byte[] decodedString = Base64.decode(bit[i],
								Base64.DEFAULT);
						BitmapFactory.Options o = new BitmapFactory.Options();
						o.inJustDecodeBounds = true;
						BitmapFactory.decodeByteArray(decodedString, 0,
								decodedString.length, o);

						int scale = 1;
						if (o.outHeight > 10 || o.outWidth > 10) {
							scale = (int) Math.pow(2, (int) Math.round(Math
									.log(20 / (double) Math.max(o.outHeight,
											o.outWidth))
									/ Math.log(0.3)));
						}
						BitmapFactory.Options o2 = new BitmapFactory.Options();
						o2.inSampleSize = scale;

						bit_array[i] = BitmapFactory.decodeByteArray(
								decodedString, 0, decodedString.length, o2);
						adapter = new LazyAdapter(ListViewActivity.this,
								username, bit_array, typ, lat, rate,
								getApplicationContext());
						list.setAdapter(adapter);

						// Log.i("decoded", decodedString.toString());
					} catch (OutOfMemoryError e) {
						Log.e("EWN", "Out of memory error catched");

					}

				}
			} catch (JSONException e1) {
				Log.e("log_tag1", "Error parsing data " + e1.toString());
			}

			// Log.i("mary", "mary111"+username[1]);

			Button b = (Button) findViewById(R.id.button1);
			b.setOnClickListener(listener);

			//

			// Toast.makeText(getBaseContext(),
			// "SUCCESS!",Toast.LENGTH_SHORT).show();

		}

	}

}