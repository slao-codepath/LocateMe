package com.codepath.apps.locateme;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.UserMeetupState.Status;

public class ChangeMeetupStatusDialogFragment extends DialogFragment {
	public interface ChangeMeetupStatusDialogListener {
		public void onChangeMeetupStatusSelected(Meetup meetup, Status status);
	}

	public final static Map<Status, Status[]> TRANSITIONS = new HashMap<Status, Status[]>();
	static {
		TRANSITIONS.put(Status.PENDING, new Status[] { Status.ACTIVE, Status.DECLINED });
		TRANSITIONS.put(Status.ACTIVE, new Status[] { Status.CANCELLED });
		TRANSITIONS.put(Status.CANCELLED, new Status[] { Status.ACTIVE});
		TRANSITIONS.put(Status.DECLINED, new Status[] { Status.ACTIVE});
	}

	ChangeMeetupStatusDialogListener mListener;
	Status mStatus;
	Meetup mMeetup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mMeetup = (Meetup) getArguments().get("meetup");
		mStatus = (Status) getArguments().get("status");
		try {
			mListener = (ChangeMeetupStatusDialogListener) getTargetFragment();
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(getTargetFragment().toString()
					+ " must implement ChangeMeetupStatusDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final Status[] statusValues = TRANSITIONS.get(mStatus);
		CharSequence[] items = new CharSequence[statusValues.length + 1];
		for (int i = 0; i < statusValues.length; ++i) {
			items[i] = statusValues[i].toString();
		}
		items[statusValues.length] = "Nevermind";

		builder.setTitle(R.string.dialog_change_meetup_status).setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which != statusValues.length) {
					mListener.onChangeMeetupStatusSelected(mMeetup, statusValues[which]);
				}
			}
		});
		return builder.create();
	}
}
