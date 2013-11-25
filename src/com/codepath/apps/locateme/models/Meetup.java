
package com.codepath.apps.locateme.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import android.location.Location;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName(value = "Meetup")
public class Meetup extends ServerModel implements Serializable {
    private static final long serialVersionUID = 708506005927655652L;

    public String name;
    public Location location;
    public Date time;

    public Meetup() {
        super(Meetup.class);
    }

    @Override
    protected void setValues(ParseObject obj) {
        name = obj.getString("name");
        location = geoPointToLocation(obj.getParseGeoPoint("location"));
        time = obj.getDate("time");
    }

    @Override
    protected void setParseValues(ParseObject obj) {
        obj.put("name", name);
        obj.put("location", locationToGeoPoint(location));
        obj.put("time", time);
    }

    public static void byIds(Collection<String> values, GetMultipleObjectListener<Meetup> listener) {
        ParseQuery<ParseObject> query = createQuery(Meetup.class);
        query.whereContainedIn("objectId", values);
        performQuery(query, Meetup.class, listener);
    }

}
