package com.wilbur.server.bean.entry;

/**
 * MonitorItemEntry 
 * @author Wang Zhao
 * @date 2017年6月28日 下午4:42:45
 *
 */
public class MonitorItemEntry {

    private int id;
    
    /**
     * 规则名称
     */
    private String name;
    
    /**
     * 业务ID
     */
    private String operationName;
    
    /**
     * 操作类型
     */
    private int pointerType;
    
    /**
     * 纬度在日志中的下标，按照数组下边从0开始
     */
    private int fieldIndex;
    
    /**
     * 纬度值
     */
    private String field;
    
    /**
     * 纬度描述
     */
    private String fieldDesc;
    
    /**
     * 报警阈值
     */
    private int threshold;
    
    /**
     * 是否存hbase
     */
    private int saveLog;
    
    /**
     * 邮件接收人
     */
    private String receiverEmail;
    
    /**
     * 短信告警接收人
     */
    private String receiverMobile;
    
    /**
     * 是否可用
     */
    private int enabled;
    
    private String createTime;
    
    private String udpateTime;

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

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
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

    public String getUdpateTime() {
        return udpateTime;
    }

    public void setUdpateTime(String udpateTime) {
        this.udpateTime = udpateTime;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public int getSaveLog() {
        return saveLog;
    }

    public void setSaveLog(int saveLog) {
        this.saveLog = saveLog;
    }
    
}
