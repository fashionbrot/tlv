package com.github.fashionbrot;

import com.github.fashionbrot.tlv.TLVUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;

public class LocalTimeTest {

    @Data
    public static class LocalTimeEntity{
        private LocalTime a1;
        private LocalTime b1;
    }

    @Test
    public void test1() throws IOException {
        LocalTimeEntity entity=new LocalTimeEntity();
        entity.setA1(LocalTime.MAX);
        entity.setB1(LocalTime.MIN);
        byte[] bytes = TLVUtil.serialize( entity);

        LocalTime max = LocalTime.of(23,59,59,0);
        LocalTime max2= LocalTime.of(23,59,59,999);
        LocalTime min = LocalTime.of(0,0,0,0);

        LocalTimeEntity deserialized = TLVUtil.deserialize(LocalTimeEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(max,deserialized.getA1());
//        Assert.assertEquals(max,deserialized.getA1());
        Assert.assertEquals(min,deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        LocalTimeEntity entity=new LocalTimeEntity();
        entity.setA1(LocalTime.of(01,00,59));//"01:00:59"));
        entity.setB1(null);
        byte[] bytes = TLVUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        LocalTimeEntity deserialized = TLVUtil.deserialize(LocalTimeEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
