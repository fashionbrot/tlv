package com.github.fashionbrot.tlv;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fashionbrot
 */

public enum BinaryCodeLength {

    B1((byte)0b000, 1),
    B2((byte)0b001, 2),
    B3((byte)0b010, 3),
    B4((byte)0b011, 4),
    B5((byte)0b100, 5),
    B6((byte)0b101, 6),
    B7((byte)0b110, 7),
    B8((byte)0b111, 8);

    private final byte binaryCode;
    private final int length;

    BinaryCodeLength(byte binaryCode, int length) {
        this.binaryCode = binaryCode;
        this.length = length;
    }

    public byte getBinaryCode() {
        return binaryCode;
    }

    public int getLength() {
        return length;
    }

    private static final Map<Byte, Integer> BINARY_TO_LENGTH_MAP = new HashMap<>();
    private static final Map<Integer, Byte> LENGTH_TO_BINARY_MAP = new HashMap<>();

    static {
        for (BinaryCodeLength code : BinaryCodeLength.values()) {
            BINARY_TO_LENGTH_MAP.put(code.getBinaryCode(), code.getLength());
            LENGTH_TO_BINARY_MAP.put(code.getLength(), code.getBinaryCode());
        }
    }

    public static int getLength(Byte binaryCode) {
        byte last3 = (byte) (binaryCode & 0b111);
        Integer length = BINARY_TO_LENGTH_MAP.get(last3);
        if (length == null) {
            throw new IllegalArgumentException("Invalid binary code: " + last3);
        }
        return length;
    }

    public static Byte getBinaryCode(int length) {
        Byte binaryCode = LENGTH_TO_BINARY_MAP.get(length);
        if (binaryCode == null) {
            throw new IllegalArgumentException("Invalid length: " + length);
        }
        return binaryCode;
    }
}
