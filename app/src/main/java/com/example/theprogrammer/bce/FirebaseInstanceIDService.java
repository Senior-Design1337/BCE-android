package com.example.theprogrammer.bce;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Warrior on 8/8/2016.
 */
public class FirebaseInstanceIDService extends com.google.firebase.iid.FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d("GOT TOKEN: ",token);
        registerToken(token);
    }

    private void registerToken(String token) {
//code to save token
    }
}