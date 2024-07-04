package com.github.fashionbrot.tlv.parser;


import com.github.fashionbrot.tlv.TLVTypeUtil;

/**
 * @author fashionbrot
 */
public class CharTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarChar((Character) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarChar(bytes);
    }

}
