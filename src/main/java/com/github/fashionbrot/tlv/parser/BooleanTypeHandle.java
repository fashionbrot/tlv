package com.github.fashionbrot.tlv.parser;



/**
 * @author fashionbrot
 */
public class BooleanTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        Boolean b = (Boolean) value;
        if (b==null){
            b = false;
        }
        return new byte[]{ (byte) (b?1:0)};
    }

    @Override
    public Object toJava(byte[] bytes) {
        if (bytes ==null || bytes.length==0){
            return null;
        }
        return bytes[0]==1?true:false;
    }

}
