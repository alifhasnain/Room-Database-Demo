package com.niloy.roomdatabase1;


import android.content.Context;
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
public class EditInfo extends Fragment {

    EditText name;
    EditText age;
    EditText id;
    EditText email;

    Button update;

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

        update = view.findViewById(R.id.update);

        final Info info = RecyclerViewAdapter.temp;

        name.setText(info.getName());
        age.setText(Integer.toString(info.getAge()));
        id.setText(info.getId());
        email.setText(info.getEmail());

        id.setEnabled(false);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Info temp = new Info(name.getText().toString() , Integer.parseInt(age.getText().toString()) , id.getText().toString() , email.getText().toString());
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

}
