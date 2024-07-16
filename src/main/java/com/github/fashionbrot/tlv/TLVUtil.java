package com.github.fashionbrot.tlv;

import com.github.fashionbrot.tlv.annotation.TLVField;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TLVUtil {
    public static final byte[] BYTE_ARRAY_EMPTY = new byte[]{};
    public static final byte[] BYTE_ARRAY_ONE = new byte[1];
    private static final Map<Class, List<Field>> CLASS_CACHE = new ConcurrentHashMap<>();


    public static <T> byte[] compressSerialize(T input) throws IOException {
        byte[] serialize = serialize(input);
        return GzipUtil.compress(serialize);
    }

    /**
     * 序列化
     * @param input  input 输入对象
     * @return       byte数组
     * @param <T>    泛型
     */
    public static <T> byte[] serialize(T input) {
        if (input == null) {
            return null;
        }
        Class<?> inputClass = input.getClass();
        return serialize(input, inputClass);
    }

    public static byte[] serialize(Object input, Class inputClass) {
        List<byte[]> byteList = new ArrayList<>();
        if (TypeHandleFactory.isPrimitive(inputClass)) {
            byteList.add(primitiveToBytes(input, inputClass));
        } else if (List.class.isAssignableFrom(inputClass)) {
            byteList.add(listToBytes(input));
        } else if (inputClass.isArray()) {
            byteList.add(arrayToBytes(input));
        } else {
            byteList.add(entityToBytes(input));
        }
        return mergeByteArrayList(byteList);
    }


    private static byte[] primitiveToBytes(Object input, Class inputClass) {
        byte[] valueBytes = TypeHandleFactory.toByte(inputClass, input);
        return createTLV(valueBytes, inputClass);
    }

    private static byte[] listOrArrayToByte(byte[] listValue, Class type) {
        return createTLV(listValue, type);
    }

    private static byte[] createTLV(byte[] valueBytes, Class type) {
        byte tag = generateTag(type, valueBytes);
        // 计算总长度
        byte[] lengthBytes = TLVTypeUtil.encodeVarInteger(valueBytes.length);
        int totalLength = 1 + lengthBytes.length + valueBytes.length;
        // 创建结果数组
        byte[] result = new byte[totalLength];
        // 填充结果数组
        int currentIndex = 0;
        // 添加 tag
        result[currentIndex++] = tag;
        // 添加 length
        for (byte b : lengthBytes) {
            result[currentIndex++] = b;
        }
        // 添加 valueBytes
        for (byte b : valueBytes) {
            result[currentIndex++] = b;
        }
        return result;
    }

    private static byte[] entityToBytes(Object input) {
        Class<?> inputClass = input.getClass();
        List<Field> fieldList = getSortedClassField(inputClass);
        if (fieldList == null || fieldList.isEmpty()) {
            return BYTE_ARRAY_EMPTY;
        }

        List<byte[]> byteList = new ArrayList<>();
        for (Field field : fieldList) {
            TLVField annotation = field.getAnnotation(TLVField.class);
            if (annotation != null && !annotation.serialize()) {
                continue;
            }
            Class<?> fieldType = field.getType();
            if (List.class.isAssignableFrom(fieldType)) {
                Object fieldValue = getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue, fieldType);
                byteList.add(listOrArrayToByte(serialize, fieldType));
            } else if (fieldType.isArray()) {
                Object fieldValue = getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue, fieldType);
                byteList.add(listOrArrayToByte(serialize, fieldType));
            } else {
                Object fieldValue = getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue, fieldType);
                byteList.add(serialize);
            }


        }
        return mergeByteArrayList(byteList);
    }

    private static byte[] listToBytes(Object input) {
        if (input == null) {
            return null;
        }

        List<Object> objectList = (List<Object>) input;
        if (objectList != null && objectList.size() == 0) {
            return BYTE_ARRAY_ONE;
        }
        List<byte[]> byteList = new ArrayList<>();
        for (Object obj : objectList) {
            byteList.add(serialize(obj, obj.getClass()));
        }
        return mergeByteArrayList(byteList);
    }

    private static byte[] arrayToBytes(Object input) {
        if (input == null) {
            return null;
        }
        Object[] arrayValue = (Object[]) input;
        if (arrayValue != null && arrayValue.length == 0) {
            return BYTE_ARRAY_ONE;
        }
        List<byte[]> byteList = new ArrayList<>();
        for (Object array : arrayValue) {
            byteList.add(serialize(array, array.getClass()));
        }
        return mergeByteArrayList(byteList);
    }


    public static byte generateTag(Class classType, byte[] valueBytes) {
        BinaryType binaryType = BinaryType.getBinaryType(classType);
        byte valueByteLengthBinary = BinaryCodeLength.getBinaryCode(TLVTypeUtil.encodeVarInteger(valueBytes!=null ? valueBytes.length : 0).length);
        return combineBitsIntoByte(binaryType.getBinaryCode() ,valueByteLengthBinary);
    }


    public static List<Field> getSortedClassField(Class clazz) {
        List<Field> cacheListField = CLASS_CACHE.get(clazz);
        if (cacheListField != null) {
            return cacheListField;
        }
        List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(clazz);
        List<FieldModel> superClassField = getSuperClassField(clazz);
        if (isNotEmpty(superClassField)) {
            classFieldList.addAll(superClassField);
        }
        Collections.sort(classFieldList, Comparator.comparing(FieldModel::getIndex).thenComparing(f -> f.getField().getName()));
        List<Field> fieldList = new ArrayList<>();
        if (isNotEmpty(classFieldList)) {
            for (FieldModel fieldModel : classFieldList) {
                fieldList.add(fieldModel.getField());
            }
        }
        CLASS_CACHE.putIfAbsent(clazz, fieldList);
        return fieldList;
    }


    public static List<FieldModel> getNonStaticNonFinalFieldModels(Class<?> clazz) {
        List<FieldModel> fieldList = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (isStaticOrFinal(declaredField)) {
                continue;
            }
            TLVField annotation = declaredField.getAnnotation(TLVField.class);
            if (annotation != null && !annotation.serialize()) {
                continue;
            }
            int index = annotation != null ? annotation.index() : Integer.MAX_VALUE;
            FieldModel fieldModel=new FieldModel();
            fieldModel.setField(declaredField);
            fieldModel.setIndex(index);
            fieldList.add(fieldModel);
        }
        return fieldList;
    }

    public static List<FieldModel> getSuperClassField(Class clazz) {
        Class superclass = clazz.getSuperclass();
        if (superclass != null && isNotObject(superclass)) {
            List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(superclass);

            List<FieldModel> superClassFieldList = getSuperClassField(superclass);
            if (isNotEmpty(superClassFieldList)) {
                classFieldList.addAll(superClassFieldList);
            }
            return classFieldList;
        }
        return null;
    }


//    /** ------------------------------------------反序列化-----------------------------------------------**/


    public static <T> T decompressDeserialize(Class<T> deserializeClass, byte[] data) throws IOException {
        byte[] bytes = GzipUtil.decompressToByte(data);
        return deserialize(deserializeClass,bytes);
    }
    /**
     * 反序列化
     * @param deserializeClass 反序列化Class
     * @param data             byte数组
     * @return                 Class对应的对象
     * @param <T>              泛型
     */
    public static <T> T deserialize(Class<T> deserializeClass, byte[] data) {
        return deserialize(deserializeClass, deserializeClass, new ByteArrayReader(data));
    }

    public static <T> T deserialize(Class type, Class<T> deserializeClass, ByteArrayReader reader) {
        if (reader.isReadComplete()) {
            return null;
        }

        if (TypeHandleFactory.isPrimitive(type)) {
            byte[] nextBytes = getNextBytes(reader);
            return (T) TypeHandleFactory.toJava(deserializeClass, nextBytes);
        } else if (type.isAssignableFrom(Object.class)) {
            byte[] nextBytes = getNextBytes(reader);
            return (T) TypeHandleFactory.toJava(reader.getLastBinaryType().getType()[0], nextBytes);
        } else if (List.class.isAssignableFrom(type)) {
            return (T) deserializeList(deserializeClass, reader);
        } else if (type.isArray()) {
            return (T) deserializeArray(deserializeClass, reader);
        } else {
            return deserializeEntity(deserializeClass, reader);
        }
    }

    public static <T> T decompressDeserializeList(Class<T> deserializeClass, byte[] data) throws IOException {
        byte[] bytes = GzipUtil.decompressToByte(data);
        return (T) deserializeList(deserializeClass, new ByteArrayReader(bytes));
    }

    public static <T> List<T> deserializeList(Class<T> clazz, byte[] bytes) {
        return deserializeList(clazz, new ByteArrayReader(bytes));
    }

    public static <T> List<T> deserializeList(Class<T> clazz, ByteArrayReader reader) {
        List<T> list = new ArrayList<>();
        if (reader.isCollectionEmpty()) {
            return list;
        }
        while (!reader.isReadComplete()) {
            list.add(deserialize(clazz, clazz, reader));
        }
        return list;
    }

    public static <T> T[] decompressDeserializeArray(Class<T> deserializeClass, byte[] data) throws IOException {
        byte[] bytes = GzipUtil.decompressToByte(data);
        return deserializeArray(deserializeClass, new ByteArrayReader(bytes));
    }

    public static <T> T[] deserializeArray(Class<T> clazz, byte[] bytes) {
        return deserializeArray(clazz, new ByteArrayReader(bytes));
    }

    public static <T> T[] deserializeArray(Class<T> clazz, ByteArrayReader reader) {
        if (reader.isCollectionEmpty()) {
            return newArrayInstance(clazz, 0);
        }
        List<Object> list = new ArrayList<>();
        while (!reader.isReadComplete()) {
            list.add(deserialize(clazz, clazz, reader));
        }
        return list.toArray(newArrayInstance(clazz, list.size()));
    }


    public static <T> T deserializeEntity(Class<T> clazz, ByteArrayReader reader) {
        T instance = newInstance(clazz);
        List<Field> fieldList = getSortedClassField(clazz);
        if (isEmpty(fieldList)) {
            return instance;
        }
        for (Field field : fieldList) {
            if (reader.isReadComplete()) {
                break;
            }

            TLVField annotation = field.getAnnotation(TLVField.class);
            if (annotation != null && !annotation.serialize()) {
                continue;
            }

            Class type = field.getType();
            Class deserializeClass = field.getType();
            if (List.class.isAssignableFrom(type)) {
                deserializeClass = getListGenericClass(field);
                byte[] nextBytes = getNextBytes(reader);
                Object fieldValue = deserialize(type, deserializeClass, new ByteArrayReader(nextBytes));
                setFieldValue(field, instance, fieldValue);
            } else if (type.isArray()) {
                deserializeClass = type.getComponentType();
                byte[] nextBytes = getNextBytes(reader);
                Object fieldValue = deserialize(type, deserializeClass, new ByteArrayReader(nextBytes));
                setFieldValue(field, instance, fieldValue);
            } else {
                Object fieldValue = deserialize(type, deserializeClass, reader);
                setFieldValue(field, instance, fieldValue);
            }

        }
        return instance;
    }

    public static byte[] getNextBytes(ByteArrayReader reader) {
        int readIndex = reader.getLastReadIndex();
        byte firstByte = reader.readFrom(readIndex);
        //第一位byte(前5个bit 是value数据类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
        BinaryType valueType = BinaryType.fromBinaryCode(firstByte);
        int valueByteLengthLength = BinaryCodeLength.getLength(firstByte);

        reader.setLastBinaryType(valueType);

        int valueByteLength = TLVTypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + valueByteLengthLength));
        if (valueByteLength == 0) {
            return BYTE_ARRAY_EMPTY;
        }
        return reader.readFromTo(readIndex + 1 + valueByteLengthLength, readIndex + 1 + valueByteLengthLength + valueByteLength);
    }




    private static Class<?> getListGenericClass(Field field) {
        java.lang.reflect.Type genericType = field.getGenericType();
        if (genericType instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) genericType;
            java.lang.reflect.Type[] fieldArgTypes = pt.getActualTypeArguments();
            if (fieldArgTypes.length > 0 && fieldArgTypes[0] instanceof Class) {
                return (Class<?>) fieldArgTypes[0];
            }
        }
        return Object.class;
    }


    // 将 List 中的 byte[] 数组合并为一个单独的字节数组
    public static byte[] mergeByteArrayList(List<byte[]> list) {
        // 计算总长度
        int totalLength = 0;
        for (byte[] array : list) {
            if (array != null && array.length > 0) {
                totalLength += array.length;
            }
        }
        // 创建结果数组
        byte[] result = new byte[totalLength];
        // 合并字节数组
        int currentIndex = 0;
        for (byte[] array : list) {
            if (array != null && array.length > 0) {
                for (byte b : array) {
                    result[currentIndex++] = b;
                }
            }
        }
        return result;
    }

    public static byte binaryStringToByte(String binaryString) {
        if (binaryString.length() != 8) {
            throw new IllegalArgumentException("Binary string must be 8 bits long");
        }
        return (byte) Integer.parseInt(binaryString, 2);
    }

    public static String byteToBinaryString(byte value) {
        StringBuilder binaryString = new StringBuilder(8);
        for (int i = 7; i >= 0; i--) {
            binaryString.append((value >> i) & 1);
        }
        return binaryString.toString();
    }

    /**
     * 根据 Field 获取 属性值
     * @param field Field
     * @param object Object
     * @return Object
     */
    public static Object getFieldValue(Field field,Object object){
        if (field!=null){
            try {
                //设置可以操作私有成员
                field.setAccessible(true);
                return field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void setFieldValue(Field field,Object object,Object value){
        if (field!=null){
            try {
                //设置可以操作私有成员
                field.setAccessible(true);
                field.set(object,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No default constructor found for " + clazz.getName(), e);
        }
    }

    public static <T> T[] newArrayInstance(Class<T> clazz, int length) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, length);
    }

    /**
     * 判断 Field 是否是 static 或者 final
     * @param field field
     * @return boolean
     */
    public static boolean isStaticOrFinal(Field field){
        return Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers());
    }

    /**
     * 判断数组是否为空。
     *
     * @param objects 待判断的数组
     * @return 如果数组为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * 判断数组是否不为空。
     *
     * @param objects 待判断的数组
     * @return 如果数组不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(final Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * 判断集合是否为空。
     *
     * @param collection 待判断的集合
     * @return 如果集合为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断集合是否不为空。
     *
     * @param collection 待判断的集合
     * @return 如果集合不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断字节数组是否为空。
     *
     * @param bytes 待判断的字节数组
     * @return 如果字节数组为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }

    /**
     * 判断字节数组是否不为空。
     *
     * @param bytes 待判断的字节数组
     * @return 如果字节数组不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(final byte[] bytes) {
        return !isEmpty(bytes);
    }

    public static boolean isObject(Class type){
        return type!=null && type.isAssignableFrom(Object.class);
    }


    public static boolean isNotObject(Class type){
        if (type==null){
            return false;
        }
        return !isObject(type);
    }

    public static byte combineBitsIntoByte(byte first5, byte last3) {
        // 使用 b5 和 b3 进行组装成一个新的字节
        // 将 b5 左移3位后，再与 b3 进行按位或操作
        return  (byte) ((first5 << 3) | last3);
    }
}
