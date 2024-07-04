package com.github.fashionbrot;

import com.github.fashionbrot.tlv.TLVUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class LocalDateTest {

    @Data
    public static class LocalDateEntity{
        private LocalDate a1;
        private LocalDate b1;
    }

    @Test
    public void test1() throws IOException {
        LocalDateEntity entity=new LocalDateEntity();
        entity.setA1(LocalDate.MIN);
        entity.setB1(LocalDate.MAX);
        byte[] bytes = TLVUtil.serialize(entity);

        LocalDate max = LocalDate.of(9999, 12, 31);
        LocalDate min = LocalDate.of(0, 1, 1);

        LocalDateEntity deserialized = TLVUtil.deserialize(LocalDateEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(min,deserialized.getA1());
        Assert.assertEquals(max,deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        LocalDateEntity entity=new LocalDateEntity();
        entity.setA1(LocalDate.of(2024,05,05));//"2024-05-05"));
        entity.setB1(null);
        byte[] bytes = TLVUtil.serialize(entity);
        System.out.println(Arrays.toString(bytes));
        LocalDateEntity deserialized = TLVUtil.deserialize(LocalDateEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
