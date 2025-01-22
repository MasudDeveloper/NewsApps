package com.mrdeveloper.newsapps;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.SourcesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.kwabenaberko.newsapilib.models.response.SourcesResponse;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navView;
    DrawerLayout drawerLayout;

    ToggleButton toggleButton;

    String LANGUAGE_PREF_KEY = "Radio_Key";

    public static String LANGUAGE = "";

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        toolbar = findViewById(R.id.toolBar);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        navView = findViewById(R.id.navView);
        drawerLayout = findViewById(R.id.main);

        toggleButton = navView.getHeaderView(0).findViewById(R.id.toggleButton);


        ViewCompat.setOnApplyWindowInsetsListener(bottomNavigationView, (v, insets) -> {
            insets.consumeSystemWindowInsets();
            return insets;
        });

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //========================================

        drawerNavigationControl();


        replaceFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.home) {
                    replaceFragment(new HomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.favourite) {
                    replaceFragment(new FavouritesFragment());
                    return true;
                } else if (item.getItemId() == R.id.discover) {
                    replaceFragment(new FavouritesFragment());
                    return true;
                } else if (item.getItemId() == R.id.profile){
                    replaceFragment(new FavouritesFragment());
                    return true;
                }

                return false;
            }
        });



    }

    private void drawerNavigationControl() {

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean(LANGUAGE_PREF_KEY,false);
        LANGUAGE = isChecked ? "bn":"en";
        toggleButton.setChecked(isChecked);

        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
                if (key.equals(LANGUAGE_PREF_KEY)) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
                    if (currentFragment instanceof HomeFragment){
                        ((HomeFragment) currentFragment).loadData();
                    }
                }
            }
        };

        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(LANGUAGE_PREF_KEY,b);
                editor.apply();

                // Display a toast message for user feedback
                String language = b ? "বাংলা" : "English";
                Toast.makeText(MainActivity.this, "Selected Language: " + language, Toast.LENGTH_SHORT).show();
                LANGUAGE = b ? "bn":"en";
            }
        });



    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment,null);
        fragmentTransaction.commit();

    }

}