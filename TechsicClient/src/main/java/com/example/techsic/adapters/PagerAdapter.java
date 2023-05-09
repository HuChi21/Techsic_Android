package com.example.techsic.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.techsic.fragments.NewsKhuyenmaiFragment;
import com.example.techsic.fragments.NewsTatcaFragment;
import com.example.techsic.fragments.NewsTintucFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabCount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new NewsTatcaFragment();
            case 1:
                return new NewsTintucFragment();
            case 2:
                return new NewsKhuyenmaiFragment();
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
