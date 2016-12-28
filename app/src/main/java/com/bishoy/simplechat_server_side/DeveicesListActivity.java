package com.bishoy.simplechat_server_side;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Set;

public class DeveicesListActivity extends AppCompatActivity {

    public static final String EXTRA_DEVICE_ADDRESS ="device_address" ;
    private static final String TAG ="DeveicesListActivity";
    ArrayAdapter<String> pairedDevicesArrayAdapter;
    ArrayAdapter<String> availableDevicesArrayAdapter;
    ListView pairedDevicesListView;
    ListView availableDevicesListView;
    BluetoothAdapter mBluetoothAdapter;
    IntentFilter intentFilter;
    ProgressBar discoveringProgressBar;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deveices_list);

        // Set result CANCELED in case the user  pressed deny or gets out
        setResult(RESULT_CANCELED);
        discoveringProgressBar= (ProgressBar) findViewById(R.id.progressBarDiscovering);
        Button startDiscovery= (Button) findViewById(R.id.startDiscoverBtn_id);
           startDiscovery.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startDiscovery();
        v.setVisibility(View.GONE);
    }
});

        pairedDevicesArrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        pairedDevicesListView= (ListView) findViewById(R.id.pairedDevicesListView_id);
        pairedDevicesListView.setAdapter(pairedDevicesArrayAdapter);
        pairedDevicesListView.setOnItemClickListener(onItemClick);

        availableDevicesListView= (ListView) findViewById(R.id.availlableDevicesListView_id);
        availableDevicesArrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        availableDevicesListView.setAdapter(availableDevicesArrayAdapter);
        availableDevicesListView.setOnItemClickListener(onItemClick);


        intentFilter =new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReciever,intentFilter);

        intentFilter =new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReciever,intentFilter);

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices=mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() >0)
        for(BluetoothDevice bd:pairedDevices){
            pairedDevicesArrayAdapter.add(bd.getName() +"\n"+bd.getAddress());
        }
        else {

            pairedDevicesArrayAdapter.add("no paired devices");
        }

    }

  private  BroadcastReceiver  mReciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            switch(action){
                case BluetoothDevice.ACTION_FOUND:
                    //add device to the listview
                    BluetoothDevice bluetoothDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    availableDevicesArrayAdapter.add(bluetoothDevice.getName() +"\n" +bluetoothDevice.getAddress());
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    discoveringProgressBar.setVisibility(View.INVISIBLE);
                    if(availableDevicesArrayAdapter.getCount()==0){
                        availableDevicesArrayAdapter.add("no device found");

                    }
                    break;
            }


        }
    };


    private AdapterView.OnItemClickListener onItemClick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(mBluetoothAdapter.isDiscovering()){
                mBluetoothAdapter.cancelDiscovery();

            }
            String text=((TextView)view).getText().toString();
            String deviceAddress=text.substring(text.length()-17);
            Log.e(TAG,"device address:"+deviceAddress);

            Intent intent=new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS,deviceAddress);
            setResult(RESULT_OK,intent);
            finish();
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
  if(mBluetoothAdapter.isDiscovering())
    mBluetoothAdapter.cancelDiscovery();
        this.unregisterReceiver(mReciever);
    }

    private void startDiscovery(){
    discoveringProgressBar.setVisibility(View.VISIBLE);
    findViewById(R.id.avilableDevices_TV_id).setVisibility(View.VISIBLE);
    if(mBluetoothAdapter.isDiscovering()){
        mBluetoothAdapter.cancelDiscovery();
    }
    mBluetoothAdapter.startDiscovery();


}
}
