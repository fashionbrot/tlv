package com.github.fashionbrot;

import com.alibaba.fastjson2.JSONObject;
import com.github.fashionbrot.tlv.TLVUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListObjectTest {



    @Data
    public static class ListObjectEntity{
        private List<Object> a1;
        private List<Object> b1;
        private List c1;
    }



    @Test
    public void test1() throws IOException {

        List<Object> a1 = Arrays.asList(1, 2, "你好");
        ArrayList b1 = new ArrayList<>();
        b1.add("b1");

        ListObjectEntity beanEntity=new ListObjectEntity();
        beanEntity.setA1(a1);
        beanEntity.setB1(b1);
        beanEntity.setC1(null);


        byte[] bytes = TLVUtil.serialize( beanEntity);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        ListObjectEntity deserialized = TLVUtil.deserialize(ListObjectEntity.class, bytes);
        System.out.println(deserialized);
//        System.out.println(a1.get(0));
        Assert.assertEquals(a1.get(0),deserialized.getA1().get(0));
        Assert.assertEquals(a1.get(1),deserialized.getA1().get(1));
        Assert.assertEquals(a1.get(2),deserialized.getA1().get(2));
        Assert.assertEquals(b1.get(0),deserialized.getB1().get(0));
    }

    @Data
    public static class ListObjectEntity2{
        private List<ListObjectEntity3> aa;
    }

    @Data
    public static class ListObjectEntity3{
        private List<Object> bb;
    }


    @Test
    public void test2() throws IOException {

        ListObjectEntity3 entity3=new ListObjectEntity3();
        entity3.setBb(Arrays.asList("entity3"));

        ListObjectEntity2 entity2=new ListObjectEntity2();
        entity2.setAa(Arrays.asList(entity3));

//        System.out.println(JSON.toJSONString(entity2));
        ListObjectEntity2 listObjectEntity2 = JSONObject.parseObject("{\"aa\":[{\"bb\":[\"entity3\"]}]}", ListObjectEntity2.class);
        System.out.println(listObjectEntity2.toString());

        byte[] bytes = TLVUtil.serialize(entity2);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        ListObjectEntity2 deserialized = TLVUtil.deserialize(ListObjectEntity2.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity2.getAa(),deserialized.getAa());
        Assert.assertEquals(entity2.getAa().get(0).getBb(),deserialized.getAa().get(0).getBb());
    }


}
