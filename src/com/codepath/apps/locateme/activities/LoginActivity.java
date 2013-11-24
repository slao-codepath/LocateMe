package com.codepath.apps.locateme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.locateme.LocateMeClient;
import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.User;
import com.codepath.oauth.OAuthLoginActivity;
import com.facebook.*;
import com.facebook.model.*;

public class LoginActivity extends OAuthLoginActivity<LocateMeClient> {
	public static User loggedInUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Intent i = new Intent(this, ListMeetupsActivity.class);
		loggedInUser = MockData.USERS.get(MockData.MOCK_NAMES[0]);
		i.putExtra("userId", loggedInUser.getId());
		startActivity(i);
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

	// Click handler method for the button used to start OAuth flow
	// Uses the client to initiate OAuth authorization
	// This should be tied to a button used to login
	public void loginToRest(View view) {
		// TODO: facebook login
		// getClient().connect();

		// Start Facebook Login
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@SuppressWarnings("deprecation")
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					// onLoginSuccess();
					// make request to the /me API
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								Toast.makeText(LoginActivity.this, "Logged in as " + user.getUsername(), Toast.LENGTH_LONG).show();
							}
						}
					});
				}
			}
		});
		
		onLoginSuccess();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
