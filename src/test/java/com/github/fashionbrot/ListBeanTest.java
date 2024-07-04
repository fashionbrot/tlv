package com.github.fashionbrot;

import com.github.fashionbrot.entity.Test1;
import com.github.fashionbrot.entity.Test1Child;
import com.github.fashionbrot.tlv.TLVUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBeanTest {

    @Data
    public static class ListChildEntity{
        private String abc;
    }

    @Data
    public static class ListBeanEntity{
        private List<ListChildEntity> a1;
        private List<ListChildEntity> b1;
//        private List<ListChildEntity> c1;
    }

    @Test
    public void test1() throws IOException {

        ListChildEntity childEntity=new ListChildEntity();
        childEntity.setAbc("1");

        ListBeanEntity beanEntity=new ListBeanEntity();
        beanEntity.setA1(Arrays.asList(childEntity));
        beanEntity.setB1(new ArrayList<>());
//        beanEntity.setC1(null);


        byte[] bytes = TLVUtil.serialize( beanEntity);
        System.out.println(Arrays.toString(bytes));

        ListBeanEntity deserialized = TLVUtil.deserialize(ListBeanEntity.class, bytes);
        System.out.println(deserialized);
//        Assert.assertEquals(beanEntity.getA1().get(0).getAbc(),childEntity.getAbc());
    }


    @Data
    public static class ListBeanEntity2{
        private List<ListChildEntity> a1;
    }


    @Test
    public void test2()  {

        ListChildEntity childEntity=new ListChildEntity();
        childEntity.setAbc("111111");
        ListChildEntity childEntity2=new ListChildEntity();
        childEntity2.setAbc("222222");


        byte[] bytes = TLVUtil.serialize(Arrays.asList(childEntity,childEntity2));
        System.out.println(Arrays.toString(bytes));

        List<ListChildEntity> deserialized = TLVUtil.deserializeList(ListChildEntity.class, (bytes));
        System.out.println(deserialized);
        Assert.assertEquals(childEntity.getAbc(),deserialized.get(0).getAbc());
        Assert.assertEquals(childEntity2.getAbc(),deserialized.get(1).getAbc());
    }



    @Test
    public void test3(){
        Test1 t=new Test1();
        t.setA1("1");

        Test1Child child=new Test1Child();
        child.setB1("2");

        t.setC1(child);
//        t.setList1(Arrays.asList(child));

        byte[] serialize = TLVUtil.serialize(t);
        System.out.println(Arrays.toString(serialize));
        System.out.println(serialize.length);

        Test1 deserialize = TLVUtil.deserialize(Test1.class, serialize);
        System.out.println(deserialize);
        Assert.assertEquals(t.getA1(),deserialize.getA1());
        Assert.assertEquals(t.getC1().getB1(),deserialize.getC1().getB1());
    }



}
