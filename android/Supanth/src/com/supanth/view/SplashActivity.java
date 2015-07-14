package com.supanth.view;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.squareup.otto.Subscribe;
import com.supanth.Application;
import com.supanth.R;
import com.supanth.model.ApplicationData;

public class SplashActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        ApplicationData.getInstance().fetchNextPage();
	}
    @Override public void onResume() {
        super.onResume();        
        Application.rssFeedFetchParseBus.register(this);
    }
    @Override public void onPause() {
        super.onPause();
        Application.rssFeedFetchParseBus.unregister(this);
    }
    @Subscribe public void rssParsingDone(Object event) {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
