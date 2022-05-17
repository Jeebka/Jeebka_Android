package edu.escuelaing.ieti.jeebka.LoginView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import edu.escuelaing.ieti.jeebka.GroupsView.GroupsViewActivity;
import edu.escuelaing.ieti.jeebka.IntroductoryActivity;
import edu.escuelaing.ieti.jeebka.R;

public class LogInActivity extends AppCompatActivity {
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        tabLayout = findViewById(R.id.tab_layout);

        FragmentManager fm = getSupportFragmentManager();
        ViewStateAdapter sa = new ViewStateAdapter(fm, getLifecycle());
        final ViewPager2 pa = findViewById(R.id.view_pager);
        pa.setAdapter(sa);

        tabLayout.addTab(tabLayout.newTab().setText("Acceder"));
        tabLayout.addTab(tabLayout.newTab().setText("Registrarse"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pa.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pa.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        tabLayout.setTranslationX(300);
        tabLayout.setAlpha(0);

        tabLayout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
    }
}