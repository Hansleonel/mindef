package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.mindef.gob.adapters.ViewPagerAdapter;
import com.mindef.gob.fragments.Pages.DocumentFragment;
import com.mindef.gob.fragments.Pages.ReplyFragment;
import com.mindef.gob.R;

public class ProcedureActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procedure);

        viewPager = findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 2);
        viewPagerAdapter.addFragment(new DocumentFragment(), getResources().getString(R.string.documents));
        viewPagerAdapter.addFragment(new ReplyFragment(), getResources().getString(R.string.responses));
        viewPager.setAdapter(viewPagerAdapter);

    }
}

