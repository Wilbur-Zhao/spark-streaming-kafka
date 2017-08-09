package com.wilbur.server.common.cache;

import java.util.concurrent.CountDownLatch;

/**
 * LoadCount 
 * @author Wang Zhao
 * @date 2017年7月10日 下午4:48:56
 *
 */
public class LoadCount {

    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    
}
