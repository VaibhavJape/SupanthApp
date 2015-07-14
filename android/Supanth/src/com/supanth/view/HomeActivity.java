package com.supanth.view;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.squareup.otto.Subscribe;
import com.supanth.Application;
import com.supanth.R;
import com.supanth.model.RSSFeedItem;

public class HomeActivity extends SherlockFragmentActivity implements StoryListFragment.OnItemSelectedListener{
	private StoryListFragment phoneListFragment;
	@Override public void onResume() {
        super.onResume();        
        Application.rssFeedFetchParseBus.register(this);
    }

    @Override public void onPause() {
        super.onPause();
        Application.rssFeedFetchParseBus.unregister(this);
    }
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        View view = findViewById(R.id.fragment_container);
        if (view != null) {
            if (savedInstanceState != null) {
                return;
            }
            phoneListFragment = new StoryListFragment();
            phoneListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, phoneListFragment).commit();
        }
        else {
            View storyDetailsView = findViewById(R.id.fragment_story_details);
            storyDetailsView.setVisibility(View.GONE);  
        }
    }
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_search:
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        case R.id.menu_info:
        	startActivity(new Intent (this, AboutActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }	
    public void onStorySelected(int position)
    {
        StoryDetailsFragment detailsFragment = (StoryDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_story_details);

        if (detailsFragment != null) {
            View detailsView = findViewById(R.id.fragment_story_details);
            View introView = findViewById(R.id.fragment_introduction);
            introView.setVisibility(View.GONE);  
            detailsView.setVisibility(View.VISIBLE);  
            detailsFragment.onStorySelected(position);

        } else {
        	StoryDetailsFragment newFragment = new StoryDetailsFragment();
        	newFragment.rssPosition = position;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    @Subscribe public void rssParsingDone(ArrayList<RSSFeedItem> result) {
        StoryListFragment tabletListFragment = (StoryListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_story_list);

        if (tabletListFragment != null) {
        	tabletListFragment.rssParsingDone(result);
        } else {
        	phoneListFragment.rssParsingDone(result);
        }
    }
}
