package com.wilbur.server.stream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import scala.Tuple2;
import scala.Tuple6;

import com.alibaba.fastjson.JSONObject;
import com.wilbur.server.common.constant.StreamConstant;
import com.wilbur.server.connect.RulesCache;
import com.wilbur.server.hbase.HbaseCounter;
import com.wilbur.server.stream.util.DateUtils;

/**
 * DistributeTask 
 * @author Wang Zhao
 * @date 2017年7月5日 下午4:37:44
 *
 */
@Component
public class DistributeTask implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = LoggerFactory.getLogger(DistributeTask.class);

    public DistributeTask(){
        
    }
    
    private static class generalDistributeTask {
        private static DistributeTask distributeTask = new DistributeTask();
    }
    
    public static DistributeTask getInstance(){
        return generalDistributeTask.distributeTask;
    }
    
    /**
     * 分发任务
     * @param logs
     */
    public void distributeTask(JavaDStream<String> logs, final Broadcast<Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>> broadCast){
        
        //lines operation 
        JavaDStream<String> collectionCalculation = logs.flatMap(new FlatMapFunction<String, String>() {

                private static final long serialVersionUID = 1L;

                @Override
                public Iterator<String> call(String line) {
                    RulesCache cache = RulesCache.getInstance();
                    final Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> count_keys = cache.loadRules();
                    List<String> keys = new ArrayList<String>();
                    try {
                        JSONObject lineObject = JSONObject.parseObject(line);
                        //日志时间
                        String logTime = lineObject.getString("time").trim();
                        //业务名称
                        String operationName = lineObject.getString("project");
                        logger.info("distributeTask count_keys={} ", count_keys);
                        if(!count_keys.containsKey(operationName)){
                            return keys.iterator();
                        }
                        List<Tuple6<Integer,Integer, String, String, Integer, Integer>> rules = count_keys.get(operationName);
                        for(Tuple6<Integer,Integer, String, String, Integer, Integer> tuple6 : rules){
                            String value = tuple6._3();
                            String field = tuple6._4();
                            String fieldValue = lineObject.getString(field);
                            if(!value.equals(fieldValue)){
                                continue;
                            }
                            keys.add(operationName + StreamConstant.ROW_KEY_SERPERATOR + field + StreamConstant.ROW_KEY_SERPERATOR + fieldValue + StreamConstant.ROW_KEY_SERPERATOR + DateUtils.formatStringTimeSeconds(logTime));
                        }
                    } catch (Exception e) {
                        logger.error("distribute task exception ", e);
                    }
                    return keys.iterator();
                }
                
        });
        
        
        //遍历规则，看是否需要进行日志存储
        this.saveLog(logs, broadCast);
        
        //遍历计算结果 进行count统计，并保存hbase
        this.forEachCounts(collectionCalculation);
        
    }
    
    /**
     * 遍历计算结果，进行hbase存储
     * @param counts
     */
    public void forEachCounts(JavaDStream<String> collectionCalculation){
        //进行key的变换，变成(key，1)的键值对
        JavaPairDStream<String, Integer> pairs = collectionCalculation.mapToPair(new PairFunction<String, String, Integer>() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public Tuple2<String, Integer> call(String key) throws Exception {
                return new Tuple2<String, Integer>(key, 1);
            }
        });
       
        //做wordcount计算
        JavaPairDStream<String, Integer> counts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        
        counts.print();
        
        counts.foreachRDD(new VoidFunction<JavaPairRDD<String,Integer>>() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void call(JavaPairRDD<String, Integer> rdd) throws Exception {
                
                if(!rdd.isEmpty()){
                    rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Integer>>>() {
                        
                        private static final long serialVersionUID = 1L;
                        
                        @Override
                        public void call(Iterator<Tuple2<String, Integer>> pairs) throws Exception {
                            System.out.println("start counter start....." + pairs.hasNext());
                            HbaseCounter counter = HbaseCounter.getInstance();
                            String qualifier = "count_value:info";
                            while(pairs.hasNext()){
                                Tuple2<String, Integer> pair = pairs.next();
                                String key = pair._1;
                                String value = pair._2.toString();
                                
                                counter.save(key, value, qualifier);
                            }
                            System.out.println("start counter end....." + pairs.hasNext());
                        }
                        
                    });
                }
            }
            
        }); 
    }
    
    /**
     * 遍历规则，看是否需要将日志进行存储
     */
    public void saveLog(JavaDStream<String> logs, final Broadcast<Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>> broadCast){
        logs.foreachRDD(new VoidFunction<JavaRDD<String>>() {
            
            private static final long serialVersionUID = 1L;

            @Override
            public void call(JavaRDD<String> rdd) throws Exception {
                
                if(!rdd.isEmpty()){
                    rdd.foreachPartition(new VoidFunction<Iterator<String>>() {
                        RulesCache cache = RulesCache.getInstance();
                        final Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> count_keys = cache.loadRules();
                        
                        private static final long serialVersionUID = 1L;
                        
                        @Override
                        public void call(Iterator<String> lines) throws Exception {
                            System.out.println("start savelog start.....");
                            HbaseCounter counter = HbaseCounter.getInstance();
                            String qualifier = "count_value:log_info";
                            while(lines.hasNext()){
                                String line = lines.next();
                                logger.info("line={}", line);
                                JSONObject lineObject = JSONObject.parseObject(line);
                                //日志时间
                                String logTime = lineObject.getString("time").trim();
                                //业务名称
                                String operationName = lineObject.getString("project");
                                if(!count_keys .containsKey(operationName)){
                                    continue;
                                }
                                
                                List<Tuple6<Integer,Integer, String, String, Integer, Integer>> rules = count_keys .get(operationName);
                                for(Tuple6<Integer,Integer, String, String, Integer, Integer> tuple6 : rules){
                                    String value = tuple6._3();
                                    String field = tuple6._4();
                                    int saveLog = tuple6._6();
                                    String fieldValue = lineObject.getString(field);
                                    if(!value.equals(fieldValue)){
                                        continue;
                                    }
                                    //1:需要保存log到hbase，0:不需要保存log到hbase
                                    if(saveLog == 1){                                        
                                        String key = operationName + StreamConstant.ROW_KEY_SERPERATOR + field + StreamConstant.ROW_KEY_SERPERATOR + fieldValue + StreamConstant.ROW_KEY_SERPERATOR + DateUtils.formatStringTimeSeconds(logTime);
                                        counter.save(key, line, qualifier);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    
    /**
     * 遍历规则，遍历数据，计算接口平均调用时长，并保存hbase
     */
    public void saveAvgCostTime(JavaDStream<String> logs, final Broadcast<Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>> broadCast){
        
        JavaDStream<String> collectionCalculation = logs.map(new Function<String, String>() {

            private static final long serialVersionUID = 1L;
            
            @Override
            public String call(String line) throws Exception {
                RulesCache cache = RulesCache.getInstance();
                final Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>> count_keys = cache.loadRules();
                logger.info("saveAvgCostTime count_keys={} ", count_keys);
                JSONObject lineObject = JSONObject.parseObject(line);
                //业务名称
                String operationName = lineObject.getString("project");
                logger.info("saveAvgCostTime count keys ={} operationName={} " , count_keys, operationName);
                if(!count_keys.containsKey(operationName)){
                    return "";
                }
                List<Tuple6<Integer,Integer, String, String, Integer, Integer>> rules = count_keys.get(operationName);
                for(Tuple6<Integer,Integer, String, String, Integer, Integer> tuple6 : rules){
                    String field = tuple6._4();
                    if("class".equals(field)){
                        return line;
                    }
                }
                return "";
            }
            
        });
        
        //格式化key与method的cost_time
        JavaPairDStream<String, Integer> pairs = collectionCalculation.mapToPair(new PairFunction<String, String, Integer>() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public Tuple2<String, Integer> call(String line) throws Exception {
                JSONObject lineObject = JSONObject.parseObject(line);
                logger.info("line={}", lineObject);
                logger.info("line object time={}", lineObject.getString("time"));
                //日志时间
                String logTime = lineObject.getString("time").trim();
                //业务名称
                String operationName = lineObject.getString("project");
                String field = "class";
                String fieldValue = lineObject.getString(field);
                int cost = lineObject.getIntValue("cost");
                String key = operationName + StreamConstant.ROW_KEY_SERPERATOR + field + StreamConstant.ROW_KEY_SERPERATOR + fieldValue + StreamConstant.ROW_KEY_SERPERATOR + DateUtils.formatStringTimeSeconds(logTime);
                Tuple2<String, Integer> tuple = new Tuple2<String, Integer>(key, cost);
                return tuple;
            }
        });
        
        //对相同key进行sum求和运算
        JavaPairDStream<String, Integer> sumCollect = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {

            private static final long serialVersionUID = 1L;
            
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        
        sumCollect.foreachRDD(new VoidFunction<JavaPairRDD<String,Integer>>() {
            
            private static final long serialVersionUID = 1L;
            
            @Override
            public void call(JavaPairRDD<String, Integer> rdd) throws Exception {
                
                if(!rdd.isEmpty()){
                    rdd.foreachPartition(new VoidFunction<Iterator<Tuple2<String,Integer>>>() {
                        
                        private static final long serialVersionUID = 1L;
                        
                        @Override
                        public void call(Iterator<Tuple2<String, Integer>> pairs)
                                throws Exception {
                            HbaseCounter counter = HbaseCounter.getInstance();
                            String qualifier = "count_value:sum_cost";
                            while(pairs.hasNext()){
                                Tuple2<String, Integer> tuple = pairs.next();
                                String key = tuple._1;
                                String value = tuple._2.toString();
                                counter.save(key, value, qualifier);
                            }
                        }
                    });
                }
                
            }
            
        });
    }
    
}
