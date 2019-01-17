package com.niloy.roomdatabase1;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewUsers extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    static List<Info> allInfos;

    RecyclerViewAdapter adapter;

    static ArrayList<String> readNames;
    static ArrayList<String> readAge;
    static ArrayList<String> readId;
    static ArrayList<String> readEmail;
    static ArrayList<byte[]> readImages;

    View view;

    RecyclerView recyclerView;

    private Snackbar sb;

    public ViewUsers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_users, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        changeToolbarTitle();

        if(!MainActivity.snackBarIsShown)   {
            MainActivity.snackBarIsShown = true;
            showSnackbar(view,"Click to edit\nSwipe to delete");
        }
        showRecyclerView(view);

        return view;
    }

    @Override
    public void onStop() {
        try {
            sb.dismiss();
        }
        catch (NullPointerException e)  {
            e.printStackTrace();
        }
        super.onStop();
    }

    public void showRecyclerView(View view) {

        updateRecyclerAdapterData();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        //Set Item Touch Helper
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT , this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }

    public void updateRecyclerAdapterData() {
        //List<Info> allInfos = MainActivity.database.myDao().readUsers();

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
        adapter = new RecyclerViewAdapter(getActivity() , readNames , readAge , readId , readEmail , readImages );
        if(adapter.getItemCount()==0)   {
            recyclerView.setBackgroundResource(R.drawable.empty_data);
        }
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

        Info temp = allInfos.get(position);

        deleteWithSwipeAndRefresh(temp ,getContext() , position);
    }


    public void deleteWithSwipeAndRefresh(final Info info , Context context , final int position)  {

        DatabaseOperationInThread.deleteData(info);

        //Delete Item From the position
        readNames.remove(position);
        readAge.remove(position);
        readId.remove(position);
        readEmail.remove(position);
        readImages.remove(position);
        adapter.notifyDataSetChanged();



        sb = Snackbar.make(view , "Item Deleted!" , Snackbar.LENGTH_LONG)
                .setAction("Undo!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseOperationInThread.addData(info);
                        readNames.add(position , info.getName());
                        readAge.add(position , String.valueOf(info.getAge()));
                        readId.add(position , info.getId());
                        readEmail.add(position , info.getEmail());
                        readImages.add(position,info.getImage());
                        adapter.notifyDataSetChanged();
                    }
                });

        /*sb.addCallback(new Snackbar.Callback()  {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if(event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT)    {
                    //This is going to delete item from database
                    //MainActivity.database.myDao().deleteItem(info);
                    adapter.notifyItemRemoved(position);
                }
            }
        });*/
        sb.show();

        if(adapter.getItemCount()==0)   {
            recyclerView.setBackgroundResource(R.drawable.empty_data);
        }
    }

    private void showSnackbar(View view,String msg) {
        sb = Snackbar.make(view,msg,10000).setAction("Okay", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sb.show();
    }

    private void changeToolbarTitle()    {
        try {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("All Information");
        }
        catch (Exception e )    {
            e.printStackTrace();
        }
    }
}
