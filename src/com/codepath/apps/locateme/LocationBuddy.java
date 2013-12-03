
package com.codepath.apps.locateme;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.codepath.apps.locateme.activities.LoginActivity;
import com.codepath.apps.locateme.models.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LocationBuddy implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = "LocationBuddy";

    private static final int MILLISECONDS_PER_SECOND = 1000;
    private static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private static LocationBuddy instance;

    private final FragmentActivity mActivity;
    private LocationClient mLocationClient;
    private final LocationRequest mLocationRequest;
    private final User mUser;

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

    public static void createInstance(FragmentActivity activity, User user) {
        instance = new LocationBuddy(activity, user);
    }

    public LocationBuddy getInstance() {
        return instance;
    }

    private LocationBuddy(FragmentActivity activity, User user) {
        mActivity = activity;
        mUser = user;
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        if (servicesConnected()) {
            mLocationClient = new LocationClient(mActivity, this, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects. If the error
         * has a resolution, try sending an Intent to start a Google Play
         * services activity that can resolve error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        mActivity,
                        LoginActivity.CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the user with
             * the error.
             */
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    @Override
    public void onConnected(Bundle dataBundle) {
        Toast.makeText(mActivity, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(mActivity, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

    private void showErrorDialog(int errorCode) {
        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, mActivity,
                LoginActivity.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            // Create a new DialogFragment for the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);
            // Show the error dialog in the DialogFragment
            errorFragment.show(mActivity.getFragmentManager(), "Location Updates");
        }
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mActivity);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(TAG, "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            showErrorDialog(resultCode);
            return false;
        }
    }

    public void startLocationUpdates() {
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    public void connect() {
        if (mLocationClient != null) {
            mLocationClient.connect();
        }
    }

    public void disconnect() {
        if (mLocationClient != null) {
            mLocationClient.disconnect();
        }
    }

    public void stop() {
        if (mLocationClient != null) {
            if (mLocationClient.isConnected()) {
                mLocationClient.removeLocationUpdates(this);
            }
            mLocationClient.disconnect();
        }
    }

    public boolean isConnected() {
        return (mLocationClient != null && mLocationClient.isConnected());
    }

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        // Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        Log.d(TAG, msg);
    }
}
