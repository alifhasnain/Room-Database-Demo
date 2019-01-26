package com.niloy.roomdatabase1;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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
    Bitmap profileImageBitmap;
    AlertDialog dialog;

    String randomFileName;

    public AddUserInfo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_user_info, container, false);

        changeToolbarTitle();

        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        id = view.findViewById(R.id.id);
        email = view.findViewById(R.id.email_address);
        save = view.findViewById(R.id.save);
        profile = view.findViewById(R.id.profile_image);

        randomFileName = generateRandomSting();

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
                    //Add Image Name
                    userInfos.setImageName(randomFileName);

                    //MainActivity.database.myDao().addInfos(userInfos);
                    DatabaseOperationInThread.addData(userInfos);

                    saveBitmapOnInternalStorage();

                    //This will hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    Snackbar.make(view , "Data Added Successfully" , 2000).show();

                    name.getEditText().setText("");
                    age.getEditText().setText("");
                    id.getEditText().setText("");
                    email.getEditText().setText("");
                    profile.setImageResource(R.drawable.upload);

                    //For generating random file name again
                    randomFileName = generateRandomSting();
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
                    Uri imageUri = data.getData();
                    try {
                        profileImageBitmap = scaledBitmap(MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageUri),999f,true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profile.setImageBitmap(profileImageBitmap);
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

    /*public void resizeAndSetImage(Uri imageUri)    {
        try {
            Bitmap myImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , imageUri);
            int h = myImage.getHeight();
            int w = myImage.getWidth();
            Bitmap resizedImage = Bitmap.createScaledBitmap(myImage , w/3 , h/3 , true);
            profile.setImageBitmap(resizedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static Bitmap scaledBitmap(Bitmap realImage,Float maxImageSizeInKb , boolean filter)    {
        float ratio = Math.min(
                maxImageSizeInKb / realImage.getWidth(),
                maxImageSizeInKb / realImage.getHeight());
        int width = Math.round( ratio * realImage.getWidth());
        int height = Math.round( ratio * realImage.getHeight());

        return  Bitmap.createScaledBitmap(realImage, width,
                height, filter);
    }

    private void saveBitmapOnInternalStorage()   {
        File imageFileDir = new File(MainActivity.defaultProfileImageDir,randomFileName);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(imageFileDir);
            profileImageBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String generateRandomSting()   {
        String ALPHABETS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        while (str.length()<=20)    {
            int index = (int)(random.nextFloat()*ALPHABETS.length());   //Used float for better random
            str.append(ALPHABETS.charAt(index));
        }
        return str.toString();
    }

    private void changeToolbarTitle()    {
        try {
            Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
            toolbar.setTitle("Add Information");
        }
        catch (Exception e )    {
            e.printStackTrace();
        }
    }
}
