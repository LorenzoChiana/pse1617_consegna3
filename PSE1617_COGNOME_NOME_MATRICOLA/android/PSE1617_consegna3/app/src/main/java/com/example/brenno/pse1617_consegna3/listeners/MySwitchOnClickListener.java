package com.example.brenno.pse1617_consegna3.listeners;

import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.brenno.pse1617_consegna3.MainActivity;
import com.example.brenno.pse1617_consegna3.R;

/**
 * Listener dello switch
 */
public class MySwitchOnClickListener implements CompoundButton.OnCheckedChangeListener{
    private MainActivity context;

    public MySwitchOnClickListener(MainActivity activity) {
        context = activity;
    }
    // al cambio dello switch viene abilitato o disattivato il componente dove viene immessa l'email
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
        EditText email = (EditText) context.findViewById(R.id.editEmail);
        if(on){
            email.setEnabled(true);
        } else {
            email.setEnabled(false);
        }
    }
}
