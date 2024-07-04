package com.github.fashionbrot.tlv.parser;


import com.github.fashionbrot.tlv.TLVTypeUtil;

/**
 * @author fashionbrot
 */
public class LongTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarLong((Long) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarLong(bytes);
    }

}
