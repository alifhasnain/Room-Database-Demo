package com.niloy.roomdatabase1;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUsers extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private static final String TAG = "ViewUsers";

    static RecyclerViewAdapter adapter;

    static ArrayList<String> readNames;
    static ArrayList<String> readAge;
    static ArrayList<String> readId;
    static ArrayList<String> readEmail;
    static ArrayList<byte[]> readImages;

    View view;

    RecyclerView recyclerView;

    public ViewUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_users, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        showRecyclerView(view);

        return view;
    }

    public void showRecyclerView(View view) {

        //List<Info> allInfos = MainActivity.database.myDao().readUsers();

        List<Info> allInfos = null;

        try {
            allInfos = DatabaseOperationInThread.readAllUsers();
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "No Data Available!", Toast.LENGTH_SHORT).show();
        }

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

        for(Info i : allInfos)    {
            Log.d(TAG, "showRecyclerView: duck");
            System.out.println(i.getImage());
            System.out.println(i.getAge());
            System.out.println(i.getEmail());
        }

        Toast.makeText(getActivity(), "Click To Edit\nSwipe Delete", Toast.LENGTH_SHORT).show();
        adapter = new RecyclerViewAdapter(getActivity() , readNames , readAge , readId , readEmail , readImages );
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        //Set Item Touch Helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT , this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    /*//This will delete the item and update RecyclerView
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
    }*/

    //This will delete items on swiped
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        Info temp = new Info();
        temp.setName(readNames.get(position));
        temp.setAge(Integer.parseInt(readAge.get(position)));
        temp.setId(readId.get(position));
        temp.setEmail(readEmail.get(position));
        temp.setImage(readImages.get(position));

        deleteWithSwipeAndRefresh(temp ,getContext() , position);
    }


    public void deleteWithSwipeAndRefresh(final Info info , Context context , final int position)  {

        //Delete Item From the position
        readNames.remove(position);
        readAge.remove(position);
        readId.remove(position);
        readEmail.remove(position);
        readImages.remove(position);
        adapter.notifyDataSetChanged();



        Snackbar sb = Snackbar.make(recyclerView , "Item Deleted!" , Snackbar.LENGTH_LONG)
                .setAction("Undo!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        readNames.add(position , info.getName());
                        readAge.add(position , String.valueOf(info.getAge()));
                        readId.add(position , info.getId());
                        readEmail.add(position , info.getEmail());
                        readImages.add(position,info.getImage());
                        adapter.notifyDataSetChanged();
                    }
                });

        sb.addCallback(new Snackbar.Callback()  {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT)    {
                    //This is going to delete item from database
                    //MainActivity.database.myDao().deleteItem(info);
                    DatabaseOperationInThread.deleteData(info);
                    adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(position);
                }
            }
        });
        sb.show();
    }
}
