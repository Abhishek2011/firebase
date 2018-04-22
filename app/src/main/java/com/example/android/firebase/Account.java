package com.example.android.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Account extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
    }
    public void quickgame(View view) {
        auth = FirebaseAuth.getInstance();
       // auth.signOut();
        //Toast.makeText(Account.this,"SIGNOUT SUCCESS",Toast.LENGTH_SHORT).show();
       // startActivity(new Intent(Account.this,game.class));

    }
}
