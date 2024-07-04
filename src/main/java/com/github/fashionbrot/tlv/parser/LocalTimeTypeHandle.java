package com.github.fashionbrot.tlv.parser;



import com.github.fashionbrot.tlv.TLVTypeUtil;

import java.time.LocalTime;
import java.util.Date;

/**
 * @author fashionbrot
 */
public class LocalTimeTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarLocalTime((LocalTime) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarLocalTime(bytes);
    }

}
