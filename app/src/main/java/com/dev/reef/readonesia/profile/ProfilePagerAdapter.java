package com.dev.reef.readonesia.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    public ProfilePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ProfileBacaanItemFragment();
            case 1: return new ProfileRiwayatFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Bacaan";
            case 1: return "Riwayat";
            default: return null;
        }
    }
}
