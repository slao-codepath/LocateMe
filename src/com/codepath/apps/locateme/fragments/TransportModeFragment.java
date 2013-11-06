package com.codepath.apps.locateme.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.locateme.R;

public class TransportModeFragment extends DialogFragment {
	
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
		return view;
	}
	
}
