package com.codepath.apps.locateme.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName(value = "UserMeetupState")
public class UserMeetupState extends ServerModel {

    public enum Status {
        PENDING, ACTIVE, DECLINED, ARRIVED, CANCELLED, DELETED, UNKNOWN;

        @Override
        public String toString() {
            String val;
            switch (this) {
                case PENDING:
                    val = "Pending";
                    break;
                case ACTIVE:
                    val = "Active";
                    break;
                case DECLINED:
                    val = "Declined";
                    break;
                case ARRIVED:
                    val = "Arrived";
                    break;
                case CANCELLED:
                    val = "Cancelled";
                    break;
                case DELETED:
                    val = "Deleted";
                    break;
                default:
                    val = "Unknown";
                    break;
            }
            return val;
        }
    }

    public String userId;
    public String meetupId;
    public Status status;

    public UserMeetupState() {
        super(UserMeetupState.class);
    }

    public String getStatusString() {
        String val = null;
        switch (status) {
            case PENDING:
                val = "Pending";
                break;
            case ACTIVE:
                val = "Active";
                break;
            case DECLINED:
                val = "Declined";
                break;
            case ARRIVED:
                val = "Arrived";
                break;
            case CANCELLED:
                val = "Cancelled";
                break;
            case DELETED:
                val = "Deleted";
                break;
            default:
                val = "Unknown";
                break;
        }
        return val;
    }

    @Override
    protected void setValues(ParseObject obj) {
        userId = obj.getString("userId");
        meetupId = obj.getString("meetupId");
        status = Status.values()[obj.getInt("status")];
    }

    @Override
    protected void setParseValues(ParseObject obj) {
        obj.put("userId", userId);
        obj.put("meetupId", meetupId);
        obj.put("status", status.ordinal());
    }

    // Record Finders

    public static void byUserId(String userId, GetMultipleObjectListener<UserMeetupState> listener) {
        ParseQuery<ParseObject> query = createQuery(UserMeetupState.class);
        query.whereEqualTo("userId", userId);
        performQuery(query, UserMeetupState.class, listener);
    }

    public static void byMeetupId(String meetupId, GetMultipleObjectListener<UserMeetupState> listener) {
        ParseQuery<ParseObject> query = createQuery(UserMeetupState.class);
        query.whereEqualTo("meetupId", meetupId);
        performQuery(query, UserMeetupState.class, listener);
    }

    public static void byUserMeetupId(String userId, String meetupId, GetSingleObjectListener<UserMeetupState> getSingleObjectListener) {
        ParseQuery<ParseObject> query = createQuery(UserMeetupState.class);
        query.whereEqualTo("userId", userId);
        query.whereEqualTo("meetupId", meetupId);
        performQuerySingle(query, UserMeetupState.class, getSingleObjectListener);
    }
}
