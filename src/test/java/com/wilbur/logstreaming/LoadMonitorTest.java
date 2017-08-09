package com.wilbur.logstreaming;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wilbur.server.bean.entry.MonitorItemEntry;
import com.wilbur.server.common.cache.RulesCache;
import com.wilbur.server.dao.LoadItemsDao;
import com.wilbur.server.runner.LoadRunner;
import com.wilbur.server.semantic.SemanticParse;

/**
 * LoadMonitorTest 
 * @author Wang Zhao
 * @date 2017年7月7日 下午2:40:27
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(value = {"classpath*:application-*.xml"})
public class LoadMonitorTest {

    @Autowired
    LoadItemsDao loadItemsDao;
    
    @Autowired
    SemanticParse semanticParse;
    
    @Test
    public  void loadItemsTest(){
        List<MonitorItemEntry> monitorItems = loadItemsDao.selectMonitorItems();
        int size = monitorItems.size();
        Assert.assertTrue(size > 0);
    }
    
    @Test
    public void semanticParseTest(){
        Map<String, Integer> map = semanticParse.loadRules();
        System.out.println( RulesCache.COUNT_KEYS);
        System.out.println( RulesCache.COUNT_FILEDS_INFO);
        Assert.assertTrue(RulesCache.COUNT_KEYS.size() > 0);
        Assert.assertTrue(RulesCache.COUNT_FILEDS_INFO.size() > 0);
    }
    
    @Test
    public void semanticParseRunnerTest(){
        LoadRunner loadRunner = new LoadRunner();
        Thread t = new Thread(loadRunner);
        t.start();
        System.out.println( RulesCache.COUNT_KEYS);
        System.out.println( RulesCache.COUNT_FILEDS_INFO);
    }
    
}
