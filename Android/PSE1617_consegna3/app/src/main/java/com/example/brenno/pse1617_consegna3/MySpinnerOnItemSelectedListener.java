package com.example.brenno.pse1617_consegna3;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.MsgTooBigException;

/**
 * Created by brenno on 06/12/2016.
 */

public class MySpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private MainActivity activity;

    public MySpinnerOnItemSelectedListener(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        Log.d("Spinner selection: ", activity.getArraySpinner()[position]);
        try {
            BluetoothConnectionManager.getInstance().sendMsg(activity.getArraySpinner()[position]);
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parentView) {
    }
}