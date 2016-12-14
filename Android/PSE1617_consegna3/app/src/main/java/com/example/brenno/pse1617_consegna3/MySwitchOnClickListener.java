package com.example.brenno.pse1617_consegna3;

import android.widget.CompoundButton;

class MySwitchOnClickListener implements CompoundButton.OnCheckedChangeListener{
    private MainActivity activity;

    MySwitchOnClickListener(MainActivity activity) {
        this.activity = activity;
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
