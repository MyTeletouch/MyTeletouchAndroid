package com.madeit.julian.myteletouch;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Main activity class
 */
public class MainActivity extends Activity {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private final static int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private Button refreshButton;
    //private Button connectButton;
    private Button buyButton;
    private final Context context = this;

    private LeDeviceListAdapter mLeDeviceListAdapter;
    private ListView bleDeviceListView;
    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;

    private String mDeviceAddress;
    //  private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private View lastSelected = null;

    private ImageView connectionIcon;
    private FrameLayout connectionIndicator;
    private FrameLayout empty_placeholder;

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                //Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                MainController.getMainController().setBluetoothService(mBluetoothLeService);
                Intent mouseActivity = new Intent(context, MouseActivity.class);
                startActivity(mouseActivity);
                refreshButton.setEnabled(true);
                connectionIndicator.setVisibility(View.INVISIBLE);
                empty_placeholder.setVisibility(View.INVISIBLE);
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                refreshButton.setEnabled(true);
                MainController.getMainController().onDisconnect();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                MainController.getMainController().updateGattServices();
            }/* else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

            }*/
        }
    };

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        /*
        if (mBluetoothLeService != null && !mDeviceAddress.isEmpty()) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }*/
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        initBluetooth();
    }

    public void addListenerOnButton() {
        final Context context = this;

        refreshButton = findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                connectionIndicator.setVisibility(View.VISIBLE);
                empty_placeholder.setVisibility(View.INVISIBLE);
                mLeDeviceListAdapter.clear();
                mLeDeviceListAdapter.notifyDataSetChanged();
                scanLeDevice(true);
            }

        });

/*
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setVisibility(View.INVISIBLE);
        connectButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                connectionIndicator.setVisibility(View.VISIBLE);
                connectButton.setVisibility(View.INVISIBLE);
                refreshButton.setEnabled(false);
                mBluetoothLeService.connect(mDeviceAddress);
            }

        });
*/
        buyButton = findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tindie.com/products/16250/"));
                startActivity(browserIntent);
            }
        });

        mHandler = new Handler();
        mLeDeviceListAdapter = new LeDeviceListAdapter(context);

        bleDeviceListView = findViewById(R.id.bleDeviceListView);
        bleDeviceListView.setAdapter(mLeDeviceListAdapter);
        bleDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
                bluetoothLeScanner.stopScan(mLeScanCallback);
                if (lastSelected != null)
                    lastSelected.setBackgroundResource(R.color.default_color);
                view.setBackgroundResource(R.color.pressed_color);
                lastSelected = view;
                BluetoothDevice device = (BluetoothDevice) mLeDeviceListAdapter.getItem(position);
                mDeviceAddress = device.getAddress();
                //connectButton.setVisibility(View.VISIBLE);
                connectionIndicator.setVisibility(View.VISIBLE);
                refreshButton.setEnabled(false);
                mBluetoothLeService.connect(mDeviceAddress);
            }
        });

        connectionIndicator = findViewById(R.id.connecting_placeholder);
        connectionIcon = findViewById(R.id.connecting_icon);
        Animation animationConnect = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        connectionIcon.startAnimation(animationConnect);

        empty_placeholder = findViewById(R.id.empty_placeholder);
    }


    private void initBluetooth() {
        if(this.context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.BLUETOOTH,
                            Manifest.permission.BLUETOOTH_ADMIN,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ENABLE_BT);
        }

        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothLeScanner.stopScan(mLeScanCallback);
                    refreshButton.setEnabled(true);
                    connectionIndicator.setVisibility(View.INVISIBLE);
                    if(mLeDeviceListAdapter.isEmpty())
                        empty_placeholder.setVisibility(View.VISIBLE);

                }
            }, SCAN_PERIOD);

            connectionIndicator.setVisibility(View.VISIBLE);
            empty_placeholder.setVisibility(View.INVISIBLE);
            refreshButton.setEnabled(false);
            bluetoothLeScanner.startScan(mLeScanCallback);
        } else {
            refreshButton.setEnabled(true);
            bluetoothLeScanner.stopScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private ScanCallback mLeScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(final int callbackType, final ScanResult result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BluetoothDevice device = result.getDevice();
                            String name = device.getName();
                            if(name != null &&
                                    name.startsWith("MyTeletouch") &&
                                    !mLeDeviceListAdapter.containsDevice(device)) {
                                connectionIndicator.setVisibility(View.INVISIBLE);
                                empty_placeholder.setVisibility(View.INVISIBLE);
                                mLeDeviceListAdapter.addDevice(device);
                                mLeDeviceListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            };
}
