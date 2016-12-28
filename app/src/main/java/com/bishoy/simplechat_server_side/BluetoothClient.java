package com.bishoy.simplechat_server_side;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by el on 11/27/2016.
 */


//this thread starts when you want to establish a connection with another device "serve"
public class BluetoothClient extends Thread{
//you will need a blutoothsocket and a bluetoothdevice
    private BluetoothSocket clientBluetoothSocket=null;
    private BluetoothDevice bluetoothDevice; //represent the device of the other hand(server) not your device
    private UUID uuid=UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");


    BluetoothClient(BluetoothDevice bluetoothDevice){
    this.bluetoothDevice=bluetoothDevice;
        BluetoothSocket tempBluetoothSocket=null;
        try {
            //create a socket from my device
            tempBluetoothSocket=bluetoothDevice.createRfcommSocketToServiceRecord(uuid);

        } catch (IOException e) {
            e.printStackTrace();
        }
        clientBluetoothSocket=tempBluetoothSocket;
    }




  public  void run(){

      try {

          clientBluetoothSocket.connect();
          Log.e("BluetoothClient","connected")     ;
      } catch (IOException e) {
          e.printStackTrace();
          try {
              clientBluetoothSocket.close();
          } catch (IOException e1) {
              e1.printStackTrace();
              try {
                  clientBluetoothSocket.close();
              } catch (IOException e2) {
                  e2.printStackTrace();
              }
          }
      }
  }
}
