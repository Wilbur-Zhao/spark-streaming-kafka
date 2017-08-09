package com.wilbur.server.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.wilbur.server.bean.entry.LoopTimeEntry;

/**
 * LogStreamJdbcTemplate 
 * @author Wang Zhao
 * @date 2017年7月6日 下午5:26:10
 *
 */
@Repository
public class SaveLoopTimeDao {

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    private static final String INSERT_TIME_SQL = "insert into loop_time (loop_time, enable) values (?, ?)";
    
    private static final String GET_TIME_SQL = "select id,loop_time as loopTime,enable from loop_time limit 1";
    
    private static final String UPDATE_TIME_SQL = "update loop_time set loop_time=? where id=?";
    
    /**
     * 加载监控图形元数据
     * @return
     */
    public void saveLoopTime(String time){
        LoopTimeEntry loopTimeEntry = null;
        try {
            loopTimeEntry =  jdbcTemplate.queryForObject(GET_TIME_SQL, new BeanPropertyRowMapper<LoopTimeEntry>(LoopTimeEntry.class));            
        } catch (Exception e) {
            loopTimeEntry = null;
        }
        if(loopTimeEntry == null){            
            jdbcTemplate.update(INSERT_TIME_SQL, new Object[] {time, 1});
            return;
        }
        jdbcTemplate.update(UPDATE_TIME_SQL, new Object[] {time, loopTimeEntry.getId()});
    }
    
}
