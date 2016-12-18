package com.example.pse1617.smartcar.listeners;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.pse1617.smartcar.C;
import com.example.pse1617.smartcar.MainActivity;
import com.example.pse1617.smartcar.bt.BluetoothConnectionManager;
import com.example.pse1617.smartcar.bt.MsgTooBigException;

/**
 * Listener nel quale viene scelto lo stato del sistema
 */
public class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private MainActivity context;
    private String state;

    public MySpinnerOnItemSelectedListener(MainActivity context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        Log.d("SpinnerSelection", context.getArraySpinner()[position]);
        try {
            //invio dello stato ad arduino
            BluetoothConnectionManager.getInstance().sendMsg(context.getArraySpinner()[position]);
            Log.d("Bt_sent", context.getArraySpinner()[position]);
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
        /**
         * a seconda dello stato scelto cambio lo stato dell'activity
         * in più nascondo tutte le UI che non devono essere
         * visualizzate in quel determinato stato
         */
        switch (context.getArraySpinner()[position]) {
            // accesa in movimento
            case C.ACCESA_MOV:
                context.setState(MainActivity.State.MOVEMENT);
                context.hideContactLocation();
                break;
            // spenta non in parcheggio
            case C.SPENTA_NON_PARC:
                context.setState(MainActivity.State.OFF);
                context.hideUIContact();
                context.hideContactLocation();
                break;
            // spenta in parcheggio
            case C.SPENTA_PARC:
                context.setState(MainActivity.State.PARK);
                context.hideUIContact();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
    }
}