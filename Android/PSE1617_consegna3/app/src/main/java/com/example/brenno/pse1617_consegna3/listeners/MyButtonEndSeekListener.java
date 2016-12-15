package com.example.brenno.pse1617_consegna3.listeners;

import android.util.Log;
import android.view.View;

import com.example.brenno.pse1617_consegna3.C;
import com.example.brenno.pse1617_consegna3.MainActivity;
import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.MsgTooBigException;

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