package com.madeit.julian.myteletouch;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.graphics.Point;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Julian on 8/6/2015.
 */
public class MainController {

    private final static String TAG = BluetoothLeService.class.getSimpleName();
    private static MainController mainController;

    private BluetoothLeService mBluetoothLeService;
    private BluetoothGattCharacteristic characteristicTX;

    private int lastCommandId;

    private Point mousePosition = new Point();
    private boolean isMouseLeftDown;
    private boolean isMouseMiddleDown;
    private boolean isMouseRightDown;

    private Point joystickPosition = new Point();
    private boolean isJoystick1Down;
    private boolean isJoystick2Down;
    private boolean isJoystick3Down;
    private boolean isJoystick4Down;
    private boolean isJoystick5Down;

    private byte[] keys = new byte[3];

    public  static  MainController getMainController(){
        if(mainController == null)
            mainController = new MainController();
        return  mainController;
    }

    public void setBluetoothService(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
        updateGattServices();
    }

    public void updateGattServices() {

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : this.mBluetoothLeService.getSupportedGattServices()) {
            if(gattService.getUuid().equals(BluetoothLeService.UUID_HM_Service)) {
                // get characteristic when UUID matches RX/TX UUID
                characteristicTX = gattService.getCharacteristic(BluetoothLeService.UUID_HM_RX_TX);
                characteristicTX.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                //mBluetoothLeService.setCharacteristicNotification(characteristicTX, true);
            }
        }
    }

    public Point getMousePosition() {
        return mousePosition;
    }

    public void setMousePosition(Point mousePosition)
    {
        this.mousePosition = mousePosition;
        sendMouseData();
    }

    public boolean isMouseLeftDown() {
        return isMouseLeftDown;
    }

    public void setIsMouseLeftDown(boolean isMouseLeftDown) {
        this.isMouseLeftDown = isMouseLeftDown;
        sendMouseData();
    }

    public boolean isMouseMiddleDown() {
        return isMouseMiddleDown;
    }

    public void setIsMouseMiddleDown(boolean isMouseMiddleDown) {
        this.isMouseMiddleDown = isMouseMiddleDown;
        sendMouseData();
    }

    public boolean isMouseRightDown() {
        return isMouseRightDown;
    }

    public void setIsMouseRightDown(boolean isMouseRightDown) {
        this.isMouseRightDown = isMouseRightDown;
        sendMouseData();
    }

    public Point getJoystickPosition() {
        return joystickPosition;
    }

    public void setJoystickPosition(Point joystickPosition) {
        this.joystickPosition = joystickPosition;
        sendJoystickData();
    }

    public boolean isJoystick1Down() {
        return isJoystick1Down;
    }

    public void setIsJoystick1Down(boolean isJoystick1Down) {
        this.isJoystick1Down = isJoystick1Down;
        sendJoystickData();
    }

    public boolean isJoystick2Down() {
        return isJoystick2Down;
    }

    public void setIsJoystick2Down(boolean isJoystick2Down) {
        this.isJoystick2Down = isJoystick2Down;
        sendJoystickData();
    }

    public boolean isJoystick3Down() {
        return isJoystick3Down;
    }

    public void setIsJoystick3Down(boolean isJoystick3Down) {
        this.isJoystick3Down = isJoystick3Down;
        sendJoystickData();
    }

    public boolean isJoystick4Down() {
        return isJoystick4Down;
    }

    public void setIsJoystick4Down(boolean isJoystick4Down) {
        this.isJoystick4Down = isJoystick4Down;
        sendJoystickData();
    }

    public boolean isJoystick5Down() {
        return isJoystick5Down;
    }

    public void setIsJoystick5Down(boolean isJoystick5Down) {
        this.isJoystick5Down = isJoystick5Down;
        sendJoystickData();
    }

    public byte[] getKeys() {
        return keys;
    }

    public void setKeys(byte[] keys)
    {
        this.keys = keys;
        sendKeyboardData();
    }

    private void sendMouseData(){
        byte buttons = getMouseButtons();

        byte[] data = new byte[4];
        data[0] = 2;
        data[1] = buttons;
        data[2] = (byte)mousePosition.x;
        data[3] = (byte)mousePosition.y;

        sendCommand(data);
    }

    private byte getMouseButtons(){
        byte res = 0x00000000;

        if(isMouseLeftDown){
            res |= 0x01;											/* If pressed, mask bit to indicate button press */
        }

        if(isMouseRightDown){
            res |= 0x02;											/* If pressed, mask bit to indicate button press */
        }

        if(isMouseMiddleDown){
            res |= 0x04;											/* If pressed, mask bit to indicate button press */
        }

        return res;
    }

    private void sendKeyboardData(){
        byte[] data = new byte[4];
        data[0] = 1;
        data[1] = keys[0];
        data[2] = keys[1];
        data[3] = keys[2];

        sendCommand(data);
    }

    private void sendJoystickData(){
        byte buttons = getJoystickButtons();

        byte[] data = new byte[4];
        data[0] = 3;
        data[1] = (byte)(127 + joystickPosition.x);
        data[2] = (byte)(127 + joystickPosition.y);
        data[3] = buttons;

        sendCommand(data);
    }

    private byte getJoystickButtons() {
        byte res = 0x00000000;

        if(isJoystick1Down){
            res |= 0x01;        									/* If pressed, mask bit to indicate button press */
        }

        if(isJoystick2Down){
            res |= 0x02;											/* If pressed, mask bit to indicate button press */
        }

        if(isJoystick3Down){
            res |= 0x04;											/* If pressed, mask bit to indicate button press */
        }

        if(isJoystick4Down){
            res |= 0x08;											/* If pressed, mask bit to indicate button press */
        }

        if(isJoystick5Down){
            res |= 0x10;											/* If pressed, mask bit to indicate button press */
        }

        return res;
    }

    private void sendCommand(byte[] commandData){
        lastCommandId++;
        if (lastCommandId > 255){
            lastCommandId = 0;
        }

        StringBuilder sbCmd = new StringBuilder();
        sbCmd.append(String.format("%d", (int) commandData[0]));
        sbCmd.append(String.format("|%d", lastCommandId));

        for (int i = 1; i < commandData.length; i++) {
            if(commandData[i] == 0 ){
                sbCmd.append("|0");
            }else {
                sbCmd.append(String.format("|%02X", commandData[i]));
            }
        }
        sbCmd.append(String.format("|%d]", lastCommandId));
        writeValue(sbCmd.toString());
    }

    private void writeValue(String data){

        try {
            if (characteristicTX == null) {
                updateGattServices();
            }

            if (characteristicTX != null) {
                characteristicTX.setValue(data);
                boolean result = mBluetoothLeService.writeCharacteristic(characteristicTX);
                if (!result) {
                    //Log.i(TAG, "error write: " + result);
                }
            }
        }
        catch (Exception ex) {
            //Log.i(TAG, "error write: " + ex);
        }

    }

    public void onDisconnect() {
        characteristicTX = null;
        mBluetoothLeService = null;
    }
}
