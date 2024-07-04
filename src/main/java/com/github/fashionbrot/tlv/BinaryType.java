package com.github.fashionbrot.tlv;


import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fashionbrot
 */

public  enum BinaryType{

    /**
     * 00000 (0)
     * 00001 (1)
     * 00010 (2)
     * 00011 (3)
     * 00100 (4)
     * 00101 (5)
     * 00110 (6)
     * 00111 (7)
     * 01000 (8)
     * 01001 (9)
     * 01010 (10)
     * 01011 (11)
     * 01100 (12)
     * 01101 (13)
     * 01110 (14)
     * 01111 (15)
     *
     * 10000 (16)
     * 10001 (17)
     * 10010 (18)
     * 10011 (19)
     * 10100 (20)
     * 10101 (21)
     * 10110 (22)
     * 10111 (23)
     * 11000 (24)
     * 11001 (25)
     * 11010 (26)
     * 11011 (27)
     * 11100 (28)
     * 11101 (29)
     * 11110 (30)
     * 11111 (31)
     */
    BOOLEAN("00000",new Class[]{boolean.class,Boolean.class}),
    BYTE("00001",new Class[]{byte.class,Byte.class}),
    CHAR("00010",new Class[]{char.class,Character.class}),
    SHORT("00011",new Class[]{short.class,Short.class}),
    INTEGER("00100",new Class[]{int.class,Integer.class}),
    FLOAT("00101",new Class[]{float.class,Float.class}),
    LONG("00110",new Class[]{long.class,Long.class}),
    DOUBLE("00111",new Class[]{double.class,Double.class}),
    STRING("01000",new Class[]{String.class,CharSequence.class}),
    DATE("01001", new Class[]{Date.class}),
    LOCAL_TIME("01010",new Class[]{LocalTime.class}),
    LOCAL_DATE("01011", new Class[]{LocalDate.class}),
    LOCAL_DATE_TIME("01100", new Class[]{LocalDateTime.class}),
    BIG_DECIMAL("01101",new Class[]{ BigDecimal.class}),
    ARRAY("01110", new Class[]{Array.class}),
    LIST("01111", new Class[]{List.class}),
    ;



    private final String binaryCode;
    private final Class[] type;

    BinaryType(String binaryCode, Class[] type) {
        this.binaryCode = binaryCode;
        this.type = type;
    }

    public String getBinaryCode() {
        return binaryCode;
    }

    public Class[] getType() {
        return type;
    }

    private static final Map<String, BinaryType> BINARY_CODE_MAP = new HashMap<>();
    private static final Map<Class<?>, BinaryType> TYPE_MAP = new HashMap<>();

    static {
        for (BinaryType binaryType : values()) {
            BINARY_CODE_MAP.put(binaryType.getBinaryCode(), binaryType);
            for (int i = 0; i < binaryType.getType().length; i++) {
                TYPE_MAP.put(binaryType.getType()[i], binaryType);
            }
        }
    }

    /**
     * Retrieves the BinaryType corresponding to the given binary code.
     *
     * @param binaryCode the binary code as a string
     * @return the corresponding BinaryType
     * @throws IllegalArgumentException if no BinaryType is found for the given binary code
     */
    public static BinaryType fromBinaryCode(String binaryCode) {
        BinaryType result = BINARY_CODE_MAP.get(binaryCode);
        if (result == null) {
            throw new IllegalArgumentException("No enum constant found for binary code: " + binaryCode);
        }
        return result;
    }

    /**
     * Retrieves the BinaryType corresponding to the given Java class.
     *
     * @param type the Java class
     * @return the corresponding BinaryType
     * @throws IllegalArgumentException if the type is not supported
     */
    public static BinaryType getBinaryType(Class<?> type) {
        BinaryType binaryType = TYPE_MAP.get(type);
        if (binaryType != null) {
            return binaryType;
        }
        if (List.class.isAssignableFrom(type)) {
            return LIST;
        }
        if (type.isArray()) {
            return ARRAY;
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }
}
