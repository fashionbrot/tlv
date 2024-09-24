package com.github.fashionbrot.tlv;



import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class TLVTypeUtil {

//    public static byte[] encodeVarChar(char value) {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        int intValue = value; // 将char转换为int以便处理
//        while ((intValue & 0xFFFFFF80) != 0) {
//            output.write((intValue & 0x7F) | 0x80);
//            intValue >>>= 7;
//        }
//        output.write(intValue & 0x7F);
//        return output.toByteArray();
//    }

    public static byte[] encodeVarChar(char value) {
        // 一个 char 类型值最多可以用 3 个字节来编码
        byte[] buffer = new byte[3];
        int position = 0;
        int intValue = value; // 将 char 转换为 int 以便处理

        // 当有超过 7 位需要编码时
        while ((intValue & 0xFFFFFF80) != 0) {
            buffer[position++] = (byte) ((intValue & 0x7F) | 0x80); // 设置延续位
            intValue >>>= 7; // 将值右移 7 位
        }
        // 写入最后一个字节，不包含延续位
        buffer[position++] = (byte) (intValue & 0x7F);

        // 只返回实际使用的部分
        byte[] result = new byte[position];
        System.arraycopy(buffer, 0, result, 0, position);
        return result;
    }


//    public static char decodeVarChar(byte[] data) {
//        int result = 0;
//        int shift = 0;
//        int index = 0;
//        byte b;
//        do {
//            if (index >= data.length) {
//                throw new RuntimeException("decodeVarChar decoding error: Insufficient data");
//            }
//            b = data[index++];
//            result |= (b & 0x7F) << shift;
//            shift += 7;
//        } while ((b & 0x80) != 0);
//        return (char) result;
//    }

    public static char decodeVarChar(byte[] data) {
        int result = 0;
        int shift = 0;
        int index = 0;

        while (index < data.length) {
            byte b = data[index++];
            result |= (b & 0x7F) << shift;

            // 如果最高位不是 1，表示这是最后一个字节
            if ((b & 0x80) == 0) {
                // 确保 result 不会超出 char 的范围
                if (result > 0xFFFF) {
                    throw new RuntimeException("decodeVarChar decoding error: Value exceeds char range");
                }
                return (char) result;
            }

            shift += 7;

            // 检查 shift 是否超出了 char 的位数范围
            if (shift >= 16) {
                throw new RuntimeException("decodeVarChar decoding error: Too many bytes");
            }
        }

        throw new RuntimeException("decodeVarChar decoding error: Insufficient data");
    }





//    public static byte[] encodeVarInteger(int value)  {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        while ((value & 0xFFFFFF80) != 0) {
//            output.write((value & 0x7F) | 0x80);
//            value >>>= 7;
//        }
//        output.write(value & 0x7F);
//        return output.toByteArray();
//    }

    public static byte[] encodeVarInteger(int value) {
        // 一个 int 类型值最多可以用 5 个字节来编码
        byte[] buffer = new byte[5];
        int position = 0;

        // 当有超过 7 位需要编码时
        while ((value & 0xFFFFFF80) != 0) {
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);  // 设置延续位
            value >>>= 7;  // 将值右移 7 位
        }
        // 写入最后一个字节，不包含延续位
        buffer[position++] = (byte) (value & 0x7F);

        // 只返回实际使用的部分
        byte[] result = new byte[position];
        System.arraycopy(buffer, 0, result, 0, position);
        return result;
    }


//    public static int decodeVarInteger(byte[] data)  {
//
//        int result = 0;
//        int shift = 0;
//        int index = 0;
//        byte b;
//        do {
//            if (index >= data.length) {
//                throw new RuntimeException("Varint decoding error: Insufficient data");
//            }
//            b = data[index++];
//            result |= (b & 0x7F) << shift;
//            shift += 7;
//        } while ((b & 0x80) != 0);
//        return result;
//    }

    public static int decodeVarInteger(byte[] data) {
        int result = 0;
        int shift = 0;
        int index = 0;

        while (index < data.length) {
            byte b = data[index++];
            result |= (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                // 如果最高位不是 1，则结束解码
                return result;
            }
            shift += 7;
            // 如果 shift 超过 int 的位数范围，说明 varint 过长
            if (shift >= 32) {
                throw new RuntimeException("Varint decoding error: Too many bytes");
            }
        }
        throw new RuntimeException("Varint decoding error: Insufficient data");
    }



    public static byte[] encodeVarShort(short value) {
        return encodeVarInteger(value);
    }

    public static short decodeVarShort(byte[] data) {
        return (short) decodeVarInteger(data);
    }



//    public static byte[] encodeVarLong(long value) {
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
//            output.write((byte) ((value & 0x7F) | 0x80));
//            value >>>= 7;
//        }
//        output.write((byte) (value & 0x7F));
//        return output.toByteArray();
//    }

    public static byte[] encodeVarLong(long value) {
        byte[] buffer = new byte[10];
        int position = 0;
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buffer[position++] = (byte) (value & 0x7F);
        byte[] result = new byte[position];
        System.arraycopy(buffer, 0, result, 0, position);
        return result;
    }

//    public static long decodeVarLong(byte[] data)  {
//        long result = 0L;
//        int shift = 0;
//        int index = 0;
//        byte b;
//        do {
//            if (index >= data.length) {
//                throw new RuntimeException("Varint decoding error: Insufficient data");
//            }
//            b = data[index++];
//            result |= (b & 0x7FL) << shift;
//            shift += 7;
//        } while ((b & 0x80) != 0);
//        return result;
//    }
        public static long decodeVarLong(byte[] data) {
            long result = 0L;
            int shift = 0;
            int index = 0;

            while (index < data.length) {
                byte b = data[index++];
                result |= (b & 0x7FL) << shift;

                // 如果最高位不是 1，表示这是最后一个字节
                if ((b & 0x80) == 0) {
                    return result;
                }

                shift += 7;

                // 检查 shift 是否超出了 long 的位数范围（最多63位，因为64位的第1位是符号位）
                if (shift >= 64) {
                    throw new RuntimeException("Varint decoding error: Too many bytes");
                }
            }

            throw new RuntimeException("Varint decoding error: Insufficient data");
        }



    public static byte[] encodeVarFloat(float value) {
        int intValue = Float.floatToIntBits(value);
        return encodeVarInteger(intValue);
    }

    public static float decodeVarFloat(byte[] data) {
        int intValue = decodeVarInteger(data);
        return Float.intBitsToFloat(intValue);
    }

    public static byte[] encodeVarDouble(double value) {
        long longValue = Double.doubleToLongBits(value);
        return encodeVarLong(longValue);
    }

    public static double decodeVarDouble(byte[] data)  {
        long longValue = decodeVarLong(data);
        return Double.longBitsToDouble(longValue);
    }


    public static byte[] encodeVarDate(Date date) {
        long longValue = date.getTime(); // 获取 Date 对象的时间戳
        return encodeVarLong(longValue); // 调用通用的长整型编码方法
    }

    public static Date decodeVarDate(byte[] data) {
        long longValue = decodeVarLong(data); // 调用通用的长整型解码方法
        return new Date(longValue); // 使用时间戳创建 Date 对象
    }



    public static byte[] encodeVarLocalDate(LocalDate localDate) {
        Date date = toDate(localDate);
        return encodeVarLong(date.getTime());
    }

    public static LocalDate decodeVarLocalDate(byte[] buffer)  {
        long varLong = decodeVarLong(buffer);
        return toLocalDate(new Date(varLong));
    }

    public static byte[] encodeVarLocalDateTime(LocalDateTime datetime) {
        Date date = toDate(datetime);
        if (date==null){
            return null;
        }
        return encodeVarDate(date);
    }

    public static LocalDateTime decodeVarLocalDateTime(byte[] data)  {
        Date date = decodeVarDate(data);
        if (date==null){
            return null;
        }
        return toLocalDateTime(date);
    }

    public static byte[] encodeVarLocalTime(LocalTime localTime) {
        if (localTime==null){
            return null;
        }
        Date date = toDate(localTime);
        return encodeVarLong(date.getTime());
    }

    public static LocalTime decodeVarLocalTime(byte[] data) {
        long varLong = decodeVarLong(data);
        return toLocalTime(new Date(varLong));
    }

    public static byte[] encodeVarBigDecimal(BigDecimal bd) {
        if (bd==null){
            return new byte[]{};
        }
        String value = toString(bd);
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public static BigDecimal decodeVarBigDecimal(byte[] data) {
        if (data==null || data.length == 0){
            return null;
        }
        String strValue = new String(data,StandardCharsets.UTF_8);
        return format(strValue);
    }



    /**
     * 默认时区
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    /**
     * 将 LocalDate 对象转换为 Date 对象，使用默认时区。
     *
     * @param localDate 要转换的 LocalDate 对象
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate!=null && localDate.getYear()>9999){
            LocalDate max = LocalDate.of(9999, 12, 31);
            return toDate(max,DEFAULT_ZONE_ID);
        }else if (localDate!=null && localDate.getYear()<=0){
            LocalDate min = LocalDate.of(0, 1, 1);
            return toDate(min,DEFAULT_ZONE_ID);
        }
        return toDate(localDate, DEFAULT_ZONE_ID);
    }

    /**
     * 将 LocalDate 对象转换为 Date 对象，使用指定时区。
     *
     * @param localDate 要转换的 LocalDate 对象
     * @param zoneId    指定的时区
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalDate localDate, ZoneId zoneId) {
        if (localDate == null) {
            return null;
        }
        try {
            Instant instant = localDate.atStartOfDay(zoneId).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            return null;
        }
    }
    public static LocalDate toLocalDate(Date date) {
        return toLocalDate(date,DEFAULT_ZONE_ID);
    }

    public static LocalDate toLocalDate(Date date,ZoneId zoneId) {
        if (date!=null) {
            try {
                Instant instant = date.toInstant();
                return instant.atZone(zoneId).toLocalDate();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 java.util.Date 转换为 "yyyy-MM-dd HH:mm:ss" 格式的字符串。
     *
     * @param dateTime 要转换的日期时间
     * @return 格式化后的日期时间字符串
     */
    public static String dateTimeToString(Date dateTime){
        return toString(toLocalDateTime(dateTime));
    }

    /**
     * 将日期时间字符串转换为 LocalDateTime 对象。
     *
     * @param dateTimeString 要转换的日期时间字符串，支持格式 "yyyy-MM-dd HH:mm:ss"
     * @return 转换后的 LocalDateTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        if (dateTimeString!=null && dateTimeString.length()>0) {
            try {
                return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(date, DEFAULT_ZONE_ID);
    }

    /**
     * 将 java.util.Date 转换为 java.time.LocalDateTime，使用指定时区。
     *
     * @param date    要转换的 java.util.Date 对象
     * @param zoneId  指定的时区
     * @return 转换后的 java.time.LocalDateTime 对象，转换失败时返回 null
     */
    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        if (zoneId == null) {
            return null;
        }
        Instant instant = date.toInstant();
        try {
            return instant.atZone(zoneId).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将 java.time.LocalDateTime 转换为 java.util.Date，使用默认时区。
     *
     * @param localDateTime 要转换的 java.time.LocalDateTime 对象
     * @return 转换后的 java.util.Date 对象，转换失败时返回 null
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime!=null && localDateTime.getYear()>9999){
            LocalDateTime max = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
            return toDate(max,DEFAULT_ZONE_ID);
        }else if (localDateTime!=null && localDateTime.getYear()<=0){
            LocalDateTime min = LocalDateTime.of(0, 1, 1, 0, 0);
            return toDate(min,DEFAULT_ZONE_ID);
        }
        return toDate(localDateTime,DEFAULT_ZONE_ID);
    }


    /**
     * 将 java.time.LocalDateTime 转换为 java.util.Date，使用指定时区。
     *
     * @param localDateTime 要转换的 java.time.LocalDateTime 对象
     * @param zoneId        指定的时区
     * @return 转换后的 java.util.Date 对象，转换失败时返回 null
     */
    public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null || zoneId == null) {
            return null;
        }
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        try {
            return Date.from(instant);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 将 Date 对象转换为 LocalTime 对象。
     *
     * @param date 要转换的 Date 对象
     * @return 转换后的 LocalTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalTime toLocalTime(Date date) {
        return toLocalTime(date,DEFAULT_ZONE_ID);
    }

    /**
     * 将 Date 对象转换为 LocalTime 对象。
     *
     * @param date 要转换的 Date 对象
     * @param zoneId 用于确定日期的时区
     * @return 转换后的 LocalTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalTime toLocalTime(Date date, ZoneId zoneId) {
        if (date != null && zoneId!=null) {
            try {
                return date.toInstant().atZone(zoneId).toLocalTime();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 LocalTime 对象转换为 Date 对象。
     *
     * @param localTime 要转换的 LocalTime 对象
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalTime localTime) {
        if (localTime!=null && localTime.getNano()>999){
            LocalTime newLocalTime = LocalTime.of(23,59,59,0);
            return toDate(newLocalTime,DEFAULT_ZONE_ID);
        }else if (localTime!=null && localTime.getNano()<0){
            LocalTime newLocalTime = LocalTime.of(0,0,0,0);
            return toDate(newLocalTime,DEFAULT_ZONE_ID);
        }
        return toDate(localTime,DEFAULT_ZONE_ID);
    }

    /**
     * 将 LocalTime 对象转换为 Date 对象。
     *
     * @param localTime 要转换的 LocalTime 对象
     * @param zoneId 用于确定日期的时区
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalTime localTime, ZoneId zoneId) {
        if (localTime != null && zoneId!=null) {
            try {
                return Date.from(localTime.atDate(LocalDate.now()).atZone(zoneId).toInstant());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 LocalDateTime 对象转换为日期时间字符串。
     *
     * @param localDateTime 要转换的 LocalDateTime 对象
     * @return 转换后的日期时间字符串，转换失败或输入为空时返回 null
     */
    public static String toString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            try {
                return localDateTime.format(DATE_TIME_FORMAT);
            } catch (DateTimeException e) {
                return null;
            }
        }
        return null;
    }

    public static String toString(BigDecimal bigDecimal){
        return format(bigDecimal).toPlainString();
    }

    /**
     * 格式化 BigDecimal 值，将 null 值转换为零值。
     *
     * @param val 要格式化的 BigDecimal 值
     * @return 格式化后的 BigDecimal 值，如果输入为 null 则返回零值
     */
    public static BigDecimal format(final BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }

    public static BigDecimal format(final String value) {
        if (value!=null && value.length()>0) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException exception) {
            }
        }
        return BigDecimal.ZERO;
    }

}
