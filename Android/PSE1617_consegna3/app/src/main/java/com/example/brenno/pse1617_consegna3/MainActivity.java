package com.example.brenno.pse1617_consegna3;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionManager;
import com.example.brenno.pse1617_consegna3.bt.BluetoothConnectionTask;
import com.example.brenno.pse1617_consegna3.bt.BluetoothUtils;
import com.example.brenno.pse1617_consegna3.email.GmailClient;
import com.example.brenno.pse1617_consegna3.email.GmailEmail;
import com.example.brenno.pse1617_consegna3.listeners.MyButtonEndSeekListener;
import com.example.brenno.pse1617_consegna3.listeners.MySeekBarListener;
import com.example.brenno.pse1617_consegna3.listeners.MySpinnerOnItemSelectedListener;
import com.example.brenno.pse1617_consegna3.listeners.MySwitchOnClickListener;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;

import static com.example.brenno.pse1617_consegna3.C.CONTACT_MESSAGE;
import static com.example.brenno.pse1617_consegna3.C.SPENTA_NON_PARC;

public class MainActivity extends Activity {
    private static final int ACCESS_FINE_LOCATION_REQUEST = 1234;

    private String[] arraySpinner;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice targetDevice;
    private LocationManager lm;
    private static MainActivityHandler uiHandler;
    public enum State {MOVEMENT, PARK, OFF};
    private State currentState;

    public void setState(State value){
        this.currentState = value;
    }

    protected State getState(){
        return this.currentState;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        initUI();
        uiHandler = new MainActivityHandler(new WeakReference<>(this));
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setState(State.OFF);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter != null) {
            if (btAdapter.isEnabled()) {
                targetDevice = BluetoothUtils.findPairedDevice(C.TARGET_BT_DEVICE_NAME, btAdapter);

                if (targetDevice != null) {
                    ((TextView) findViewById(R.id.btFoundFlagLabel)).setText("Target BT Device: Found " + targetDevice.getName());
                    connectToTargetBtDevice();
                }
            } else {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), C.ENABLE_BT_REQUEST);
            }
        } else {
            showBluetoothUnavailableAlert();
        }


        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        BluetoothConnectionManager.getInstance().cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.d("PSE-APP", "Permission denied!");
                }
                break;
        }
    }

    public String[] getArraySpinner() {
        return arraySpinner;
    }

    @Override
    public void onActivityResult(int reqID, int res, Intent data) {

        if (reqID == C.ENABLE_BT_REQUEST && res == Activity.RESULT_OK) {
            targetDevice = BluetoothUtils.findPairedDevice(C.TARGET_BT_DEVICE_NAME, btAdapter);

            if (targetDevice != null) {
                ((TextView) findViewById(R.id.btFoundFlagLabel)).setText("Target BT Device: Found " + targetDevice.getName());
                connectToTargetBtDevice();
            }
        }

        if (reqID == C.ENABLE_BT_REQUEST && res == Activity.RESULT_CANCELED) {
            // BT enabling process aborted
        }
    }

    public void hideUIContact() {
        TextView textMechanism = (TextView) findViewById(R.id.textMeccanismo);
        textMechanism.setVisibility(View.GONE);

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setVisibility(View.GONE);

        TextView textContact = (TextView) findViewById(R.id.textContatto);
        textContact.setVisibility(View.GONE);

        Button buttonMechanism = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMechanism.setEnabled(false);
        buttonMechanism.setVisibility(View.GONE);
    }

    public void showUIContact() {
        TextView textMechanism = (TextView) findViewById(R.id.textMeccanismo);
        textMechanism.setVisibility(View.VISIBLE);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setVisibility(View.VISIBLE);
        Button buttonMechanism = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMechanism.setEnabled(true);
        buttonMechanism.setVisibility(View.VISIBLE);
    }

    private void initUI() {
        Switch notifica = (Switch) findViewById(R.id.switchNotifica);
        notifica.setOnCheckedChangeListener(new MySwitchOnClickListener(this));
        this.arraySpinner = new String[]{
                SPENTA_NON_PARC, C.SPENTA_PARC, C.ACCESA_MOV
        };

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerMod);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arraySpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener(this));

        TextView textAlarmMessage = (TextView) findViewById(R.id.textAlarmMessage);
        textAlarmMessage.setMovementMethod(new ScrollingMovementMethod());

        hideContactLocation();
        hideUIContact();

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekBarListener());

        Button buttonMeccanismo = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMeccanismo.setOnClickListener(new MyButtonEndSeekListener(this));

        final EditText mail = (EditText) findViewById(R.id.editEmail);
        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("addTextChangedListener", "is valid?" + isValidEmail(charSequence));
                if (isValidEmail(charSequence)) {
                    mail.setTextColor(Color.GREEN);
                } else {
                    mail.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showBluetoothUnavailableAlert() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.btUnavailableAlertTitle))
                .setMessage(getString(R.string.btUnavailableAlertMessage))
                .setCancelable(false)
                .setNeutralButton(getString(R.string.btUnavailableAlertBtnText), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                })
                .create();

        dialog.show();
    }

    private void connectToTargetBtDevice() {
        UUID uuid = UUID.fromString(C.TARGET_BT_DEVICE_UUID);

        BluetoothConnectionTask task = new BluetoothConnectionTask(this, targetDevice, uuid);
        task.execute();
    }

    void showContactLocation() {
        //Memorizzo la posizione geografica del contatto su una textView
        TextView textContatto = (TextView) findViewById(R.id.textContatto);

        String lp = LocationManager.NETWORK_PROVIDER;
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_REQUEST);
        }

        Location lastKnownLocation = lm.getLastKnownLocation(lp);
        textContatto.setText("Punto di contatto: (" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
        textContatto.setVisibility(View.VISIBLE);
    }

    public void hideContactLocation() {
        TextView textContatto = (TextView) findViewById(R.id.textContatto);
        textContatto.setVisibility(View.GONE);
    }

    public static MainActivityHandler getHandler() {
        return uiHandler;
    }

    public class MainActivityHandler extends Handler {
        private final WeakReference<MainActivity> context;

        MainActivityHandler(WeakReference<MainActivity> context) {
            this.context = context;
        }

        public void handleMessage(Message msg) {

            Object obj = msg.obj;

            if (obj instanceof String) {
                String message = obj.toString();
                TextView textAlarmMessage = (TextView) findViewById(R.id.textAlarmMessage);
                Log.d("RecivedMsg", message);
                switch (message) {

                    case CONTACT_MESSAGE:
                        textAlarmMessage.append(DateFormat.getDateTimeInstance().format(new Date()) + ": " + CONTACT_MESSAGE + "\n");

                        if (context.get().getState().equals(State.MOVEMENT)) {
                            //comparire l’opportuna UI per regolare il meccanismo
                            showUIContact();
                        } else if (context.get().getState().equals(State.PARK)) {
                            showContactLocation();
                            //se in C è specificata una modalità “notifica”, allora mando una mail
                            Switch switchNotifica = (Switch) findViewById(R.id.switchNotifica);
                            if (switchNotifica.isChecked()) {
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(isValidEmail(((EditText) findViewById(R.id.editEmail)).getText())) {
                                                //invio email
                                                sendMail();
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        CharSequence text = C.MAIL_SENT;
                                                        int duration = Toast.LENGTH_SHORT;
                                                        Toast.makeText(context.get(), text, duration).show();
                                                    }
                                                });
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        CharSequence text = C.EMAIL_ERROR;
                                                        int duration = Toast.LENGTH_SHORT;
                                                        Toast.makeText(context.get(), text, duration).show();
                                                    }
                                                });
                                            }
                                        }
                                    });

                                    thread.start();
                            }
                            break;
                        }
                    default:
                        if (context.get().getState().equals(State.MOVEMENT)) {
                            if (message.contains(C.PRESENCE_MESSAGE)) {
                                textAlarmMessage.append(DateFormat.getDateTimeInstance().format(new Date()) + ": " + message + "\n");
                            }
                        }
                        break;
                }
            }

            if (obj instanceof JSONObject) {
                //TODO
            }
        }
    }

     void sendMail() {
        try {
            String fromUsername = C.FROM_USERNAME;
            String fromPassword = C.FROM_PASSWORD;
            EditText toUsername = (EditText) findViewById(R.id.editEmail);
            List<String> toAddress = new ArrayList<>(Arrays.asList(new String[]{toUsername.getText().toString()}));
            String mailSubject = C.MAIL_SUBJECT;
            String mailBody = C.BODY_MAIL;
            GmailEmail email = new GmailEmail(mailSubject, mailBody, toAddress);
            GmailClient client = new GmailClient(fromUsername, fromPassword);
            client.sendEmail(email);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
