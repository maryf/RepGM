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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Base64;
import android.util.FloatMath;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import com.actionbarsherlock.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

public class PinpointView extends SherlockActivity {
	Button brating, getDir, bFb;
	ImageButton bFav;
	String rate, rate_db, username;
	// HttpClient httpclient;
	List<NameValuePair> nameValuePairs, nameValuePairs1;
	ArrayList<HashMap<String, String>> mylist;
	String erar, link, bit, typ, imid, frat, user1;
	String valuefs;
	String bitmap3;
	String s = null;
	SessionManager session;
	Bitmap bitmap4;
	int jarraylen;
	HashMap<String, String> map1;
	int intLat, intLon;

	String lat, lon;
	ImageView imageView;
	
	   float userRankValue = 0;
	
	 private static final String TAG = "Touch";
	    @SuppressWarnings("unused")
	    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

	    // These matrices will be used to scale points of the image
	    Matrix matrix = new Matrix();
	    Matrix savedMatrix = new Matrix();

	    // The 3 states (events) which the user is trying to perform
	    static final int NONE = 0;
	    static final int DRAG = 1;
	    static final int ZOOM = 2;
	    int mode = NONE;

	    // these PointF objects are used to record the point(s) the user is touching
	    PointF start = new PointF();
	    PointF mid = new PointF();
	    float oldDist = 1f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pipnpointview);
		session = new SessionManager(getApplicationContext());

		HashMap<String, String> imageId = session.getImageId();
		valuefs = imageId.get(SessionManager.KEY_IMAGEID);
		// tvsimple.setText(valuefs);
		Log.i("bitmapMary0", "[" + valuefs + "]");
		
		imageView = (ImageView) findViewById(R.id.imageView1);
		imageView.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            	imageView.setScaleType(ImageView.ScaleType.MATRIX);
                float scale;

                dumpEvent(event);
                // Handle touch events here...

                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                   savedMatrix.set(matrix);
                   start.set(event.getX(), event.getY());
                   Log.d(TAG, "mode=DRAG");
                   mode = DRAG;
                   break;
                case MotionEvent.ACTION_POINTER_DOWN:
                   oldDist = spacing(event);
                   Log.d(TAG, "oldDist=" + oldDist);
                   if (oldDist > 10f) {
                      savedMatrix.set(matrix);
                      midPoint(mid, event);
                      mode = ZOOM;
                      Log.d(TAG, "mode=ZOOM");
                   }
                   break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                   mode = NONE;
                   Log.d(TAG, "mode=NONE");
                   break;
                case MotionEvent.ACTION_MOVE:
                   if (mode == DRAG) {
                      // ...
                      matrix.set(savedMatrix);
                      matrix.postTranslate(event.getX() - start.x,
                            event.getY() - start.y);
                   }
                   else if (mode == ZOOM) {
                      float newDist = spacing(event);
                      Log.d(TAG, "newDist=" + newDist);
                      if (newDist > 10f) {
                         matrix.set(savedMatrix);
                         scale = newDist / oldDist;
                         matrix.postScale(scale, scale, mid.x, mid.y);
                      }
                   }
                   break;
                }

                imageView.setImageMatrix(matrix); // display the transformation on screen

                return true; // indicate event was handled
            }

			
       });

		maryAsyncTask(SignIn.add + "decode_bitmap.php");

	}
	
	

    /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) 
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) 
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) 
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) 
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }
	
	
	

	/*
	 * @Override public void onBackPressed() { Intent backKey=new
	 * Intent(getApplicationContext(),OpenMap.class); startActivity(backKey);
	 * finish(); }
	 */

	public void maryAsyncTask(String url) {
		new MaryAsyncTask().execute(url);
	}

	public void maryAsyncTask1(String url, String rate2) {
		new MaryAsynctask1().execute(url, rate2);
	}

	public void favoritesAsyncTask(String url) {
		new FavoritesAsyncTask().execute(url);
	}

	public void deleteImageAsyncTask(String url) {
		new DeleteImageAsyncTask().execute(url);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.pinpoint_menu, menu);
		
		MenuItem mitem = menu.findItem(R.id.delete);
		if (session.getLan())
			mitem.setTitle("Διαγραφή");
		else
			mitem.setTitle("Delete");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {

		final Dialog rankDialog;
		RatingBar ratingBar;
		switch (item.getItemId()) {

		case R.id.fav:

			if (!session.isLoggedIn()) {
				if (session.getLan()) {
					Toast.makeText(getApplicationContext(),
							"Πρέπει να συνδεθείς στο λογαριασμό σου",
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getApplicationContext(),
							SignIn.class));
				} else {
					Toast.makeText(getApplicationContext(),
							"You have to log in first", Toast.LENGTH_SHORT)
							.show();
					startActivity(new Intent(getApplicationContext(),
							SignIn.class));
				}
			} else
				favoritesAsyncTask(SignIn.add + "set_favorite.php");
			break;

		case R.id.fb:

			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			startActivity(intent);
			break;

		case R.id.button1:
			Bundle extras = getIntent().getExtras();
			

			double dlat = extras.getDouble("touchedLat");
			double dlon = extras.getDouble("touchedLon");
			lat = Double.toString(dlat);
			lon = Double.toString(dlon);

			Intent intent1 = new Intent(android.content.Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?daddr=" + lat + ","
							+ lon + ""));
			startActivity(intent1);
			break;

		case R.id.rating:
			
			
			
						rankDialog = new Dialog(PinpointView.this);
				        rankDialog.setContentView(R.layout.rank_dialog);
				        rankDialog.setCancelable(true);
				        ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);
				     
						ratingBar.setRating(userRankValue);
				        
				        //TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
			
				 
				        Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
				        updateButton.setOnClickListener(new View.OnClickListener() {
				            @Override
				            public void onClick(View v) {
				            	maryAsyncTask1(SignIn.add + "set_rate.php",
										String.valueOf(userRankValue * 2));
				                rankDialog.dismiss();
				            }
				        });
				        //now that the dialog is set up, it's time to show it    
				        rankDialog.show();   
					

			
			
			break;

		case R.id.delete:
			AlertDialog.Builder alertDialogBuild = new AlertDialog.Builder(this);

			if (!session.isLoggedIn()) {
				if (session.getLan()) {
					Toast.makeText(getApplicationContext(),
							"Πρέπει να συνδεθείς στο λογαριασμό σου!",
							Toast.LENGTH_SHORT).show();
					startActivity(new Intent(getApplicationContext(),
							SignIn.class));
				} else {
					Toast.makeText(getApplicationContext(),
							"You have to login first!", Toast.LENGTH_SHORT)
							.show();
					startActivity(new Intent(getApplicationContext(),
							SignIn.class));
				}
			} else
				// notify user you are not online
				if (session.getLan()){
					alertDialogBuild.setTitle("Διαγραφή εικόνας");

					// set dialog message
					alertDialogBuild
							.setMessage("Είσαι σίγουρος ότι θες να διαγράψεις αυτή την εικόνα?")
							.setCancelable(false)
							.setPositiveButton("Ναι",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {
											deleteImageAsyncTask(SignIn.add
													+ "delete_image.php");
											startActivity(new Intent(
													getApplicationContext(),
													OpenMap.class));
											finish();
										}
									})
							.setNegativeButton("Όχι",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {
											// if this button is clicked, just close
											// the dialog box and do nothing

											dialog.cancel();
											// Toast.makeText(getApplicationContext(),
											// "Sorry you cannot receive any data without internet connection",Toast.LENGTH_SHORT).show();

										}
									});
				}else{
				// set title
				alertDialogBuild.setTitle("Delete Image");

			// set dialog message
			alertDialogBuild
					.setMessage("Are you sure you want to delete this image?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									deleteImageAsyncTask(SignIn.add
											+ "delete_image.php");
									startActivity(new Intent(
											getApplicationContext(),
											OpenMap.class));
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing

									dialog.cancel();
									// Toast.makeText(getApplicationContext(),
									// "Sorry you cannot receive any data without internet connection",Toast.LENGTH_SHORT).show();

								}
							});
				}
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuild.create();

			// show it
			alertDialog.show();

			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.rate1:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate2:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			// if (item.isChecked()) item.setChecked(false);
			// else item.setChecked(true);
			return true;
		case R.id.rate3:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate4:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate5:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate6:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate7:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate8:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate9:
			rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);
			return true;
		case R.id.rate10:
			String rate = (String) item.getTitle();
			maryAsyncTask1(SignIn.add + "set_rate.php", rate);

			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

	private class MaryAsyncTask extends AsyncTask<String, Void, String> {
		private final HttpClient Client = new DefaultHttpClient();
		String theString = null;
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {

			dialog = ProgressDialog
					.show(PinpointView.this, "", "Loading Image");
			// dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		}

		@Override
		protected String doInBackground(String... urls) {

			try {
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
				HttpConnectionParams.setSoTimeout(httpParameters, 10000);
				Log.i("OK1111", "ok");
				HttpPost httppost = new HttpPost(urls[0]);

				// httppost = new
				// HttpPost("http://192.168.1.60/login/decode_bitmap.php");

				nameValuePairs1 = new ArrayList<NameValuePair>();
				nameValuePairs1
						.add(new BasicNameValuePair("image_id", valuefs));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
				HttpResponse response = Client.execute(httppost);
				HttpEntity entity = response.getEntity();

				String is = EntityUtils.toString(entity);

				Log.i("response", is);

				JSONObject jsonResponse = new JSONObject(is);

				frat = jsonResponse.getString("final_rating");//
				link = jsonResponse.getString("wiki_link");
				theString = jsonResponse.getString("bitmap");
				typ = jsonResponse.getString("type");
				erar = jsonResponse.getString("era");
				user1 = jsonResponse.getString("username");

				is = null;
			} catch (Exception e2) {
				Log.e("log_rating", "Error in http connection " + e2.toString());

			}

			bitmap3 = theString;
			session.bitsession(bitmap3);

			return bitmap3;
		}

		@Override
		protected void onPostExecute(String bitm) {

			
			try {
				byte[] decodedString;

				decodedString = Base64.decode(bitm, Base64.DEFAULT);

				// Log.i("SIZE", Integer.toString(decodedString.length));

				// scale for avoiding out of memory exception
				BitmapFactory.Options o = new BitmapFactory.Options();
				o.inJustDecodeBounds = true;
				BitmapFactory.decodeByteArray(decodedString, 0,
						decodedString.length, o);

				int scale = 1;
				if (o.outHeight > 1000 || o.outWidth > 1000) {
					scale = (int) Math.pow(
							2,
							(int) Math.round(Math.log(1000 / (double) Math.max(
									o.outHeight, o.outWidth)) / Math.log(0.5)));
				}
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				o2.inSampleSize = scale;

				bitmap4 = BitmapFactory.decodeByteArray(decodedString, 0,
						decodedString.length, o2);

				imageView.setImageBitmap(bitmap4);
				bitmap4 = null;
			} catch (OutOfMemoryError e) {
				Log.e("EWN", "Out of memory error catched");

			}// catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			// }

			TextView tv1 = (TextView) findViewById(R.id.textType);
			TextView tv2 = (TextView) findViewById(R.id.textEra);
			TextView tv3 = (TextView) findViewById(R.id.textRatings);
			TextView tv4 = (TextView) findViewById(R.id.textLink);
			TextView tv5 = (TextView) findViewById(R.id.textUser);
			tv1.setText("Category : " + typ);
			if (erar.equals("ottoman"))
				tv2.setText("Era : Venetian/French/English");
			else
				tv2.setText("Era : " + erar);
			tv3.setText("Description/Link : " + link);
			if (frat.equals("0"))
				tv4.setText("Ratings : " + "-");
			else
				tv4.setText("Ratings : " + frat);
			tv5.setText("Uploaded by user : " + user1);
			// create clickable link
			if (session.getLan()) {
				tv1.setText("Κατηγορία : " + typ);
				if (erar.equals("ottoman"))
					tv2.setText("Χρονολογική περίοδος : Ενετική/Γαλλική/Αγγλική");
				else
					tv3.setText("Περγραφή/Link : " + link);
				if (frat.equals("0"))
					tv4.setText("Βαθμολογία : " + "-");
				else
					tv4.setText("Βαθμολογία : " + frat);
				tv5.setText("Ανέβηκε από τον χρήστη : " + user1);
			}
			Linkify.addLinks(tv3, Linkify.ALL);
			dialog.dismiss();

		}

	}

	private class MaryAsynctask1 extends AsyncTask<String, Void, Void> {
		private final HttpClient httpClient = new DefaultHttpClient();
		private String Error = null;
		String ab = null;

		@Override
		protected Void doInBackground(String... urls) {
			// TODO Auto-generated method stub

			try {
				Log.i("OK1", "ok");
				String rating = urls[1];
				Log.i("OK1", rating);
				HttpPost httppost = new HttpPost(urls[0]);
				// httppost = new
				// HttpPost("http://192.168.1.60/login/set_rate.php");
				Log.i("OK2", "ok");
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image_id", valuefs));
				nameValuePairs.add(new BasicNameValuePair("rating1", rating));
				Log.i("OK3", "ok");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpClient.execute(httppost);
				Log.i("OK4", "ok");
				if (response.getStatusLine().getStatusCode() == 200) {
					Log.i("OK5", "ok");

					HttpEntity entity = response.getEntity();
					if (entity != null) {
						Log.i("OK6", "ok");
						String is = EntityUtils.toString(entity);
						Log.i("response", is);
						// InputStream instream = entity.getContent();

					}
				}

			} catch (Exception e1) {
				Log.e("log_rating", "Error in http connection " + e1.toString());
				Error = "1";
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void unused) {
			// if (Error==null)
			// Toast.makeText(getApplicationContext(),
			// "SUCCESS!",Toast.LENGTH_SHORT).show();

		}
	}

	private class FavoritesAsyncTask extends AsyncTask<String, Void, Void> {
		private final HttpClient httpClient = new DefaultHttpClient();
		private String Error = null;
		Boolean checkUser = false;
		String is;

		@Override
		protected Void doInBackground(String... urls) {
			// TODO Auto-generated method stub
			HashMap<String, String> user = session.getUserDetails();
			username = user.get(SessionManager.KEY_NAME);
			// Log.i("username1", username);

			try {
				Log.i("OK1", "ok");

				HttpPost httppost = new HttpPost(urls[0]);
				// httppost = new
				// HttpPost("http://192.168.1.60/login/set_rate.php");
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image_id", valuefs));
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpClient.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {

					HttpEntity entity = response.getEntity();
					if (entity != null) {
						is = EntityUtils.toString(entity);
						if (!is.equals("ok")) {
							checkUser = true;

						}
						Log.i("response", is);

					}
				}

			} catch (Exception e1) {
				Log.e("log_rating", "Error in http connection " + e1.toString());
				Error = "1";
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void unused) {
			if (checkUser) {
				Log.i("is", is);
				Toast.makeText(getApplicationContext(), is, Toast.LENGTH_SHORT)
						.show();
			} else if (Error == null) {
				if (session.getLan())
					Toast.makeText(getApplicationContext(),
							"Προστέθηκε στα Αγαπημένα!", Toast.LENGTH_SHORT)
							.show();
				else
					Toast.makeText(getApplicationContext(),
							"Added to your favorite images!",
							Toast.LENGTH_SHORT).show();
			}
		}
	}

	private class DeleteImageAsyncTask extends AsyncTask<String, Void, Void> {
		private final HttpClient httpClient = new DefaultHttpClient();
		private String Error = null;
		Boolean checkUser = false;
		String is;

		@Override
		protected Void doInBackground(String... urls) {
			// TODO Auto-generated method stub
			HashMap<String, String> user = session.getUserDetails();
			username = user.get(SessionManager.KEY_NAME);
			// Log.i("username1", username);

			try {
				Log.i("OK1", "ok");

				HttpPost httppost = new HttpPost(urls[0]);
				// httppost = new
				// HttpPost("http://192.168.1.60/login/set_rate.php");
				nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("image_id", valuefs));
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpClient.execute(httppost);
				if (response.getStatusLine().getStatusCode() == 200) {

					HttpEntity entity = response.getEntity();
					if (entity != null) {
						is = EntityUtils.toString(entity);
						if (!is.equals("ok")) {
							checkUser = true;

						}
						Log.i("response", is);

					}
				}

			} catch (Exception e1) {
				Log.e("log_rating", "Error in http connection " + e1.toString());
				Error = "1";
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void unused) {
			if (checkUser) {
				Toast.makeText(getApplicationContext(), is, Toast.LENGTH_SHORT)
						.show();
			} else if (Error == null) {
				// Toast.makeText(getApplicationContext(),
				// "Added to your favorite images!",Toast.LENGTH_SHORT).show();
			}
		}
	}

}
