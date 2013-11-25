package com.codepath.apps.locateme.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.locateme.LocateMeClient;
import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.User;
import com.codepath.oauth.OAuthLoginActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
		ParseFacebookUtils.initialize(getString(R.string.parse_fbAppId));
		ParseFacebookUtils.logIn(this, new LogInCallback() {
			  @Override
			  public void done(ParseUser user, ParseException err) {
			    if (user != null) {
			      	getCurrentUser();
			      	onLoginSuccess();
			    }
			    else {
			    	Toast.makeText(LoginActivity.this, "FB login failed", Toast.LENGTH_LONG).show();
			    }
			  }
			});
	}
	
	@SuppressWarnings("deprecation")
	private void getCurrentUser() {
		  Request.executeMeRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
		    @Override
		    public void onCompleted(GraphUser user, Response response) {
		      if (user != null) {
		        ParseUser.getCurrentUser().put("fbId", user.getId());
		        ParseUser.getCurrentUser().saveInBackground();
		        Toast.makeText(LoginActivity.this, "Logged in as " + user.getUsername(), Toast.LENGTH_LONG).show();
		        getFbFriends();
		      }
		    }
		  });
	}
	
	@SuppressWarnings("deprecation")
	private void getFbFriends() {
		Request.executeMyFriendsRequestAsync(ParseFacebookUtils.getSession(), new Request.GraphUserListCallback() {
			  @Override
			  public void onCompleted(List<GraphUser> users, Response response) {
			    if (users != null) {
			      List<String> friendsList = new ArrayList<String>();
			      for (GraphUser user : users) {
			        friendsList.add(user.getId());
			        Toast.makeText(LoginActivity.this, "Got friend " + user.getUsername(), Toast.LENGTH_SHORT).show();
					Log.d("DEBUG", "Got friend " + user.getUsername());
			      }

			      // Construct a ParseUser query that will find friends whose
			      // facebook IDs are contained in the current user's friend list.
			      ParseQuery<ParseUser> friendQuery = ParseQuery.getUserQuery();
			      friendQuery.whereContainedIn("fbId", friendsList);

			      // findObjects will return a list of ParseUsers that are friends with
			      // the current user
			      try {
					List<ParseUser> friendUsers = friendQuery.find();
					for (ParseUser friend : friendUsers) {
						
					}
			      } catch (ParseException e) {
			    	  e.printStackTrace();
			      }
			    }
			  }
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

}
