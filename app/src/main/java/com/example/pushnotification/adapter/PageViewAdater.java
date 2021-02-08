package com.example.pushnotification.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import com.example.pushnotification.fragment.AddImageFragment;
import com.example.pushnotification.fragment.HomeFragment;
import com.example.pushnotification.fragment.ProfileFragment;

public class PageViewAdater extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Home", "New Post", "Profile" };


    public PageViewAdater(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                HomeFragment homeFragment=new HomeFragment();
                return homeFragment;

            case 1:
                AddImageFragment addImageFragment=new AddImageFragment();
                return addImageFragment;

            case 2:
                ProfileFragment profile_fragment=new ProfileFragment();
                return profile_fragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
