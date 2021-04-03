package com.usf_mobile_dev.filmfriend;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.usf_mobile_dev.filmfriend.ui.qr.QrActivity;
//import com.usf_mobile_dev.filmfriend.ui.savedPreferences.PreferencesActivity;
import com.usf_mobile_dev.filmfriend.ui.settings.Settings;

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
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int nav_id = item.getItemId();

        // Handle navigation view item clicks here.
        switch (nav_id) {
            case R.id.settings_about:
                Toast.makeText(getApplicationContext(), "About pressed!",
                        Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);

                Settings.openPopUp(this, (String) item.getTitle());

                return true;

            case R.id.settings_credit:
                Toast.makeText(getApplicationContext(), "Credit pressed!",
                        Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(GravityCompat.START);

                Settings.openPopUp(this, (String) item.getTitle());

                return true;

            default:
                // do nothing
        }

        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    public void launchTutorial(View view) {
        Tutorial t = new Tutorial();
        t.launchTutorial(view);
    }
}