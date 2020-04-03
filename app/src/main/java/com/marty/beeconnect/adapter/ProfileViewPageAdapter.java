package com.marty.beeconnect.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.marty.beeconnect.fragment.ProfileFragment;

public class ProfileViewPageAdapter extends FragmentPagerAdapter {
    int size =0;
    String uid="0";
    String current_state="0";
    public ProfileViewPageAdapter(FragmentManager fm, int size, String uid,String current_state) {

        super(fm);
        this.size = size;
        this.uid = uid;
        this.current_state = current_state;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                ProfileFragment profileFragment = new ProfileFragment ();
                Bundle bundle = new Bundle (  );
                bundle.putString ( "uid" ,uid);
                bundle.putString ( "current_state" ,current_state);
                profileFragment.setArguments ( bundle );
                return profileFragment;
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
