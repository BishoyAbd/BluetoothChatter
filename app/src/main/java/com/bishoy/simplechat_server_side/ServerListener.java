package com.bishoy.simplechat_server_side;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by el on 11/27/2016.
 */
public class ServerListener extends Thread {
    private static final String TAG ="ServerListener" ;
    private BluetoothServerSocket serverSocket=null;
    private UUID uuid=UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");

    private BluetoothSocket socket=null;
    private  BluetoothAdapter bluetoothAdapter=null;
    private  BluetoothDevice listenerBluetoothDevice=null;
    private String deviceName=null;

  public  ServerListener(BluetoothAdapter bluetoothAdapter) {

      //only one task for the constructor (initialise the server)
        this.bluetoothAdapter=bluetoothAdapter;
      BluetoothServerSocket tempBluetoothServerSocket = null;
      try {
           tempBluetoothServerSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord("server listener",uuid);
      } catch (IOException e) {
          Log.e(TAG, "Socket Type: " + "listen() failed", e);

      }
           serverSocket=tempBluetoothServerSocket;

  }

     public void run(){
Log.e(TAG,"server started listening");
         BluetoothSocket bluetoothSocket=null;

         try {
             //if someone tries to connect to the listening server
             //the server will accept the connection and return a bluetoothSocket
             // to be used in data transmitting with the bluetoothSocket of the other device who make the connection to my server

             bluetoothSocket=serverSocket.accept();
         } catch (IOException e) {
             e.printStackTrace();
         }

     }
}
