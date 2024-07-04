package com.github.fashionbrot;

import com.github.fashionbrot.tlv.TLVUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CharTest {

    @Data
    public static class CharEntity{
        private Character a1;
        private char b1;
    }

    @Test
    public void test1() throws IOException {
        CharEntity entity=new CharEntity();
        entity.setA1(Character.MAX_VALUE);
        entity.setB1(Character.MIN_VALUE);
        byte[] bytes = TLVUtil.serialize( entity);

        CharEntity deserialized = TLVUtil.deserialize(CharEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        CharEntity entity=new CharEntity();
        entity.setA1(null);
        entity.setB1('A');
        byte[] bytes = TLVUtil.serialize( entity);

        CharEntity deserialized = TLVUtil.deserialize(CharEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
