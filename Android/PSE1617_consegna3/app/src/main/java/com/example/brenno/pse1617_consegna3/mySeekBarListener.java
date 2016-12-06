package com.example.brenno.pse1617_consegna3;

import android.util.Log;
import android.widget.SeekBar;

import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.MsgTooBigException;

/**
 * Created by brenno on 06/12/2016.
 */

public class MySeekBarListener implements SeekBar.OnSeekBarChangeListener {
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
        // TODO Auto-generated method stub
        Log.d("seek", "Progress is: " + progress);
        //mando in tempo reale i valori di cambiamento della seekbar ad arduino
        try {
            BluetoothConnectionManager.getInstance().sendMsg(String.valueOf(progress));
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
    }
}
