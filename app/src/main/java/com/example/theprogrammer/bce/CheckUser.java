package com.example.theprogrammer.bce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


public class CheckUser extends AppCompatActivity {

    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user);
        user = (TextView) findViewById(R.id.textView2);
        function();
    }



    void function(){
        user.setText(FirebaseMessagingService.x);
        }

}
