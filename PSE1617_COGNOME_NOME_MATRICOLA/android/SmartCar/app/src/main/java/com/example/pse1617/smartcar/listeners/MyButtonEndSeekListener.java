package com.example.pse1617.smartcar.listeners;

import android.util.Log;
import android.view.View;

import com.example.pse1617.smartcar.C;
import com.example.pse1617.smartcar.MainActivity;
import com.example.pse1617.smartcar.bt.BluetoothConnectionManager;
import com.example.pse1617.smartcar.bt.MsgTooBigException;

/**
 * Listener nel bottone che segna la fine del trasferimento dei dati provenienti dalla seekbar
 */
public class MyButtonEndSeekListener implements View.OnClickListener {
    private MainActivity context;

    public MyButtonEndSeekListener(MainActivity activity) {
        context = activity;
    }

    @Override
    public void onClick(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //mando messaggio "fine" ad arduino
                try {
                    BluetoothConnectionManager.getInstance().sendMsg(C.END_SEEK_COMUNICATION);
                    Log.d("Bt_sent", C.END_SEEK_COMUNICATION);
                } catch (MsgTooBigException e) {
                    e.printStackTrace();
                }
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        context.hideUIContact();
                    }
                });

            }
        });

        thread.start();
    }
}