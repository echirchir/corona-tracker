package ke.co.simpledeveloper.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ke.co.simpledeveloper.R;
import ke.co.simpledeveloper.fragments.HomeFragment;
import ke.co.simpledeveloper.fragments.InfoFragment;
import ke.co.simpledeveloper.fragments.MapFragment;

public class CoronaListMapActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_list_map);

        toolbar = getSupportActionBar();

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        openFragment(new HomeFragment());
        toolbar.setTitle("Corona Virus Tracker");
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(new HomeFragment());
                            return true;
                        case R.id.navigation_map:
                            openFragment(new MapFragment());
                            return true;
                        case R.id.navigation_info:
                            openFragment(new InfoFragment());
                            return true;
                    }
                    return false;
                }
    };

}
