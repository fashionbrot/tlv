package com.github.fashionbrot.tlv.parser;



import com.github.fashionbrot.tlv.TLVTypeUtil;

import java.time.LocalDateTime;

/**
 * @author fashionbrot
 */
public class LocalDateTimeTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarLocalDateTime((LocalDateTime) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarLocalDateTime(bytes);
    }

}
