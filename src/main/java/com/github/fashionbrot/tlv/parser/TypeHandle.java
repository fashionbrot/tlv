package com.github.fashionbrot.tlv.parser;


/**
 * @author fashionbrot
 */
public interface TypeHandle {

    byte[] toByte(Object value);

    Object toJava(byte[] bytes);

}
