package com.supanth.view;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockActivity;
import com.supanth.R;

public class AboutActivity extends SherlockActivity {
	   public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_about);
	        RelativeLayout aboutOuterView = (RelativeLayout)findViewById(R.id.about_outer_view);
	        aboutOuterView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					finish();
					return false;
				}
			});
	   }
}
