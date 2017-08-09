package com.wilbur.server.stream;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import scala.Tuple2;
import scala.Tuple6;

import com.wilbur.server.dao.LoadItemsDao;
import com.wilbur.server.dao.SaveLoopTimeDao;
import com.wilbur.server.listener.JobListener;
import com.wilbur.server.runner.LoadRunner;

/**
 * LogStream
 * 
 * @author Wang Zhao
 * @date 2017年7月5日 下午3:23:00
 */
public class LogStream implements Serializable  {
    
    private static final long serialVersionUID = 1L;

    private static final String APP_NAME = "MONITOR_STEAMING";
    
    //spark host
    private static final String MASTER = "spark://namenode:7077";
    
    //zk host
    private static final String ZK_QUORUM = "slave01:2181,slave02:2181,master01:2181";
    
    //Kafka group
    private static final String GROUP = "default1";
    
    //kafka topic
    private static final String TOPICSS = "monitorTopic";
    
    //consumer threads
    private static final int NUM_THREAD = Runtime.getRuntime().availableProcessors();
    
    //需要广播的规则配置
    private static volatile Broadcast<Map<String, List<Tuple6<Integer,Integer, String, String, Integer, Integer>>>> broadCast = null;
    
    @Autowired
    LoadRunner loadRunner;
    
    @Autowired
    transient SaveLoopTimeDao saveLoopTimeDao;
    
    @Autowired
    transient LoadItemsDao loadItemsDao;
    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        LogStream logStream = (LogStream) context.getBean("logStream");
        
        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(APP_NAME);
        sparkConf.setMaster(MASTER);
        
        //设置一分钟读一次kafka
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, new Duration(60000));
        
        try {
            Thread.sleep(1000*3);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        
        Map<String, Integer> topicsMap = new HashMap<String, Integer>();
        String[] topics = TOPICSS.split(",");
        for (String topic: topics) {
            topicsMap.put(topic, NUM_THREAD);
        }
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("auto.offset.reset", "largest");
        params.put("zookeeper.connect", ZK_QUORUM);
        params.put("group.id", GROUP);
        
        JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, String.class, String.class, StringDecoder.class, StringDecoder.class, params, topicsMap, StorageLevel.MEMORY_AND_DISK());
        //JavaPairReceiverInputDStream<String, String> messages = KafkaUtils.createStream(jssc, ZK_QUORUM, GROUP, topicMap);
        
        JavaDStream<String> logs = messages.map(new Function<Tuple2<String, String>, String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String call(Tuple2<String, String> tuple2) {
                return tuple2._2();
            }
        });
        
        //初始化计算任务
        logStream.initDistribute(logs, jssc);
        
        try {
            jssc.start();
            jssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化任务
     * @param logs
     */
    public void initDistribute(JavaDStream<String> logs, JavaStreamingContext jssc){
        DistributeTask distributeTask = DistributeTask.getInstance();
        distributeTask.distributeTask(logs, broadCast);
        distributeTask.saveAvgCostTime(logs, broadCast);
        
        //添加任务监听
        JobListener jobListener = new JobListener();
        jobListener.setSaveLoopTimeDao(saveLoopTimeDao);
        jssc.addStreamingListener(jobListener);
    }
    
}
