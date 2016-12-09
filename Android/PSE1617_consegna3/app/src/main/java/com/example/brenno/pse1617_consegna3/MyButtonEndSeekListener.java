package com.example.brenno.pse1617_consegna3;

import android.util.Log;
import android.view.View;

import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.MsgTooBigException;

class MyButtonEndSeekListener implements View.OnClickListener {
    private MainActivity activity;

    MyButtonEndSeekListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //mando messaggio "fine" ad arduino
                try {
                    BluetoothConnectionManager.getInstance().sendMsg(C.END_SEEK_COMUNICATION);
                    Log.d("Bt sent: ", C.END_SEEK_COMUNICATION);
                } catch (MsgTooBigException e) {
                    e.printStackTrace();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.hideUIContact();
                    }
                });

            }
        });

        thread.start();
    }
}