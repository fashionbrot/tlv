package com.github.fashionbrot;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.tlv.TLVUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class FloatTest {

    @Data
    public static class FloatEntity{
        private Float a1;
        private float b1;
    }

    @Test
    public void test1() throws IOException {
        FloatEntity entity=new FloatEntity();
        entity.setA1(Float.MAX_VALUE);
        entity.setB1(Float.MIN_VALUE);
        byte[] bytes = TLVUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        FloatEntity deserialized = TLVUtil.deserialize(FloatEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertTrue(ObjectUtil.equals(entity.getB1(),deserialized.getB1()));
    }

    @Test
    public void test2() throws IOException {
        FloatEntity entity=new FloatEntity();
        entity.setA1(null);
        entity.setB1(123456.12f);
        byte[] bytes = TLVUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        FloatEntity deserialized = TLVUtil.deserialize(FloatEntity.class, bytes);
        System.out.println(deserialized);
        System.out.println(JSON.toJSONString(entity));
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertTrue(ObjectUtil.equals(entity.getB1(),deserialized.getB1()));
    }

}
