package com.gnufsociety.openchallenge;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private Button regBtn;



    private EditText emailEdit, passEdit, confPassEdit;
    private FirebaseAuth auth;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        regBtn = (Button) view.findViewById(R.id.reg_btn);

        emailEdit = (EditText) view.findViewById(R.id.email_reg);
        passEdit = (EditText) view.findViewById(R.id.password_reg);
        confPassEdit = (EditText) view.findViewById(R.id.confirm_pass_reg);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        auth = FirebaseAuth.getInstance();
        auth.signOut();

        return view;
    }

    public void registerUser(){
        final String email = emailEdit.getText().toString();
        final String pass = passEdit.getText().toString();
        final String confPass = confPassEdit.getText().toString();

        if (!pass.equals(confPass)){
            Toast.makeText(getContext(),"Le password non corrispondono!", Toast.LENGTH_LONG).show();
            return;
        }
        //create new account and start main activity with boolean new set to true, so it will show the configuration activity
        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Complimenti ti sei registrato stronzo",Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(getContext(), MainActivity.class);
                            Bundle extra = new Bundle();
                            extra.putBoolean("new", true);
                            in.putExtras(extra);
                            startActivity(in);
                        }
                    }
                });

    }

}