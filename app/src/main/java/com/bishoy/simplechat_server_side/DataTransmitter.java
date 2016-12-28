package com.bishoy.simplechat_server_side;

/**
 * Created by el on 11/27/2016.
 */


import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This thread runs during a connection with a remote device.
 * It handles all incoming and outgoing transmissions.
 */
public class DataTransmitter extends Thread {
//you will nedd socket and inputstream as well as outputstream

    BluetoothSocket bluetoothSocket=null;
    InputStream inputStream;
    OutputStream outputStream;

    public DataTransmitter(BluetoothSocket socket, String socketType) {
        //Log.d(TAG, "create ConnectedThread: " + socketType);
        bluetoothSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            outputStream=socket.getOutputStream();
            inputStream=socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream=tmpIn;
        outputStream=tmpOut;
    }
    public void run(){
        byte [] buffer=new byte[1024];
        int read;
        try {
            while((read=inputStream.read(buffer,0,buffer.length))!=-1){
                //make a handler to print the message on the ui thread



            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void write(byte[] buffer){
        try {
            outputStream.write(buffer);
            //by using handler print on the message

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
