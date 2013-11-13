package com.gmapssimple;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UploadImage extends Activity {
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath;
	InputStream is;
	String imageFilePath, imageid1;
	Button butt2;
	TextView tv, tv2;
	HttpPost httppost;
	HttpResponse response;
	HttpEntity entity;
	ArrayList<NameValuePair> nameValuePairs;
	String ba1, name, value2;
	SessionManager session;

	Bitmap bitmapOrg;
	ByteArrayOutputStream bao;
	byte[] ba;
	String value1, flag;
	Button butt1;
	private static final String TAG = UploadImage.class.getSimpleName();
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;

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

		butt1 = (Button) findViewById(R.id.button1);
		if (session.getLan()) {
			butt2.setText("Gallery");
			butt1.setText("Κάμερα");
			tv.setText("Επέλεξε μια φωτογραφία από την Gallery ή άνοιξε την κάμερα!");
		}
		butt1.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View arg0) {

				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
				String date = dateFormat.format(new Date());

				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, "IMG_" + date
						+ ".jpg");

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				// fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create
				// a file to save the image (this doesn't work at all for
				// images)
				fileUri = getContentResolver().insert(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values); // store
																				// content
																				// values
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

				// start the image capture Intent
				startActivityForResult(intent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

			}
		});

	}

	// grab the name of the media from the Uri
	protected String getName(Uri uri) {
		String filename = null;

		try {
			String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
			Cursor cursor = getContentResolver().query(uri, projection, null,
					null, null);

			if (cursor != null && cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
				filename = cursor.getString(column_index);
			} else {
				filename = null;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error getting file name: " + e.getMessage());
		}

		return filename;
	}

	public void asyncTask5(String url) {
		new AsyncTask5().execute(url);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {
				Uri selectedImageUri = data.getData();
				selectedImagePath = getPath(selectedImageUri);

				// Toast.makeText(this.getApplicationContext(),
				// selectedImagePath,
				// Toast.LENGTH_SHORT).show();

				// decode in bitmap format the image that the user selects from
				// his gallery
				bitmapOrg = BitmapFactory.decodeFile(selectedImagePath);

				bao = new ByteArrayOutputStream();
				// compress the bitmap and put in in output stream bao, 80
				// specifies the quality parametre
				bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 70, bao);
				ba = bao.toByteArray();
				// converts byte array to string so it can be saved in the db
				ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
				// ba1=Base64.encodeBytes(ba);

				asyncTask5(SignIn.add + "base.php");

			}

			if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
				if (fileUri != null) {
					Log.d(TAG, "Image saved to:\n" + fileUri);
					Log.d(TAG, "Image path:\n" + fileUri.getPath());
					Log.d(TAG, "Image name:\n" + getName(fileUri)); // use
																	// uri.getLastPathSegment()
																	// if store
																	// in folder
					Bundle bundle = new Bundle();
					bundle.putString("path_photo", fileUri.getPath());

					Intent mIntent = new Intent();
					mIntent.putExtras(bundle);
					setResult(RESULT_OK, mIntent);

				}

			} else if (resultCode == RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}

		}

	}

	// asynchronous task for saving the image into the db
	private class AsyncTask5 extends AsyncTask<String, Void, Void> {
		private final HttpClient httpclient = new DefaultHttpClient();

		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			dialog = ProgressDialog.show(UploadImage.this, "",
					"Uploading Image");
			// dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		}

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
				// httppost = new
				// HttpPost("http://192.168.1.60/login/base.php");

				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("bitmap", ba1));
				nameValuePairs.add(new BasicNameValuePair("username", name));
				nameValuePairs.add(new BasicNameValuePair("image_id", value1));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				response = httpclient.execute(httppost);
				entity = response.getEntity();
				// is = entity.getContent();
				flag = EntityUtils.toString(entity);
				Log.i("exists", flag);

			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
				// tv.setText("Connection error");
			}
			// TODO Auto-generated method stub
			return null;
		}

		protected void onPostExecute(Void unused) {

			dialog.dismiss();
			// checks if the user has already uploaded the specific image
			if (flag.equals("exists")) {
				if (session.getLan())
					Toast.makeText(
							getApplicationContext(),
							"Έχεις ήδη ανεβάσει αυτή τη φωτογραφία. Μπορείς να επιλέξεις κάποια άλλη!",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(
							getApplicationContext(),
							"You have already uploaded this image,please select another one",
							Toast.LENGTH_SHORT).show();

			}

			else {

				startActivity(new Intent(UploadImage.this,
						PinpointDetails.class));
				finish();
			}

		}

	}

	// returns the string of the path on the sd card where the selected image is
	// saved
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };

		// Cursor cursor = managedQuery(uri, projection, null, null, "");
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		if (cursor != null) {

			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

}