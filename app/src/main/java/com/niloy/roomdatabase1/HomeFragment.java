package com.niloy.roomdatabase1;


import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener  {


    private static final String TAG = "HomeFragment";


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button add = view.findViewById(R.id.add);
        Button viewUsers = view.findViewById(R.id.btn_view);

        add.setOnClickListener(this);
        viewUsers.setOnClickListener(this);
        askPermission();
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())   {
            case R.id.add:
                Log.d(TAG , "Add Called");
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container , new AddUserInfo())
                        .addToBackStack(null).commit();
                break;
            case R.id.btn_view:
                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container , new ViewUsers())
                        .addToBackStack(null).commit();
                Log.d(TAG , "View Called");
                break;
        }

    }

    public void askPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions( getActivity() , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
}
