package com.example.brenno.pse1617_consegna3;

import android.util.Log;
import android.widget.SeekBar;

import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.MsgTooBigException;

class MySeekBarListener implements SeekBar.OnSeekBarChangeListener {
    private int progress;
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("seek", "Progress is: " + this.progress);
        //mando in tempo reale i valori di cambiamento della seekbar ad arduino
        try {
            BluetoothConnectionManager.getInstance().sendMsg(String.valueOf(this.progress));
            Log.d("Bt_sent", String.valueOf(this.progress));
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
        this.progress = progress;
    }
}
