package com.wilbur.server.bean.entry;

/**
 * ChartsItemEntry 
 * @author Wang Zhao
 * @date 2017年6月28日 下午4:43:12
 *
 */
public class ChartsItemEntry {

    private int id;
    
    private String name;
    
    private String operationName;
    
    private int pointerType;
    
    private int fieldIndex;
    
    private String field;
    
    private String fieldDesc;
    
    private int enabled;
    
    private String createTime;
    
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public int getPointerType() {
        return pointerType;
    }

    public void setPointerType(int pointerType) {
        this.pointerType = pointerType;
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(int fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
}
