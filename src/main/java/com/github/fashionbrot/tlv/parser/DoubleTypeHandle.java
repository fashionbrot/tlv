package com.github.fashionbrot.tlv.parser;


import com.github.fashionbrot.tlv.TLVTypeUtil;

/**
 * @author fashionbrot
 */
public class DoubleTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarDouble((Double) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarDouble(bytes);
    }

}
