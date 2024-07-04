package com.github.fashionbrot.tlv.parser;


import java.nio.charset.StandardCharsets;

/**
 * @author fashionbrot
 */
public class StringTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        if (value==null){
            return new byte[]{};
        }
        if (((String)value).length()==0){
            return new byte[1];
        }
        return ((String) value).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Object toJava(byte[] bytes) {
        if (bytes.length==1 && bytes[0] == 0x00){
            return "";
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
