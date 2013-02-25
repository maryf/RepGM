package com.gmapssimple;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial_screen);

		Button bMap = (Button) findViewById(R.id.map);
		bMap.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.gmapssimple.OPENMAP"));
			}
		});

		Button bLogin = (Button) findViewById(R.id.log);
		bLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.gmapssimple.SIGNIN"));
			}
		});

		Button bRegister = (Button) findViewById(R.id.regis);
		bRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.gmapssimple.REGISTER"));

			}
		});
		
		
		Button bAbout = (Button) findViewById(R.id.about);
		bAbout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent("com.gmapssimple.ABOUTAB"));

			}
		});
	}
}
