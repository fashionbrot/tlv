package com.github.fashionbrot;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.fashionbrot.entity.TestEntity;
import com.github.fashionbrot.entity.TestEntityProto;
import com.github.fashionbrot.tlv.TLVUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PerformanceTest {

//    @Rule
//    public Stopwatch stopwatch = new Stopwatch() {
//        @Override
//        protected void succeeded(long nanos, Description description) {
//            long millis = nanos / 1000000; // 将纳秒转换为毫秒
//            long seconds = millis / 1000;  // 将毫秒转换为秒
//            millis = millis % 1000;        // 计算剩余的毫秒数
//
//            System.out.println(description.getMethodName() + " took " + seconds + " 秒 " + millis + " 毫秒");
//        }
//    };

    private static final String maxString= "1233456789009876544321qazwsxedcrfvtgbyhnujmik,ol./;p[]TLV（Type-Length-Value）序列化是一种常见的数据编码格式，通常用于在计算机网络通信和存储中对数据进行结构化编码和解码。\n" +
            "\n" +
            "TLV 的含义解释如下：\n" +
            "\n" +
            "Type：表示数据项的类型，用于标识数据项的含义或类别。通常是一个数值或者一个固定长度的标识符，它告诉解析器接下来是什么类型的数据。\n" +
            "\n" +
            "Length：表示数据项的长度，即数据部分的字节数或者元素个数。它告诉解析器要读取多少字节的数据。\n" +
            "\n" +
            "Value：实际的数据内容，根据前面的 Length 字段指定的长度来确定。TLV 序列化通常用于构建复杂的数据结构，例如网络协议消息、存储系统中的数据记录等。它的优点包括灵活性和简单性，因为每个数据项都包含自己的类型和长度信息，可以方便地解析和处理。\n" +
            "\n" +
            "示例\n" +
            "假设有一个 TLV 数据结构，其中包含多个数据项：\n";

    private static final TestEntity template = TestEntity.builder()
            .id(22L)
            .name(maxString)
            .parentId(33L)
            .parentName("父节点")
            .test5(55L)
            .test6(66L)
            .test7("test7")
            .test8(88L)
            .test9(99L)
            .test10("aa")
            .build();

    private static final int count = 100000;



    @Test
    public void jsonTest(){
        long startTime = System.currentTimeMillis();
        System.out.println();
        for (int i = count; i > 0; i--) {
            TestEntity build = TestEntity.builder()
                    .id(template.getId())
                    .name(template.getName())
                    .parentId(template.getParentId())
                    .parentName(template.getParentName())
                    .test5(template.getTest5())
                    .test6(template.getTest6())
                    .test7(template.getTest7())
                    .test8(template.getTest8())
                    .test9(template.getTest9())
                    .test10(template.getTest10())
                    .build();

            String jsonString = JSON.toJSONString(build);
            if (i==1) {
                System.out.println("json序列化 字节长度:" + jsonString.getBytes(StandardCharsets.UTF_8).length);
            }
            TestEntity deserialize = JSONObject.parseObject(jsonString, TestEntity.class);
            if (deserialize==null){
                System.out.println("deserialize is null");
            }
        }


        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    @Test
    public void tlvTest(){
        System.out.println();
        long startTime = System.currentTimeMillis();
        for (int i = count; i > 0; i--) {
            TestEntity build = TestEntity.builder()
                    .id(template.getId())
                    .name(template.getName())
                    .parentId(template.getParentId())
                    .parentName(template.getParentName())
                    .test5(template.getTest5())
                    .test6(template.getTest6())
                    .test7(template.getTest7())
                    .test8(template.getTest8())
                    .test9(template.getTest9())
                    .test10(template.getTest10())
                    .build();
            byte[] serialize = TLVUtil.serialize(build);
            if (i==1){
                System.out.println("tlv 序列化 字节长度:"+serialize.length);
            }

            TestEntity deserialize = TLVUtil.deserialize(TestEntity.class, serialize);
            if (deserialize==null){
                System.out.println("deserialize is null");
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    @Test
    public void tlvCompressTest() throws IOException {
        System.out.println();
        long startTime = System.currentTimeMillis();
        for (int i = count; i > 0; i--) {
            TestEntity build = TestEntity.builder()
                    .id(template.getId())
                    .name(template.getName())
                    .parentId(template.getParentId())
                    .parentName(template.getParentName())
                    .test5(template.getTest5())
                    .test6(template.getTest6())
                    .test7(template.getTest7())
                    .test8(template.getTest8())
                    .test9(template.getTest9())
                    .test10(template.getTest10())
                    .build();

            byte[] serialize = TLVUtil.compressSerialize(build);
            if (i==1){
                System.out.println("tlv压缩 序列化 字节长度:"+serialize.length);
            }

            TestEntity deserialize = TLVUtil.decompressDeserialize(TestEntity.class, serialize);
            if (deserialize==null){
                System.out.println("deserialize is null");
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

    @Test
    public void protobufTest() throws InvalidProtocolBufferException {
        System.out.println();
        long startTime = System.currentTimeMillis();
        for (int i = count; i > 0; i--) {
            TestEntityProto.TestEntity test= TestEntityProto.TestEntity.newBuilder()
                    .setId(template.getId())
                    .setParentId(template.getParentId())
                    .setName(template.getName())
                    .setParentName(template.getParentName())
                    .setTest5(template.getTest5())
                    .setTest6(template.getTest6())
                    .setTest7(template.getTest7())
                    .setTest8(template.getTest8())
                    .setTest9(template.getTest9())
                    .setTest10(template.getTest10())
                    .build();
            byte[] byteArray = test.toByteArray();
            if (i==1) {
                System.out.println("protobuf序列化 字节长度:" + byteArray.length);
            }
            TestEntityProto.TestEntity deserialize = TestEntityProto.TestEntity.parseFrom(byteArray);
            if (deserialize==null){
                System.out.println("deserialize is null");
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("Execution time: " + executionTime + " milliseconds");
    }

}
