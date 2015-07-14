package com.supanth.view;

import java.util.Dictionary;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.supanth.R;
import com.supanth.model.ApplicationData;
import com.supanth.model.RSSFeedItem;
import com.supanth.model.StoryDetailsParser;

public class StoryDetailsFragment extends Fragment {
	int rssPosition = -1;
	RSSFeedItem rssFeed = null;
    TextView blogTitleView;
    TextView blogAuthorView;
    TextView blogPublishedOnView;
	TextView blogTextView;
    public interface ShareCompletionCallback {
		public void invoke(Boolean success, Dictionary<String, String> params, Error error);
	}
    
    private void closethisFragment(){
    	getActivity().getSupportFragmentManager().popBackStack();
   }

	private void loadViews()
	{
        if(rssPosition >= 0)
        {
            rssFeed = ApplicationData.getInstance().mRssFeedList.get(rssPosition);
        }
        else
        {
        	return;
        }
        
        if(rssFeed != null){
	        
	        blogTitleView.setText(rssFeed.getTitle());
	        blogAuthorView.setText(rssFeed.getAuthor());
	        blogPublishedOnView.setText(DateUtils.getRelativeTimeSpanString(getActivity(), rssFeed.getPubDate().getTime(), true));
	        blogTextView.setText(rssFeed.getTitle());
	        //new DoGetBlogTextTask().execute(rssFeed.getLink());
	    }
        else{
        	Toast.makeText(getActivity(), "Blog could not be read", Toast.LENGTH_SHORT).show();
        }
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_details, container, false);
        View detailsButtonsGroup = view.findViewById(R.id.details_buttons_group);
        ImageButton backButton = (ImageButton)detailsButtonsGroup.findViewById(R.id.back_button);
        if(getResources().getBoolean(R.bool.isTablet) == false) {
	        backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					closethisFragment();
				}
			});
        } else {
        	backButton.setVisibility(View.GONE);        	
            View backButtonPadding = detailsButtonsGroup.findViewById(R.id.back_gap_view);
            backButtonPadding.setVisibility(View.GONE);
        }
        ImageButton shareFBButton = (ImageButton)detailsButtonsGroup.findViewById(R.id.share_fb_button);
        shareFBButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new ShareFacebook(getActivity(), rssFeed);
		}
		});
        ImageButton shareTwitterButton = (ImageButton)detailsButtonsGroup.findViewById(R.id.share_twitter_button);
        shareTwitterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	Toast.makeText(getActivity(), "Sharing on Twitter Coming Soon...", Toast.LENGTH_SHORT).show();
		}
		});
        ImageButton shareGPlusButton = (ImageButton)detailsButtonsGroup.findViewById(R.id.share_gplus_button);
        shareGPlusButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	Toast.makeText(getActivity(), "Sharing on G+ Coming Soon...", Toast.LENGTH_SHORT).show();
		}
		});
        blogTitleView = (TextView)view.findViewById(R.id.blog_title);
        blogAuthorView = (TextView)view.findViewById(R.id.blog_author);
        blogPublishedOnView = (TextView)view.findViewById(R.id.blog_published_on);
        blogTextView = (TextView)view.findViewById(R.id.blog_text);
        
        loadViews();

        return view;
	}
	public class DoGetBlogTextTask extends AsyncTask<String, Void, String> {
        ProgressDialog prog;
        String jsonStr = null;
        Handler innerHandler;
        String blogText;
        StoryDetailsParser mBlogParser;

        @Override
        protected void onPreExecute() {
            prog = new ProgressDialog(getActivity());
            prog.setMessage("Getting details....");
            prog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            for (String urlVal : params) {
            	mBlogParser = new StoryDetailsParser(urlVal);
            }
            blogText = mBlogParser.parse();
            return blogText;
        }

        @Override
        protected void onPostExecute(String result) {
            prog.dismiss();
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
        	        blogTextView.setText(blogText);
        	    }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    
    public void onStorySelected(int position)
    {
    	rssPosition = position;
    	loadViews();
    }
}
