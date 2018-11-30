package com.niloy.roomdatabase1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
public class AddUserInfo extends Fragment {

    TextInputLayout name;
    TextInputLayout age;
    TextInputLayout id;
    TextInputLayout email;
    ImageView profile;

    Button save;
    Uri imageUri;
    AlertDialog dialog;

    public AddUserInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user_info, container, false);

        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        id = view.findViewById(R.id.id);
        email = view.findViewById(R.id.email_address);
        save = view.findViewById(R.id.save);
        profile = view.findViewById(R.id.profile_image);

        profile.setOnClickListener(new View.OnClickListener() {

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



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = name.getEditText().getText().toString().trim();

                int userAge;

                try {
                    userAge = Integer.parseInt(age.getEditText().getText().toString());
                }
                catch (Exception e) {
                    userAge = 0;
                    e.printStackTrace();
                }

                String userId = id.getEditText().getText().toString();
                String userMail = email.getEditText().getText().toString();

                if(!userName.isEmpty() && userName.length()<25 && userAge>0 && !userId.isEmpty() && !userMail.isEmpty() && profile!=null)    {

                    Info userInfos = new Info();

                    userInfos.setName(userName);
                    userInfos.setAge(userAge);
                    userInfos.setId(userId);
                    userInfos.setEmail(userMail);
                    //Add Image
                    Bitmap bitmap = ((BitmapDrawable) profile.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageInByte = baos.toByteArray();
                    userInfos.setImage(imageInByte);

                    //MainActivity.database.myDao().addInfos(userInfos);
                    DatabaseOperationInThread.addData(userInfos);

                    //This will hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    Snackbar.make(view , "Data Added Successfully" , 2000).show();

                    name.getEditText().setText("");
                    age.getEditText().setText("");
                    id.getEditText().setText("");
                    email.getEditText().setText("");
                    profile.setImageResource(R.drawable.profile);

                }
                else    {
                    //This will hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                    Snackbar.make(view , "Incorrect Entry!" , 2000).show();
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            profile.setImageBitmap(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
