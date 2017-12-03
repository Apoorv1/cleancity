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

public class signin extends Fragment {

    TextView signuplink;
    Fragment fr;
    String signin_email,signin_password;
    TextView signine;
    FragmentManager fm;
    FragmentTransaction ft;
    Fragment currentFragment;
    View signinLayout;
    EditText esignin_email,esignin_password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

         signinLayout= inflater.inflate(R.layout.signin,container,false);
        signuplink=(TextView) signinLayout.findViewById(R.id.signuplink);
        signuplink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                fr = new signup();
                fm = getFragmentManager();
                if(fm!=null) {
                    ft = fm.beginTransaction();
                    ft.replace(R.id.fragment_container, fr);
                    ft.commit();
                }

            }
        });
        //.................


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Intent myIntent = new Intent(getActivity(), Home.class);
                    getActivity().startActivity(myIntent);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        signine = (TextView)signinLayout.findViewById(R.id.signine);

        signine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checksignin();
            }
        });




        return signinLayout;

    }
    public void checksignin(){

        esignin_email =(EditText)signinLayout.findViewById(R.id.signin_email);
        signin_email = esignin_email.getText().toString();
        esignin_password = (EditText) signinLayout.findViewById(R.id.signin_password);
        signin_password = esignin_password.getText().toString();
        Log.v("TAG","signin_email="+signin_email);
        Log.v("TAG","signin_password="+signin_password);
        if(signin_email.matches("") || signin_password.matches("")){
            Toast.makeText(getActivity(),"You cannot leave email or password field empty",
                    Toast.LENGTH_SHORT).show();

        }
        else {
                mAuth.signInWithEmailAndPassword(signin_email,signin_password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(getActivity(),"Failed to sign in",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
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
