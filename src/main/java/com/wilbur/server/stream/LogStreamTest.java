package com.wilbur.server.stream;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.wilbur.server.common.cache.RulesCache;
import com.wilbur.server.dao.SaveLoopTimeDao;
import com.wilbur.server.runner.LoadRunner;
import com.wilbur.server.semantic.SemanticParse;

/**
 * LogStreamTest 
 * @author Wang Zhao
 * @date 2017年7月25日 下午11:29:22
 *
 */
public class LogStreamTest {
   
    @Autowired
    SemanticParse semanticParse;
    
    @Autowired
    LoadRunner loadRunner;
    
    @Autowired
    SaveLoopTimeDao loopTimeDao;
    
    ExecutorService service = Executors.newSingleThreadExecutor();
    
    private static final Logger logger = LoggerFactory.getLogger(LogStreamTest.class);
    
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        LogStreamTest logStream = (LogStreamTest) context.getBean("logStreamTest");
        logStream.loadRulesInit();
        System.out.println("aaaaaaa");
    }
    
    public void loadRulesInit(){
        Thread t = new Thread(new Runnable() {
            
            @Override
            public void run() {
                while(true){                    
                    logger.info("load rules start");
                    //load cache
                    if(!RulesCache.COUNT_KEYS.isEmpty()){
                        RulesCache.COUNT_KEYS.clear();
                    }
                    semanticParse.loadRules();
                    logger.info("load rules end");
                    logger.info("rules cache={}", RulesCache.COUNT_KEYS);
                    try {
                        Thread.sleep(1000*60);
                    } catch (InterruptedException e) {
                        logger.error("loop load cache and broadcast exception ", e);
                    }
                }
            }
        });
        service.execute(t); 
    }
    
}
