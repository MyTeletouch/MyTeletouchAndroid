package com.madeit.julian.myteletouch;

/// Class to exctract scan code from ANSII char
    public class AnsiiToScanCodeConverter {

    /// Pressed shift key bit set
    private static byte ShiftKey = 0x00000000 | 1 << 1;
    private static byte ZeroKey = 0x00000000;

    /**
     Scan code information from character

     :param: char Character value

     :returns: Scan code and shift state
     */
    public static ScanCodeInfo scanCodeFromChar(char c) {
        return scanCode(c);
    }

    /**
     Scan code information from ANSII code

     :param: charCode ANSII char code

     :returns: Scan code and shift state
     */
    public static ScanCodeInfo  scanCode(int charCode) {
        switch(charCode){
            case 8: return new ScanCodeInfo((byte)0x2A, ZeroKey);//BS
            case 9: return new ScanCodeInfo((byte)0x2B, ZeroKey);//TAB
            case 10: return new ScanCodeInfo((byte)0x28, ZeroKey);//New line
            case 32: return new ScanCodeInfo((byte)0x2C, ZeroKey);//spaece
            case 33: return new ScanCodeInfo((byte)0x1E, ShiftKey);//!
            case 34: return new ScanCodeInfo((byte)0x34, ShiftKey);//"
            case 35: return new ScanCodeInfo((byte)0x20, ShiftKey);//#
            case 36: return new ScanCodeInfo((byte)0x21, ShiftKey);//$
            case 37: return new ScanCodeInfo((byte)0x22, ShiftKey);//%
            case 38: return new ScanCodeInfo((byte)0x24, ShiftKey);//&
            case 39: return new ScanCodeInfo((byte)0x34, ZeroKey);//'
            case 40: return new ScanCodeInfo((byte)0x26, ShiftKey);//(
            case 41: return new ScanCodeInfo((byte)0x27, ShiftKey);//)
            case 42: return new ScanCodeInfo((byte)0x25, ShiftKey);//*
            case 43: return new ScanCodeInfo((byte)0x2E, ShiftKey);//+
            case 44: return new ScanCodeInfo((byte)0x36, ZeroKey);//,
            case 45: return new ScanCodeInfo((byte)0x2D, ZeroKey);//-
            case 46: return new ScanCodeInfo((byte)0x37, ZeroKey);//.
            case 47: return new ScanCodeInfo((byte)0x38, ZeroKey);///
            case 48: return new ScanCodeInfo((byte)0x27, ZeroKey);//0
            case 49: return new ScanCodeInfo((byte)0x1E, ZeroKey);//1
            case 50: return new ScanCodeInfo((byte)0x1F, ZeroKey);//2
            case 51: return new ScanCodeInfo((byte)0x20, ZeroKey);//3
            case 52: return new ScanCodeInfo((byte)0x21, ZeroKey);//4
            case 53: return new ScanCodeInfo((byte)0x22, ZeroKey);//5
            case 54: return new ScanCodeInfo((byte)0x23, ZeroKey);//6
            case 55: return new ScanCodeInfo((byte)0x24, ZeroKey);//7
            case 56: return new ScanCodeInfo((byte)0x25, ZeroKey);//8
            case 57: return new ScanCodeInfo((byte)0x26, ZeroKey);//9
            case 58: return new ScanCodeInfo((byte)0x33, ShiftKey);//:
            case 59: return new ScanCodeInfo((byte)0x33, ZeroKey);//;
            case 60: return new ScanCodeInfo((byte)0x36, ShiftKey);//<
            case 61: return new ScanCodeInfo((byte)0x2E, ZeroKey);//=
            case 62: return new ScanCodeInfo((byte)0x37, ShiftKey);//>
            case 63: return new ScanCodeInfo((byte)0x38, ShiftKey);//?
            case 64: return new ScanCodeInfo((byte)0x1F, ShiftKey);//@
            case 65: return new ScanCodeInfo((byte)0x04, ShiftKey);//A
            case 66: return new ScanCodeInfo((byte)0x05, ShiftKey);//B
            case 67: return new ScanCodeInfo((byte)0x06, ShiftKey);//C
            case 68: return new ScanCodeInfo((byte)0x07, ShiftKey);//D
            case 69: return new ScanCodeInfo((byte)0x08, ShiftKey);//E
            case 70: return new ScanCodeInfo((byte)0x09, ShiftKey);//F
            case 71: return new ScanCodeInfo((byte)0x0A, ShiftKey);//G
            case 72: return new ScanCodeInfo((byte)0x0B, ShiftKey);//H
            case 73: return new ScanCodeInfo((byte)0x0C, ShiftKey);//I
            case 74: return new ScanCodeInfo((byte)0x0D, ShiftKey);//J
            case 75: return new ScanCodeInfo((byte)0x0E, ShiftKey);//K
            case 76: return new ScanCodeInfo((byte)0x0F, ShiftKey);//L
            case 77: return new ScanCodeInfo((byte)0x10, ShiftKey);//M
            case 78: return new ScanCodeInfo((byte)0x11, ShiftKey);//N
            case 79: return new ScanCodeInfo((byte)0x12, ShiftKey);//O
            case 80: return new ScanCodeInfo((byte)0x13, ShiftKey);//P
            case 81: return new ScanCodeInfo((byte)0x14, ShiftKey);//Q
            case 82: return new ScanCodeInfo((byte)0x15, ShiftKey);//R
            case 83: return new ScanCodeInfo((byte)0x16, ShiftKey);//S
            case 84: return new ScanCodeInfo((byte)0x17, ShiftKey);//T
            case 85: return new ScanCodeInfo((byte)0x18, ShiftKey);//U
            case 86: return new ScanCodeInfo((byte)0x19, ShiftKey);//V
            case 87: return new ScanCodeInfo((byte)0x1A, ShiftKey);//W
            case 88: return new ScanCodeInfo((byte)0x1B, ShiftKey);//X
            case 89: return new ScanCodeInfo((byte)0x1C, ShiftKey);//Y
            case 90: return new ScanCodeInfo((byte)0x1D, ShiftKey);//Z
            case 91: return new ScanCodeInfo((byte)0x2F, ZeroKey);//[
            case 92: return new ScanCodeInfo((byte)0x31, ZeroKey);//\
            case 93: return new ScanCodeInfo((byte)0x30, ZeroKey);//]
            case 94: return new ScanCodeInfo((byte)0x23, ShiftKey);//^
            case 95: return new ScanCodeInfo((byte)0x2D, ShiftKey);//_
            case 96: return new ScanCodeInfo((byte)0x32, ZeroKey);//`
            case 97: return new ScanCodeInfo ((byte)0x04, ZeroKey);//a
            case 98: return new ScanCodeInfo ((byte)0x05, ZeroKey);//b
            case 99: return new ScanCodeInfo ((byte)0x06, ZeroKey);//c
            case 100: return new ScanCodeInfo((byte)0x07, ZeroKey);//d
            case 101: return new ScanCodeInfo((byte)0x08, ZeroKey);//e
            case 102: return new ScanCodeInfo((byte)0x09, ZeroKey);//f
            case 103: return new ScanCodeInfo((byte)0x0A, ZeroKey);//g
            case 104: return new ScanCodeInfo((byte)0x0B, ZeroKey);//h
            case 105: return new ScanCodeInfo((byte)0x0C, ZeroKey);//i
            case 106: return new ScanCodeInfo((byte)0x0D, ZeroKey);//j
            case 107: return new ScanCodeInfo((byte)0x0E, ZeroKey);//k
            case 108: return new ScanCodeInfo((byte)0x0F, ZeroKey);//l
            case 109: return new ScanCodeInfo((byte)0x10, ZeroKey);//m
            case 110: return new ScanCodeInfo((byte)0x11, ZeroKey);//n
            case 111: return new ScanCodeInfo((byte)0x12, ZeroKey);//o
            case 112: return new ScanCodeInfo((byte)0x13, ZeroKey);//p
            case 113: return new ScanCodeInfo((byte)0x14, ZeroKey);//q
            case 114: return new ScanCodeInfo((byte)0x15, ZeroKey);//r
            case 115: return new ScanCodeInfo((byte)0x16, ZeroKey);//s
            case 116: return new ScanCodeInfo((byte)0x17, ZeroKey);//t
            case 117: return new ScanCodeInfo((byte)0x18, ZeroKey);//u
            case 118: return new ScanCodeInfo((byte)0x19, ZeroKey);//v
            case 119: return new ScanCodeInfo((byte)0x1A, ZeroKey);//w
            case 120: return new ScanCodeInfo((byte)0x1B, ZeroKey);//x
            case 121: return new ScanCodeInfo((byte)0x1C, ZeroKey);//y
            case 122: return new ScanCodeInfo((byte)0x1D, ZeroKey);//z
            case 123: return new ScanCodeInfo((byte)0x2F, ShiftKey);//{
            case 124: return new ScanCodeInfo((byte)0x31, ShiftKey);//|
            case 125: return new ScanCodeInfo((byte)0x30, ShiftKey);//}
            case 126: return new ScanCodeInfo((byte)0x32, ShiftKey);//~
            default: return new ScanCodeInfo(ZeroKey, ZeroKey);
        }
    }
}