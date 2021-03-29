package com.usf_mobile_dev.filmfriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView settings_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up drawer layout for ham_menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
                    @Override
                    public void onDrawerOpened(View view) {
                        super.onDrawerOpened(view);
                    }
                    @Override
                    public void onDrawerClosed(View view) {
                        super.onDrawerClosed(view);
                    }
        };
        drawer.bringToFront();
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        settings_nav = (NavigationView) findViewById(R.id.settings_nav_view);
        settings_nav.setNavigationItemSelectedListener(this);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_history,
                R.id.navigation_match,
                R.id.navigation_discover)
                .build();
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(
                this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    /**
     * Handles the Back button: closes the nav drawer.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * Handles a navigation drawer item click. It detects which item was
     * clicked and displays a toast message showing which item.
     * @param item  Item in the navigation drawer
     * @return      Returns true after closing the nav drawer
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int nav_id = item.getItemId();

        // Clear checks
        for (int i = 0; i < settings_nav.getMenu().size(); i++) {
            settings_nav.getMenu().getItem(i).setChecked(false);
        }
        settings_nav.getMenu().findItem(nav_id).setChecked(true);

        // Handle navigation view item clicks here.
        switch (nav_id) {
            case R.id.settings_about:
                // Handle the camera import action (for now display a toast).
                Toast.makeText(getApplicationContext(),item.getTitle(), Toast.LENGTH_SHORT).show();



                break;
            case R.id.settings_credit:
                // Handle the camera import action (for now display a toast).
                Toast.makeText(getApplicationContext(),item.getTitle(), Toast.LENGTH_SHORT).show();



                break;
        }
        // If option not selected, close drawer anyway.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}