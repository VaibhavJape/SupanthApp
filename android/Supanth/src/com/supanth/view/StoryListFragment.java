package com.supanth.view;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.supanth.R;
import com.supanth.model.ApplicationData;
import com.supanth.model.RSSFeedItem;

public class StoryListFragment extends Fragment implements OnItemClickListener {
	private OnItemSelectedListener listener;
    private ListView mRssListView = null;
    private RssAdapter mRssAdap;
    private View loadMoreView;
    private boolean mLoading = false;
    private boolean mLastItemFetched = false;
    
    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
      if (activity instanceof OnItemSelectedListener) {
    	  listener = (OnItemSelectedListener) activity;
        } else {
          throw new ClassCastException(activity.toString()
              + " must implemenet MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
       	if(mRssAdap == null)
    	{
    		mRssAdap = new RssAdapter(getActivity(), R.layout.rss_list_item,
    				ApplicationData.getInstance().mRssFeedList);
    	}
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	    listener = null;
	}
    
    @Override
    public void onStart() {
        super.onStart();
    }

    private OnScrollListener scollListener = new OnScrollListener(){

    	
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int lastInScreen = firstVisibleItem + visibleItemCount;    
			if((lastInScreen == totalItemCount) && !(mLoading)){
				if(mLastItemFetched == true) return;
				mLoading = true;
				//Toast.makeText(getActivity(), "Loading page " + mCurrentPageIndex.toString(), Toast.LENGTH_SHORT).show();
				ApplicationData.getInstance().fetchNextPage();
			}
		}
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {}	
    };
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story_list, container, false);

        if (mRssListView == null)
        {
	        mRssListView = (ListView) view.findViewById(R.id.rss_list_view);
	        mRssListView.setOnItemClickListener(this);
	        loadMoreView = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleSmall);
	        mRssListView.addFooterView(loadMoreView);    
	 
	        mRssListView.setOnScrollListener(scollListener);
        }
        else
        {
        	if(mRssListView.getAdapter() == null)
        	{
        		mRssListView.setAdapter(mRssAdap);
        	}
        	else
        	{
        		mRssAdap.notifyDataSetInvalidated();
        	}
        }
        return view;
    }

    private class RssAdapter extends ArrayAdapter<RSSFeedItem> {
        private List<RSSFeedItem> rssFeedLst;

        public RssAdapter(Context context, int textViewResourceId, List<RSSFeedItem> rssFeedLst) {
            super(context, textViewResourceId, rssFeedLst);
            	this.rssFeedLst = rssFeedLst;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            RssHolder rssHolder = null;
            if (convertView == null) {
                view = View.inflate(getActivity(), R.layout.rss_list_item, null);
                rssHolder = new RssHolder();
                rssHolder.rssFeedTitleView = (TextView) view.findViewById(R.id.feed_title);
                rssHolder.rssFeedAuthorView = (TextView) view.findViewById(R.id.feed_author);
                rssHolder.rssFeedPublishedOnView = (TextView) view.findViewById(R.id.feed_published_on);
                view.setTag(rssHolder);
            } else {
                rssHolder = (RssHolder) view.getTag();
            }
            RSSFeedItem rssFeed = rssFeedLst.get(position);
            rssHolder.rssFeedTitleView.setText(rssFeed.getTitle());
            rssHolder.rssFeedAuthorView.setText(rssFeed.getAuthor());
            rssHolder.rssFeedPublishedOnView.setText(DateUtils.getRelativeTimeSpanString(getActivity(), rssFeed.getPubDate().getTime(), true));
            return view;
        }
    }

    static class RssHolder {
        public TextView rssFeedTitleView;
        public TextView rssFeedAuthorView;
        public TextView rssFeedPublishedOnView;
        }
    public void rssParsingDone(List<RSSFeedItem> result) {
    	if(mRssAdap == null)
    	{
    		mRssAdap = new RssAdapter(getActivity(), R.layout.rss_list_item,
    				ApplicationData.getInstance().mRssFeedList);
    		int count = mRssAdap.getCount();
    		if (count != 0 && mRssAdap != null) {
    			mRssListView.setAdapter(mRssAdap);
    		}
    	}else{
    		if(mRssListView.getAdapter() == null)
    		{
        		int count = ApplicationData.getInstance().mRssFeedList.size();
    			if (count != 0 && mRssAdap != null) {
    				mRssListView.setAdapter(mRssAdap);
    			}
    		}
    		if(result == null)
    		{
    			mLastItemFetched = true;
                mRssListView.removeFooterView(loadMoreView);    
    		}
    		else
    		{
    			mRssAdap.notifyDataSetChanged();
    		}
    	}
			mLoading = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
    	listener.onStorySelected(position);
    }
    
    public interface OnItemSelectedListener {
        public void onStorySelected(int position);
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
    
    
}
