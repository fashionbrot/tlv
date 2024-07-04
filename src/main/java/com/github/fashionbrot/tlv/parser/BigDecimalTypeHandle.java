package com.github.fashionbrot.tlv.parser;


import com.github.fashionbrot.tlv.TLVTypeUtil;

import java.math.BigDecimal;

/**
 * @author fashionbrot
 */
public class BigDecimalTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarBigDecimal((BigDecimal) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarBigDecimal(bytes);
    }

}
