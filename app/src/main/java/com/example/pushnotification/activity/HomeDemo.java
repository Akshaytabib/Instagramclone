package com.example.pushnotification.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pushnotification.R;
import com.example.pushnotification.adapter.PageViewAdater;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeDemo extends AppCompatActivity {

    DrawerLayout dLayout;
    Toolbar toolbar;
    ViewPager frameLayout;
    LinearLayout linearLayout, linearLayout2;
    ImageView home, addimg, profile, rightImage, ThreeDotImage;
    ViewPager viewPager;
    TextView thome, taddimage, tprofile, toolbarName;
    FirebaseAuth firebaseAuth;
    private PageViewAdater pageViewAdater;
    String userid;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_demo);



        firebaseAuth = FirebaseAuth.getInstance();
        userid = firebaseAuth.getCurrentUser().getUid();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        viewPager = findViewById(R.id.frame);
        toolbarName = findViewById(R.id.textView12);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout2 = findViewById(R.id.linearLayout2);
        frameLayout = findViewById(R.id.frameNav);
        Initialazationdmodule();
        Initialazationdmodulefornavigation();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.third) {
            dLayout.openDrawer(Gravity.RIGHT);
            setNavigationDrawer();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Initialazationdmodulefornavigation() {

        toolbar = findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar
        setNavigationDrawer();

    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly

                if (itemId == R.id.second) {
                    startActivity(new Intent(HomeDemo.this, SaveData.class));
                } else if (itemId == R.id.third) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(HomeDemo.this, MainActivity.class));
                }

                dLayout.closeDrawers();
                return false;
            }

        });
    }

    private void Initialazationdmodule() {

        home = findViewById(R.id.nav_home_item);
        addimg = findViewById(R.id.addimg);
        profile = findViewById(R.id.profile);

        thome = findViewById(R.id.hme_text);
        taddimage = findViewById(R.id.ad_text);
        tprofile = findViewById(R.id.profile_text);


        pageViewAdater = new PageViewAdater(getSupportFragmentManager());
        viewPager.setAdapter(pageViewAdater);
        viewPager.setOffscreenPageLimit(2);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarName.setText("Home");
                viewPager.setCurrentItem(0);
                FragmentManager fm = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.commit(); // save the changes
            }
        });

        addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
                toolbarName.setText("New Post");
                FragmentManager fm = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.commit(); // save the changes
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarName.setText("Profile");
                viewPager.setCurrentItem(2);
                FragmentManager fm = getFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.commit(); // save the changes

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {
                changTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void changTab(int position) {

        if (position == 0) {
            home.setColorFilter(getApplication().getColor(R.color.purple_500));
            thome.setTextColor(getApplication().getColor(R.color.purple_500));

            addimg.setColorFilter(getApplication().getColor(R.color.black));
            taddimage.setTextColor(getApplication().getColor(R.color.black));

            profile.setColorFilter(getApplication().getColor(R.color.black));
            tprofile.setTextColor(getApplication().getColor(R.color.black));
        }

        if (position == 1) {
            home.setColorFilter(getApplication().getColor(R.color.black));
            thome.setTextColor(getApplication().getColor(R.color.black));

            addimg.setColorFilter(getApplication().getColor(R.color.purple_500));
            taddimage.setTextColor(getApplication().getColor(R.color.purple_500));

            profile.setColorFilter(getApplication().getColor(R.color.black));
            tprofile.setTextColor(getApplication().getColor(R.color.black));
        }

        if (position == 2) {
            home.setColorFilter(getApplication().getColor(R.color.black));
            thome.setTextColor(getApplication().getColor(R.color.black));

            addimg.setColorFilter(getApplication().getColor(R.color.black));
            taddimage.setTextColor(getApplication().getColor(R.color.black));

            profile.setColorFilter(getApplication().getColor(R.color.purple_500));
            tprofile.setTextColor(getApplication().getColor(R.color.purple_500));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}