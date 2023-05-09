package com.example.techsic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.techsic.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TintucActivity extends AppCompatActivity implements View.OnClickListener {
    TabLayout tabLayout;
    TabItem nTatca, nTintuc, nKhuyenmai;
    com.example.techsic.adapters.PagerAdapter pagerAdapter;
    ImageView imageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tintuc);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWidget();
    }

    private void getWidget() {
        imageButton=(ImageView) findViewById(R.id.imgBack);
        imageButton.setOnClickListener(this);

        nTatca =findViewById(R.id.newstatca);
        nTintuc =findViewById(R.id.newstintuc);
        nKhuyenmai =findViewById(R.id.newskhuyenmai);

        ViewPager viewPager = findViewById(R.id.tintucViewpage);
        tabLayout=findViewById(R.id.tablayout);

        pagerAdapter = new com.example.techsic.adapters.PagerAdapter(getSupportFragmentManager(),3);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0||tab.getPosition()==1||tab.getPosition()==2)
                {
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.nothing,R.anim.slide_out);
    }
}