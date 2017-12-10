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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * Created by mukund on 12/2/2017.
 */

public class signup extends AppCompatActivity {
    TextView signinlink;
    String signup_email;
    String signup_password;
    String signup_reenterpassword;
    EditText esignup_reenterpassword;
    TextView signupe;
    FragmentManager fm;
    EditText esignup_email,esignup_password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    GoogleSignInClient mGoogleSignInClient;
    TextView signupg;
    int RC_SIGN_IN =1000;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

     getSupportActionBar().hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        signinlink=(TextView)findViewById(R.id.signinlink);
        signupg = (TextView) findViewById(R.id.signupg);
        signinlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               startActivity(new Intent(signup.this,signin.class));
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
                    sendVerificationEmail();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //startActivity(new Intent(signup.this,signin.class));


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        signupe = (TextView)findViewById(R.id.signupe);

        signupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checksignup();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();


        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateui(account);//perform actions according to account object
        signupg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }


    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                updateui(account);
            }
            catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
                updateui(null);
            }
        }

    }
    public void checksignup(){

        esignup_email = (EditText)findViewById(R.id.signup_email);
        esignup_password = (EditText) findViewById(R.id.signup_password);
        signup_email = esignup_email.getText().toString();
        signup_password = esignup_password.getText().toString();
        esignup_reenterpassword =(EditText)findViewById(R.id.signup_renterpassword);
        signup_reenterpassword =esignup_reenterpassword.getText().toString();
        if(signup_email.matches("") || signup_password.matches("")){
            Toast.makeText(this, "You cannot leave email or password field empty",
                    Toast.LENGTH_SHORT).show();

        }
        else {
                if(signup_reenterpassword.equals(signup_password)) {

                    mAuth.createUserWithEmailAndPassword(signup_email, signup_password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(signup.this , "Authorization failed" , Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
                else{
                    Toast.makeText(this, "Renter your Password",
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
    public void updateui(GoogleSignInAccount user)
    {
        if(user!=null)
        {
            Intent myIntent = new Intent(this, Home.class);
            this.startActivity(myIntent);

        }

    }


    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(signup.this,"check your inbox and verify email",Toast.LENGTH_SHORT).show();
                          startActivity(new Intent(signup.this,signin.class));
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            Toast.makeText(signup.this,"email not sent RETRY AGAIN",Toast.LENGTH_SHORT).show();

                            //restart this activity
                        }
                    }
                });
    }

}
