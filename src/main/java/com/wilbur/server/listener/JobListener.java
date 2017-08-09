package com.wilbur.server.listener;

import java.text.ParseException;

import org.apache.spark.streaming.scheduler.StreamingListener;
import org.apache.spark.streaming.scheduler.StreamingListenerBatchCompleted;
import org.apache.spark.streaming.scheduler.StreamingListenerBatchStarted;
import org.apache.spark.streaming.scheduler.StreamingListenerBatchSubmitted;
import org.apache.spark.streaming.scheduler.StreamingListenerOutputOperationCompleted;
import org.apache.spark.streaming.scheduler.StreamingListenerOutputOperationStarted;
import org.apache.spark.streaming.scheduler.StreamingListenerReceiverError;
import org.apache.spark.streaming.scheduler.StreamingListenerReceiverStarted;
import org.apache.spark.streaming.scheduler.StreamingListenerReceiverStopped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wilbur.server.dao.SaveLoopTimeDao;
import com.wilbur.server.stream.util.DateUtils;

public class JobListener implements StreamingListener {

    private static final Logger logger = LoggerFactory.getLogger(JobListener.class);
    
    private SaveLoopTimeDao saveLoopTimeDao;
    
    @Override
    public void onBatchCompleted(StreamingListenerBatchCompleted arg0) {
        logger.info("a batch completed !");
        String time = DateUtils.getCurrentDateTimeStr();
        try {
            String seconds = DateUtils.formatStringTimeSeconds(time);
            saveLoopTimeDao.saveLoopTime(seconds);
        } catch (ParseException e) {
            // TODO
        }
    }

    @Override
    public void onBatchStarted(StreamingListenerBatchStarted arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onBatchSubmitted(StreamingListenerBatchSubmitted arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOutputOperationCompleted(
            StreamingListenerOutputOperationCompleted arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onOutputOperationStarted(
            StreamingListenerOutputOperationStarted arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiverError(StreamingListenerReceiverError arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiverStarted(StreamingListenerReceiverStarted arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiverStopped(StreamingListenerReceiverStopped arg0) {
        // TODO Auto-generated method stub

    }

    public void setSaveLoopTimeDao(SaveLoopTimeDao saveLoopTimeDao) {
        this.saveLoopTimeDao = saveLoopTimeDao;
    }
    
}
