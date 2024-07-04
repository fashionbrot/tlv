package com.github.fashionbrot.tlv.parser;


/**
 * @author fashionbrot
 */
public class ByteTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return new byte[]{(byte) value};
    }

    @Override
    public Object toJava(byte[] bytes) {
        if (bytes!=null && bytes.length>0) {
            return bytes[0];
        }
        return null;
    }

}
