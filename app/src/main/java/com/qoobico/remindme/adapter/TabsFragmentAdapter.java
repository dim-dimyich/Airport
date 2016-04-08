package com.qoobico.remindme.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.qoobico.remindme.fragment.CrewFragment;
import com.qoobico.remindme.fragment.FlightsFragment;
import com.qoobico.remindme.fragment.NewsFragment;
import com.qoobico.remindme.fragment.StaffFragment;

public class TabsFragmentAdapter extends FragmentPagerAdapter {
   // int mNumOfTabs;
    private String[] tabs;


    public TabsFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[]{
                "Notific",
                "Flight crew",
                "Flight",
                "Staff"

        };

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }



    @Override
    public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    NewsFragment tab1 = new NewsFragment();
                    return tab1;
                case 1:
                    CrewFragment tab2 = new CrewFragment();
                    return tab2;
                case 2:
                    FlightsFragment tab3 = new FlightsFragment();
                    return tab3;
                case 3:
                    StaffFragment tab4 = new StaffFragment();
                    return tab4;
                default:
                    return null;
            }
        }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
