package com.example.brenno.pse1617_consegna3.listeners;

import android.util.Log;
import android.widget.SeekBar;

import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.MsgTooBigException;

/**
 * Per regolare un meccanismo con cui si attua un certo effetto sul mondo fisico
 * si è scelto di usare il componente seekBar
 * ad ogni suo cambiamento viene inviato un messaggio
 * è stato scelto di inviare i dati non in tempo reale, ma a fine di ogni sngolo cambiamento della seekbar
 * altrimenti i troppi messaggi inviti in poco tempo vanno a saturare la comunicazione
 */
public class MySeekBarListener implements SeekBar.OnSeekBarChangeListener {
    private int progress;

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("seek", "Progress is: " + this.progress);
        //invio i valori di cambiamento della seekbar ad arduino
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
