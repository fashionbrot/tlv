package com.github.fashionbrot.tlv;

import java.util.Arrays;

/**
 * @author fashionbrot
 */
public  class ByteArrayReader {
    private final byte[] data;
    private int lastReadIndex; // 最后读取的下标
    private BinaryType lastBinaryType;

    public ByteArrayReader(byte[] data) {
        this.data = data;
        this.lastReadIndex = 0; // 初始化为0
    }

    public byte readFrom(int end){
        if ( end > data.length) {
            throw new IndexOutOfBoundsException("Start or end index out of bounds");
        }
        lastReadIndex = end;
        return data[end];
    }

    /**
     * 从给定的起始下标开始读取到结束下标（不包含结束下标位置的元素），并返回子数组。
     *
     * @param start 开始下标（包含）
     * @param end 结束下标（不包含）
     * @return 子数组
     * @throws IndexOutOfBoundsException 如果起始下标或结束下标超出数组范围
     */
    public byte[] readFromTo(int start, int end) {
        if (start < 0 || start > end || end > data.length) {
            throw new IndexOutOfBoundsException("Start or end index out of bounds");
        }
        lastReadIndex =  end;
        return Arrays.copyOfRange(data, start, end);
    }

    public int getLastReadIndex() {
        return lastReadIndex;
    }

    public BinaryType getLastBinaryType() {
        return lastBinaryType;
    }

    public void setLastBinaryType(BinaryType lastBinaryType) {
        this.lastBinaryType = lastBinaryType;
    }

    /**
     * 检查是否已读取完所有数据
     *
     * @return 如果已读取完所有数据则返回true，否则返回false
     */
    public boolean isReadComplete() {
        return lastReadIndex == data.length;
    }

    public boolean isCollectionEmpty(){
        return this.data.length==1 && data[0] == 0x00;
    }
}
