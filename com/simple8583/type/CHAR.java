package com.simple8583.type;

/**
 * <p>.</p>
 *
 * @author Magic Joey
 * @version CHAR.java 1.0 Created@2015-06-15 23:03 $
 */
public class CHAR implements IsoType {

    @Override
    public boolean isLVar() {
        return false;
    }

    @Override
    public String setByteValue(byte[] bts) {
        return null;
    }

    @Override
    public byte[] setValue(String bts) {
        return new byte[0];
    }

    @Override
    public int varLength(byte[] bytes) {
        return 0;
    }

}
