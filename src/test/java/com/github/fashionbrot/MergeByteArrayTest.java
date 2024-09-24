package com.github.fashionbrot;

import java.util.ArrayList;
import java.util.List;

public class MergeByteArrayTest {

    // 原始方法
    public static byte[] mergeByteArrayListOriginal(List<byte[]> list) {
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

    // 优化后方法
    public static byte[] mergeByteArrayListOptimized(List<byte[]> list) {
        // 计算总长度
        int totalLength = 0;
        for (byte[] array : list) {
            if (array != null) {
                totalLength += array.length;
            }
        }
        // 创建结果数组
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        // 合并字节数组
        for (byte[] array : list) {
            if (array != null) {
                System.arraycopy(array, 0, result, currentIndex, array.length);
                currentIndex += array.length;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // 生成测试数据
        List<byte[]> byteArrayList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            byteArrayList.add(new byte[100]); // 每个数组 100 个字节
        }

        // 测试原始方法
        long start1 = System.nanoTime();
        mergeByteArrayListOriginal(byteArrayList);
        long end1 = System.nanoTime();
        System.out.println("Original method time: " + (end1 - start1) + " ns");

        // 测试优化后方法
        long start2 = System.nanoTime();
        mergeByteArrayListOptimized(byteArrayList);
        long end2 = System.nanoTime();
        System.out.println("Optimized method time: " + (end2 - start2) + " ns");
    }
}

