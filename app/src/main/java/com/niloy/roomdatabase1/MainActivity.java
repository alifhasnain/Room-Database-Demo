package com.niloy.roomdatabase1;


import androidx.fragment.app.Fragment;
import androidx.room.Room;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private long currentTimeInMillis;

    public static FragmentManager fragmentManager;
    public static MyDatabase database;


    //This will add menu to the activity/fragment
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        database = Room.databaseBuilder(getApplicationContext() , MyDatabase.class , "userdb").build();

        if(findViewById(R.id.fragment_container)!=null) {

            if(savedInstanceState!=null)    {
                return;
            }
            fragmentManager.beginTransaction().add(R.id.fragment_container,new HomeFragment(),"T_Home_Fragemnt").commit();
        }

    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(currentFragment instanceof HomeFragment) {
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
}
