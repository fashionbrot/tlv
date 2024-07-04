package com.github.fashionbrot.entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response implements Serializable{
    private static final long serialVersionUID = -3655390020082644681L;

    public static final int SUCCESS = 0;
    public static final int FAILED = 1;


    private int code;
    private String msg;
    private PageResponse data;

}
