package com.example.michael.bluetoothcontroll;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * responsible for all bluetooth threads
 */
public class blueToothControl {

    BluetoothSocket mmSocket;
    String connectionConfirmation = "nothing";
    String workConfirmation = "nothing";

    /**
     *
     * connect thread
     * to establish the bluetooth socket
     */
    final class connectThread implements Runnable{

        BluetoothDevice mmDevice;

        public connectThread(BluetoothDevice Device){

            mmDevice = Device;
        }

        @Override
        public void run() {

            // try and connect to pi
            connectBT(mmDevice);
        }
    }

    /**
     *
     * workerThread
     * for sending data over the bluetooth socket
     *
     */
    final class workerThread implements Runnable {

        private String btMsg = "";

        public workerThread(String msg) {

            // chars that get sent to the pi
            btMsg = msg;
        }

        @Override
        public void run()
        {
            try {

                if(mmSocket != null) {

                    OutputStream mmOutputStream = mmSocket.getOutputStream();
                    mmOutputStream.write(btMsg.getBytes());
                    workConfirmation = "sent \n >";

                }else{

                    workConfirmation = "Error: failed to send " + btMsg + "\n > ";
                }

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }

    /**
     * connectBT
     * @param mmDevice
     * takes the device and trys to create a socket with it
     */
    protected void connectBT(BluetoothDevice mmDevice) {

        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        final UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee"); //Standard SerialPortService ID


        try {

            if(mmDevice != null) {
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                if (!mmSocket.isConnected()) {
                    mmSocket.connect();

                    connectionConfirmation = "socket has been created";
                }
            }else{

                connectionConfirmation = "Error: failed to connect to device \n >";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String callConnectThread(BluetoothDevice Device){

        Thread thread = new Thread(new connectThread(Device));
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return connectionConfirmation;
    }

    protected String callWorkThread(String msg){

        Thread thread = new Thread(new workerThread(msg));
        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return workConfirmation;
    }

}
