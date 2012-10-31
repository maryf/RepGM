package com.gmapssimple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignIn extends Activity implements OnClickListener{
    EditText etUser, etPass;
    Button bLogin;
    
    //Create string variables that will have the input assigned to them
    String username, password;
    
    //Create a HTTPClient as the form container
    HttpClient httpclient;
    
    //Use HTTP POST method
    HttpPost httppost;
    
    //Create an array list for the input data to be sent
    ArrayList<NameValuePair> nameValuePairs;
    
    //Create a HTTP Response and HTTP Entity
    HttpResponse response;
    HttpEntity entity;
    

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        
        initialise();
    }

	private void initialise() {
		// TODO Auto-generated method stub
		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPass);
		bLogin = (Button) findViewById(R.id.bSubmit);
		bLogin.setOnClickListener(this);
	}

	public void onClick(View v) {
				
		//Dimiourgia HTTPClient
		httpclient = new DefaultHttpClient();
		
		//Dimiourgia HTTP POST me parameter ti dieythinsi tou php arxeiou
		httppost = new HttpPost("http://10.0.2.2/login/index.php");
		
		username = etUser.getText().toString();
		password = etPass.getText().toString();
		
		
		try {
			
			nameValuePairs = new ArrayList<NameValuePair>();
			
			//prosthiki username k pass se arraylist
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
			response = httpclient.execute(httppost);
			
			//an status code 200 ok i sundesi
			if(response.getStatusLine().getStatusCode()== 200){
				
			    entity = response.getEntity();
				
				if(entity != null){
					
					
					//Create new input stream with received data assigned
					InputStream instream = entity.getContent();
					
					//Create new JSON Object. assign converted data as parameter.
					JSONObject jsonResponse = new JSONObject(convertStreamToString(instream));
					
					//assign json responses to local strings
					String retUser = jsonResponse.getString("user");//mySQL table field
					String retPass = jsonResponse.getString("pass");
					
					//Validate login
					if(username.equals(retUser)&& password.equals(retPass)){
						
						//Create a new shared preference by getting the preference
						//Give the shared preference any name you like.
						SharedPreferences sp = getSharedPreferences("logindetails", 0);
						
						//Edit the Shared Preference
						SharedPreferences.Editor spedit = sp.edit();
						
						//Put the login details as strings
						spedit.putString("user", username);
						spedit.putString("pass", password);
						
						//Close the editor
						spedit.commit();
					
						Toast.makeText(getBaseContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
						
						
					} else {
						
						Toast.makeText(getBaseContext(), "Invalid Login Details", Toast.LENGTH_SHORT).show();
					}
					
				}
				
				
			}
			
			
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("ERROR : " + e.getMessage());
			Toast.makeText(getBaseContext(), "Login Unsuccessful.", Toast.LENGTH_SHORT).show();
		}

		
		
	}	
	
	//metatropi InputStream se String
	//mexri o BufferedReader na epistrepsei null, dld mexri to telos tou arxeiou
	//kathe grammi prostithetai se ena StringBuilder kai epistrefetai se String
	private static String convertStreamToString(InputStream is) {
       
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	
}