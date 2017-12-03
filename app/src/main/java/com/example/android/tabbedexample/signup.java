package com.example.android.tabbedexample;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * Created by mukund on 12/2/2017.
 */

public class signup extends Fragment {
    TextView signinlink;
    Fragment fr;
    String signup_email;
    String signup_password;
    String signup_reenterpassword;
    EditText esignup_reenterpassword;
    TextView signupe;
    FragmentManager fm;
    EditText esignup_email,esignup_password;
    FragmentTransaction ft;
    View signupLayout;
    private Fragment currentFragment;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

         signupLayout= inflater.inflate(R.layout.signup,container,false);
        signinlink=(TextView) signupLayout.findViewById(R.id.signinlink);
        signinlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fr = new signin();
                fm = getFragmentManager();
                if (fm !=null) {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, fr);
                    ft.commit();
                }

            }
        });
        //.........................
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Intent myIntent = new Intent(getActivity(), Home.class);
                    getActivity().startActivity(myIntent);
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        signupe = (TextView)signupLayout.findViewById(R.id.signupe);

        signupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checksignup();
            }
        });


        return signupLayout;






    }
    public void checksignup(){

        esignup_email = (EditText) signupLayout.findViewById(R.id.signup_email);
        esignup_password = (EditText) signupLayout.findViewById(R.id.signup_password);
        signup_email = esignup_email.getText().toString();
        signup_password = esignup_password.getText().toString();
        esignup_reenterpassword =(EditText) signupLayout.findViewById(R.id.signup_renterpassword);
        signup_reenterpassword =esignup_reenterpassword.getText().toString();
        if(signup_email.matches("") || signup_password.matches("")){
            Toast.makeText(getActivity(), "You cannot leave email or password field empty",
                    Toast.LENGTH_SHORT).show();

        }
        else {
                if(signup_reenterpassword.equals(signup_password)) {

                    mAuth.createUserWithEmailAndPassword(signup_email, signup_password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Authorization failed",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
                else{
                    Toast.makeText(getActivity(), "Renter your Password",
                            Toast.LENGTH_SHORT).show();
                    esignup_reenterpassword.setText("");
                    esignup_password.setText("");

                }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
