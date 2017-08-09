package com.wilbur.server.common.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.spark.broadcast.Broadcast;

import scala.Tuple5;
import scala.Tuple6;

import com.wilbur.server.bean.entry.RuleEntry;

/**
 * RulesCache 
 * @author Wang Zhao
 * @date 2017年6月26日 下午4:10:42
 *
 */
public class RulesCache {


    /**
     * 后台配置的规则集
     */
    public static Map<String, List<RuleEntry>> rules = new ConcurrentHashMap<String, List<RuleEntry>>();
    
    /**
     * count key
     * (Integer,Integer, String, String, Integer) (pointerType-操作类型, fieldIndex-字段下标索引, field-字段, fieldValue-字段描述, definedValue-字段具体阈值)
     */
    public volatile static Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> COUNT_KEYS = new ConcurrentHashMap<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>();
    
    /**
     * group key
     * (Integer,Integer, String, String, Integer) (pointerType-操作类型, fieldIndex-字段下标索引, field-字段, fieldValue-字段描述, definedValue-字段具体阈值)
     */
    public static Map<String, List<Tuple5<Integer,Integer, String, String, Integer>>> GROUP_KEYS = new ConcurrentHashMap<String, List<Tuple5<Integer,Integer, String, String, Integer>>>();
    
    public static Map<String, List<String>> COUNT_FILEDS_INFO = new ConcurrentHashMap<String, List<String>>();
    
    public static Map<String, List<String>> GROUP_FILEDS_INFO = new ConcurrentHashMap<String, List<String>>();
    
    public static Broadcast<Map<String, List<Tuple5<Integer,Integer, String, String, Integer>>>> COUNT_BROAD_CASE = null;
    
    public static Broadcast<Map<String, List<String>>> COUNT_FIELD_BROAD_CASE = null;
    
    //需要广播的规则配置
    public static Broadcast<Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>> broadCast = null;
    
}
