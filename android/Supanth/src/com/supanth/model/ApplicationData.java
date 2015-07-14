package com.supanth.model;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

import com.supanth.Application;

public class ApplicationData {    

    private static ApplicationData singleton = new ApplicationData();
    public List<RSSFeedItem> mRssFeedList = new ArrayList<RSSFeedItem>();
    private NewsFeedParser mNewsFeeder;
    private Integer mCurrentPageIndex = 0;
    private static String TOPSTORIES;

    private ApplicationData() {}

    public static ApplicationData getInstance() {
        return singleton;
    }
    public void fetchNextPage()
    {
		mCurrentPageIndex ++;
        //TOPSTORIES = getResources().getString(R.string.story_portal_url);
        TOPSTORIES = "http://66.155.40.249/support/rss/topic/how-to-add-a-rss-feed-to-my-blog";
        TOPSTORIES = "http://newsrss.bbc.co.uk/rss/newsonline_world_edition/help/rss.xml";
		String rssFeelUrl = TOPSTORIES;
		if(mCurrentPageIndex > 1) rssFeelUrl += "?paged="+mCurrentPageIndex;
	    new DoRssFeedTask().execute(rssFeelUrl);
    }
    
    private class DoRssFeedTask extends AsyncTask<String, Void, List<RSSFeedItem>> {
        List<RSSFeedItem> parsedList;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<RSSFeedItem> doInBackground(String... params) {
            for (String urlVal : params) {
                mNewsFeeder = new NewsFeedParser(urlVal);
            }
            parsedList = mNewsFeeder.parse();
            if(parsedList != null) {
            	ApplicationData.getInstance().mRssFeedList.addAll(parsedList);
            } else {
            }
            return parsedList;
        }

        @Override
        protected void onPostExecute(List<RSSFeedItem> result) {
        	Application.rssFeedFetchParseBus.post(result);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}