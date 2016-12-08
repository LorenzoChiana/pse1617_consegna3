package com.example.brenno.pse1617_consegna3;

import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

import static com.example.brenno.pse1617_consegna3.C.CONTACT_MESSAGE;

/**
 * The Handler Associated to the MainActivity Class
 */

public class MainActivityHandler extends Handler {
    private MainActivity activity;
    private final WeakReference<MainActivity> context;

    public MainActivityHandler(MainActivity activity, WeakReference<MainActivity> context) {
        this.activity = activity;
        this.context = context;
    }

    public void handleMessage(Message msg) {

        Object obj = msg.obj;

        if(obj instanceof String){
            String message = obj.toString();
            TextView textAlarmMessage = (TextView) activity.findViewById(R.id.textAlarmMessage);
            Spinner spinnerMod = (Spinner) activity.findViewById(R.id.spinnerMod);

            switch (message){

                case CONTACT_MESSAGE:
                    textAlarmMessage.setText(textAlarmMessage.getText() + CONTACT_MESSAGE + "\n");
                    if (spinnerMod.getSelectedItem().toString().equals("Accessa in movimento")) {
                        //comparire l’opportuna UI per regolare il meccanismo
                        activity.showUIContact();

                    } else if (spinnerMod.getSelectedItem().toString().equals("Spenta in parcheggio")) {
                        activity.showContactLocation();
                        //se in C è specificata una modalità “notifica”, allora mando una mail
                        Switch switchNotifica = (Switch) activity.findViewById(R.id.switchNotifica);
                        if(switchNotifica.isChecked()) {
                            EditText emailText = (EditText) activity.findViewById(R.id.editEmail);
                            if(!emailText.getText().equals("")) {
                                try {
                                    String fromUsername = C.FROM_USERNAME;
                                    String fromPassword = C.FROM_PASSWORD;
                                    EditText toUsername = (EditText) activity.findViewById(R.id.editEmail);
                                    List<String> toAddress = new ArrayList<>(Arrays.asList(new String[]{toUsername.getText().toString()}));
                                    String mailSubject = C.MAIL_SUBJECT;
                                    String mailBody = C.BODY_MAIL;
                                    GmailEmail email = new GmailEmail(mailSubject, mailBody, toAddress);
                                    GmailClient client = new GmailClient(fromUsername,fromPassword);
                                    client.sendEmail(email);
                                } catch (UnsupportedEncodingException | MessagingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                    break;

                default:
                        /*if(message.contains(C.TEMP_ANSWER_PREFIX)) {
                            context.get().showTempValue(Double.parseDouble(message.replace(C.TEMP_ANSWER_PREFIX, "")));
                        }*/
                    if (spinnerMod.getSelectedItem().toString().equals("Accessa in movimento")) {
                        if (message.contains(C.PRESENCE_MESSAGE + "\n")) {
                            textAlarmMessage.setText(textAlarmMessage.getText() + message);
                        }
                    }
                    break;
            }
        }

        if(obj instanceof JSONObject){
            //TODO
        }
    }
}

