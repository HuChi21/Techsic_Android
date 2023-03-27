package com.example.techsic.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.techsic.fragments.DienThoaiFragment;
import com.example.techsic.fragments.HomeFragment;
import com.example.techsic.fragments.LaptopFragment;
import com.example.techsic.fragments.ProfileFragment;
import com.example.techsic.fragments.TabletFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new DienThoaiFragment();
            case 2:
                return new LaptopFragment();
            case 3:
                return new TabletFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
