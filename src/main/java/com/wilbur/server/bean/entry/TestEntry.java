package com.wilbur.server.bean.entry;

/**
 * TestEntry 
 * @author Wang Zhao
 * @date 2017年5月12日 下午12:02:58
 *
 */
public class TestEntry {

    private String operationName;
    
    private byte[] data;

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public TestEntry() {
        super();
        // TODO Auto-generated constructor stub
    }
    
}
