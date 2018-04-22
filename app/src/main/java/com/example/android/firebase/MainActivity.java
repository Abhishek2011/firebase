package com.example.android.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    /*Button b;
    private EditText user, pass;*/
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener ab;
    private SignInButton goo;
    private static final int RC_SIGN_IN= 1;
    private GoogleApiClient mGoogleSignInClient;
    private  FirebaseAuth.AuthStateListener mauth;
    //TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mauth =new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    startActivity(new Intent(MainActivity.this,Account.class ));
                }
            }
        };
        auth = FirebaseAuth.getInstance();
        goo=(SignInButton)findViewById(R.id.g1);
        goo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient= new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(MainActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        auth.addAuthStateListener(mauth);
        //updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
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
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(MainActivity.this,"Google sign in failed",Toast.LENGTH_SHORT).show();               // Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"signInWithCredential:failure",Toast.LENGTH_SHORT).show();
                           // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }


}
     /*   auth = FirebaseAuth.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
       // final DatabaseReference ref = FirebaseDatabase.getInstance()
         //       .getReferenceFromUrl("https://fireapp-9caa6.firebaseio.com/age");

       b=(Button)findViewById(R.id.b1);
        m=(EditText)findViewById(R.id.e1);
        t=(TextView)findViewById(R.id.t1);
      user=(EditText)findViewById(R.id.e1);
      pass=(EditText)findViewById(R.id.e2);
      b=(Button) findViewById(R.id.b1);
     /* ab=new FirebaseAuth.AuthStateListener() {
          @Override
          public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this,Account.class));

        }
          }
      };
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String u=user.getText().toString();
                //Toast.makeText(MainActivity.this,u , Toast.LENGTH_LONG).show();
                signin();
            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=m.getText().toString();
                //DatabaseReference abhi=ref.child("Name");
                //abhi.setValue(a);
                ref.push().setValue(a);
            }
        });
       ref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               String a=dataSnapshot.getValue(String.class);
               t.setText(a);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
    }



    private void signin()
             {

                 final String u=user.getText().toString();
                 String p=pass.getText().toString();
                 if(TextUtils.isEmpty(u)||TextUtils.isEmpty(p))
                 {
                     Toast.makeText(MainActivity.this,"Empty Field",Toast.LENGTH_LONG).show();
                 }
                 else {
                     auth.signInWithEmailAndPassword(u,p)
                             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                 @Override
                                 public void onComplete(@NonNull Task<AuthResult> task) {
                                     if (!task.isSuccessful()) {
                                         Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                     }

                                     // ...
                                 }
                             });
                     //auth.addAuthStateListener(ab);
                 }
    }
    @Override
    protected void onStart() {
        super.onStart();
       // auth.addAuthStateListener(ab);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }*/

