package com.niloy.roomdatabase1;


import android.content.Context;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserInfo extends Fragment {

    EditText name;
    EditText age;
    EditText id;
    EditText email;

    Button save;

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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = name.getText().toString();

                int userAge;

                try {
                    userAge = Integer.parseInt(age.getText().toString());
                }
                catch (Exception e) {
                    userAge = 0;
                    e.printStackTrace();
                }

                String userId = id.getText().toString();
                String userMail = email.getText().toString();

                if(!userName.isEmpty() && userAge>0 && !userId.isEmpty() && !userMail.isEmpty())    {

                    Info userInfos = new Info();

                    userInfos.setName(userName);
                    userInfos.setAge(userAge);
                    userInfos.setId(userId);
                    userInfos.setEmail(userMail);

                    MainActivity.database.myDao().addInfos(userInfos);

                    //This will hide keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

                    Snackbar.make(view , "Data Added Successfully" , 2000).show();

                    name.setText("");
                    age.setText("");
                    id.setText("");
                    email.setText("");
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

}
