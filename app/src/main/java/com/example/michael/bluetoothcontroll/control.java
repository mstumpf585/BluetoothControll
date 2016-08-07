package com.example.michael.bluetoothcontroll;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

public class control extends ActionBarActivity {

    BluetoothDevice mmDevice;
    blueToothControl bt = new blueToothControl();
    String tvMsg = "";

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

    // Define the Handler that receives messages from the thread and update the progress
    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);


        final TextView tv  = (TextView) findViewById(R.id.confirm);
        final Button fwd   = (Button) findViewById(R.id.FWD);
        final Button rev   = (Button) findViewById(R.id.REV);
        final Button left  = (Button) findViewById(R.id.Left);
        final Button right = (Button) findViewById(R.id.Right);
        final Button start = (Button) findViewById(R.id.settings);

        tv.setMovementMethod(new ScrollingMovementMethod());

        /**
         *
         * button handlers
         *
         * */
        //start start button handler
        start.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                bt.callConnectThread(mmDevice);
            }
        });
        //end start button handler

        // start fwd button handler
        fwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                tv.append("sending fwd \n");
                tvMsg = bt.callWorkThread("fwd");
                tv.append(tvMsg);

            }
        });
        //end fwd button handler

        //start left on button handler
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv.append("sending left \n");
                tvMsg = bt.callWorkThread("left");
                tv.append(tvMsg);
            }
        });
        //end left button handler

        //start right  button handler
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv.append("sending right \n");
                tvMsg = bt.callWorkThread("right");
                tv.append(tvMsg);

            }
        });
        // end right button handler

        //start reverse  button handler
        rev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv.append("sending rev \n");
                tvMsg = bt.callWorkThread("rev");
                tv.append(tvMsg);

            }
        });
        // end reverse button handler

        /**
         *
         *  check on bluetooth adapter
         *
         */
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter != null) {

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    if (device.getName().equals("raspberrypi")) //Note, you will need to change this to match the name of your device
                    {
                        Log.e("Robo", device.getName());
                        mmDevice = device;
                        break;
                    }
                }
            }
        } else{

            // failed send the response to user
            tv.setText("Error: could not find device \n");
        }
    }
}


