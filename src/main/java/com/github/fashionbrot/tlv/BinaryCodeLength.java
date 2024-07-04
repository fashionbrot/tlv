package com.github.fashionbrot.tlv;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fashionbrot
 */
@Getter
@AllArgsConstructor
public enum BinaryCodeLength {

    B1("000", 1),
    B2("001", 2),
    B3("010", 3),
    B4("011", 4),
    B5("100", 5),
    B6("101", 6),
    B7("110", 7),
    B8("111", 8);

    private final String binaryCode;
    private final int length;

    private static final Map<String, Integer> BINARY_TO_LENGTH_MAP = new HashMap<>();
    private static final Map<Integer, String> LENGTH_TO_BINARY_MAP = new HashMap<>();

    static {
        for (BinaryCodeLength code : BinaryCodeLength.values()) {
            BINARY_TO_LENGTH_MAP.put(code.getBinaryCode(), code.getLength());
            LENGTH_TO_BINARY_MAP.put(code.getLength(), code.getBinaryCode());
        }
    }

    public static int getLength(String binaryCode) {
        Integer length = BINARY_TO_LENGTH_MAP.get(binaryCode);
        if (length == null) {
            throw new IllegalArgumentException("Invalid binary code: " + binaryCode);
        }
        return length;
    }

    public static String getBinaryCode(int length) {
        String binaryCode = LENGTH_TO_BINARY_MAP.get(length);
        if (binaryCode == null) {
            throw new IllegalArgumentException("Invalid length: " + length);
        }
        return binaryCode;
    }
}