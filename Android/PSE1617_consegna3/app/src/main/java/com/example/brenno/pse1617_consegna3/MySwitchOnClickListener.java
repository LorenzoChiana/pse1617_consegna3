package com.example.brenno.pse1617_consegna3;

import android.widget.CompoundButton;

class MySwitchOnClickListener implements CompoundButton.OnCheckedChangeListener{
    private MainActivity context;

    MySwitchOnClickListener(MainActivity activity) {
        context = activity;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            context.showMailUI();
        } else {
            context.hideMailUI();
        }
    }
}
