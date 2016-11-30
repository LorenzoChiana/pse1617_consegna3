package it.unibo.pse.pse_example01;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.UUID;

import it.unibo.pse.pse_example01.bt.BluetoothConnectionManager;
import it.unibo.pse.pse_example01.bt.BluetoothConnectionTask;
import it.unibo.pse.pse_example01.bt.BluetoothUtils;
import it.unibo.pse.pse_example01.bt.MsgTooBigException;

public class MainActivity extends AppCompatActivity {

    private TextView virtualLed, temperatureLabel;

    private BluetoothAdapter btAdapter;
    private BluetoothDevice targetDevice;

    private static MainActivityHandler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        initUI();

        uiHandler = new MainActivityHandler(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if(btAdapter != null){
            if(btAdapter.isEnabled()){
                targetDevice = BluetoothUtils.findPairedDevice(C.TARGET_BT_DEVICE_NAME, btAdapter);

                if(targetDevice != null){
                    ((TextView) findViewById(R.id.btFoundFlagLabel)).setText("Target BT Device: Found " + targetDevice.getName());
                    connectToTargetBtDevice();
                }
            } else {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), C.ENABLE_BT_REQUEST);
            }
        } else {
            showBluetoothUnavailableAlert();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        BluetoothConnectionManager.getInstance().cancel();
    }

    @Override
    public void onActivityResult (int reqID , int res , Intent data ){

        if(reqID == C.ENABLE_BT_REQUEST && res == Activity.RESULT_OK){
            targetDevice = BluetoothUtils.findPairedDevice(C.TARGET_BT_DEVICE_NAME, btAdapter);

            if(targetDevice != null){
                ((TextView) findViewById(R.id.btFoundFlagLabel)).setText("Target BT Device: Found " + targetDevice.getName());
                connectToTargetBtDevice();
            }
        }

        if(reqID == C.ENABLE_BT_REQUEST && res == Activity.RESULT_CANCELED ){
            // BT enabling process aborted
        }
    }

    private void initUI() {
        virtualLed = (TextView) findViewById(R.id.virtualLed);
        turnOffVirtualLed();

        Button turnOnButton = (Button) findViewById(R.id.turnOnButton);
        turnOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnArduinoLed();
            }
        });

        Button turnOffButton = (Button) findViewById(R.id.turnOffButton);
        turnOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffArduinoLed();
            }
        });

        Button readTempButton = (Button) findViewById(R.id.readTempButton);
        readTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTempValue();
            }
        });

        temperatureLabel = (TextView) findViewById(R.id.tempLabel);
        showTempValue(0);
    }

    private void turnOnVirtualLed() {
        virtualLed.setBackgroundColor(getResources().getColor(R.color.virtualLedOn));
    }

    private void turnOffVirtualLed() {
        virtualLed.setBackgroundColor(getResources().getColor(R.color.virtualLedOff));
    }

    private void turnOnArduinoLed() {
        try {
            BluetoothConnectionManager.getInstance().sendMsg(C.LED_ON_MESSAGE);
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
    }

    private void turnOffArduinoLed() {
        try {
            BluetoothConnectionManager.getInstance().sendMsg(C.LED_OFF_MESSAGE);
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
    }

    private void requestTempValue() {
        try {
            BluetoothConnectionManager.getInstance().sendMsg(C.READ_TEMP_MESSAGE);
        } catch (MsgTooBigException e) {
            e.printStackTrace();
        }
    }

    private void showTempValue(double value) {
        temperatureLabel.setText(getString(R.string.tempLabelPrefix) + " " + value);
    }

    private void showBluetoothUnavailableAlert(){
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

    private void connectToTargetBtDevice(){
        UUID uuid = UUID.fromString(C.TARGET_BT_DEVICE_UUID);

        BluetoothConnectionTask task = new BluetoothConnectionTask(this, targetDevice, uuid);
        task.execute();
    }


    public static MainActivityHandler getHandler(){
        return uiHandler;
    }


    /**
     * The Handler Associated to the MainActivity Class
     */
    public static class MainActivityHandler extends Handler {
        private final WeakReference<MainActivity> context;

        MainActivityHandler(MainActivity context){
            this.context = new WeakReference<>(context);
        }

        public void handleMessage(Message msg) {

            Object obj = msg.obj;

            if(obj instanceof String){
                String message = obj.toString();

                switch (message){
                    case C.BUTTON_PRESSED_MESSAGE:
                        context.get().turnOnVirtualLed();
                        break;

                    case C.BUTTON_RELEASED_MESSAGE:
                        context.get().turnOffVirtualLed();
                        break;

                    default:
                        if(message.contains(C.TEMP_ANSWER_PREFIX)) {
                            context.get().showTempValue(Double.parseDouble(message.replace(C.TEMP_ANSWER_PREFIX, "")));
                        }
                        break;
                }
            }

            if(obj instanceof JSONObject){
                //TODO
            }
        }
    }
}
