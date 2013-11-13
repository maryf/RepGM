package com.gmapssimple;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private static final int SETTINGS = 2;
	private static final int FRAGMENT_COUNT = SETTINGS + 1;

	// private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	// represent the menu item for the settings fragment
	// private MenuItem settings;

	// flag for checking the session state changes
	// private boolean isResumed = false;

	private MainFragment mainFragment;
	private static final String TAG = "MainFragment";
	SessionManager session1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		// uiHelper = new UiLifecycleHelper(this, callback);
		// uiHelper.onCreate(savedInstanceState);
		session1 = new SessionManager(this);
		String bitmap = session1.getBit();
		Bundle bundle = new Bundle();
		bundle.putString("bitmap", bitmap);
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();
			mainFragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, mainFragment).commit();
		} else {
			// Or set the fragment from restored state info
			mainFragment = (MainFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}

}

class MainFragment extends Fragment {

	private static final int SETTINGS = 2;
	private static final int SPLASH = 0;
	private static final int SELECTION = 1;
	private static final int FRAGMENT_COUNT = SETTINGS + 1;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	// represent the menu item for the settings fragment
	private MenuItem settings;

	// flag for checking the session state changes
	private boolean isResumed = false;

	private Button shareButton;
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private UiLifecycleHelper uiHelper;
	String bit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_activity, container, false);
		shareButton = (Button) view.findViewById(R.id.shareButton);
		bit = getArguments().getString("bitmap");

		shareButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				publishStory();
			}
		});

		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		//authButton.setFragment(this);
		// authButton.setReadPermissions(PERMISSIONS);

		/*
		 * if (savedInstanceState != null) { pendingPublishReauthorization =
		 * savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false); }
		 */
		 
		 authButton.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			shareButton.setVisibility(View.VISIBLE);

			if (pendingPublishReauthorization
					&& state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				pendingPublishReauthorization = false;
				publishStory();
			}
		} else if (state.isClosed()) {
			shareButton.setVisibility(View.INVISIBLE);
			Log.i(TAG, "Logged out...");
		}
	}

	private static final String TAG = "MainFragment";

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				pendingPublishReauthorization = true;
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			Bundle postParams = new Bundle();
			postParams.putString("name", "Check out this photo via ZanteApp!");
			postParams.putString("caption",
					"Build great social apps and get more installs.");
			postParams
					.putString(
							"description",
							"The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
			postParams.putString("link",
					"https://developers.facebook.com/android");
			// String
			byte[] decodedString = Base64.decode(bit, Base64.DEFAULT);
			// byte[] data=bitmap.getBytes();
			postParams.putByteArray("source", decodedString);
			// postParams.putString("picture",
			// "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					// JSONObject json = Util.parseJson(response);
					// message = json.getString("message");
					String postId = null;
					try {
						postId = graphResponse.getString("id");
					} catch (JSONException e) {
						Log.i(TAG, "JSON error " + e.getMessage());
					}
					FacebookRequestError error = response.getError();
					// if (error != null) {
					// Toast.makeText(getActivity().getApplicationContext(),
					// error.getErrorMessage(),Toast.LENGTH_SHORT).show();
					// } else {
					// Toast.makeText(getActivity().getApplicationContext(),
					// postId, Toast.LENGTH_LONG).show();
					// }
				}
			};

			Request request = new Request(session, "me/photos", postParams,
					HttpMethod.POST, callback);

			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
		uiHelper.onSaveInstanceState(outState);
	}

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}
}
