package com.codepath.apps.locateme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.codepath.apps.locateme.LocateMeClient;
import com.codepath.apps.locateme.MockData;
import com.codepath.apps.locateme.MockData.OnMockDataListener;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.models.ServerModel.GetSingleObjectListener;
import com.codepath.apps.locateme.models.User;
import com.codepath.oauth.OAuthLoginActivity;

public class LoginActivity extends OAuthLoginActivity<LocateMeClient> implements OnMockDataListener {
    // Setting MOCK to true will clear out Parse database and recreate new mock data (Users, Meetups, etc.)
    private static final boolean MOCK = false;

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
        if (!MOCK) {
            startNextScreen();
        } else {
            new MockData(this).mock();
        }
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
        onLoginSuccess();
    }

    @Override
    public void onMockCompleted() {
        startNextScreen();
    }

    private void startNextScreen() {
        User.byName(MockData.MOCK_NAMES[0], new GetSingleObjectListener<User>() {

            @Override
            public void onSuccess(User object) {
                loggedInUser = object;
                Intent i = new Intent(LoginActivity.this, ListMeetupsActivity.class);
                i.putExtra("userId", loggedInUser.getId());
                startActivity(i);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
