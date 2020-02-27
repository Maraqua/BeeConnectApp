package com.marty.beeconnect.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.marty.beeconnect.fragment.ProfileFragment;

public class ProfileViewPageAdapter extends FragmentPagerAdapter {
    int size =0;
    public ProfileViewPageAdapter(FragmentManager fm, int size) {

        super(fm);
        this.size = size;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {

        return size;
    }
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "Posts";
            default:
                return null;

        }
    }
}
