package com.dev.reef.readonesia.beranda;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.reef.readonesia.CreateBacaanActivity;
import com.dev.reef.readonesia.R;
import com.dev.reef.readonesia.beranda.MyPagerAdapter;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private FloatingActionButton fab;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.fab);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.pager_fragment);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);

        fab.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                Intent intent = new Intent(getContext(), CreateBacaanActivity.class);
                startActivity(intent);
        }
    }
}
