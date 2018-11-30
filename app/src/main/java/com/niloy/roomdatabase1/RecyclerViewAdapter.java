package com.niloy.roomdatabase1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>   {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mNames;
    private ArrayList<String> mAges;
    private ArrayList<String> mId;
    private ArrayList<String> emails;
    private ArrayList<byte[]> imageByteArrays;
    RelativeLayout foreground,background;

    static Info temp;

    private Context mContext ;

    //This is the constructor
    public RecyclerViewAdapter(Context mContext , ArrayList<String> mNames, ArrayList<String> mAges, ArrayList<String> mId, ArrayList<String> emails , ArrayList<byte[]> images) {
        this.mContext = mContext;
        this.mNames = mNames;
        this.mAges = mAges;
        this.mId = mId;
        this.emails = emails;
        this.imageByteArrays = images;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_items,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        Log.d(TAG,"onBindViewHolder: called.");

        //Convert byte[] to imageView
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArrays.get(i) ,0 , imageByteArrays.get(i).length);

        viewHolder.name.setText(mNames.get(i));
        viewHolder.age.setText("Age : " + mAges.get(i));
        viewHolder.id.setText("ID : " + mId.get(i));
        viewHolder.email.setText("Email : " + emails.get(i));
        viewHolder.profile.setImageBitmap(bitmap);

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //temp = new Info(mNames.get(i) , Integer.parseInt(mAges.get(i)) , mId.get(i) , emails.get(i) , imageByteArrays.get(i) );
                temp = ViewUsers.allInfos.get(i);

                MainActivity.fragmentManager.beginTransaction().replace(R.id.fragment_container , new EditInfo())
                        .addToBackStack(null).commit();

            }
        });

        /*viewHolder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Info info = new Info();
                info.setId(mId.get(i));
                ViewUsers.deleteDataItemAndRefreshRecyclerView(info , mContext);
                return true;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView age;
        TextView id;
        TextView email;
        ImageView profile;

        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            id = itemView.findViewById(R.id.id);
            email = itemView.findViewById(R.id.email);
            profile = itemView.findViewById(R.id.profile_image);
            background = itemView.findViewById(R.id.background_delete_ic_area);

            relativeLayout = itemView.findViewById(R.id.relative_layout);
        }
    }
}