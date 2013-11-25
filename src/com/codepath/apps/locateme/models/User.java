package com.codepath.apps.locateme.models;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName(value = "_User")
public class User extends ServerModel {
    public enum TransportMode {
        CAR, PUBLIC, WALK, UNKNOWN
    }

    public User() {
        mParseObject = new ParseUser();
    }

    public String name;
    public Location location;
    public TransportMode transportMode;

    // TODO fix
    public int eta;

    @Override
    // User is a special class managed by ParseUser objects
    public void save() {
        setParseValues(mParseObject);
        try {
            if (mParseObject.getObjectId() != null) {
                mParseObject.save();
            } else {
                ParseUser parseUser = (ParseUser) mParseObject;
                parseUser.signUp();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setValues(ParseObject obj) {
        name = obj.getString("username");
        location = geoPointToLocation(obj.getParseGeoPoint("location"));
        transportMode = TransportMode.values()[obj.getInt("transportMode")];
    }

    @Override
    protected void setParseValues(ParseObject obj) {
        ParseUser user = (ParseUser) obj;
        user.setUsername(name);
        user.setPassword("mypass");
        user.put("location", locationToGeoPoint(location));
        user.put("transportMode", transportMode.ordinal());
    }

    public static void byName(String name, GetSingleObjectListener<User> listener) {
        ParseQuery<ParseObject> query = createQuery(User.class);
        query.whereEqualTo("username", name);
        performQuerySingle(query, User.class, listener);
    }

    public static void byIds(List<String> ids, GetMultipleObjectListener<User> listener) {
        ParseQuery<ParseObject> query = createQuery(User.class);
        query.whereContainedIn("objectId", ids);
        performQuery(query, User.class, listener);
    }

    public static void byUserStates(List<UserMeetupState> userStates, GetMultipleObjectListener<User> listener) {
        List<String> userIds = new ArrayList<String>();
        for (UserMeetupState userState : userStates) {
            userIds.add(userState.userId);
        }
        ParseQuery<ParseObject> query = createQuery(User.class);
        query.whereContainedIn("objectId", userIds);
        performQuery(query, User.class, listener);
    }
}
