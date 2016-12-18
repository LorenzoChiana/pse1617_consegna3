package com.example.pse1617.smartcar;

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
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
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

import com.example.pse1617.smartcar.bt.BluetoothConnectionManager;
import com.example.pse1617.smartcar.bt.BluetoothConnectionTask;
import com.example.pse1617.smartcar.bt.BluetoothUtils;
import com.example.pse1617.smartcar.email.GmailClient;
import com.example.pse1617.smartcar.email.GmailEmail;
import com.example.pse1617.smartcar.listeners.MyButtonEndSeekListener;
import com.example.pse1617.smartcar.listeners.MySeekBarListener;
import com.example.pse1617.smartcar.listeners.MySpinnerOnItemSelectedListener;
import com.example.pse1617.smartcar.listeners.MySwitchOnClickListener;

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

public class MainActivity extends Activity {
    private static final int ACCESS_FINE_LOCATION_REQUEST = 1234;

    private String[] arraySpinner;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice targetDevice;
    private LocationManager lm;
    private static MainActivityHandler uiHandler;

    /**
     * Simula lo stato corrente dell'auto nell'app:
     * MOVEMENT: in movimento
     * PARK: spenta in parcheggio
     * OFF: spenta non in parcheggio
     */
    public enum State {
        MOVEMENT, PARK, OFF
    }

    ;
    private State currentState;

    public void setState(State value) {
        this.currentState = value;
    }

    protected State getState() {
        return this.currentState;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        // inizializzazione UI
        initUI();
        uiHandler = new MainActivityHandler(new WeakReference<>(this));
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // stato iniziale
        setState(State.OFF);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        // Permessi per l'uso del bluetooth
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

        // permessi per il gps
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
        // rilascio il bluetooth
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

    // l'UI relativa al contatto con l'auto in movimento non viene fatta vedere
    public void hideUIContact() {
        TextView textMechanism = (TextView) findViewById(R.id.textMeccanismo);
        textMechanism.setVisibility(View.GONE);

        // uso la seekbar per il setting del servo
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setVisibility(View.GONE);

        TextView textContact = (TextView) findViewById(R.id.textContatto);
        textContact.setVisibility(View.GONE);

        Button buttonMechanism = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMechanism.setEnabled(false);
        buttonMechanism.setVisibility(View.GONE);
    }

    // l'UI relativa al contatto con l'auto in movimento viene fatta vedere
    public void showUIContact() {
        TextView textMechanism = (TextView) findViewById(R.id.textMeccanismo);
        textMechanism.setVisibility(View.VISIBLE);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setVisibility(View.VISIBLE);
        Button buttonMechanism = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMechanism.setEnabled(true);
        buttonMechanism.setVisibility(View.VISIBLE);
    }

    // inizializzazione dell'UI
    private void initUI() {
        Switch notifica = (Switch) findViewById(R.id.switchNotifica);
        //inizializzazione dello spinner con gli stati che il sistema può avere
        notifica.setOnCheckedChangeListener(new MySwitchOnClickListener(this));
        this.arraySpinner = new String[]{
                C.SPENTA_NON_PARC, C.SPENTA_PARC, C.ACCESA_MOV
        };

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerMod);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arraySpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener(this));

        TextView textAlarmMessage = (TextView) findViewById(R.id.textAlarmMessage);
        textAlarmMessage.setMovementMethod(new ScrollingMovementMethod());

        // inizialmente le UI relative ai vari contatti nei vari stati non vengono visualizzate
        hideContactLocation();
        hideUIContact();

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekBarListener());

        Button buttonMeccanismo = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMeccanismo.setOnClickListener(new MyButtonEndSeekListener(this));

    }

    // metodo che restituisce se la stringa inserita nel campo email ha una sintassi valida per essere una email
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

    // l'UI relativa alla visualizzazione delle coordinate del contatto viene fatta vedere
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

        // prendo l'ultima posizione conosciuta dal gps
        Location lastKnownLocation = lm.getLastKnownLocation(lp);
        textContatto.setText("Punto di contatto: (" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + ")");
        textContatto.setVisibility(View.VISIBLE);
    }

    // l'UI relativa alla visualizzazione delle coordinate del contatto non viene fatta vedere
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

                // a seconda del messaggio ricevuto da arduio:
                switch (message) {

                    // rilevato contatto
                    case C.CONTACT_MESSAGE:
                        // viene fatto vedere nell'UI ora e data del contatto
                        textAlarmMessage.append(DateFormat.getDateTimeInstance().format(new Date()) + ": " + C.CONTACT_MESSAGE + "\n");

                        // a seconda dello stato corrente faccio l'opportuna azione
                        if (context.get().getState().equals(State.MOVEMENT)) {
                            //comparire l’opportuna UI per regolare il servo
                            showUIContact();
                        } else if (context.get().getState().equals(State.PARK)) {
                            //compare l'opportuna UI con la posizione del contatto
                            showContactLocation();
                            //se in C è specificata una modalità “notifica”, allora mando una mail
                            Switch switchNotifica = (Switch) findViewById(R.id.switchNotifica);
                            if (switchNotifica.isChecked()) {
                                Thread thread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // se l'email è valida allora la invio
                                        if (isValidEmail(((EditText) findViewById(R.id.editEmail)).getText())) {
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
                                        }
                                        //altrimenti faccio comparire a video un avviso nel quale segnale che l'email non è valida
                                        else {
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
                        // se è nello stato di movimento
                        // e ha ricevuto il messaggio di presenza di un veicolo esterno troppo vicino
                        // allora viene fatta visualizzare a video la distanza con data e ora
                        if (context.get().getState().equals(State.MOVEMENT) && message.contains(C.PRESENCE_MESSAGE)) {
                            textAlarmMessage.append(DateFormat.getDateTimeInstance().format(new Date()) + ": " + message + " m\n");
                        }
                        break;
                }
            }

            if (obj instanceof JSONObject) {
                //TODO
            }
        }
    }

    //metodo per l'invio dell'email
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
