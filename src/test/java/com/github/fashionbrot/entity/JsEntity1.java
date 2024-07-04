package com.github.fashionbrot.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
public class JsEntity1 {

    private short shortMin;
    private short shortMax;
    private Short shortNull;

    private int intMin;
    private int intMax;
    private Integer intNull;


    private long longMin;
    private long longMax;
    private Long longNull;

    private float floatMin;
    private float floatMax;
    private Float floatNull;

    private double doubleMin;
    private double doubleMax;
    private Double doubleNull;

    private boolean booleanTrue;
    private boolean booleanFalse;
    private Boolean booleanNull;

    private char charMin;
    private char charMax;
    private Character charNull;

    private byte byteMin;
    private byte byteMax;
    private Byte byteNull;

    private BigDecimal bigDecimalMin;
    private BigDecimal bigDecimalMax;
    private BigDecimal bigDecimalNull;

    private String string1;
    private String string2;
    private String stringNull;

    private Date date1;
    private Date date2;

    private LocalTime localTime1;
    private LocalTime localTime2;

    private LocalDate localDate1;
    private LocalDate localDate2;

    private LocalDateTime localDateTime1;
    private LocalDateTime localDateTime2;
}
