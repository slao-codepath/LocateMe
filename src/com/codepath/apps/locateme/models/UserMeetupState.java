package com.codepath.apps.locateme.models;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "user_meetup_state")
public class UserMeetupState extends Model {

	public enum Status {
		PENDING, ACTIVE, DECLINED, ARRIVED, CANCELLED, DELETED
	}

	@Column(name = "user_id")
	public long userId;

	@Column(name = "meetup_id")
	public long meetupId;

	@Column(name = "status")
	private int status;

	public UserMeetupState() {
		super();
	}

	public void setStatus(Status status) {
		this.status = status.ordinal();
	}

	public Status getStatus() {
		return Status.values()[status];
	}

	public String getStatusString() {
		String val = null;
		Status status = Status.values()[this.status];
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

	// Record Finders

	public static List<UserMeetupState> byUserId(long userId) {
		return new Select().from(UserMeetupState.class).where("user_id = ?", userId).execute();
	}

	public static List<UserMeetupState> byMeetupId(long meetupId) {
		return new Select().from(UserMeetupState.class).where("meetup_id = ?", meetupId).execute();
	}

	public static UserMeetupState byIds(long userId, long meetupId) {
		return new Select().from(UserMeetupState.class).where("meetup_id = ? and user_id = ?", meetupId, userId)
				.executeSingle();
	}
}
