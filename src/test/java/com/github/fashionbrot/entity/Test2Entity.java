package com.github.fashionbrot.entity;

import lombok.Data;

import java.util.List;

/**
 * @author fashionbrot
 */
@Data
public class Test2Entity {

//    private BigDecimal b1;

    private List<Test2ChildEntity> list;

    private Test2ChildEntity[]  array;

}
