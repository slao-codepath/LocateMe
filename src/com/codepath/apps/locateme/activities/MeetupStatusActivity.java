package com.codepath.apps.locateme.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.codepath.apps.locateme.FragmentTabListener;
import com.codepath.apps.locateme.R;
import com.codepath.apps.locateme.fragments.MeetupMapFragment;
import com.codepath.apps.locateme.fragments.MeetupStatusFragment;
import com.codepath.apps.locateme.models.Meetup;
import com.codepath.apps.locateme.models.ServerModel.GetSingleObjectListener;

public class MeetupStatusActivity extends FragmentActivity implements TabListener {
    Meetup mMeetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetup_status);
        String meetupId = getIntent().getStringExtra("meetupId");
        // TODO: fix
        //        meetup = (Meetup) Meetup.byObjectId(meetupId, Meetup.class);
        Meetup.byObjectId(meetupId, Meetup.class, new GetSingleObjectListener<Meetup>() {

            @Override
            public void onSuccess(Meetup meetup) {
                mMeetup = meetup;
                MeetupStatusActivity.this.getActionBar().setTitle(mMeetup.name + " Status");
                setupTabs();
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Bundle args = new Bundle();
        args.putString("meetupId", mMeetup.getId());

        Tab tab1 = actionBar
                .newTab()
                .setText("List")
                .setTag("MeetupListStatusFragment")
                .setTabListener(
                        new FragmentTabListener<MeetupStatusFragment>(R.id.meetup_status_frame_container, this, "first",
                                MeetupStatusFragment.class, args));
        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        Tab tab2 = actionBar
                .newTab()
                .setText("Map")
                .setTag("MeetupMapStatusFragment")
                .setTabListener(
                        new FragmentTabListener<MeetupMapFragment>(R.id.meetup_status_frame_container, this, "second",
                                MeetupMapFragment.class, args));
        actionBar.addTab(tab2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meetup_status, menu);
        return true;
    }

    @Override
    public void onTabReselected(Tab arg0, android.app.FragmentTransaction arg1) {
    }

    @Override
    public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
    }

    @Override
    public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
    }

}
