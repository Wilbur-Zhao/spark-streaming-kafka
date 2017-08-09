package com.wilbur.server.bean.entry;

/**
 * KeysInfoEntry 
 * @author Wang Zhao
 * @date 2017年6月26日 下午6:19:34
 *
 */
public class KeysInfoEntry {

    /**
     * 字段下标
     */
    private int fieldIndex;
    
    /**
     * 规则指示类型
     */
    private String pointerType;
    
    /**
     * 字段
     */
    private String field;

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public String getPointerType() {
        return pointerType;
    }

    public void setPointerType(String pointerType) {
        this.pointerType = pointerType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "fieldIndex=" + fieldIndex + " & pointerType=" + pointerType
            + " & field=" + field;
    }
    
}
