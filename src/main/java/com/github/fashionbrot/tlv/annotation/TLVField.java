package com.github.fashionbrot.tlv.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TLVField {
    /**
     * 序号 从 1~N
     * @return 序号
     */
    int index() default Integer.MAX_VALUE;

    /**
     * 是否序列化
     * @return boolean
     */
    boolean serialize() default true;
}
