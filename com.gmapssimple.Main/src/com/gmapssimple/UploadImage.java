package com.gmapssimple;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UploadImage extends Activity {
	private static final int SELECT_PICTURE = 1;
	private String selectedImagePath;
    //private String filemanagerstring;
   // private ImageView imageView1;
  InputStream is;
  String imageFilePath;
  Button but,butt2;
  TextView tv,tv2;
  
  @Override
  public void onCreate(Bundle icicle) {
	  super.onCreate(icicle);
	  setContentView(R.layout.upimage);
	  //imageView1=(ImageView)findViewById(R.id.imageView1);
	  tv = (TextView)findViewById(R.id.tv);
	  //tv2 = (TextView)findViewById(R.id.tv2);
	  
	  
	  
	  //button upload image
	  butt2=(Button)findViewById(R.id.bUpload2);
	  butt2.setOnClickListener(new Button.OnClickListener() {            
	            
	            public void onClick(View arg0) {
	          	  
	          	  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	                intent.setType("image/*");
	                startActivityForResult (Intent.createChooser(intent, "Message"), 1);
	               
	            }
	  });
	  
	  
 }
  
  //kaleitai molis patame to button bUpload2
  //molis epilegei mia eikona, vriskei to URI kai to path tis, anoigei http sundesi kai ti stelnei 
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK) {
          if (requestCode == SELECT_PICTURE) {
            
        	  Uri selectedImageUri = data.getData();
              selectedImagePath = getPath(selectedImageUri);
              //emfanizei to path
              Toast.makeText(this.getApplicationContext(), selectedImagePath, Toast.LENGTH_SHORT).show();

          }
      }
      Bitmap bitmapOrg= BitmapFactory.decodeFile(selectedImagePath);
      // tv2.setText("Environment.getExternalStorageDirectory().getPath() ") ;
 	  ByteArrayOutputStream bao = new ByteArrayOutputStream();
 	  bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
 	  byte [] ba = bao.toByteArray();
 	  String ba1=Base64.encodeBytes(ba);
 	  ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

 	  nameValuePairs.add(new BasicNameValuePair("image",ba1));
 	  try{
 		  HttpClient httpclient = new DefaultHttpClient();
 		  HttpPost httppost = new HttpPost("http://10.0.2.2/base.php");
 		  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
 		  HttpResponse response = httpclient.execute(httppost);
 		  HttpEntity entity = response.getEntity();
 		  is = entity.getContent();
 		  tv.setText("File Upload Completed.");
 	  }catch(Exception e){
 		  Log.e("log_tag", "Error in http connection "+e.toString());
 	  }
     
  }


  public String getPath(Uri uri) {
      String[] projection = { MediaStore.Images.Media.DATA };
     
	Cursor cursor = managedQuery(uri, projection, null, null, "");
      if(cursor!=null)
      {
          
          int column_index = cursor
          .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
          cursor.moveToFirst();
          return cursor.getString(column_index);
      }
      else return null;
  }
}