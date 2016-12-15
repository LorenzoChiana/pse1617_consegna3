package com.example.brenno.pse1617_consegna3;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.MsgTooBigException;

class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private MainActivity context;
    private String state;

    MySpinnerOnItemSelectedListener(MainActivity context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        Log.d("SpinnerSelection", context.getArraySpinner()[position]);
        try {
            BluetoothConnectionManager.getInstance().sendMsg(context.getArraySpinner()[position]);
            Log.d("Bt_sent", context.getArraySpinner()[position]);
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
        switch (context.getArraySpinner()[position]) {
            case C.ACCESA_MOV:
                context.setState(MainActivity.State.MOVEMENT);
                context.hideContactLocation();
                break;
            case C.SPENTA_NON_PARC:
                context.setState(MainActivity.State.OFF);
                context.hideUIContact();
                context.hideContactLocation();
                break;
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