package com.example.profile.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.profile.R;
import com.example.profile.fragment.User_home;
import com.example.profile.fragment.User_profile;
import com.example.profile.fragment.You;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class profileActivity extends AppCompatActivity {



    Fragment fragment;
    public static int ac = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setSelectedItemId(R.id.home);


        final Fragment fragment1 = new User_home();
//        final Fragment fragment3 = new New();
        final Fragment fragment2 = new User_profile();
        final Fragment fragment4 = new You();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rlContainer, new User_home())
                .commit();
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {


                    case R.id.home:
                        // do something here
                        fragment = fragment1;
                        replaceFragment(fragment);
                        return true;
                    case R.id.profile:
                        fragment = fragment2;
                        // do something here
                        replaceFragment(fragment);
                        return true;


                    case R.id.You:
                        fragment = fragment4;
                        // do something here
                        replaceFragment(fragment);
                        return true;

//                    case R.id.New:
//                        fragment = fragment3;
//                        // do something here
//                        replaceFragment(fragment);
//                        return true;

                    default: return true;
                }
            }
        });
    }



//    private void gotoProfile() {
//        getFragmentManager().beginTransaction()
//                .replace(R.id.container,  User_home())
//                .addToBackStack(profileActivity.class.getSimpleName())
//                .commit();
//
//    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rlContainer,fragment);
        fragmentTransaction.commit();
    }
}
