package com.github.fashionbrot;

import com.github.fashionbrot.tlv.TLVUtil;
import com.github.fashionbrot.tlv.annotation.TLVField;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class IntegerTest {

    @Data
    public static class IntegerEntity{
        @TLVField(index = 2)
        private int a1;
        @TLVField(index = 1)
        private Integer b1;
    }

    @Test
    public void test1() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(Integer.MAX_VALUE);
        entity.setB1(Integer.MIN_VALUE);
        byte[] bytes = TLVUtil.serialize( entity);

        IntegerEntity deserialized = TLVUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(0);
        entity.setB1(null);
        byte[] bytes = TLVUtil.serialize(entity);
        System.out.println(Arrays.toString(bytes));

        IntegerEntity deserialized = TLVUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test3(){
        Integer abc = 12;
        byte[] bytes = TLVUtil.serialize(abc);
        System.out.println(Arrays.toString(bytes));

        Integer deserialized = TLVUtil.deserialize(Integer.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(abc,deserialized);
    }

}
