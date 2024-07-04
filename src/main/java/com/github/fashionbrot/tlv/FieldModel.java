package com.github.fashionbrot.tlv;



import java.lang.reflect.Field;


public class FieldModel {

    private int index;
    private Field field;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
