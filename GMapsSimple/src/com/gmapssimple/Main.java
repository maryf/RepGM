package com.gmapssimple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Main extends Activity {
	SessionManager session;
	TextView map, about, register, login;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initial_screen);

		about = (TextView) findViewById(R.id.textView2);
		register = (TextView) findViewById(R.id.textView4);
		login = (TextView) findViewById(R.id.textView3);
		map = (TextView) findViewById(R.id.textView1);

		session = new SessionManager(getApplicationContext());

		about.setText("About");
		register.setText("Sign Up");
		login.setText("Login");
		map.setText("Map");

		ImageButton bMap = (ImageButton) findViewById(R.id.map);
		bMap.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						OpenMap.class);
				startActivity(intent);
			}
		});

		// login screen
		ImageButton bLogin = (ImageButton) findViewById(R.id.log);
		bLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Main.this, SignIn.class));
			}
		});

		// register screen
		ImageButton bRegister = (ImageButton) findViewById(R.id.regis);
		bRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Main.this, Register.class));

			}
		});

		// about screen
		ImageButton bAbout = (ImageButton) findViewById(R.id.about);
		bAbout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Main.this, About.class));

			}
		});

		ImageButton bGreek = (ImageButton) findViewById(R.id.greek);
		bGreek.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				session.languageSession(true);
				Log.i("language", "lan");
				about.setText("Σχετικά...");
				register.setText("Εγγραφή");
				login.setText("Σύνδεση");
				map.setText("Χάρτης");
				// finish();
				// startActivity(getIntent());

			}
		});

		ImageButton bEnglish = (ImageButton) findViewById(R.id.english);
		bEnglish.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				session.languageSession(false);
				finish();
				startActivity(getIntent());
				// about.setText("About");
				// register.setText("Sign Up");
				// login.setText("Login");
				// map.setText("Map");

			}
		});

	}
}
