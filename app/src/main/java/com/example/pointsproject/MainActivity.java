package com.example.pointsproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.pointsproject.Account.Login;
import com.example.pointsproject.Constants.ConstantsClass;
import com.example.pointsproject.Fragments.HistoryFragment;
import com.example.pointsproject.Fragments.PointsFragment;
import com.example.pointsproject.Fragments.ProfileFragment;
import com.example.pointsproject.Fragments.SearchFragment;
import com.example.pointsproject.Fragments.TransactionsFragment;
import com.example.pointsproject.Fragments.UserProfile;
import com.example.pointsproject.Interfaces.GeneralInterface;
import com.example.pointsproject.Model.Fragments;
import com.example.pointsproject.Model.PreferencesHelper;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GeneralInterface {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView notMenu;
    private ImageView menuButton;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;

    //fragments
    private Dashboard dashboard;
    private PointsFragment pointsFragment;
    private HistoryFragment historyFragment;
    private ProfileFragment profileFragment;
    private TransactionsFragment transactionsFragment;
    private UserProfile profile;
    private SearchFragment searchFragment;
    
    private static final float END_SCALE = 0.7f;

    private ArrayList<String> tags = new ArrayList<>();
    private ArrayList<Fragments> fragments = new ArrayList<>();
    private int mCount = 0;

    private PreferencesHelper preferencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        notMenu = (ImageView) findViewById(R.id.notificationMenu);
        menuButton = (ImageView) findViewById(R.id.imgMenu);
        linearLayout = (LinearLayout) findViewById(R.id.linearL);
        relativeLayout = (RelativeLayout) findViewById(R.id.toolMain);
        relativeLayout.setVisibility(View.VISIBLE);
        preferencesHelper = new PreferencesHelper(this);

        firstFragment();
        drawerSetUp();
        setNavigation();

        notMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (transactionsFragment == null){
                    transactionsFragment = new TransactionsFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.frame, transactionsFragment, getString(R.string.transaction));
                    fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    fragmentTransaction.commit();
                    tags.add(getString(R.string.transaction));
                    fragments.add(new Fragments(transactionsFragment, getString(R.string.transaction)));
                }
                else {
                    tags.remove(getString(R.string.transaction));
                    tags.add(getString(R.string.transaction));
                }
                setVisibility(getString(R.string.transaction));
            }
        });
        createChannel();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(ConstantsClass.CHANNEL_ID, getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void firstFragment() {
        if (dashboard == null){
            dashboard = new Dashboard();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame, dashboard, getString(R.string.dashboard));
            fragmentTransaction.commit();
            tags.add(getString(R.string.dashboard));
            fragments.add(new Fragments(dashboard, getString(R.string.dashboard)));
        }
        else {
            tags.remove(getString(R.string.dashboard));
            tags.add(getString(R.string.dashboard));
        }
        setVisibility(getString(R.string.dashboard));
    }

    private void setNavigation() {
        navigationView.setCheckedItem(R.id.dashboard);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        if (dashboard == null){
                            dashboard = new Dashboard();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.frame, dashboard, getString(R.string.dashboard));
                            fragmentTransaction.commit();
                            tags.add(getString(R.string.dashboard));
                            fragments.add(new Fragments(dashboard, getString(R.string.dashboard)));
                        }
                        else {
                            tags.remove(getString(R.string.dashboard));
                            tags.add(getString(R.string.dashboard));
                        }
                        setVisibility(getString(R.string.dashboard));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.history:
                        if (historyFragment == null){
                            historyFragment = new HistoryFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.add(R.id.frame, historyFragment, getString(R.string.history));
                            fragmentTransaction.commit();
                            tags.add(getString(R.string.history));
                            fragments.add(new Fragments(historyFragment, getString(R.string.history)));
                        }
                        else {
                            tags.remove(getString(R.string.history));
                            tags.add(getString(R.string.history));
                        }
                        setVisibility(getString(R.string.history));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.points:
                        if (pointsFragment == null){
                            pointsFragment = new PointsFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.add(R.id.frame, pointsFragment, getString(R.string.points));
                            fragmentTransaction.commit();
                            tags.add(getString(R.string.points));
                            fragments.add(new Fragments(pointsFragment, getString(R.string.points)));
                        }
                        else {
                            tags.remove(getString(R.string.points));
                            tags.add(getString(R.string.points));
                        }
                        setVisibility(getString(R.string.points));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.profile:
                        if (profile == null){
                            profile = new UserProfile();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.frame, profile, getString(R.string.edit_profile));
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.commit();
                            tags.add(getString(R.string.edit_profile));
                            fragments.add(new Fragments(profile, getString(R.string.edit_profile)));
                        }
                        else {
                            tags.remove(getString(R.string.edit_profile));
                            tags.add(getString(R.string.edit_profile));
                        }
                        setVisibility(getString(R.string.edit_profile));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    case R.id.logOut:
                        preferencesHelper.setLoggedIn(false);
                        startActivity(new Intent(MainActivity.this, Login.class));
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                }
                return false;
            }
        });
    }

    private void drawerSetUp() {
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        drawerLayout.bringToFront();
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                final float diffScaleOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaleOffset;
                linearLayout.setScaleX(offsetScale);
                linearLayout.setScaleY(offsetScale);

                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = linearLayout.getWidth() * diffScaleOffset / 2;
                final float translation = xOffset - xOffsetDiff;
                linearLayout.setTranslationX(translation);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int total = tags.size();
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else if (total > 1){
            String top = tags.get(total - 1);
            String bot = tags.get(total - 2);
            setVisibility(bot);
            tags.remove(top);
            mCount = 0;
        }
        else if (total == 1){
            String top = tags.get(total - 1);
            if (top.equals(getString(R.string.dashboard))){
                Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                finish();
                mCount++;
            }
            else {
                mCount++;
            }
        }
        else {
            super.onBackPressed();
        }

    }

    private void setVisibility(String tag){
        if (tag.equals(getString(R.string.dashboard))){
            relativeLayout.setVisibility(View.VISIBLE);
        }
        else if (tag.equals(getString(R.string.history))){
            relativeLayout.setVisibility(View.GONE);
        }
        else if (tag.equals(getString(R.string.points))){
            relativeLayout.setVisibility(View.VISIBLE);
        }
        else if (tag.equals(getString(R.string.profile))){
            relativeLayout.setVisibility(View.GONE);
        }
        else if (tag.equals(getString(R.string.transaction))){
            relativeLayout.setVisibility(View.GONE);
        }
        else if (tag.equals(getString(R.string.edit_profile))){
            relativeLayout.setVisibility(View.GONE);
        }
        else if (tag.equals(getString(R.string.search))){
            relativeLayout.setVisibility(View.GONE);
        }
        for (int i = 0; i < fragments.size(); i++){
            if (tag.equals(fragments.get(i).getTag())){
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.show(fragments.get(i).getFragment());
                fragmentTransaction.commit();
            }
            else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(fragments.get(i).getFragment());
                fragmentTransaction.commit();

            }
        }
    }

    @Override
    public void geToPoints() {
        if (pointsFragment == null){
            pointsFragment = new PointsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.add(R.id.frame, pointsFragment, getString(R.string.points));
            fragmentTransaction.commit();
            tags.add(getString(R.string.points));
            fragments.add(new Fragments(pointsFragment, getString(R.string.points)));
        }
        else {
            tags.remove(getString(R.string.points));
            tags.add(getString(R.string.points));
        }
        setVisibility(getString(R.string.points));

    }

    @Override
    public void goToHistory() {
        if (historyFragment == null){
            historyFragment = new HistoryFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.add(R.id.frame, historyFragment, getString(R.string.history));
            fragmentTransaction.commit();
            tags.add(getString(R.string.history));
            fragments.add(new Fragments(historyFragment, getString(R.string.history)));
        }
        else {
            tags.remove(getString(R.string.history));
            tags.add(getString(R.string.history));
        }
        setVisibility(getString(R.string.history));

    }

    @Override
    public void goToProfile() {
        if (profile == null){
            profile = new UserProfile();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame, profile, getString(R.string.edit_profile));
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.commit();
            tags.add(getString(R.string.edit_profile));
            fragments.add(new Fragments(profile, getString(R.string.edit_profile)));
        }
        else {
            tags.remove(getString(R.string.edit_profile));
            tags.add(getString(R.string.edit_profile));
        }
        setVisibility(getString(R.string.edit_profile));
    }

    @Override
    public void goToTransaction() {
        if (transactionsFragment == null){
            transactionsFragment = new TransactionsFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame, transactionsFragment, getString(R.string.transaction));
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.commit();
            tags.add(getString(R.string.transaction));
            fragments.add(new Fragments(transactionsFragment, getString(R.string.transaction)));
        }
        else {
            tags.remove(getString(R.string.transaction));
            tags.add(getString(R.string.transaction));
        }
        setVisibility(getString(R.string.transaction));
    }
    @Override
    public void goToEditProfile() {
        if (profileFragment == null){
            profileFragment = new ProfileFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame, profileFragment, getString(R.string.profile));
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.commit();
            tags.add(getString(R.string.profile));
            fragments.add(new Fragments(profileFragment, getString(R.string.profile)));
        }
        else {
            tags.remove(getString(R.string.profile));
            tags.add(getString(R.string.profile));
        }
        setVisibility(getString(R.string.profile));
    }

    @Override
    public void goToSearch() {
        if (searchFragment == null){
            searchFragment = new SearchFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame, searchFragment, getString(R.string.search));
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.commit();
            tags.add(getString(R.string.search));
            fragments.add(new Fragments(searchFragment, getString(R.string.search)));
        }
        else {
            tags.remove(getString(R.string.search));
            tags.add(getString(R.string.search));
        }
        setVisibility(getString(R.string.search));
    }
}
