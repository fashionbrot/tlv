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

public enum BinaryType2 {

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
    BOOLEAN((byte) 0b00000, new Class[]{boolean.class, Boolean.class}),
    BYTE((byte) 0b00001, new Class[]{byte.class, Byte.class}),
    CHAR((byte) 0b00010, new Class[]{char.class, Character.class}),
    SHORT((byte) 0b00011, new Class[]{short.class, Short.class}),
    INTEGER((byte) 0b00100, new Class[]{int.class, Integer.class}),
    FLOAT((byte) 0b00101, new Class[]{float.class, Float.class}),
    LONG((byte) 0b00110, new Class[]{long.class, Long.class}),
    DOUBLE((byte) 0b00111, new Class[]{double.class, Double.class}),
    STRING((byte) 0b01000, new Class[]{String.class, CharSequence.class}),
    DATE((byte) 0b01001, new Class[]{Date.class}),
    LOCAL_TIME((byte) 0b01010, new Class[]{LocalTime.class}),
    LOCAL_DATE((byte) 0b01011, new Class[]{LocalDate.class}),
    LOCAL_DATE_TIME((byte) 0b01100, new Class[]{LocalDateTime.class}),
    BIG_DECIMAL((byte) 0b01101, new Class[]{BigDecimal.class}),
    ARRAY((byte) 0b01110, new Class[]{Array.class}),
    LIST((byte) 0b01111, new Class[]{List.class}),
    ;

    private final byte binaryCode;
    private final Class[] type;

    BinaryType2(byte binaryCode, Class[] type) {
        this.binaryCode = binaryCode;
        this.type = type;
    }

    public byte getBinaryCode() {
        return binaryCode;
    }

    public Class[] getType() {
        return type;
    }

    private static final Map<Byte, BinaryType2> BINARY_CODE_MAP = new HashMap<>();
    private static final Map<Class<?>, BinaryType2> TYPE_MAP = new HashMap<>();

    static {
        for (BinaryType2 binaryType : values()) {
            BINARY_CODE_MAP.put(binaryType.getBinaryCode(), binaryType);
            for (int i = 0; i < binaryType.getType().length; i++) {
                TYPE_MAP.put(binaryType.getType()[i], binaryType);
            }
        }
    }

    /**
     * Retrieves the BinaryType corresponding to the given binary code.
     *
     * @param binaryCode the binary code as a byte
     * @return the corresponding BinaryType
     * @throws IllegalArgumentException if no BinaryType is found for the given binary code
     */
    public static BinaryType2 fromBinaryCode(byte binaryCode) {
        BinaryType2 result = BINARY_CODE_MAP.get(binaryCode);
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
    public static BinaryType2 getBinaryType(Class<?> type) {
        BinaryType2 binaryType = TYPE_MAP.get(type);
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
