package com.example.pointsproject.Admin.Classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pointsproject.Account.Login;
import com.example.pointsproject.Account.Register;
import com.example.pointsproject.Admin.Fragments.AdminTransactions;
import com.example.pointsproject.Admin.Fragments.DetailsFragment;
import com.example.pointsproject.Admin.Fragments.PaymentFragment;
import com.example.pointsproject.Admin.Fragments.RedeemedUsersFragment;
import com.example.pointsproject.Admin.Fragments.SearchAdminFragment;
import com.example.pointsproject.Admin.Fragments.UsersFragment;
import com.example.pointsproject.Admin.Interfaces.AdminInterfaces;
import com.example.pointsproject.MainActivity;
import com.example.pointsproject.Model.Fragments;
import com.example.pointsproject.Model.PreferencesHelper;
import com.example.pointsproject.Model.Redeem;
import com.example.pointsproject.Model.Users;
import com.example.pointsproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements AdminInterfaces {
    private BottomNavigationView bottomNavigationView;

    //fragments
    private UsersFragment usersFragment;
    private DetailsFragment detailsFragment;
    private PaymentFragment paymentFragment;
    private RedeemedUsersFragment redeemedUsersFragment;
    private AdminTransactions adminTransactions;
    private SearchAdminFragment searchAdminFragment;

    private ArrayList<Fragments> fragments = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private int mCount = 0;
    private PreferenceHelperAdmin preferencesHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setNavigationBarColor(getResources().getColor(R.color.purple_700));
        }
        preferencesHelper = new PreferenceHelperAdmin(this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom);
        initBottom();
        initFragment();
    }

    private void initBottom() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.homeAdmin:
                        if (usersFragment == null){
                            usersFragment = new UsersFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.frameAdmin, usersFragment, getString(R.string.user_fragment));
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.commit();
                            tags.add(getString(R.string.user_fragment));
                            fragments.add(new Fragments(usersFragment, getString(R.string.user_fragment)));
                        }
                        else {
                            tags.remove(getString(R.string.user_fragment));
                            tags.add(getString(R.string.user_fragment));
                        }
                        setVisibility(getString(R.string.user_fragment));
                        return true;
                    case R.id.histAdmin:
                        if (adminTransactions == null){
                            adminTransactions = new AdminTransactions();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.frameAdmin, adminTransactions, getString(R.string.admin_transaction));
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.commit();
                            tags.add(getString(R.string.admin_transaction));
                            fragments.add(new Fragments(adminTransactions, getString(R.string.admin_transaction)));
                        }
                        else {
                            tags.remove(getString(R.string.admin_transaction));
                            tags.add(getString(R.string.admin_transaction));
                        }
                        setVisibility(getString(R.string.admin_transaction));
                        return true;
                    case R.id.redeemed:
                        if (redeemedUsersFragment == null){
                            redeemedUsersFragment = new RedeemedUsersFragment();
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.frameAdmin, redeemedUsersFragment, getString(R.string.redeemed_user));
                            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            fragmentTransaction.commit();
                            tags.add(getString(R.string.redeemed_user));
                            fragments.add(new Fragments(redeemedUsersFragment, getString(R.string.redeemed_user)));
                        }
                        else {
                            tags.remove(getString(R.string.redeemed_user));
                            tags.add(getString(R.string.redeemed_user));
                        }
                        setVisibility(getString(R.string.redeemed_user));
                        return true;
                }
                return false;
            }
        });
    }

    private void initFragment() {
        if (usersFragment == null){
            usersFragment = new UsersFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.add(R.id.frameAdmin, usersFragment, getString(R.string.user_fragment));
            fragmentTransaction.commit();
            tags.add(getString(R.string.user_fragment));
            fragments.add(new Fragments(usersFragment, getString(R.string.user_fragment)));
        }
        else {
            tags.remove(getString(R.string.user_fragment));
            tags.add(getString(R.string.user_fragment));
        }
        setVisibility(getString(R.string.user_fragment));
    }

    private void setVisibility(String tag){
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
    public void onBackPressed() {
        int total = tags.size();
        if (total > 1){
            String top = tags.get(total - 1);
            String bot = tags.get(total - 2);
            setVisibility(bot);
            tags.remove(top);
            mCount = 0;
        }
        else if (total == 1){
            String top = tags.get(total - 1);
            if (top.equals(getString(R.string.user_fragment))){
                Toast.makeText(this, "End", Toast.LENGTH_SHORT).show();
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

    @Override
    public void logOut() {
        preferencesHelper.setLoggedIn(false);
        startActivity(new Intent(this, Login.class));
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void goToDetails(Users users) {
        if (detailsFragment != null){
            getSupportFragmentManager().beginTransaction().remove(detailsFragment).commitAllowingStateLoss();
        }
        detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("User", users);
        detailsFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameAdmin, detailsFragment, getString(R.string.details_fragment_name));
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.commit();
        tags.add(getString(R.string.details_fragment_name));
        fragments.add(new Fragments(detailsFragment, getString(R.string.details_fragment_name)));
        setVisibility(getString(R.string.details_fragment_name));

    }

    @Override
    public void goToHome() {
        if (usersFragment == null){
            usersFragment = new UsersFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameAdmin, usersFragment, getString(R.string.user_fragment));
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.commit();
            tags.add(getString(R.string.user_fragment));
            fragments.add(new Fragments(usersFragment, getString(R.string.user_fragment)));
        }
        else {
            tags.remove(getString(R.string.user_fragment));
            tags.add(getString(R.string.user_fragment));
        }
        setVisibility(getString(R.string.user_fragment));

    }

    @Override
    public void sendPaymentDetails(Redeem redeem) {
        if (paymentFragment != null){
            getSupportFragmentManager().beginTransaction().remove(paymentFragment).commitAllowingStateLoss();
        }
        paymentFragment = new PaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Redeem", redeem);
        paymentFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameAdmin, paymentFragment, getString(R.string.payment));
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.commit();
        tags.add(getString(R.string.payment));
        fragments.add(new Fragments(paymentFragment, getString(R.string.payment)));
        setVisibility(getString(R.string.payment));

        /**Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("Email", redeem.getEmail());
        intent.putExtra("Points", redeem.getPoints());
        startActivity(intent);**/
    }

    @Override
    public void goToSearch() {
        if (searchAdminFragment == null){
            searchAdminFragment = new SearchAdminFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameAdmin, searchAdminFragment, getString(R.string.search));
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            fragmentTransaction.commit();
            tags.add(getString(R.string.search));
            fragments.add(new Fragments(searchAdminFragment, getString(R.string.search)));
        }
        else {
            tags.remove(getString(R.string.search));
            tags.add(getString(R.string.search));
        }
        setVisibility(getString(R.string.search));
    }

}