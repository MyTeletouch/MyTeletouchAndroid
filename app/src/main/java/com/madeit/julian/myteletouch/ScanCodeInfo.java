package com.madeit.julian.myteletouch;

/**
 * Created by Julian on 8/9/2015.
 */
    public class ScanCodeInfo{
        public  ScanCodeInfo(byte scanCode, byte specialKeys)
        {
            this.ScanCode = scanCode;
            this.SpecialKeys = specialKeys;
        }

        public byte ScanCode;
        public byte SpecialKeys;
    }
