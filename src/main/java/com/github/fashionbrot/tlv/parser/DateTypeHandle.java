package com.github.fashionbrot.tlv.parser;



import com.github.fashionbrot.tlv.TLVTypeUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fashionbrot
 */
public class DateTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarDate((Date) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarDate(bytes);
    }

}
