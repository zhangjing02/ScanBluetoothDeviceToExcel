package com.example.zhangj.mybluetooth_demo11;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhangj.mybluetooth_demo11.entity.BleDevice;
import com.example.zhangj.mybluetooth_demo11.entity.ExcelUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liangls on 2016/6/17.
 */
public class ScanDeviceActivity extends Activity {
    private static final long SCAN_PERIOD = 150000;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private ListView scan_list;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private static final int REQUEST = 11;
    private static final int REQUEST_FINE_LOCATION = 0;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private Button mScan_btn,mOutput_btn;
    private String xls_name="device_address";
    private int xls_count=0;
    private TextView mExcel_name;

    private ArrayList<ArrayList<String>> billList = new ArrayList<ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scandevice);
        mHandler = new Handler();
        init();
        scanLeDevice(true);

        mScan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBluetoothAdapter.stopLeScan(mLeScanCallback);
                scanLeDevice(false);
                if (mLeDevices!=null){
                    mLeDevices.clear();
                    mLeDeviceListAdapter.notifyDataSetChanged();
                  //  mBluetoothAdapter.startLeScan(mLeScanCallback);
                    scanLeDevice(true);
                }

            }
        });
        mOutput_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xls_count++;

                File file = new File(getSDPath() + "/"+xls_name);
                makeDir(file);
                ArrayList<String> title= new ArrayList<String>();
                ArrayList<String> title2= new ArrayList<String>();
                for (int i=0;i<mLeDevices.size();i++){
                    title.add(0,mLeDevices.get(i).getDeviceAddress());
                    title2.add(0,mLeDevices.get(i).getDeviceName());
                }
                billList.add(title2);
                billList.add(title);


                ExcelUtils.initExcel(getSDPath()+"/"+xls_name+"/"+xls_name+xls_count+
                        ".xls", billList);

                mExcel_name.setText(getSDPath()+"/"+xls_name+"/"+xls_name+xls_count+".xls");
//                title.clear();
//                billList.clear();
            }
        });

    }

    private void init() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        scan_list = (ListView) findViewById(R.id.lv_device);
        mScan_btn= (Button) findViewById(R.id.scan_btn);
        mOutput_btn= (Button) findViewById(R.id.output_btn);
        mExcel_name= (TextView) findViewById(R.id.excel_name);
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        scan_list.setAdapter(mLeDeviceListAdapter);

    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mScanning) {
                        mScanning = false;
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                }
            }, SCAN_PERIOD);
            if (!mScanning) {
                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);

            }
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);

        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (device.getName() == null) return;
                            Log.e("BluetoothLeService11", device.getName() + device.getAddress());
                            BleDevice bleDevice = new BleDevice();
                            bleDevice.setDeviceAddress(device.getAddress());
                            bleDevice.setDeviceName(device.getName());
                            bleDevice.setRssi(rssi);

                            mLeDeviceListAdapter.addDevice(bleDevice);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            };

    private ArrayList<BleDevice> mLeDevices;
    private class LeDeviceListAdapter extends BaseAdapter {

        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BleDevice>();
            mInflator = ScanDeviceActivity.this.getLayoutInflater();
        }

        public void addDevice(BleDevice device) {
            if (device.getDeviceName().equals("SENSSUN FAT")){
                if (!mLeDevices.contains(device)) {
                    mLeDevices.add(device);
                } else {
                    int index = mLeDevices.indexOf(device);
                    mLeDevices.remove(device);
                    mLeDevices.add(index,device);
                }
            }

        }

        public ArrayList<BleDevice> getmLeDevices() {
            return mLeDevices;
        }

        public void clear() {
            mLeDevices.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.item_device, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            BleDevice device = mLeDevices.get(i);
            viewHolder.deviceName.setText(device.getDeviceName());
            viewHolder.deviceRssi.setText(String.valueOf(device.getRssi()));
            return view;
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceRssi;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }
    public static void makeDir(File file) {
        boolean isExist = file.exists();
        if(!isExist){
            file.mkdirs();
        }
    }

}
