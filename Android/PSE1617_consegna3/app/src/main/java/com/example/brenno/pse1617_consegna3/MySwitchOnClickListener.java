package com.example.brenno.pse1617_consegna3;

import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by brenno on 08/12/2016.
 */
public class MySwitchOnClickListener implements CompoundButton.OnCheckedChangeListener{
    private MainActivity activity;
    private Switch notifica;

    public MySwitchOnClickListener(MainActivity activity) {
        this.activity = activity;
        this.notifica = (Switch) this.activity.findViewById(R.id.switchNotifica);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            activity.showMailUI();
        } else {
            activity.hideMailUI();
        }
    }
}
