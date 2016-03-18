package org.ncfrcteams.frcscoutinghub2016.ui.hub;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import org.ncfrcteams.frcscoutinghub2016.R;

import java.util.ArrayList;
import java.util.List;

public class HubActivity extends AppCompatActivity implements
        HubContentsFragment.OnHubFrag1Listener, HubListFragment.OnHubFrag2Listener{

    public Toolbar toolbar;
    public ViewPager hubViewPager;
    public MyPageAdapter pageAdapter;
    public ArrayList<Fragment> fragments = new ArrayList<>();
    public ArrayList<String> fragTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        hubViewPager = (ViewPager) findViewById(R.id.hubViewPager);
        setSupportActionBar(toolbar);

        ArrayList<String> matchesArray = new ArrayList<>();
        for(int i = 1; i < 16; i++){
            matchesArray.add("Match " + (i < 10 ? "0" + String.valueOf(i) : String.valueOf(i)));
        }
        fragments.add(HubContentsFragment.newInstance("Create Match"));
        fragments.add(HubListFragment.newInstance(matchesArray));
        fragments.add(HubContentsFragment.newInstance("Show Match QRs"));
        fragTitles.add("Create");
        fragTitles.add("View");
        fragTitles.add("Show");

        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments, fragTitles);
        hubViewPager.setAdapter(pageAdapter);
        hubViewPager.setCurrentItem(1);
        hubViewPager.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hubnew:
                hubViewPager.setCurrentItem(0);
                return true;
            case R.id.hubview:
                hubViewPager.setCurrentItem(1);
                return true;
            case R.id.hubshow:
                hubViewPager.setCurrentItem(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onHubFrag1Interaction(Uri uri) {
    }

    @Override
    public void onHubFrag2Interaction(Uri uri) {
    }

    private class MyPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private List<String> fragtitles;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments, List<String> fragtitles) {
            super(fm);
            this.fragments = fragments;
            this.fragtitles = fragtitles;
        }

        @Override

        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.fragtitles.get(position);
        }

    }

}
