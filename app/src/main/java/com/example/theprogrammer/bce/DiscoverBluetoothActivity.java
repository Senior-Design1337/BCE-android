package com.example.theprogrammer.bce;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.theprogrammer.bce.model.RequestData;
import com.example.theprogrammer.bce.model.Result;
import com.example.theprogrammer.bce.rest.ApiClient;
import com.example.theprogrammer.bce.rest.ApiInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class DiscoverBluetoothActivity extends AppCompatActivity {
//look for battery consumption. OR how to save power

    ListView listView;
    TextView statusTextView;
    Button searchButton;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action", action);

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                statusTextView.setText("finished");
                searchButton.setEnabled(true);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));

                //Log.i("Device Found", "Name: " + name + " Address: " + address + " RSSI: " + rssi);
            }
        }

    };
    ImageView imageView;
    BluetoothAdapter bluetooth;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_bluetooth);

        Log.i("onCreate", "IamWorking");
        //listView = findViewById(R.id.listView);
        statusTextView = findViewById(R.id.textView);
        searchButton = findViewById(R.id.btnSearch);
        //imageView = findViewById(R.id.imageView);
        bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 70);
        }
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);

        //AcceptThread acceptThread = new AcceptThread(bluetooth);

    }

    public void searchClicked(View view) {
        statusTextView.setText("Searching...");
        searchButton.setEnabled(false);
        //bluetooth.startDiscovery();
        //Toast.makeText(this,"oso thread Started",Toast.LENGTH_SHORT).show();
        //Log.i("oso thread waithing", "accepting thread");
        //*
        Intent discoverableIntent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        //text.setText("Discoverable!!");
        //while(mmServerSocket==null);
        AcceptThread acceptThread = new AcceptThread(bluetooth, imageView, statusTextView);
        acceptThread.start();
        //*/
        /*
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);
        //*/
    }
}

class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    ImageView imageView;
    TextView textView;

    //TextView statusTextView = findViewById(R.id.textView);
    public AcceptThread(BluetoothAdapter mBluetoothAdapter, ImageView imageView0, TextView textView0) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        textView = textView0;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.

            UUID uuid = UUID.fromString("2a447d47-8f0b-4a28-8673-01087bf2618c");
            Log.i("oso", "UUID done");
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("BCE app", uuid);///put name and UUID here.
            Log.i("oso", "adapter done");
        } catch (IOException e) {
            Log.e("oso", "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
        imageView = imageView0;
    }

    public void run() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("BCE");

        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            //BluetoothSocket finalSocket = null;
            try {

                while (mmServerSocket == null)
                    Log.i("blll", "null socket");
                Log.i("blll", "waiting for socket accept");
                socket = mmServerSocket.accept();
                Log.i("blll", "socket accepted");

                //finalSocket = socket
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                Log.i("blll", "error socket");

                break;
            }
            Log.i("oso", "socket accepted");
            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                //manageMyConnectedSocket(socket);
                try {
                    //remove this to always accept more connections
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
/*
                    Log.i("oso", "before input stream");
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1000];
                    inputStream.read(bytes);

                    //Log.i("oso bytes read = ", bytes.toString());

//*/

                    int bytes;
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[2000];

                    while (true) {
                        try {
                            bytes = inputStream.read(buffer);            //read bytes from input buffer

                            String readMessage = new String(buffer, 0, bytes);
                            //sendFaceFeatures(readMessage);
                            //Map<String, Object> childUpdates = new HashMap<>();
                            myRef.child("FacialFeatures").setValue(readMessage);
                            //Send the obtained bytes to the UI Activity via handler
                            Log.i("oso logging", readMessage + "");
                        } catch (IOException e) {
                            Log.i("oso error", "error exception");
                            break;
                        }
                    }

                } catch (Exception e) {
                    Log.e("oso Connection socket failied ", ":(");
                }

            }

            break;
        }
    }

    public void sendFaceFeatures(String faceFeatures) {
        RequestData rd = new RequestData();
        rd.setFacialfeature(faceFeatures);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<Result> call = apiService.sendFaceFeatures(rd);
        //Call<User> call = apiService.login("Abdallah5@","1234");
        Log.d("oso", "authenticating");
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Log.i("oso", "send face features");
                Log.i("oso", "names in photo: ");
                Log.i("oso", response.toString());
                textView.setText(response.body().getName());
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.i("oso", "failed sending face features");
                Log.i("oso", t.toString());

            }
        });
    }


    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}