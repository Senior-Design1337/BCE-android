package com.example.theprogrammer.bce;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.theprogrammer.bce.model.RequestData;
import com.example.theprogrammer.bce.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    String photoPath;
    String userID;
    String token;
    private int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoPath = photoPath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + File.separator + "img.jpg";

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        token = intent.getStringExtra("token");

        Log.i("oso", "intent userID from main = " + userID);
        //onTokenRefresh();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BCE");
        myRef.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("test");
        FirebaseInstanceId.getInstance().getToken();

        Log.d("TOKEN",FirebaseInstanceId.getInstance().getToken());

    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("abdul", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("abdul", "Message data payload: " + remoteMessage.getData());


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("abdul", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go

            File photoFile = new File(photoPath);
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileproviderBCE",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //((TextView) findViewById(R.id.locationText)).setText("getting your location...");
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    public void CheckUser(View view) {
        Intent checker = new Intent( this , CheckUser.class );
        startActivity(checker);
    }

/*
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button:
                Intent checker = new Intent( this , CheckUser.class );
                startActivity(checker);
                break;
            case R.id.btnTakePicture:
                takePicture();
                break;
        }
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_bluetooth:
                Toast.makeText(this, "settings menu opens", Toast.LENGTH_SHORT).show();
                DiscoverBluetoothActivity di = new DiscoverBluetoothActivity();

                Intent intent = new Intent(this, DiscoverBluetoothActivity.class);
                startActivity(intent);
                //di.statusTextView.setText("hii");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("oso ", "result from camera");
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(this, "image taken", Toast.LENGTH_SHORT).show();

            //compare faces here and display the results
            {
                try {

                    RequestData requestData = new RequestData();
                    User userInfo = new User();
                    userInfo.setId(userID);

                    //Log.i("oso encoder=", encodedfile);
                    //byte[] photo = Files.readAllBytes((new File(photoPath)).toPath());
                    List<Byte> myPhoto = new ArrayList<Byte>();

                   // String encodedfile = new String(Base64.encodeToString(Files.readAllBytes((new File(photoPath)).toPath()), 0));
/*
                    for (byte b : photo)
                        myPhoto.add(b);
                    //*/
/*
                    Log.i("oso osama", "test");
                    File file = new File(photoPath);
                    Log.i("oso PhotoPath", photoPath);
                    //new File("/storage/emulated/0/Download/Corrections 6.jpg");
                    //*/
                    userInfo.setToken(token);
                    userInfo.setId(userID);
                    //userInfo.setPhoto(encodedfile);

                    //sendPhoto(requestData);
                   // Log.i("oso", String.valueOf(photo));
                    Log.i("oso", String.valueOf(myPhoto.size()));
                    //requestData.setPhoto(photo);
                    //sendPhoto(encodedfile);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



/*
    private void sendPhoto(RequestData rd) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call = apiService.updateProfile(rd);
        Log.d("oso", "authenticating");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.i("oso", response.toString());
                if (response.body() != null) {
                    //boolean b = response.body().getSucess();
                    Log.i("oso", "something");

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i("oso error: ", t.getMessage() + "\n" + t.toString());

            }
        });

    }*/
/*
    private void sendPhoto(File file) {

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        // add another part within the multipart request
        //RequestBody id =
        //RequestBody.create(MediaType.parse("multipart/form-data"), "50");

        //MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("photo"), file));
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Log.i("oso ", "Sending Photo");
        RequestBody tokenBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), token);

        Call<ResponseBody> call = apiService.updateProfile(body, tokenBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i("oso", response.toString());
                Log.i("oso", call.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("oso", t.toString());
                Log.i("oso", call.toString());
            }
        });


    }
    //*/
/*
    private void sendPhoto(String s) {
        image i = new image();
        i.setPhoto(s);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Integer> call = apiService.sendPhoto0(i);
        Log.d("oso", "sendingPhoto");
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.i("oso", response.toString());
                if (response.body() != null) {
                    //boolean b = response.body().getSucess();
                    Log.i("oso", "photo sent");
                    //showProgress(false);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.i("oso error: ", t.getMessage() + "\n" + t.toString());
                //showProgress(false);
            }
        });

    }
    //*/
}