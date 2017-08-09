package com.wilbur.server.hbase;

import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HbaseCounter 
 * @author Wang Zhao
 * @date 2017年7月12日 下午4:10:58
 *
 */
public class HbaseCounter implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HbaseCounter.class);
    
    public Connection connection = null;
    
    public Table table = null;
    
    String tableName = "infra_gift:logstream";
    String familyName = "count_value";
    
    public HbaseCounter(){
        openHbase();
    }
    
    private static class generalHbaseCounter {
        private static HbaseCounter hbaseCounter = new HbaseCounter();
    } 
    
    public static HbaseCounter getInstance(){
        return generalHbaseCounter.hbaseCounter;
    }
    
    /**
     * 初始化hbase
     */
    public void openHbase(){
        try {
            Configuration configuration = HBaseConfiguration.create();
            configuration.addResource("hbase-site-test.xml");
            connection = ConnectionFactory.createConnection(configuration);
            table = connection.getTable(TableName.valueOf(tableName));
        } catch (IOException e) {
            logger.error("save count hbase exception", e);
        }
    }
    
    public void save(String key, String value, String qualifier){
        System.out.println("save start......");
        try {
            Put put = new Put(Bytes.toBytes(key));
            put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), Bytes.toBytes(value));
            table.put(put);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("save error" + e);
        }
        System.out.println("save end......");
    }
    
}
