package com.supanth.view;

import java.util.Dictionary;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.supanth.R;
import com.supanth.model.RSSFeedItem;
import com.supanth.view.StoryDetailsFragment.ShareCompletionCallback;

public class ShareFacebook {
	RSSFeedItem rssFeed;
	Activity baseActivity;
	ShareCompletionCallback callback = new ShareCompletionCallback() {
		@Override
		public void invoke(Boolean success, Dictionary<String, String> params, Error error) {
			if(success){
				Log.i("AK", "handlePostForSelf callback Success");
				
				baseActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(baseActivity, "Post success!", Toast.LENGTH_SHORT).show();
					}
					
				});
			}else{
				Log.i("AK", "handlePostForSelf callback Error");
				baseActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(baseActivity, "Error sending post", Toast.LENGTH_SHORT).show();
					}
				});
			}			
		}
	};

	public ShareFacebook(Activity activity, RSSFeedItem feed) {
	      rssFeed = feed;
	      baseActivity = activity;
	      // start Facebook Login
	      Session session = Session.openActiveSession(baseActivity, true, new Session.StatusCallback() {

	        // callback when session changes state
	        @Override
	        public void call(Session session, SessionState state, Exception exception) {
	      	  if(session.isOpened())
	      	  {
	      		  Toast.makeText(baseActivity, "Opened", Toast.LENGTH_SHORT).show();
	      		  shareOnFaceBook(callback);
	      	  }
	      	  else if((state == SessionState.CLOSED_LOGIN_FAILED)||(state == SessionState.CLOSED))
	      	  {
	      		  Toast.makeText(baseActivity, "Login to Facebook failed!", Toast.LENGTH_SHORT).show();
	      	  }
	        }
	      });
	      Session.setActiveSession(session);
	}
    private void shareOnFaceBook(final ShareCompletionCallback callback)
    {
    	Session session = Session.getActiveSession();
		Bundle params = new Bundle();
		params.putString("name", rssFeed.getTitle());
		params.putString("caption", rssFeed.getCategory());
		params.putString("link", rssFeed.getLink());
		params.putString("picture", baseActivity.getResources().getString(R.string.app_icon_url));
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(baseActivity,
				session, params)).setOnCompleteListener(
				new OnCompleteListener() {
					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								callback.invoke(true, null, null);
							} else {
								// User clicked the Cancel button
								callback.invoke(false, null, null);
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							callback.invoke(false, null, null);
						} else {
							// Generic, ex: network error
							callback.invoke(false, null, null);
						}
					}
				}).build();
		feedDialog.show();		
   }
}
