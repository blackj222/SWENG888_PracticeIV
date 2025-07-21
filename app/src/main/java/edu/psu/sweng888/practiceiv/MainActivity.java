package edu.psu.sweng888.practiceiv;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.psu.sweng888.practiceiv.fragments.AccountFragment;
import edu.psu.sweng888.practiceiv.fragments.ItemsFragment;
import edu.psu.sweng888.practiceiv.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;

    // Instead of switch cases with R.id, define your own menu keys
    private static final int MENU_ITEMS = 1;
    private static final int MENU_ACCOUNT = 2;
    private static final int MENU_SETTINGS = 3;
    private static final int MENU_LOGOUT = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.textViewUserName);
        TextView navUserEmail = headerView.findViewById(R.id.textViewUserEmail);

        if (user != null) {
            navUserEmail.setText(user.getEmail());
            navUserName.setText("Welcome!");
        }

        // Default fragment
        if (savedInstanceState == null) {
            loadFragment(MENU_ITEMS);
            navigationView.setCheckedItem(R.id.nav_items);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Map the R.id to our own menu key constants
        int menuKey = getMenuKey(item.getItemId());
        handleMenuAction(menuKey);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private int getMenuKey(int menuId) {
        // This method maps R.id to our internal constants
        if (menuId == R.id.nav_items) return MENU_ITEMS;
        if (menuId == R.id.nav_account) return MENU_ACCOUNT;
        if (menuId == R.id.nav_settings) return MENU_SETTINGS;
        if (menuId == R.id.nav_logout) return MENU_LOGOUT;
        return -1;
    }

    private void handleMenuAction(int menuKey) {
        Fragment selectedFragment = null;

        if (menuKey == MENU_ITEMS) {
            selectedFragment = new ItemsFragment();
        } else if (menuKey == MENU_ACCOUNT) {
            selectedFragment = new AccountFragment();
        } else if (menuKey == MENU_SETTINGS) {
            selectedFragment = new SettingsFragment();
        } else if (menuKey == MENU_LOGOUT) {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        if (selectedFragment != null) {
            loadFragment(menuKey);
        }
    }

    private void loadFragment(int menuKey) {
        Fragment fragment = null;
        if (menuKey == MENU_ITEMS) {
            fragment = new ItemsFragment();
        } else if (menuKey == MENU_ACCOUNT) {
            fragment = new AccountFragment();
        } else if (menuKey == MENU_SETTINGS) {
            fragment = new SettingsFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
