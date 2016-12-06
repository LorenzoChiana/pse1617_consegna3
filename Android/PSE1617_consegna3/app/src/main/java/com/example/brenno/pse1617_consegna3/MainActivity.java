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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.example.brenno.pse1617_consegna3.bt.*;

import java.lang.ref.WeakReference;
import java.util.UUID;

public class MainActivity extends Activity {
    private static final int ACCESS_FINE_LOCATION_REQUEST = 1234;

    private String[] arraySpinner;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice targetDevice;
    private LocationManager lm;
    private LocationListener locationListener;
    private static MainActivityHandler uiHandler;

    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener(this);
        initUI();
        uiHandler = new MainActivityHandler(this, new WeakReference<>(this));
        EditText TO = (EditText) findViewById(R.id.editEmail);
        TO.setText("lorenzo.chiana@gmail.com");
        new SendEmailActivity();
        /*sendEmail();*/
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

        if (permission == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        } else {
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationUpdates();
                } else {
                    Log.d("PSE-APP", "Permission denied!");
                }
                break;
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public String[] getArraySpinner() {
        return arraySpinner;
    }

    private void requestLocationUpdates() throws SecurityException {
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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

    void hideUIContact() {
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

    void showUIContact() {
        TextView textMechanism = (TextView) findViewById(R.id.textMeccanismo);
        textMechanism.setVisibility(View.VISIBLE);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setVisibility(View.VISIBLE);
        Button buttonMechanism = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMechanism.setEnabled(true);
        buttonMechanism.setVisibility(View.VISIBLE);
    }

    private void initUI() {
        this.arraySpinner = new String[]{
                "Accesa in movimento", "Spenta in parcheggio", "Spenta non in parcheggio"
        };

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerMod);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arraySpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MySpinnerOnItemSelectedListener(this));

        TextView textAlarmMessage = (TextView) findViewById(R.id.textAlarmMessage);
        textAlarmMessage.setMovementMethod(new ScrollingMovementMethod());

        hideUIContact();
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekBarListener());
        Button buttonMeccanismo = (Button) findViewById(R.id.buttonMeccanismo);
        buttonMeccanismo.setOnClickListener(new MyButtonEndSeekListener(this));


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
        textContatto.setText("Punto di contatto: (" + latitude + "," + longitude + ")");
        textContatto.setVisibility(View.VISIBLE);
    }

   /* protected void sendEmail() {
        Log.i("Send email", "");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        EditText TO = (EditText) findViewById(R.id.editEmail);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO.getText());
        emailIntent.putExtra(Intent.EXTRA_CC, C.EMAIL_CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
*/
    public static MainActivityHandler getHandler() {
        return uiHandler;
    }
}
