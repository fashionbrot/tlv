package com.github.fashionbrot;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.entity.JsEntity1;
import com.github.fashionbrot.tlv.TLVTypeUtil;
import com.github.fashionbrot.tlv.TLVUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author fashionbrot
 */
public class AllTypeTest {


    @Test
    public   void test() {
        JsEntity1 entity = new JsEntity1();
        entity.setShortMax(Short.MAX_VALUE);
        entity.setShortMin(Short.MIN_VALUE);
        entity.setShortNull(null);

        entity.setIntMax(Integer.MAX_VALUE);
        entity.setIntMin(Integer.MIN_VALUE);
        entity.setIntNull(null);

        entity.setLongMax(Long.MAX_VALUE);
        entity.setLongMin(Long.MIN_VALUE);
        entity.setLongNull(null);

        entity.setFloatMax(12345.1234f);
        entity.setFloatMin(-54321.6543f);
        entity.setFloatNull(null);

        entity.setDoubleMax(Double.MAX_VALUE);
        entity.setDoubleMin(Double.MIN_VALUE);
        entity.setDoubleNull(null);

        entity.setBooleanTrue(true);
        entity.setBooleanFalse(false);
        entity.setBooleanNull(null);

        entity.setCharMax(Character.MAX_VALUE);
        entity.setCharMin(Character.MIN_VALUE);
        entity.setCharNull(null);

        entity.setByteMax(Byte.MAX_VALUE);
        entity.setByteMin(Byte.MIN_VALUE);
        entity.setByteNull(null);

        entity.setBigDecimalMax(new BigDecimal("999999.1234567"));
        entity.setBigDecimalMin(new BigDecimal("-999999.1234567"));
        entity.setBigDecimalNull(null);

        entity.setString1("你好啊TLVBuffer");
        entity.setString2("0");
        entity.setStringNull(null);

        entity.setDate1(new Date());
        entity.setDate2(null);

        entity.setLocalTime1(LocalTime.of(12,12,12));
        entity.setLocalTime2(null);

        entity.setLocalDate1(LocalDate.of(2024,06,02));
        entity.setLocalDate2(null);

        entity.setLocalDateTime1(TLVTypeUtil.toLocalDateTime("2024-06-02 15:41:30"));
        entity.setLocalDateTime2(null);

        System.out.println(JSON.toJSONString(entity));
        System.out.println(JSON.toJSONString(entity).getBytes(StandardCharsets.UTF_8).length);

        byte[] serialize = TLVUtil.serialize(entity);
        System.out.println(Arrays.toString(serialize));
        System.out.println(serialize.length);

        JsEntity1 deserialize = TLVUtil.deserialize(JsEntity1.class, serialize);
        System.out.println(deserialize);

        // Assertions
        assertEquals(entity.getShortMax(), deserialize.getShortMax());
        assertEquals(entity.getShortMin(), deserialize.getShortMin());
        assertEquals(entity.getShortNull(), deserialize.getShortNull());

        assertEquals(entity.getIntMax(), deserialize.getIntMax());
        assertEquals(entity.getIntMin(), deserialize.getIntMin());
        assertEquals(entity.getIntNull(), deserialize.getIntNull());

        assertEquals(entity.getLongMax(), deserialize.getLongMax());
        assertEquals(entity.getLongMin(), deserialize.getLongMin());
        assertEquals(entity.getLongNull(), deserialize.getLongNull());

        assertTrue(ObjectUtil.equals(entity.getFloatMax(), deserialize.getFloatMax()));
        assertTrue(ObjectUtil.equals(entity.getFloatMin(), deserialize.getFloatMin()));
        assertEquals(entity.getFloatNull(), deserialize.getFloatNull());

        assertTrue(ObjectUtil.equals(entity.getDoubleMax(), deserialize.getDoubleMax()));
        assertTrue(ObjectUtil.equals(entity.getDoubleMin(), deserialize.getDoubleMin()));
        assertEquals(entity.getDoubleNull(), deserialize.getDoubleNull());

        assertEquals(entity.isBooleanTrue(), deserialize.isBooleanTrue());
        assertEquals(entity.isBooleanFalse(), deserialize.isBooleanFalse());
        assertEquals(entity.getBooleanNull(), deserialize.getBooleanNull());

        assertEquals(entity.getCharMax(), deserialize.getCharMax());
        assertEquals(entity.getCharMin(), deserialize.getCharMin());
        assertEquals(entity.getCharNull(), deserialize.getCharNull());

        assertEquals(entity.getByteMax(), deserialize.getByteMax());
        assertEquals(entity.getByteMin(), deserialize.getByteMin());
        assertEquals(entity.getByteNull(), deserialize.getByteNull());

        assertEquals(entity.getBigDecimalMax(), deserialize.getBigDecimalMax());
        assertEquals(entity.getBigDecimalMin(), deserialize.getBigDecimalMin());
        assertEquals(entity.getBigDecimalNull(), deserialize.getBigDecimalNull());

        assertEquals(entity.getString1(), deserialize.getString1());
        assertEquals(entity.getString2(), deserialize.getString2());
        assertEquals(entity.getStringNull(), deserialize.getStringNull());

        assertEquals(entity.getDate1(), deserialize.getDate1());
        assertEquals(entity.getDate2(), deserialize.getDate2());

        assertEquals(entity.getLocalTime1(), deserialize.getLocalTime1());
        assertEquals(entity.getLocalTime2(), deserialize.getLocalTime2());

        assertEquals(entity.getLocalDate1(), deserialize.getLocalDate1());
        assertEquals(entity.getLocalDate2(), deserialize.getLocalDate2());

        assertEquals(entity.getLocalDateTime1(), deserialize.getLocalDateTime1());
        assertEquals(entity.getLocalDateTime2(), deserialize.getLocalDateTime2());
    }


}
