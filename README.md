# java TLV（Tag-Length-Value） 高效的序列化工具，可应用于通信、Redis缓存和消息队列等多种场景中

### 支持类型如下，其他类型待实现
```
Integer.class,int.class
byte.class,Byte.class
boolean.class,Boolean.class
char.class,Character.class
short.class,Short.class
float.class,Float.class
long.class,Long.class
String.class,CharSequence.class
double.class,Double.class
BigDecimal.class
Date.class
LocalTime.class
LocalDate.class
LocalDateTime.class
List<XXX>
数组[XXX]
```

### gradle引入方式
```gradle
implementation "com.github.fashionbrot:tlv:0.0.2"
```

###  tlv 序列化 示例
```java
public class TLVSerializeTest {

    @Test
    public void test9(){

        Test2ChildEntity childEntity=new Test2ChildEntity();
        childEntity.setL1(1L);

        Test2ChildEntity childEntity2=new Test2ChildEntity();
        childEntity2.setL1(2L);

        Test2Entity entity=new Test2Entity();
        entity.setArray(new Test2ChildEntity[]{childEntity2,childEntity});
        entity.setList(Arrays.asList(childEntity,childEntity2));


        PageResponse pageResponse=new PageResponse();
        pageResponse.setRows(Arrays.asList(entity));
        pageResponse.setTotal(1);


        Response response=new Response();
        response.setData(pageResponse);
        response.setCode(0);
        response.setMsg("成功");


        byte[] bytes = TLVUtil.serialize(response);
        System.out.println(Arrays.toString(bytes));
        System.out.println(bytes.length);

        Response deserialize = TLVUtil.deserialize(Response.class, bytes);
        String jsonString = JSON.toJSONString(deserialize);
        System.out.println(jsonString);
        System.out.println(jsonString.getBytes().length);
        Assert.assertTrue(Objects.equals(response,deserialize));
    }

}
```

### 示例1
```java
public class AllTypeTest {
    @Test
    public void test2() throws IOException {

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
        entity.setString2("");
        entity.setStringNull(null);

        entity.setDate1(new Date());
        entity.setDate2(null);

        entity.setLocalTime1(LocalTime.of(12,12,12));
        entity.setLocalTime2(null);

        entity.setLocalDate1(LocalDate.of(2024,06,02));
        entity.setLocalDate2(null);

        entity.setLocalDateTime1(TLVTypeUtil.toLocalDateTime("2024-06-02 15:41:30"));
        entity.setLocalDateTime2(null);

        System.out.println("json:"+JSON.toJSONString(entity));
        System.out.println("json 序列化后 length:"+JSON.toJSONString(entity).getBytes(StandardCharsets.UTF_8).length);


        byte[] serialize = TLVUtil.serialize(entity);
        System.out.println("tlv:"+Arrays.toString(serialize));
        System.out.println("tlv 序列化后 length:"+serialize.length);

        JsEntity1 deserialize = TLVUtil.deserialize(JsEntity1.class, serialize);
        Assert.assertEquals(JSON.toJSONString(entity),JSON.toJSONString(deserialize));

        byte[] compressSerialize = TLVUtil.compressSerialize(entity);
        System.out.println("tlv compress:"+Arrays.toString(compressSerialize));
        System.out.println("tlv 压缩序列化后 length:"+compressSerialize.length);

        JsEntity1 decompressDeserialize = TLVUtil.decompressDeserialize(JsEntity1.class, compressSerialize);
        Assert.assertEquals(JSON.toJSONString(entity),JSON.toJSONString(decompressDeserialize));

    }

    //json:{"bigDecimalMax":999999.1234567,"bigDecimalMin":-999999.1234567,"booleanFalse":false,"booleanTrue":true,"byteMax":127,"byteMin":-128,"charMax":"￿","charMin":" ","date1":"2024-07-13T15:12:07.856+08:00","doubleMax":1.7976931348623157E308,"doubleMin":4.9E-324,"floatMax":12345.123,"floatMin":-54321.656,"intMax":2147483647,"intMin":-2147483648,"localDate1":"2024-06-02","localDateTime1":"2024-06-02 15:41:30","localTime1":"12:12:12","longMax":9223372036854775807,"longMin":-9223372036854775808,"shortMax":32767,"shortMin":-32768,"string1":"你好啊TLVBuffer","string2":""}
    //json 序列化后 length:572
    //tlv:[104, 14, 57, 57, 57, 57, 57, 57, 46, 49, 50, 51, 52, 53, 54, 55, 104, 15, 45, 57, 57, 57, 57, 57, 57, 46, 49, 50, 51, 52, 53, 54, 55, 104, 0, 0, 1, 0, 0, 0, 0, 1, 1, 8, 1, 127, 8, 1, -128, 8, 0, 16, 3, -1, -1, 3, 16, 1, 0, 16, 0, 72, 6, -80, -103, -67, -41, -118, 50, 72, 0, 56, 9, -1, -1, -1, -1, -1, -1, -1, -9, 127, 56, 1, 1, 56, 0, 40, 5, -2, -56, -125, -78, 4, 40, 5, -88, -29, -48, -70, 12, 40, 0, 32, 5, -1, -1, -1, -1, 7, 32, 5, -128, -128, -128, -128, 8, 32, 0, 88, 6, -128, -8, -99, -92, -3, 49, 88, 0, 96, 6, -112, -24, -107, -65, -3, 49, 96, 0, 80, 6, -32, -94, -86, -46, -118, 50, 80, 0, 48, 9, -1, -1, -1, -1, -1, -1, -1, -1, 127, 48, 10, -128, -128, -128, -128, -128, -128, -128, -128, -128, 1, 48, 0, 24, 3, -1, -1, 1, 24, 5, -128, -128, -2, -1, 15, 24, 0, 64, 18, -28, -67, -96, -27, -91, -67, -27, -107, -118, 84, 76, 86, 66, 117, 102, 102, 101, 114, 64, 1, 0, 64, 0]
    //tlv 序列化后 length:213
    //tlv compress:[31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -53, -32, -77, 4, 3, 61, 67, 35, 99, 19, 83, 51, -13, 12, 126, 93, 52, 1, 6, 6, 70, 6, 32, 96, 100, -28, 96, -84, -25, 96, 108, -32, 96, 16, 96, -2, -1, -97, 89, -128, -111, 65, -128, -63, -125, 109, -61, -52, -67, -41, -69, -116, 60, 24, 44, 56, -1, 67, -64, -9, 122, 11, 70, 70, 11, 6, 13, -42, 127, 39, -102, 55, -79, 104, -80, -82, 120, 124, 97, 23, -113, 6, -125, 2, 43, 72, -110, 93, -127, -75, 1, 8, 56, 20, 24, 34, -40, 26, 126, -52, 93, -14, -41, 48, -126, 33, -127, 109, -62, -117, -87, -5, -1, 26, 38, 48, 4, -80, 61, 88, -76, -22, 82, -105, 81, 0, -125, 1, -52, -76, -1, -11, 6, 92, 13, 48, -64, 104, -64, 32, 1, -76, -102, 81, 2, 104, -56, -65, -1, -4, 18, 12, 14, 66, 79, -10, 46, 120, -70, 116, -17, -45, -87, 93, 33, 62, 97, 78, -91, 105, 105, -87, 69, 14, -116, 12, 14, 12, 0, -44, -83, 59, 70, -43, 0, 0, 0]
    //tlv 压缩序列化后 length:197
}
```
### 示例2

```java
public class AllTypeTest {
    @Test
    public void test3() throws IOException {
        String abc = "soflkdsfdls;jflkdjsljflds收到付你好啊你好啊你好啊，，，你好啊你好啊你好啊你好啊你好啊你好啊你好" +
                "啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊" +
                "你好啊你好啊你好啊了款建设大街范德萨啦久啊离开家发多少是的范德萨范德萨dsfdsafdsa1111111111f撒娇法律监督撒" +
                "辣椒发啦电视剧发来的撒娇；撒范德萨发生范德萨打撒菲利克斯较大附件阿萨达萨罗咖啡机达萨罗激发打撒放大算啦撒了发动" +
                "机撒了范德萨发打撒进啦打撒付了款拒收到付量较大萨法到拉萨撒酒疯啦实际付款洒进来发沙发的山卡拉人哦iu企鹅无偶体温" +
                "枪euro饿我去如我饿111111111111111111122222222222222222224444444444444444444444444444444444" +
                "哦iu饿我去肉IE我去扑热哦武器扑热武器哦日配我去让娃都是佛i啊上佛菩萨范德萨破啊u法搜反扒撒u发啥哦对四方坡iU盾" +
                "soflkdsfdls;jflkdjsljflds收到付你好啊你好啊你好啊，，，你好啊你好啊你好啊你好啊你好啊你好啊你好" +
                "啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊" +
                "你好啊你好啊你好啊了款建设大街范德萨啦久啊离开家发多少是的范德萨范德萨dsfdsafdsa1111111111f撒娇法律监督撒" +
                "辣椒发啦电视剧发来的撒娇；撒范德萨发生范德萨打撒菲利克斯较大附件阿萨达萨罗咖啡机达萨罗激发打撒放大算啦撒了发动" +
                "机撒了范德萨发打撒进啦打撒付了款拒收到付量较大萨法到拉萨撒酒疯啦实际付款洒进来发沙发的山卡拉人哦iu企鹅无偶体温" +
                "枪euro饿我去如我饿111111111111111111122222222222222222224444444444444444444444444444444444" +
                "哦iu饿我去肉IE我去扑热哦武器扑热武器哦日配我去让娃都是佛i啊上佛菩萨范德萨破啊u法搜反扒撒u发啥哦对四方坡iU盾";

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
        entity.setString2(abc);
        entity.setStringNull(null);

        entity.setDate1(new Date());
        entity.setDate2(null);

        entity.setLocalTime1(LocalTime.of(12, 12, 12));
        entity.setLocalTime2(null);

        entity.setLocalDate1(LocalDate.of(2024, 06, 02));
        entity.setLocalDate2(null);

        entity.setLocalDateTime1(TLVTypeUtil.toLocalDateTime("2024-06-02 15:41:30"));
        entity.setLocalDateTime2(null);

        System.out.println("json:" + JSON.toJSONString(entity));
        System.out.println("json 序列化后 length:" + JSON.toJSONString(entity).getBytes(StandardCharsets.UTF_8).length);


        byte[] serialize = TLVUtil.serialize(entity);
        System.out.println("tlv:" + Arrays.toString(serialize));
        System.out.println("tlv 序列化后 length:" + serialize.length);

        JsEntity1 deserialize = TLVUtil.deserialize(JsEntity1.class, serialize);
        Assert.assertEquals(JSON.toJSONString(entity), JSON.toJSONString(deserialize));

        byte[] compressSerialize = TLVUtil.compressSerialize(entity);
        System.out.println("tlv compress:" + Arrays.toString(compressSerialize));
        System.out.println("tlv 压缩序列化后 length:" + compressSerialize.length);

        JsEntity1 decompressDeserialize = TLVUtil.decompressDeserialize(JsEntity1.class, compressSerialize);
        Assert.assertEquals(JSON.toJSONString(entity), JSON.toJSONString(decompressDeserialize));

    }

    //json:{"bigDecimalMax":999999.1234567,"bigDecimalMin":-999999.1234567,"booleanFalse":false,"booleanTrue":true,"byteMax":127,"byteMin":-128,"charMax":"￿","charMin":" ","date1":"2024-07-13T15:13:21.825+08:00","doubleMax":1.7976931348623157E308,"doubleMin":4.9E-324,"floatMax":12345.123,"floatMin":-54321.656,"intMax":2147483647,"intMin":-2147483648,"localDate1":"2024-06-02","localDateTime1":"2024-06-02 15:41:30","localTime1":"12:12:12","longMax":9223372036854775807,"longMin":-9223372036854775808,"shortMax":32767,"shortMin":-32768,"string1":"你好啊TLVBuffer","string2":"soflkdsfdls;jflkdjsljflds收到付你好啊你好啊你好啊，，，你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊了款建设大街范德萨啦久啊离开家发多少是的范德萨范德萨dsfdsafdsa1111111111f撒娇法律监督撒辣椒发啦电视剧发来的撒娇；撒范德萨发生范德萨打撒菲利克斯较大附件阿萨达萨罗咖啡机达萨罗激发打撒放大算啦撒了发动机撒了范德萨发打撒进啦打撒付了款拒收到付量较大萨法到拉萨撒酒疯啦实际付款洒进来发沙发的山卡拉人哦iu企鹅无偶体温枪euro饿我去如我饿111111111111111111122222222222222222224444444444444444444444444444444444哦iu饿我去肉IE我去扑热哦武器扑热武器哦日配我去让娃都是佛i啊上佛菩萨范德萨破啊u法搜反扒撒u发啥哦对四方坡iU盾soflkdsfdls;jflkdjsljflds收到付你好啊你好啊你好啊，，，你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊了款建设大街范德萨啦久啊离开家发多少是的范德萨范德萨dsfdsafdsa1111111111f撒娇法律监督撒辣椒发啦电视剧发来的撒娇；撒范德萨发生范德萨打撒菲利克斯较大附件阿萨达萨罗咖啡机达萨罗激发打撒放大算啦撒了发动机撒了范德萨发打撒进啦打撒付了款拒收到付量较大萨法到拉萨撒酒疯啦实际付款洒进来发沙发的山卡拉人哦iu企鹅无偶体温枪euro饿我去如我饿111111111111111111122222222222222222224444444444444444444444444444444444哦iu饿我去肉IE我去扑热哦武器扑热武器哦日配我去让娃都是佛i啊上佛菩萨范德萨破啊u法搜反扒撒u发啥哦对四方坡iU盾"}
    //json 序列化后 length:2620
    //tlv:[104, 14, 57, 57, 57, 57, 57, 57, 46, 49, 50, 51, 52, 53, 54, 55, 104, 15, 45, 57, 57, 57, 57, 57, 57, 46, 49, 50, 51, 52, 53, 54, 55, 104, 0, 0, 1, 0, 0, 0, 0, 1, 1, 8, 1, 127, 8, 1, -128, 8, 0, 16, 3, -1, -1, 3, 16, 1, 0, 16, 0, 72, 6, -95, -37, -63, -41, -118, 50, 72, 0, 56, 9, -1, -1, -1, -1, -1, -1, -1, -9, 127, 56, 1, 1, 56, 0, 40, 5, -2, -56, -125, -78, 4, 40, 5, -88, -29, -48, -70, 12, 40, 0, 32, 5, -1, -1, -1, -1, 7, 32, 5, -128, -128, -128, -128, 8, 32, 0, 88, 6, -128, -8, -99, -92, -3, 49, 88, 0, 96, 6, -112, -24, -107, -65, -3, 49, 96, 0, 80, 6, -32, -94, -86, -46, -118, 50, 80, 0, 48, 9, -1, -1, -1, -1, -1, -1, -1, -1, 127, 48, 10, -128, -128, -128, -128, -128, -128, -128, -128, -128, 1, 48, 0, 24, 3, -1, -1, 1, 24, 5, -128, -128, -2, -1, 15, 24, 0, 64, 18, -28, -67, -96, -27, -91, -67, -27, -107, -118, 84, 76, 86, 66, 117, 102, 102, 101, 114, 65, -128, 16, 115, 111, 102, 108, 107, 100, 115, 102, 100, 108, 115, 59, 106, 102, 108, 107, 100, 106, 115, 108, 106, 102, 108, 100, 115, -26, -108, -74, -27, -120, -80, -28, -69, -104, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -17, -68, -116, -17, -68, -116, -17, -68, -116, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -70, -122, -26, -84, -66, -27, -69, -70, -24, -82, -66, -27, -92, -89, -24, -95, -105, -24, -116, -125, -27, -66, -73, -24, -112, -88, -27, -107, -90, -28, -71, -123, -27, -107, -118, -25, -90, -69, -27, -68, -128, -27, -82, -74, -27, -113, -111, -27, -92, -102, -27, -80, -111, -26, -104, -81, -25, -102, -124, -24, -116, -125, -27, -66, -73, -24, -112, -88, -24, -116, -125, -27, -66, -73, -24, -112, -88, 100, 115, 102, 100, 115, 97, 102, 100, 115, 97, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 102, -26, -110, -110, -27, -88, -121, -26, -77, -107, -27, -66, -117, -25, -101, -111, -25, -99, -93, -26, -110, -110, -24, -66, -93, -26, -92, -110, -27, -113, -111, -27, -107, -90, -25, -108, -75, -24, -89, -122, -27, -119, -89, -27, -113, -111, -26, -99, -91, -25, -102, -124, -26, -110, -110, -27, -88, -121, -17, -68, -101, -26, -110, -110, -24, -116, -125, -27, -66, -73, -24, -112, -88, -27, -113, -111, -25, -108, -97, -24, -116, -125, -27, -66, -73, -24, -112, -88, -26, -119, -109, -26, -110, -110, -24, -113, -78, -27, -120, -87, -27, -123, -117, -26, -106, -81, -24, -66, -125, -27, -92, -89, -23, -103, -124, -28, -69, -74, -23, -104, -65, -24, -112, -88, -24, -66, -66, -24, -112, -88, -25, -67, -105, -27, -110, -106, -27, -107, -95, -26, -100, -70, -24, -66, -66, -24, -112, -88, -25, -67, -105, -26, -65, -128, -27, -113, -111, -26, -119, -109, -26, -110, -110, -26, -108, -66, -27, -92, -89, -25, -82, -105, -27, -107, -90, -26, -110, -110, -28, -70, -122, -27, -113, -111, -27, -118, -88, -26, -100, -70, -26, -110, -110, -28, -70, -122, -24, -116, -125, -27, -66, -73, -24, -112, -88, -27, -113, -111, -26, -119, -109, -26, -110, -110, -24, -65, -101, -27, -107, -90, -26, -119, -109, -26, -110, -110, -28, -69, -104, -28, -70, -122, -26, -84, -66, -26, -117, -110, -26, -108, -74, -27, -120, -80, -28, -69, -104, -23, -121, -113, -24, -66, -125, -27, -92, -89, -24, -112, -88, -26, -77, -107, -27, -120, -80, -26, -117, -119, -24, -112, -88, -26, -110, -110, -23, -123, -110, -25, -106, -81, -27, -107, -90, -27, -82, -98, -23, -103, -123, -28, -69, -104, -26, -84, -66, -26, -76, -110, -24, -65, -101, -26, -99, -91, -27, -113, -111, -26, -78, -103, -27, -113, -111, -25, -102, -124, -27, -79, -79, -27, -115, -95, -26, -117, -119, -28, -70, -70, -27, -109, -90, 105, 117, -28, -68, -127, -23, -71, -123, -26, -105, -96, -27, -127, -74, -28, -67, -109, -26, -72, -87, -26, -98, -86, 101, 117, 114, 111, -23, -91, -65, -26, -120, -111, -27, -114, -69, -27, -90, -126, -26, -120, -111, -23, -91, -65, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, -27, -109, -90, 105, 117, -23, -91, -65, -26, -120, -111, -27, -114, -69, -24, -126, -119, 73, 69, -26, -120, -111, -27, -114, -69, -26, -119, -111, -25, -125, -83, -27, -109, -90, -26, -83, -90, -27, -103, -88, -26, -119, -111, -25, -125, -83, -26, -83, -90, -27, -103, -88, -27, -109, -90, -26, -105, -91, -23, -123, -115, -26, -120, -111, -27, -114, -69, -24, -82, -87, -27, -88, -125, -23, -125, -67, -26, -104, -81, -28, -67, -101, 105, -27, -107, -118, -28, -72, -118, -28, -67, -101, -24, -113, -87, -24, -112, -88, -24, -116, -125, -27, -66, -73, -24, -112, -88, -25, -96, -76, -27, -107, -118, 117, -26, -77, -107, -26, -112, -100, -27, -113, -115, -26, -119, -110, -26, -110, -110, 117, -27, -113, -111, -27, -107, -91, -27, -109, -90, -27, -81, -71, -27, -101, -101, -26, -106, -71, -27, -99, -95, 105, 85, -25, -101, -66, 115, 111, 102, 108, 107, 100, 115, 102, 100, 108, 115, 59, 106, 102, 108, 107, 100, 106, 115, 108, 106, 102, 108, 100, 115, -26, -108, -74, -27, -120, -80, -28, -69, -104, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -17, -68, -116, -17, -68, -116, -17, -68, -116, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -67, -96, -27, -91, -67, -27, -107, -118, -28, -70, -122, -26, -84, -66, -27, -69, -70, -24, -82, -66, -27, -92, -89, -24, -95, -105, -24, -116, -125, -27, -66, -73, -24, -112, -88, -27, -107, -90, -28, -71, -123, -27, -107, -118, -25, -90, -69, -27, -68, -128, -27, -82, -74, -27, -113, -111, -27, -92, -102, -27, -80, -111, -26, -104, -81, -25, -102, -124, -24, -116, -125, -27, -66, -73, -24, -112, -88, -24, -116, -125, -27, -66, -73, -24, -112, -88, 100, 115, 102, 100, 115, 97, 102, 100, 115, 97, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 102, -26, -110, -110, -27, -88, -121, -26, -77, -107, -27, -66, -117, -25, -101, -111, -25, -99, -93, -26, -110, -110, -24, -66, -93, -26, -92, -110, -27, -113, -111, -27, -107, -90, -25, -108, -75, -24, -89, -122, -27, -119, -89, -27, -113, -111, -26, -99, -91, -25, -102, -124, -26, -110, -110, -27, -88, -121, -17, -68, -101, -26, -110, -110, -24, -116, -125, -27, -66, -73, -24, -112, -88, -27, -113, -111, -25, -108, -97, -24, -116, -125, -27, -66, -73, -24, -112, -88, -26, -119, -109, -26, -110, -110, -24, -113, -78, -27, -120, -87, -27, -123, -117, -26, -106, -81, -24, -66, -125, -27, -92, -89, -23, -103, -124, -28, -69, -74, -23, -104, -65, -24, -112, -88, -24, -66, -66, -24, -112, -88, -25, -67, -105, -27, -110, -106, -27, -107, -95, -26, -100, -70, -24, -66, -66, -24, -112, -88, -25, -67, -105, -26, -65, -128, -27, -113, -111, -26, -119, -109, -26, -110, -110, -26, -108, -66, -27, -92, -89, -25, -82, -105, -27, -107, -90, -26, -110, -110, -28, -70, -122, -27, -113, -111, -27, -118, -88, -26, -100, -70, -26, -110, -110, -28, -70, -122, -24, -116, -125, -27, -66, -73, -24, -112, -88, -27, -113, -111, -26, -119, -109, -26, -110, -110, -24, -65, -101, -27, -107, -90, -26, -119, -109, -26, -110, -110, -28, -69, -104, -28, -70, -122, -26, -84, -66, -26, -117, -110, -26, -108, -74, -27, -120, -80, -28, -69, -104, -23, -121, -113, -24, -66, -125, -27, -92, -89, -24, -112, -88, -26, -77, -107, -27, -120, -80, -26, -117, -119, -24, -112, -88, -26, -110, -110, -23, -123, -110, -25, -106, -81, -27, -107, -90, -27, -82, -98, -23, -103, -123, -28, -69, -104, -26, -84, -66, -26, -76, -110, -24, -65, -101, -26, -99, -91, -27, -113, -111, -26, -78, -103, -27, -113, -111, -25, -102, -124, -27, -79, -79, -27, -115, -95, -26, -117, -119, -28, -70, -70, -27, -109, -90, 105, 117, -28, -68, -127, -23, -71, -123, -26, -105, -96, -27, -127, -74, -28, -67, -109, -26, -72, -87, -26, -98, -86, 101, 117, 114, 111, -23, -91, -65, -26, -120, -111, -27, -114, -69, -27, -90, -126, -26, -120, -111, -23, -91, -65, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, -27, -109, -90, 105, 117, -23, -91, -65, -26, -120, -111, -27, -114, -69, -24, -126, -119, 73, 69, -26, -120, -111, -27, -114, -69, -26, -119, -111, -25, -125, -83, -27, -109, -90, -26, -83, -90, -27, -103, -88, -26, -119, -111, -25, -125, -83, -26, -83, -90, -27, -103, -88, -27, -109, -90, -26, -105, -91, -23, -123, -115, -26, -120, -111, -27, -114, -69, -24, -82, -87, -27, -88, -125, -23, -125, -67, -26, -104, -81, -28, -67, -101, 105, -27, -107, -118, -28, -72, -118, -28, -67, -101, -24, -113, -87, -24, -112, -88, -24, -116, -125, -27, -66, -73, -24, -112, -88, -25, -96, -76, -27, -107, -118, 117, -26, -77, -107, -26, -112, -100, -27, -113, -115, -26, -119, -110, -26, -110, -110, 117, -27, -113, -111, -27, -107, -91, -27, -109, -90, -27, -81, -71, -27, -101, -101, -26, -106, -71, -27, -99, -95, 105, 85, -25, -101, -66, 64, 0]
    //tlv 序列化后 length:2261
    //tlv compress:[31, -117, 8, 0, 0, 0, 0, 0, 0, 0, -19, 83, 61, 76, 19, 97, 24, -2, 46, -40, 90, -115, -111, -58, -87, 35, 35, -117, -122, 86, 84, -116, 11, -102, -104, 96, -30, -64, -96, -122, 17, 19, 104, 0, 73, 72, 104, 58, 83, 40, -123, -2, -128, -91, 71, 127, 40, 71, -111, 74, 91, 90, 34, 45, 32, 86, -6, 119, -115, -101, -93, 113, 113, 112, 49, -15, -34, -17, -18, 38, 70, 93, -96, -66, 95, 15, 46, 68, 18, -35, -107, 39, 119, -19, -13, 126, -33, -5, 127, -17, 59, 116, -11, 110, 19, 55, -84, -74, -101, -99, -73, 110, -33, 25, 106, -67, -2, -37, 1, 33, 28, 65, 112, -100, -119, -101, 48, 113, 46, 19, 49, -73, 52, 26, 45, 102, -114, -104, 73, -113, 49, -7, -11, -61, 23, -65, -83, -121, 116, 93, 106, 104, -8, 49, -47, -59, 113, 93, -92, -35, 112, -12, -47, -67, 123, -95, -35, -112, -3, -2, -87, 124, -91, -99, -76, 25, -40, -27, -59, 54, -125, 11, 97, 106, 35, 125, 70, -41, -49, 68, -22, -48, -38, 71, -6, -115, 11, 74, -92, 126, 104, -19, 39, -67, -58, 111, -81, 55, 63, -5, 109, -67, -92, -29, -60, 91, 99, -94, -29, -78, -21, 4, 92, 7, -79, 96, 104, -50, -126, 78, -114, 26, -83, 22, -46, 125, 77, -86, -83, 65, -70, 6, 17, -1, -109, -57, -49, 30, 56, -19, -10, -63, -15, -5, 46, -77, 99, -52, 62, -6, 98, -64, 97, 31, 24, 117, -36, 27, 97, 116, -60, 49, -118, -1, 3, 14, 26, 46, -126, 119, 91, -86, 44, -23, 102, 103, -55, 65, 117, 78, 123, -2, -96, -13, -17, -109, -14, 12, 125, 35, 66, -91, -84, -28, 69, 72, 109, 40, -55, -104, 50, -25, 6, -15, -67, -78, -112, -123, 72, 70, 42, 121, 80, 71, -50, 84, -96, -22, -126, 124, 17, -126, 33, 72, 45, -61, 118, -120, 46, 21, -28, -27, 105, 93, 83, 39, -20, 83, 56, -98, -77, -41, -86, -61, 78, 121, 30, -78, -77, -12, 109, 4, -60, -128, 44, -124, -28, -60, 58, -98, 40, -30, 58, 77, -15, -52, 97, 36, 35, -121, -33, 41, 27, 51, -32, -37, 64, -111, 38, -46, -24, 89, 51, 57, -88, 10, 76, 83, -49, 39, 24, -110, -61, -81, 116, -111, -6, 22, -39, 109, 112, 23, -68, 57, -16, 4, 104, -76, -96, -120, 110, 44, 65, -115, 79, 75, -107, -94, -70, 84, 103, -119, -119, 34, -2, -54, -75, 24, -16, 81, -120, 36, -23, 74, 89, 63, -95, 117, 23, 11, -41, 116, 66, -61, -84, 118, 57, 31, -61, 100, 80, -60, -98, -80, -60, -4, 89, -44, -41, -60, -45, 57, 28, -57, -83, 11, 76, -71, -55, -39, -104, 53, -37, 72, 3, -68, 62, 120, -22, 108, 80, -53, -121, -91, -118, -75, 123, -73, 105, -64, -57, 56, -49, -85, 30, 94, -114, 22, -48, 28, -14, -85, 106, -36, -125, -54, -52, 118, -113, -7, -60, -14, 89, -120, -35, 56, 43, 118, 121, 26, 118, 118, 96, 62, -119, -122, 82, -71, 12, -117, -103, 97, -89, 84, -99, 84, 75, 30, 26, 91, -125, -55, -94, 84, 91, -92, -5, 57, -70, -70, 57, -24, 28, 31, 83, -45, 117, -22, 13, -63, -53, 10, 100, -90, -112, -96, 104, 61, 11, -37, 89, 116, -2, 21, -51, -72, -70, 123, 101, -54, -9, -24, -95, 70, -87, 47, 36, -69, -73, -16, -102, 110, 101, 32, -98, -43, 68, -115, -77, -61, 88, 90, -11, -52, 31, 27, -27, 115, -112, 117, -85, -18, 26, -50, -115, 84, 19, -122, -39, -36, -19, -29, 12, 10, 74, 48, 119, 122, 124, -28, -75, 61, -68, 114, 98, -69, -24, -62, 10, 4, -25, -87, -113, -57, 118, 57, -101, 83, -110, 70, -97, 80, 40, -127, 32, -48, 104, 9, 18, -55, -31, -89, -78, 32, -98, -17, -1, -7, -2, -97, -17, -1, -1, -69, -1, -35, -28, 23, 50, 119, 16, -44, -43, 8, 0, 0]
    //tlv 压缩序列化后 length:696
}
```
