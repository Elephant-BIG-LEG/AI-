package com.elephant.ai.mapper;

import com.elephant.ai.domain.ChatRecords;
import com.elephant.ai.domain.InterviewRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
public interface InterviewRecordsMapper extends BaseMapper<InterviewRecords> {

    List<InterviewRecords> getChatRecordsByPage(@Param("subject") String subject, @Param("openId") String openId, @Param("offset") int offset, @Param("limit") int limit);
}

