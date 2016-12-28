package com.bishoy.simplechat_server_side;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
private BluetoothAdapter mBluetoothAdapter=null;
private static final  int BLUETOOTH_ENABLE_REQUEST_CODE=1;
Button selectDevice;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectDevice= (Button) findViewById(R.id.button);
        selectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this,DeveicesListActivity.class);
                startActivityForResult(intent,2);

            }
        });
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter==null){
            Toast.makeText(this, "bluetooth not supported in this device", Toast.LENGTH_SHORT).show();
            finish();
        }

        enableBluetooth(mBluetoothAdapter);
        //MaKeDiscoverable(mBluetoothAdapter);



    }
//request eanabling bluetooth
    void enableBluetooth(BluetoothAdapter bluetoothAdapter){

        if(!bluetoothAdapter.isEnabled()){

            Intent enableBluetoothIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetoothIntent,BLUETOOTH_ENABLE_REQUEST_CODE);
            //after the system enables the bluetooth it will fire onActivityResult()
            //do sth in onActivityResult after it return with the request code you have given
        }


    }
    //make it dicoverable if it's not
    void MaKeDiscoverable(BluetoothAdapter bluetoothAdapter){
        if(bluetoothAdapter.getScanMode()!=BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){

            Intent discoverableBluetoothIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableBluetoothIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
            startActivity(discoverableBluetoothIntent);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("MainActivity","onActivityResultWasCalled()");

        if (requestCode==2 && resultCode==RESULT_OK){
            address= data.getStringExtra(DeveicesListActivity.EXTRA_DEVICE_ADDRESS).toString();
        }

        if (requestCode==BLUETOOTH_ENABLE_REQUEST_CODE){

            if (resultCode==RESULT_OK){

                initializeCommunications(address);

            }
          else  {

            Toast.makeText(this, "please enable bluetooth and try again", Toast.LENGTH_SHORT).show();
            finish();
        }
        }

    }

    private void initializeCommunications(String macAdress) {
        BluetoothDevice remoteDevice=mBluetoothAdapter.getRemoteDevice(macAdress);
        BluetoothClient bluetoothClientThread=new BluetoothClient(remoteDevice);
        bluetoothClientThread.start();
       // DataTransmitter dataTransmitterThread=new DataTransmitter();



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(address!=null){
            startServer();
        }

    }

    private void startServer() {
        ServerListener serverListenerThread=new ServerListener(mBluetoothAdapter);
        serverListenerThread.start();

    }
}
