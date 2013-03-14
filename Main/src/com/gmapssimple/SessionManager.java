package com.gmapssimple;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	
	SharedPreferences pref;
	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "LoginDetails";
	private static final String IS_LOGIN = "IsLoggedIn";
	public static final String KEY_NAME = "username";
	public static final String KEY_LAT = "latitude";
	public static final String KEY_PASS = "pass";
	public static final String KEY_IMAGEID = "image_id";

	
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	
	public void createLoginSession(String username, String pass) {
	
		editor.putBoolean(IS_LOGIN, true);

		editor.putString(KEY_NAME, username);

		editor.putString(KEY_PASS, pass);

		editor.commit();
	}
	
	
	public void createImageIdSession(String imageid) {
		
		editor.putString(KEY_IMAGEID,imageid);
		
		editor.commit();
	}
	
	public void latsession(String latitud) {
		
		//editor.putBoolean(IS_LOGIN, true);

		editor.putString(KEY_LAT, latitud);

		editor.commit();
	}

	


	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		
		user.put(KEY_NAME, pref.getString(KEY_NAME, null));

	
		user.put(KEY_PASS, pref.getString(KEY_PASS, null));

		
		return user;
	}
	
	public HashMap<String, String> getImageId() {
		HashMap<String, String> imageId = new HashMap<String, String>();
		// user name
		imageId.put(KEY_IMAGEID, pref.getString(KEY_IMAGEID, null));

		
		return imageId;
	}
	
	
	public HashMap<String, String> getLat() {
		HashMap<String, String> user = new HashMap<String, String>();
	
		user.put(KEY_LAT, pref.getString(KEY_LAT, null));

		return user;
	}
	

	
	public void logoutUser() {
		
		editor.clear();
		editor.commit();

		
		Intent i = new Intent(_context, SignIn.class);
	
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		
		_context.startActivity(i);
	}

	
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
