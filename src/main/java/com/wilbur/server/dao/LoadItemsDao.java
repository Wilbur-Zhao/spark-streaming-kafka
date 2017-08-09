package com.wilbur.server.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import scala.Tuple6;

import com.wilbur.server.bean.entry.ChartsItemEntry;
import com.wilbur.server.bean.entry.MonitorItemEntry;

/**
 * LogStreamJdbcTemplate 
 * @author Wang Zhao
 * @date 2017年7月6日 下午5:26:10
 *
 */
@Repository
public class LoadItemsDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    private static final String MONITOR_ITEMS_SQL = "select oi.operation_name as operationName, mi.field_index as fieldIndex,mi.field as field,mi.field_desc as fieldDesc,mi.threshold as threshold,mi.receiver_email as receiverEmail,mi.receiver_mobile as receiverMobile,mi.pointer_type as pointerType,mi.save_log as saveLog from monitor_item mi, operation_info oi where mi.enabled=1 and oi.id=mi.operation_id";
    private static final String CHARTS_ITEMS_SQL = "select * from charts_item where enabled=1";
    
    private static final Logger logger = LoggerFactory.getLogger(LoadItemsDao.class);
    
    /**
     * 加载监控元数据
     * @return
     */
    public List<MonitorItemEntry> selectMonitorItems(){
        List<MonitorItemEntry> monitorItems = jdbcTemplate.query(MONITOR_ITEMS_SQL, new BeanPropertyRowMapper<MonitorItemEntry>(MonitorItemEntry.class));
        return monitorItems;
    }
    
    /**
     * 加载监控图形元数据
     * @return
     */
    public List<ChartsItemEntry> selectChartsItems(){
        List<ChartsItemEntry> chartsItems = jdbcTemplate.query(CHARTS_ITEMS_SQL, new BeanPropertyRowMapper<ChartsItemEntry>(ChartsItemEntry.class));
        return chartsItems;
    }
    
    public Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> loadRules(){
        //count key(Integer,Integer, String, String, Integer) (pointerType-操作类型, fieldIndex-字段下标索引, field-字段, fieldValue-字段描述, definedValue-字段具体阈值)
        Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> count_keys = new ConcurrentHashMap<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>();
        try {
            List<MonitorItemEntry> monitorItems = jdbcTemplate.query(MONITOR_ITEMS_SQL, new BeanPropertyRowMapper<MonitorItemEntry>(MonitorItemEntry.class));
            for(MonitorItemEntry monitorEntry : monitorItems){
                String operationName = monitorEntry.getOperationName();
                int fieldIndex = monitorEntry.getFieldIndex();
                String field = monitorEntry.getField();
                String fieldDesc = monitorEntry.getFieldDesc();
                int threshold = monitorEntry.getThreshold();
                int pointerType = monitorEntry.getPointerType();
                int saveLog = monitorEntry.getSaveLog();
                
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
    
}
