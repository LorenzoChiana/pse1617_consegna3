package com.example.brenno.pse1617_consegna3.listeners;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.brenno.pse1617_consegna3.MainActivity;
import com.example.brenno.pse1617_consegna3.R;

public class MySwitchOnClickListener implements CompoundButton.OnCheckedChangeListener{
    private MainActivity context;

    public MySwitchOnClickListener(MainActivity activity) {
        context = activity;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
        EditText email = (EditText) context.findViewById(R.id.editEmail);
        Switch simpleSwitch = (Switch) context.findViewById(R.id.switchNotifica);
        if(on){
            email.setEnabled(true);
        } else {
            //if (isValidEmail(email.getText())) {
                email.setEnabled(false);
           /* }
            else {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CharSequence text = C.EMAIL_ERROR;
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(context, text, duration).show();
                    }
                });
                simpleSwitch.setChecked(true);
            }*/
        }
    }
}
