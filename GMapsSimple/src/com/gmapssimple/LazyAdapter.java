package com.gmapssimple;

import java.io.IOException;
import java.util.ArrayList;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private Bitmap[] data;
	private String[] user1, type1, lat1, rating1;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;
	Context ctx;
	SessionManager session;
	String latP;
	String lonP;

	public LazyAdapter(Activity a, String[] user, Bitmap[] d, String[] type,
			String[] lat, String[] rating, Context context) {
		activity = a;
		data = d;
		user1 = user;
		type1 = type;
		lat1 = lat;
		rating1 = rating;
		ctx = context;
		session = new SessionManager(ctx);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.item1, null);

		TextView text = (TextView) vi.findViewById(R.id.text);
		text.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(ctx, PinpointView.class);
				latP = lat1[position];
				// lonP=lon1[position];
				getURL3(SignIn.add + "get_imId.php");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("touchedLat", lat1[position]);
				i.putExtra("touchedLon", "0");
				ctx.startActivity(i);
			}

		});
		/*
		 * TextView text1=(TextView)vi.findViewById(R.id.text1);
		 * text1.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * Intent i = new Intent(ctx, PinpointView.class); latP=lat1[position];
		 * //lonP=lon1[position]; getURL3(SignIn.add+"get_imId.php");
		 * i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		 * i.putExtra("touchedLat",lat1[position] ); i.putExtra("touchedLon",
		 * "0"); ctx.startActivity(i); }
		 * 
		 * });
		 */

		TextView text2 = (TextView) vi.findViewById(R.id.text2);
		text2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(ctx, PinpointView.class);
				latP = lat1[position];
				// lonP=lon1[position];
				getURL3(SignIn.add + "get_imId.php");
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.putExtra("touchedLat", lat1[position]);
				i.putExtra("touchedLon", "0");
				ctx.startActivity(i);
			}

		});
		ImageView image = (ImageView) vi.findViewById(R.id.image);
		// Log.i("username", "mary"+user1[position]);
		// Log.i("type", "mary"+type1[position]);
		if (session.getLan()) {
			text.setText("Χρήστης: " + user1[position]);
			text2.setText("Βαθμολογία: " + rating1[position]);
		} else {
			text.setText("User: " + user1[position]);
			text2.setText("Rating: " + rating1[position]);
			// text1.setText("Type:" +type1[position]);}
		}
		// imageLoader.DisplayImage(url, imageView)(data[position], image);
		image.setImageBitmap(data[position]);
		return vi;
	}

	public void getURL3(String url) {

		try {
			new GetURL3().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class GetURL3 extends AsyncTask<String, Void, Void> {
		private final HttpClient Client = new DefaultHttpClient();
		private String Error = null;

		@Override
		protected Void doInBackground(String... urls) {
			try {
				Log.i("RESPONSE", "ok");
				// Log.i("stringLa", stringLat);
				HttpPost httppost = new HttpPost(urls[0]);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				Log.i("lat", latP);
				nameValuePairs.add(new BasicNameValuePair("latitude", latP));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = Client.execute(httppost);
				Log.i("RESPONSE", "OK");

				HttpEntity entity = response.getEntity();
				String inputs = EntityUtils.toString(entity);
				Log.i("RESPONSE3", inputs);
				session.createImageIdSession(inputs);

			} catch (ClientProtocolException e) {
				Error = e.getMessage();
				cancel(true);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();

			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			// if (Error ==null )
			// Toast.makeText(ctx, "OK!",Toast.LENGTH_SHORT).show();

		}

	}
}