package com.niloy.roomdatabase1;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private long currentTimeInMillis;

    public static FragmentManager fragmentManager;
    public static MyDatabase database;

    Toolbar toolbar;
    private DrawerLayout drawer;


    /*//This will add menu to the activity/fragment
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_items , menu);

        return super.onCreateOptionsMenu(menu);
    }

    //Menu item selecton
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId())   {
            case R.id.exit_menu:
                showExitAlert();
                return true;
        }
        return false;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        prepareNavigationDrawer();

        fragmentManager = getSupportFragmentManager();

        database = Room.databaseBuilder(getApplicationContext() , MyDatabase.class , "userdb").build();

        if(findViewById(R.id.fragment_container)!=null) {

            if(savedInstanceState!=null)    {
                return;
            }
            fragmentManager.beginTransaction().replace(R.id.fragment_container,new ViewUsers(),"T_Home_Fragemnt").commit();
        }

    }

    public void prepareNavigationDrawer()    {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_nav_drawer,R.string.close_nav_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.view_users);
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START))    {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(currentFragment instanceof ViewUsers) {
            if(currentTimeInMillis + 2000 > System.currentTimeMillis()) {
                finish();
            }
            else {
                currentTimeInMillis = System.currentTimeMillis();
                Toast.makeText(this, "Press Back Again To Exit!", Toast.LENGTH_SHORT).show();
            }
        }
        else
            super.onBackPressed();

    }

    //Exit Alert Dialog
    public void showExitAlert() {
        /*new AlertDialog.Builder(this).setTitle("Do you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        })
                .setNegativeButton("No" , null).show();*/
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.view_users:
                fragmentManager.beginTransaction().replace(R.id.fragment_container,new ViewUsers()).commit();
                break;
            case R.id.add_users:
                fragmentManager.beginTransaction().replace(R.id.fragment_container,new AddUserInfo()).commit();
                break;
            case R.id.about_dev:
                openLinkInBrowser("https://sites.google.com/view/alif-hasnain/home");
                break;
            case R.id.exit:
                showExitAlert();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openLinkInBrowser(String url)  {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(browserIntent);
    }
}
