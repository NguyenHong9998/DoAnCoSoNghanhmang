package com.example.multiplechoiceexaminationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import dashboard.DashBoard;
import login.Login;
import utils.ShareStorageUtil;
import Object.*;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    ShareStorageUtil shareStorageUtil;
    TextView name;
    ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        shareStorageUtil = new ShareStorageUtil(getApplicationContext());
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        name = navigationView.getHeaderView(0).findViewById(R.id.name);
        avatar = navigationView.getHeaderView(0).findViewById(R.id.userImage);
        loadFragment(new DashBoard());
        String token = shareStorageUtil.getValue("token");
        if (token != "") {
            navigationView.getMenu().getItem(0).setVisible(false);
            Gson gson = new Gson();
            Student student = gson.fromJson(token, Student.class);
            name.setText(student.getName());
            avatar.setImageResource(R.mipmap.user1);
        } else {
            navigationView.getMenu().getItem(3).setVisible(false);
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Fragment fragment = null;
                switch (id) {
                    case R.id.login:
                        fragment = new Login();
                        loadFragment(fragment);
                        break;
                    case R.id.dashboard:

                        fragment = new DashBoard();
                        loadFragment(fragment);

                        break;
                    case R.id.logout:
                        shareStorageUtil.applyValue("token", "");
                        navigationView.getMenu().getItem(0).setVisible(true);
                        navigationView.getMenu().getItem(3).setVisible(false);
                        fragment = new Login();
                        loadFragment(fragment);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).commitAllowingStateLoss();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }
}