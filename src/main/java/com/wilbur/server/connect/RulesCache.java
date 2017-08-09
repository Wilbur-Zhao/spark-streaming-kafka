package com.wilbur.server.connect;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.Tuple6;

/**
 * HbaseCounter 
 * @author Wang Zhao
 * @date 2017年7月12日 下午4:10:58
 *
 */
public class RulesCache implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(RulesCache.class);
    
    private String sql = "select oi.operation_name as operationName, mi.field_index as fieldIndex,mi.field as field,mi.field_desc as fieldDesc,mi.threshold as threshold,mi.receiver_email as receiverEmail,mi.receiver_mobile as receiverMobile,mi.pointer_type as pointerType,mi.save_log as saveLog from monitor_item mi, operation_info oi where mi.enabled=1 and oi.id=mi.operation_id";
    
    // 驱动程序名
    private String driver = "com.mysql.jdbc.Driver";

    // URL指向要访问的数据库名
    private String url = "jdbc:mysql://127.0.0.1:3306/database";

    // MySQL配置时的用户名
    private String user = "test"; 

    // MySQL配置时的密码
    private String password = "test123";
    
    private transient Connection connection = null;
    
    private transient PreparedStatement ps = null;
    
    public RulesCache(){
        openConnection();
    }
    
    private static class generalHbaseCounter {
        private static RulesCache hbaseCounter = new RulesCache();
    } 
    
    public static RulesCache getInstance(){
        return generalHbaseCounter.hbaseCounter;
    }
    
    public void openConnection(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> loadRules(){
        //count key(Integer,Integer, String, String, Integer) (pointerType-操作类型, fieldIndex-字段下标索引, field-字段, fieldValue-字段描述, definedValue-字段具体阈值)
        Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> count_keys = new ConcurrentHashMap<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>();
        try {
            ps = connection.prepareStatement(sql);
            ResultSet result = ps.executeQuery();
            while(result.next()){
                String operationName = result.getString(1);
                int fieldIndex = result.getInt(2);
                String field = result.getString(3);
                String fieldDesc = result.getString(4);
                int threshold = result.getInt(5);
                int pointerType = result.getInt(8);
                int saveLog = result.getInt(9);
                
                Tuple6<Integer,Integer, String, String, Integer, Integer> keysInfo = new Tuple6<Integer,Integer, String, String, Integer, Integer>(pointerType, fieldIndex, field, fieldDesc, threshold, saveLog);
                if(!count_keys.containsKey(operationName)){
                    List<Tuple6<Integer,Integer, String, String, Integer, Integer>> keysInfoList = new ArrayList<Tuple6<Integer,Integer, String, String, Integer, Integer>>();
                    count_keys.put(operationName, keysInfoList);
                }
                count_keys.get(operationName).add(keysInfo);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
       logger.info("count_keys={}", count_keys);
       return count_keys;
    }
    
    public static void main(String[] args) {
        RulesCache cache = RulesCache.getInstance();
        logger.info("count_keys={}", cache.loadRules());
    }
    
}
