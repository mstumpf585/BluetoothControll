package com.example.michael.bluetoothcontroll;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import java.lang.Runnable;



public class control extends ActionBarActivity {





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**My Code**/
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice = null;

    final static Handler handler = new Handler();

    final byte delimiter = 33;
    int readBufferPosition = 0;



    public void sendBtMsg() {
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        final UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID


        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (!mmSocket.isConnected()) {
                mmSocket.connect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Define the Handler that receives messages from the thread and update the progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);




        final TextView confirm = (TextView) findViewById(R.id.confirm);
        final Button fwd = (Button) findViewById(R.id.FWD);
        final Button rev = (Button) findViewById(R.id.REV);
        final Button left = (Button) findViewById(R.id.Left);
        final Button right = (Button) findViewById(R.id.Right);
        final Button start = (Button) findViewById(R.id.settings);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        final class connectThread implements Runnable{
            @Override
            public void run() {
                sendBtMsg();
            }
        }
        final class workerThread implements Runnable {

            private String btMsg;

            public workerThread(String msg) {
                btMsg = msg;

            }

            public void run()
            {
                try {
                    String msg = btMsg;
                    //msg += "\n";
                    OutputStream mmOutputStream = mmSocket.getOutputStream();
                    mmOutputStream.write(msg.getBytes());



                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }


        //start start button handler

        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                (new Thread (new connectThread())).start();
            }
        });

        //end start button handler

        // start fwd button handler

        fwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on temp button click

                (new Thread(new workerThread("fwd"))).start();

            }
        });

        //end fwd button handler

        //start left on button handler
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on temp button click

                (new Thread(new workerThread("left"))).start();

            }
        });
        //end left button handler

        //start right  button handler

        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on temp button click

                (new Thread(new workerThread("right"))).start();

            }
        });

        // end right button handler

        //start right  button handler

        rev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                (new Thread(new workerThread("rev"))).start();

            }
        });

        // end right button handler

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("raspberrypi-0")) //Note, you will need to change this to match the name of your device
                {
                    Log.e("Aquarium", device.getName());
                    mmDevice = device;
                    break;
                }
            }
        }


    }



}


