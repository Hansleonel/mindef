package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mindef.gob.adapters.PagerAdapter;
import com.mindef.gob.fragments.PagesIntro.AdvantageFragment;
import com.mindef.gob.fragments.PagesIntro.PermissionFragment;
import com.mindef.gob.fragments.PagesIntro.WelcomeFragment;
import com.mindef.gob.R;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    private View View_first, View_second, View_third;
    private LinearLayout lv_slide;
    private Button btn_permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        lv_slide = findViewById(R.id.lv_slide);
        btn_permissions = findViewById(R.id.btn_permissions);
        View_first = findViewById(R.id.View_first);
        View_second = findViewById(R.id.View_second);
        View_third = findViewById(R.id.View_third);

        viewPager = findViewById(R.id.viewPagerSlide);


        List<Fragment> list = new ArrayList<>();
        list.add(new WelcomeFragment());
        list.add(new AdvantageFragment());
        list.add(new PermissionFragment());

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 2, list);
        viewPager.setAdapter(pagerAdapter);

        btn_permissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPermissions();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    View_first.setBackground(getDrawable(R.drawable.shape_status_current));
                    View_second.setBackground(getDrawable(R.drawable.shape_status_completed));
                    View_third.setBackground(getDrawable(R.drawable.shape_status_completed));
                } else if (position == 1) {
                    View_first.setBackground(getDrawable(R.drawable.shape_status_completed));
                    View_second.setBackground(getDrawable(R.drawable.shape_status_current));
                    View_third.setBackground(getDrawable(R.drawable.shape_status_completed));
                    lv_slide.setVisibility(View.VISIBLE);
                    btn_permissions.setVisibility(View.GONE);
                } else if (position == 2) {
                    View_first.setBackground(getDrawable(R.drawable.shape_status_completed));
                    View_second.setBackground(getDrawable(R.drawable.shape_status_completed));
                    View_third.setBackground(getDrawable(R.drawable.shape_status_current));
                    lv_slide.setVisibility(View.GONE);
                    btn_permissions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getPermissions() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(StartActivity.this, R.string.permissions_granted, Toast.LENGTH_LONG).show();
                SharedPreferences sharedPreferencesUser = getSharedPreferences("UserSharedFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
                editorUser.putString("UserViewSlideString", "Seen");
                editorUser.commit();
                toMain();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                SharedPreferences sharedPreferencesUser = getSharedPreferences("UserSharedFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
                editorUser.putString("UserViewSlideString", "Seen");
                editorUser.commit();
                toMain();
            }
        };

        TedPermission.with(StartActivity.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET)
                .check();
    }

    private void toMain() {
        Intent intent = new Intent(StartActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    ;
}
