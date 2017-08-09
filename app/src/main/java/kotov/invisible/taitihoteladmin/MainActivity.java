package kotov.invisible.taitihoteladmin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import kotov.invisible.taitihoteladmin.dialogs.AddOrderDialog;
import kotov.invisible.taitihoteladmin.fragments.OrdersFragment;
import kotov.invisible.taitihoteladmin.fragments.RequestsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RequestsFragment requestsFragment;
    OrdersFragment ordersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kotov.invisible.taitihoteladmin.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(kotov.invisible.taitihoteladmin.R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(kotov.invisible.taitihoteladmin.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addOrderDialog = new AddOrderDialog();
                addOrderDialog.show(getSupportFragmentManager(), "addOrder");
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(kotov.invisible.taitihoteladmin.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, kotov.invisible.taitihoteladmin.R.string.navigation_drawer_open, kotov.invisible.taitihoteladmin.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(kotov.invisible.taitihoteladmin.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(kotov.invisible.taitihoteladmin.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(kotov.invisible.taitihoteladmin.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == kotov.invisible.taitihoteladmin.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == kotov.invisible.taitihoteladmin.R.id.nav_orders) {
            ordersFragment = new OrdersFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(kotov.invisible.taitihoteladmin.R.id.fragment_container, ordersFragment, "orders")
                    .commit();

        } else if (id == kotov.invisible.taitihoteladmin.R.id.nav_requests) {
            requestsFragment = new RequestsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(kotov.invisible.taitihoteladmin.R.id.fragment_container, requestsFragment, "requests")
                    .commit();

        } else if (id == kotov.invisible.taitihoteladmin.R.id.nav_share) {

        } else if (id == kotov.invisible.taitihoteladmin.R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(kotov.invisible.taitihoteladmin.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
