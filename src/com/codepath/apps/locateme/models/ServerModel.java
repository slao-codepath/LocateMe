
package com.codepath.apps.locateme.models;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public abstract class ServerModel {

    /*
     * Interfaces
     */

    public interface GetSingleObjectListener<T> {
        public void onSuccess(T object);

        public void onFailure(Exception e);
    }

    public interface GetMultipleObjectListener<T> {
        public void onSuccess(List<T> objects);

        public void onFailure(Exception e);
    }

    public interface OnDeleteListener {
        public void onDeleteSuccess();

        public void onDeleteFailure(Exception e);
    }

    /*
     * Instance variables
     */

    protected ParseObject mParseObject;
    private String mClassName;

    /*
     * Constructors
     */
    protected ServerModel() {
    }

    protected ServerModel(Class<? extends ServerModel> cls) {
        mClassName = getClassName(cls);
        mParseObject = new ParseObject(mClassName);
    }

    /*
     * Class methods
     */

    private static String getClassName(Class<? extends ServerModel> cls) {
        String className = null;
        ParseClassName annotation = cls.getAnnotation(ParseClassName.class);
        if (annotation != null) {
            className = annotation.value();
        }
        return className;
    }

    private void setParseObject(ParseObject obj) {
        mParseObject = obj;
        setValues(mParseObject);
    }

    private static <T extends ServerModel> T newInstance(ParseObject obj, final Class<T> cls,
            String tableName) {
        T model = null;
        try {
            model = cls.getConstructor().newInstance();
            model.setParseObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    protected static <T extends ServerModel> void performQuerySingle(ParseQuery<ParseObject> query,
            final Class<T> cls, final GetSingleObjectListener<T> listener) {
        final String className = getClassName(cls);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    T newModel = newInstance(results.get(0), cls, className);
                    listener.onSuccess(newModel);
                } else {
                    listener.onFailure(e);
                }
            }
        });
    }

    protected static <T extends ServerModel> void performQuery(ParseQuery<ParseObject> query,
            final Class<T> cls,
            final GetMultipleObjectListener<T> listener) {
        final String className = getClassName(cls);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null) {
                    List<T> models = new ArrayList<T>();
                    for (ParseObject result : results) {
                        models.add(newInstance(result, cls, className));
                    }
                    listener.onSuccess(models);
                } else {
                    listener.onFailure(e);
                }
            }
        });
    }

    protected static ParseQuery<ParseObject> createQuery(Class<? extends ServerModel> cls) {
        return ParseQuery.getQuery(getClassName(cls));
    }

    public static void deleteAll(Class<? extends ServerModel> cls, final OnDeleteListener listener) {
        String className = getClassName(cls);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    try {
                        ParseObject.deleteAll(objects);
                        listener.onDeleteSuccess();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                        listener.onDeleteFailure(e);
                    }
                } else {
                    listener.onDeleteFailure(e);
                }
            }
        });
    }

    public static <T extends ServerModel> void byObjectId(final String objectId,
            final Class<T> cls,
            final GetSingleObjectListener<T> listener) {
        final String tableName = getClassName(cls);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(tableName);
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    T newModel = newInstance(obj, cls, tableName);
                    listener.onSuccess(newModel);
                } else {
                    listener.onFailure(e);
                }
            }
        });
    }

    public static ParseGeoPoint locationToGeoPoint(Location location) {
        return new ParseGeoPoint(location.getLatitude(), location.getLongitude());
    }

    public static Location geoPointToLocation(ParseGeoPoint geoPoint) {
        Location location = new Location("");
        location.setLatitude(geoPoint.getLatitude());
        location.setLongitude(geoPoint.getLongitude());
        return location;
    }

    /*
     * Instance methods
     */
    public void save() {
        if (mParseObject == null) {
            mParseObject = new ParseObject(mClassName);
        }
        setParseValues(mParseObject);
        try {
            mParseObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return mParseObject.getObjectId();
    }

    /*
     * Override this method to set instance variables as soon as a new
     * ParseObject instance is created
     */
    protected abstract void setValues(ParseObject obj);

    /*
     * Override this method to set values to be sent to Parse
     */
    protected abstract void setParseValues(ParseObject obj);
}
