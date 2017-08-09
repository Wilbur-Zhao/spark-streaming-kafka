package com.wilbur.server.runner;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wilbur.server.common.cache.LoadCount;
import com.wilbur.server.semantic.SemanticParse;

/**
 * LoadRunner 
 * @author Wang Zhao
 * @date 2017年7月10日 下午3:30:16
 *
 */
@Component
public class LoadRunner implements Runnable, Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    SemanticParse semanticParse;
    
    private static final Logger logger = LoggerFactory.getLogger(LoadRunner.class);
    
    public LoadRunner(){
    }
    
    @Override
    public void run() {
        logger.info("load rules start");
        //load cache
        semanticParse.loadRules();
        logger.info("load rules end");
        LoadCount.countDownLatch.countDown();
    }
    
}
