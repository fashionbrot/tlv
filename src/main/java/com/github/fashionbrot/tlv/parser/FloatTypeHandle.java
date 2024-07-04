package com.github.fashionbrot.tlv.parser;


import com.github.fashionbrot.tlv.TLVTypeUtil;

/**
 * @author fashionbrot
 */
public class FloatTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarFloat((Float) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarFloat(bytes);
    }

}
