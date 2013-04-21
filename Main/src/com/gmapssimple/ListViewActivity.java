package com.gmapssimple;

import java.io.IOException;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewActivity extends Activity {
	/** Called when the activity is first created. */
	
	
	ArrayList<HashMap<String, String>> mylist;
	String[] bit, typ,imid;
	Bitmap[] bit_array;
	int jarraylen;
	String getType;
	ArrayList<Bitmap> bitmapArray ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        Intent intent = getIntent();
        getType = intent.getStringExtra("type");
        
        //ArrayList<ItemDetails> image_details = GetSearchResults();
        sliderAsTask1(SignIn.add+"type_list.php");
        
        final ListView lv1 = (ListView) findViewById(R.id.listV_main);
        //lv1.setAdapter(new Adapter(this, image_details,bit_array));
        lv1.setAdapter(new Adapter(this, bit_array.length,bit_array));
        lv1.setOnItemClickListener(new OnItemClickListener() {
        	public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
        		Object o = lv1.getItemAtPosition(position);
            	ItemDetails obj_itemDetails = (ItemDetails)o;
        		Toast.makeText(ListViewActivity.this, "You have chosen : " + " " + obj_itemDetails.getName(), Toast.LENGTH_LONG).show();
        	}  
        });
    }
    
    
public void sliderAsTask1(String url) {  
		
		try {
			new SliderAsTask1().execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			Log.i("interrupted","interrupte");
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.i("execution","execution");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	 } 
    


   /* private ArrayList<ItemDetails> GetSearchResults(){
    	ArrayList<ItemDetails> results = new ArrayList<ItemDetails>();
    	
    	ItemDetails item_details = new ItemDetails();
    	item_details.setName("Pizza");
    	item_details.setItemDescription("Spicy Chiken Pizza");
    	item_details.setPrice("RS 310.00");
    	item_details.setImageNumber(1);
    	results.add(item_details);
    	
    	item_details = new ItemDetails();
    	item_details.setName("Burger");
    	item_details.setItemDescription("Beef Burger");
    	item_details.setPrice("RS 350.00");
    	item_details.setImageNumber(2);
    	results.add(item_details);
    	
    	item_details = new ItemDetails();
    	item_details.setName("Pizza");
    	item_details.setItemDescription("Chiken Pizza");
    	item_details.setPrice("RS 250.00");
    	item_details.setImageNumber(3);
    	results.add(item_details);
    	
    	item_details = new ItemDetails();
    	item_details.setName("Burger");
    	item_details.setItemDescription("Chicken Burger");
    	item_details.setPrice("RS 350.00");
    	item_details.setImageNumber(4);
    	results.add(item_details);
    	
    	item_details = new ItemDetails();
    	item_details.setName("Burger");
    	item_details.setItemDescription("Fish Burger");
    	item_details.setPrice("RS 310.00");
    	item_details.setImageNumber(5);
    	results.add(item_details);
    	
    	item_details = new ItemDetails();
    	item_details.setName("Mango");
    	item_details.setItemDescription("Mango Juice");
    	item_details.setPrice("RS 250.00");
    	item_details.setImageNumber(6);
    	results.add(item_details);
    	
    	
    	return results;
    }
    */
    
    public class SliderAsTask1 extends AsyncTask<String, Void, String[]> {  
    	
    	
    	
    	private final HttpClient httpclient1 = new DefaultHttpClient();
    	private String Content;  
    	private String Error = null; 
    	//String url = "http://192.168.1.60/login/selpic.php";
    	protected void onPreExecute() {  
    	}
    	
    	public String[] doInBackground(String... urls) {  
    	try {
    	HttpPost httppost1 = new HttpPost(urls[0]);
    	List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
    		nameValuePairs1.add(new BasicNameValuePair("type",getType));
    		Log.i("ok1", "OK!");
    		
    		
    		httppost1.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
    	//HttpGet httpget = new HttpGet(urls[0]);
    	//HttpEntity entity1 = httpclient1.execute(httpget).getEntity();
    	//String response = EntityUtils.toString(entity1);
    	
    		
    		HttpResponse response1 = httpclient1.execute(httppost1);
    		HttpEntity entity1 = response1.getEntity();
    	
    	String is = EntityUtils.toString(entity1);
    		
    		Log.i("ok2",is);
    	//tv.setText("ok!");
    		
    		//HttpResponse response = httpclient1.execute(httppost1);
    		//HttpEntity entity1 = response.getEntity();
    		//HttpGet httpget = new HttpGet(urls[0]);
    	// get the response entity
    	//entity1 = Client.execute(httpget).getEntity();
    	//String is = EntityUtils.toString(entity1);
    	JSONObject json = new JSONObject(is.trim());
    	
    	//JSONObject jsonResponse = new JSONObject(is);

    	JSONArray jArray = json.getJSONArray("posts");
    	Log.i("ok3", "OK!");
    	mylist = new ArrayList<HashMap<String, String>>();
    	jarraylen=jArray.length();
    	bit_array = new Bitmap[jArray.length()];
    	//getting the bitmap attribute of the images and assigning them to a list
    	for (int i = 0; i < jArray.length(); i++) {

    		HashMap<String, String> map = new HashMap<String, String>();
    		JSONObject e1 = jArray.getJSONObject(i);
    		String s = e1.getString("post");
    		
    		JSONObject jObject = new JSONObject(s);

    		
    		//bit = new String[jArray.length()];
    		typ = new String[jArray.length()];
    		imid= new String[jArray.length()];
    		bit = new String[jArray.length()];
    		
    		map.put("bitmap", jObject.getString("bitmap"));
    		bit[i] = map.get("bitmap");
    		
    		
    		//map.put("type", jObject.getString("type"));
    		//typ[i] = map.get("type");
    		byte[] decodedString = Base64.decode(bit[i], Base64.DEFAULT);
    		Log.i("decoded", decodedString.toString());
    		
    		bit_array[i]=BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    		//bitmapArray = new ArrayList<Bitmap>();
    		//bitmapArray.add(bit_array); // Add a bitmap
    		
    		
    		//map.put("type", jObject.getString("type"));
    		//typ[i] = map.get("type");
    		
    		//map.put("image_id", jObject.getString("image_id"));
    		//imid[i] = map.get("image_id");
    		//Log.i("Toast", bit_array[i].toString());
    		
    		//mylist.add(map);

    		
    		
    		
    	
    	}
    	
    	
    	
    	} catch (ClientProtocolException e) { 
    		Log.i("errorrr",e.getMessage());
            Error = e.getMessage();  
            cancel(true);  
        } catch (JSONException e1) {
    	Log.e("log_tag1", "Error parsing data " + e1.toString());
    	} catch (IOException e2) {
    	// TODO Auto-generated catch block
    	e2.printStackTrace();
          
    }
    	return bit;
    }
    public void onPostExecute(String[] bitArray) { 
    	
    	Log.i("array", bit_array.toString());
    	
    	//Toast.makeText(getBaseContext(), "SUCCESS!",Toast.LENGTH_SHORT).show();

    		
    }

    }



}