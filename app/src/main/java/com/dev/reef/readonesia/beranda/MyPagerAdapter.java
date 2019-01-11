package com.dev.reef.readonesia.beranda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dev.reef.readonesia.beranda.BerandaFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    public MyPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new BerandaFragment("semua");
            case 1: return new BerandaFragment("bukis");
            case 2: return new BerandaFragment("artikel");
            case 3: return new BeritaFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Semua";
            case 1: return "Bukis";
            case 2: return "Artikel";
            case 3: return "Berita";
            default: return null;
        }
    }
}
