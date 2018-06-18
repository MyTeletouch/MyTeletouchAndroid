package com.madeit.julian.myteletouch;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author Julian
 *
 */
public class LeDeviceListAdapter extends BaseAdapter
{
    private TextView devNameTextView, devAddressTextView;
    private ArrayList<BluetoothDevice> mDevices;
    private Context context;

    /**
     *
     */
    public LeDeviceListAdapter(Context context)
    {
        this.context = context;
        mDevices = new ArrayList<BluetoothDevice>();
    }

    /**
     * @return the mDevices
     */
    public ArrayList<BluetoothDevice> getMDevices( )
    {
        return mDevices;
    }

    /**
     * @param mDevices the mDevices to set
     */
    public void setMDevices( ArrayList<BluetoothDevice> mDevices )
    {
        this.mDevices = mDevices;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount( )
    {
        return getMDevices( ).size( );
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem( int position )
    {
        return getMDevices( ).get(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId( int position )
    {
        return -1;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        LinearLayout layout = null;

        if (convertView == null) {
            layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_list, null);
            devNameTextView = (TextView) layout.findViewById(R.id.textViewDevName);
            devAddressTextView = (TextView) layout.findViewById(R.id.textViewDevAddress);
            convertView = layout;
        }

        // add-Parameters
        BluetoothDevice device = mDevices.get(position);
        String devName = device.getName();
        if (devName != null && devName.length() > 0) {
            devNameTextView.setText(devName);
        } else {
            devNameTextView.setText("unknow-device");
        }
        devAddressTextView.setText(device.getAddress());

        return convertView;
    }

    public void addDevice( BluetoothDevice device )
    {
        getMDevices( ).add(device);
    }

    public boolean containsDevice(BluetoothDevice device) {
        for (BluetoothDevice current: mDevices) {
            if(current.getName().equals(device.getName()) &&
                    current.getAddress().equals(device.getAddress())) {
                return  true;
            }
        }

        return false;
    }

    public void clear() {
        mDevices.clear();
    }
}
