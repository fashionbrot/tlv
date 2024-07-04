package com.github.fashionbrot;

import com.github.fashionbrot.tlv.TLVUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class LongTest {

    @Data
    public static class IntegerEntity{
        private Long a1;
        private long b1;
    }

    @Test
    public void test1() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(Long.MAX_VALUE);
        entity.setB1(Long.MIN_VALUE);
        byte[] bytes = TLVUtil.serialize(entity);
        System.out.println(Arrays.toString(bytes));
        IntegerEntity deserialized = TLVUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(null);
        entity.setB1(100L);
        byte[] bytes = TLVUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        IntegerEntity deserialized = TLVUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
