package com.niloy.roomdatabase1;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUsers extends Fragment {

    static RecyclerViewAdapter adapter;

    static ArrayList<String> readNames;
    static ArrayList<String> readAge;
    static ArrayList<String> readId;
    static ArrayList<String> readEmail;
    static ArrayList<byte[]> readImages;

    public ViewUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_users, container, false);
        showRecyclerView(view);
        return view;
    }

    public void showRecyclerView(View view) {

        List<Info> allInfos = MainActivity.database.myDao().readUsers();

        readNames = new ArrayList<>();
        readAge = new ArrayList<>();
        readId = new ArrayList<>();
        readEmail = new ArrayList<>();
        readImages = new ArrayList<>();

        for(Info i : allInfos)    {
            readNames.add(i.getName());
            readAge.add(Integer.toString(i.getAge()));
            readId.add(i.getId());
            readEmail.add(i.getEmail());
            readImages.add(i.getImage());
        }

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(getActivity() , readNames , readAge , readId , readEmail , readImages );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    //This will delete the item and update RecyclerView
    public static void deleteDataItemAndRefreshRecyclerView(final Info info , Context context)    {

        new AlertDialog.Builder(context).setTitle("Do you want to delete this item?").setIcon(R.drawable.delete)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {

                        //This is going to delete item from database
                        MainActivity.database.myDao().deleteItem(info);

                        //Now will get these data again and update recyclerview
                        List<Info> allInfos = MainActivity.database.myDao().readUsers();

                        readNames.clear();
                        readAge.clear();
                        readId.clear();
                        readEmail.clear();
                        readImages.clear();

                        for(Info i : allInfos)    {
                            readNames.add(i.getName());
                            readAge.add(Integer.toString(i.getAge()));
                            readId.add(i.getId());
                            readEmail.add(i.getEmail());
                            readImages.add(i.getImage());
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No",null).show();
    }
}
