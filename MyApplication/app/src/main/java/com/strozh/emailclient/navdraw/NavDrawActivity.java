package com.strozh.emailclient.navdraw;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.strozh.emailclient.R;
import com.strozh.emailclient.fragments.Inbox.InboxFragmentActivity;
import com.strozh.emailclient.login.LoginActivity;

public class NavDrawActivity extends MvpActivity<NavDrawView, NavDrawPresenter> implements NavDrawView, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_draw);
        //init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init FAB
        initFAB();
        //init drawer view
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setTitle("Email client 1.0");
        //set userName in header
        setUserNameInHeader(navigationView, getPresenter().getUserName());

        if (savedInstanceState == null) {
            MenuItem item = navigationView.getMenu().getItem(0);
            onNavigationItemSelected(item);
        }
    }

    @NonNull
    @Override
    public NavDrawPresenter createPresenter() {
        return new NavDrawPresenterImpl(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            singOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void singOut() {
        getApplicationContext().deleteFile("messages.ser");
        Intent intent = new Intent(this, LoginActivity.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean("isLogin", false).commit();
        startActivity(intent);
        this.finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_inbox:
                fragment = new InboxFragmentActivity();
                break;
            case R.id.nav_sent:
                //TODO: make sent fragment
                break;
            case R.id.nav_NewMail:
                //TODO: make new mail fragment
                break;
            case R.id.nav_exit:
                //TODO: make function of exit
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            //Set menuItem checked and title
            item.setChecked(true);
            //set action bar tittle
            setTitle(item.getTitle());
            //close the navigation drawer
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.e("EmailClient", "Error to create a fragment");
        }
        return false;
    }

    public void initFAB() {
        //init FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void setUserNameInHeader(NavigationView navigationView, String userNameInHeader) {
        final View header = navigationView.getHeaderView(0);
        final TextView userNameInHeaderTV = (TextView) header.findViewById(R.id.userNameInHeader);

        try {
            userNameInHeaderTV.setText(userNameInHeader);
            userNameInHeaderTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("", userNameInHeaderTV.getText().toString());
                    clipboardManager.setPrimaryClip(clipData);
                }
            });
        } catch (Exception e) {
            Log.e("EmailClient", "UserName: " + userNameInHeader, e);
        }
    }
}




