package com.tux.dms.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.tux.dms.fragment.dashboard.AdminDashboardFragment;
import com.tux.dms.ManageUserFragment;
import com.tux.dms.R;
import com.tux.dms.ReportFragment;
import com.tux.dms.TicketAssignDrawerFragment;
import com.tux.dms.fragment.tickets.TicketCreateFragment;
import com.tux.dms.TicketSearchFragment;
import com.tux.dms.cache.SessionCache;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        SessionCache sessionCache = SessionCache.getSessionCache();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.userName);
        userName.setText("Hello " + sessionCache.getUser().getName());
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AdminDashboardFragment()).addToBackStack(null).commit();
            navigationView.setCheckedItem(R.id.ticket);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.dashboard:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AdminDashboardFragment()).addToBackStack(null).commit();

                break;
            case R.id.create_new:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TicketCreateFragment()).addToBackStack(null).commit();
                break;

            case R.id.search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TicketSearchFragment()).addToBackStack(null).commit();
                break;
            case R.id.assign_tickets:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TicketAssignDrawerFragment()).addToBackStack(null).commit();
                break;

            case R.id.this_month:

                //TODO: write code for current month report generation

                break;

            case R.id.custom_month:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ReportFragment()).addToBackStack(null).commit();
                break;

            case R.id.userListSpinner:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ManageUserFragment()).addToBackStack(null).commit();
                break;

            case R.id.logout:
                finishAndRemoveTask();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}