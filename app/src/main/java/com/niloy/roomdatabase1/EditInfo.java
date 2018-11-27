package com.niloy.roomdatabase1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditInfo extends Fragment {

    TextInputLayout name;
    TextInputLayout age;
    TextInputLayout id;
    TextInputLayout email;
    ImageView vImageView;

    byte[] image;

    Button update;
    AlertDialog dialog;

    public EditInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit_info, container, false);

        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        id = view.findViewById(R.id.id);
        email = view.findViewById(R.id.email_address);
        vImageView = view.findViewById(R.id.profile_image);

        update = view.findViewById(R.id.update);

        final Info info = RecyclerViewAdapter.temp;

        name.getEditText().setText(info.getName());
        age.getEditText().setText(Integer.toString(info.getAge()));
        id.getEditText().setText(info.getId());
        email.getEditText().setText(info.getEmail());
        //Set ImageView
        image = info.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image , 0 , image.length);
        vImageView.setImageBitmap(bitmap);


        vImageView.setOnClickListener(new View.OnClickListener() {

            String[] items = {"Camera", "Gallery"};

            @Override
            public void onClick(View view) {

                AlertDialog.Builder mDialog = new AlertDialog.Builder(getContext());
                mDialog.setTitle("Select an option");
                mDialog.setSingleChoiceItems(items , -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(getActivity() , ImagePicker.class);

                        if(items[i].equals("Camera"))   {
                            intent.putExtra("choice" , 0);
                        }
                        else {
                            intent.putExtra("choice" , 1);
                        }

                        startActivityForResult(intent , 2);
                    }
                });
                dialog = mDialog.create();
                dialog.show();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Info temp = new Info(name.getEditText().getText().toString() , Integer.parseInt(age.getEditText().getText().toString()) , id.getEditText().getText().toString() , email.getEditText().getText().toString() , image);
                MainActivity.database.myDao().updateItem(temp);

                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken() , 0);

                //For snackbar
                Snackbar.make(view , "Information Successfully Updated!" , 2000).show();

                //ViewUsers.adapter.notifyDataSetChanged();

            }
        });

        return view;
    }

    public byte[] convertToByteArray(ImageView imageView)   {
        byte[] image;
        Bitmap imageBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

        image = baos.toByteArray();

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri imageUri;
        switch (requestCode)
        {
            case 2:
                if(resultCode==RESULT_OK)   {
                    imageUri = data.getData();
                    resizeAndSetImage(imageUri);
                    //profile.setImageURI(imageUri);
                }
                else if(resultCode==420) {
                    Toast.makeText(getContext() , "No image was selected!", Toast.LENGTH_SHORT).show();
                }
                if(dialog.isShowing())  {
                    dialog.cancel();
                }
                break;
            default:
                break;
        }
    }

    public void resizeAndSetImage(Uri imageUri)    {
        String path = "";
        try {
            Bitmap myImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , imageUri);
            int h = myImage.getHeight();
            int w = myImage.getWidth();
            Bitmap resizedImage = Bitmap.createScaledBitmap(myImage , w/3 , h/3 , true);
            vImageView.setImageBitmap(resizedImage);
            //To update the value of image
            image = convertToByteArray(vImageView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
