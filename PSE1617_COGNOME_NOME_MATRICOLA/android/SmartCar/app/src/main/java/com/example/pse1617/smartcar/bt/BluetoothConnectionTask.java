package com.example.pse1617.smartcar.bt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.pse1617.smartcar.MainActivity;
import com.example.pse1617.smartcar.R;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnectionTask extends AsyncTask<Void, Void, Boolean> {

    private BluetoothSocket btSocket = null ;
    private MainActivity context = null;

    public BluetoothConnectionTask(MainActivity context, BluetoothDevice server, UUID uuid){

        this.context = context;

        try {
            btSocket = server.createRfcommSocketToServiceRecord(uuid);
        } catch ( IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected Boolean doInBackground (Void ... params ){
        try{
            btSocket.connect();
        } catch(IOException connectException){
            try{
                btSocket.close();
            } catch(IOException closeException){
                closeException.printStackTrace();
            }

            connectException.printStackTrace();
            return false;
        }

        BluetoothConnectionManager cm = BluetoothConnectionManager.getInstance ();
        cm.setChannel(btSocket);
        cm.start();

        return true;
    }

    @Override
    protected void onPostExecute(Boolean connected) {
        TextView flagLabel = (TextView) context.findViewById(R.id.btConnectedFlagLabel);
        Spinner spinner = (Spinner) context.findViewById(R.id.spinnerMod);

        if(connected) {
            flagLabel.setText("Target BT Status: Connected");
            try {
                BluetoothConnectionManager.getInstance().sendMsg(String.valueOf(spinner.getSelectedItem()));
            } catch (MsgTooBigException e) {
                e.printStackTrace();
            }
        } else {
            flagLabel.setText("Target BT Status: Not Connected");
        }
    }
}
