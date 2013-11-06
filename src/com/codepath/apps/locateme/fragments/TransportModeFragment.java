package com.codepath.apps.locateme.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.codepath.apps.locateme.R;

public class TransportModeFragment extends DialogFragment implements OnClickListener {
	
	static TransportModeFragment newInstance(CharSequence[] options) {
		TransportModeFragment f = new TransportModeFragment();
		Bundle args = new Bundle();
		args.putCharSequenceArray("options", options);
		f.setArguments(args);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NORMAL;
		int theme = android.R.style.Theme_Holo_Light_Dialog;
		setStyle(style, theme);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_transportmode, container, false);
		getDialog().setTitle("Set transportation mode");
		setupViews(view);
		return view;
	}
	
	private void setupViews(View view) {
		ImageButton ibCar = (ImageButton) view.findViewById(R.id.ibCar);
		ImageButton ibTransit = (ImageButton) view.findViewById(R.id.ibTransit);
		ImageButton ibWalk = (ImageButton) view.findViewById(R.id.ibWalk);
		
		ibCar.setOnClickListener(this);
		ibTransit.setOnClickListener(this);
		ibWalk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = getActivity().getIntent();
		if (R.id.ibCar == v.getId()) {
			i.putExtra("selectedTransport", "car");
		} else if (R.id.ibTransit == v.getId()) {
			i.putExtra("selectedTransport", "public");
		} else if (R.id.ibWalk == v.getId()) {
			i.putExtra("selectedTransport", "walk");
		}
		getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
		dismiss();
	}
	
}
