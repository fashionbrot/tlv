package com.github.fashionbrot.tlv.parser;



import com.github.fashionbrot.tlv.TLVTypeUtil;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author fashionbrot
 */
public class LocalDateTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarLocalDate((LocalDate) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarLocalDate(bytes);
    }

}
