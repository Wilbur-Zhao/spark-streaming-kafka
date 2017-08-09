package com.wilbur.server.bean.entry;

/**
 * LoopTimeEntry 
 * @author Wang Zhao
 * @date 2017年7月26日 下午6:26:42
 *
 */
public class LoopTimeEntry {

    private int id;
    
    private String loopTime;
    
    private int enable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoopTime() {
        return loopTime;
    }

    public void setLoopTime(String loopTime) {
        this.loopTime = loopTime;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }
    
}
