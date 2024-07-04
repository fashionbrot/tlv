package com.github.fashionbrot.tlv;

import com.github.fashionbrot.tlv.parser.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fashionbrot
 */
public class TypeHandleFactory {

    public static final byte[] BYTE_ARRAY_EMPTY = new byte[]{};
    public static final byte[] BYTE_ARRAY_ONE = new byte[1];

    private static Map<Class,TypeHandle> TYPE_HANDLE_MAP=new ConcurrentHashMap<>();
    static {
        addTypeHandle(new IntegerTypeHandle(),Integer.class,int.class);
        addTypeHandle(new ByteTypeHandle(),byte.class,Byte.class);
        addTypeHandle(new BooleanTypeHandle(),boolean.class,Boolean.class);
        addTypeHandle(new CharTypeHandle(),char.class,Character.class);
        addTypeHandle(new ShortTypeHandle(),short.class,Short.class);
        addTypeHandle(new FloatTypeHandle(),float.class,Float.class);
        addTypeHandle(new LongTypeHandle(),long.class,Long.class);
        addTypeHandle(new StringTypeHandle(),String.class,CharSequence.class);
        addTypeHandle(new DoubleTypeHandle(),double.class,Double.class);
        addTypeHandle(new BigDecimalTypeHandle(), BigDecimal.class);
        addTypeHandle(new DateTypeHandle(),Date.class);
        addTypeHandle(new LocalTimeTypeHandle(), LocalTime.class);
        addTypeHandle(new LocalDateTypeHandle(), LocalDate.class);
        addTypeHandle(new LocalDateTimeTypeHandle(), LocalDateTime.class);
    }

    public static void addTypeHandle(TypeHandle handle, Class... classes){
        if (classes!=null && classes.length>0){
            for (Class clazz : classes) {
                if (!TYPE_HANDLE_MAP.containsKey(clazz)){
                    TYPE_HANDLE_MAP.put(clazz,handle);
                }
            }
        }
    }

    public static TypeHandle getTypeHandle(Class type){
        TypeHandle typeHandle = TYPE_HANDLE_MAP.get(type);
        if (typeHandle!=null){
            return typeHandle;
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    public static Object toJava(Class type,byte[] bytes){
        if (bytes==null || bytes.length==0) {
            return getDefaultForType(type);
        }
        TypeHandle typeHandle = getTypeHandle(type);
        return typeHandle.toJava(bytes);
    }

    public static byte[] toByte(Class type,Object value){
        if (value==null){
            return BYTE_ARRAY_EMPTY;
        }
        TypeHandle typeHandle = getTypeHandle(type);
        return typeHandle.toByte(value);
    }

    private static Object getDefaultForType(Class<?> type) {
        if (isPrimitive(type)) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\0';
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0f;
            if (type == double.class) return 0d;
        }
        return null;
    }

    public static boolean isPrimitive(Class type){
        return TYPE_HANDLE_MAP.containsKey(type);
    }

}
