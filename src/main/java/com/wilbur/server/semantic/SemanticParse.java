package com.wilbur.server.semantic;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import scala.Tuple6;

import com.wilbur.server.bean.entry.MonitorItemEntry;
import com.wilbur.server.common.cache.RulesCache;
import com.wilbur.server.dao.LoadItemsDao;
import com.wilbur.server.stream.util.DateUtils;

/**
 * SemanticDefined
 * 
 * @author Wang Zhao
 * @date 2017年6月22日 上午10:09:56
 *       web端配置每个业务的多个规则，每条规则都是以JSON格式定义
 */
@Component
public class SemanticParse implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(SemanticParse.class);

    private static final int POINTER_TYPE_COUNT = 2;
    
    @Autowired
    transient LoadItemsDao loadItemsDao;
    
    /**
     * 加载后台web创建的业务计算规则
     */
    public Map<String, Integer> loadRules() {
        Map<String, Integer> keysDetail = new HashMap<String, Integer>();

        // load DB into RulesCache
        List<MonitorItemEntry> monitorItems = loadItemsDao.selectMonitorItems();
        
        this.parseKeysInfo(monitorItems);
        
        return keysDetail;
    }
    
    /**
     * 解析规则信息
     * @param monitorItems
     * @param chartsItems
     */
    public void parseKeysInfo(List<MonitorItemEntry> items){
        for(MonitorItemEntry object : items){
            try {
                encaplsulationKeysInfo(object);
            } catch (Exception e) {
                logger.error("encaplsulation exception e", e);
            }
        }
    }
    
    /**
     * 获取对象中属性值，存储到规则缓存中
     * @param item
     * @throws Exception
     */
    public void encaplsulationKeysInfo(Object item) throws Exception{
        String operationName = "";
        int pointerType = 0;
        int fieldIndex = 0;
        int saveLog = 0;
        String fieldDesc = "";
        String fieldValue = "";
        int definedValue = -1;
        
        Class<?> cls = item.getClass();
        Method[] methods = cls.getDeclaredMethods();
        for(Method method : methods){
            String methodName = method.getName();
            switch (methodName) {
                case "getOperationName":
                    method.setAccessible(true);
                    operationName = (String) method.invoke(item);
                    break;
                case "getPointerType":
                    method.setAccessible(true);
                    pointerType = (int) method.invoke(item);
                    break;
                case "getFieldIndex":
                    method.setAccessible(true);
                    fieldIndex = (int) method.invoke(item);
                    break;
                case "getField":
                    method.setAccessible(true);
                    fieldValue = (String) method.invoke(item);
                    break;
                case "getFieldDesc":
                    method.setAccessible(true);
                    fieldDesc = (String) method.invoke(item);
                    break;
                case "getThreshold":
                    method.setAccessible(true);
                    definedValue = (int) method.invoke(item);
                    break;
                case "getSaveLog":
                    method.setAccessible(true);
                    saveLog = (int)method.invoke(item);
                    break;
            }
        }
        
        if(StringUtils.isBlank(operationName)){
            return;
        }
        
        String [] fieldValues = fieldValue.split(",");
        List<String> fledsValuesList = Arrays.asList(fieldValues);
        
        if(POINTER_TYPE_COUNT == pointerType){                    
            Tuple6<Integer,Integer, String, String, Integer, Integer> keysInfo = new Tuple6<Integer,Integer, String, String, Integer, Integer>(pointerType, fieldIndex, fieldValue, fieldDesc, definedValue, saveLog);
            List<Tuple6<Integer,Integer, String, String, Integer, Integer>> keysInfoList = new ArrayList<Tuple6<Integer,Integer, String, String, Integer, Integer>>();
            keysInfoList.add(keysInfo);
            if(RulesCache.COUNT_KEYS.containsKey(operationName)){
                RulesCache.COUNT_KEYS.get(operationName).add(keysInfo);
            } else {
                RulesCache.COUNT_KEYS.put(operationName, keysInfoList);
            }
            if(RulesCache.COUNT_FILEDS_INFO.containsKey(fieldValue) && !RulesCache.GROUP_FILEDS_INFO.isEmpty()){
                RulesCache.COUNT_FILEDS_INFO.get(fieldValue).addAll(fledsValuesList);
            } else {
                RulesCache.COUNT_FILEDS_INFO.put(fieldValue, fledsValuesList);                
            }
        }
        
    }

    public void test(){
        String time = DateUtils.getCurrentDateTimeStr();
        logger.info("time = {}", time);
        System.out.println(time);
    }
    
}
