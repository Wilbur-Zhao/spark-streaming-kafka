package com.wilbur.server.bean.entry;

/**
 * RuleEntry 
 * @author Wang Zhao
 * @date 2017年6月26日 下午4:11:35
 *
 */
public class RuleEntry {

    private int id;
    
    //业务名称
    private String operationName;
    
    //规则内容，内容以JSON形式存储
    private String rule;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "id=" + id + " & operationName=" + operationName + " & rule="
            + rule;
    }
    
}
