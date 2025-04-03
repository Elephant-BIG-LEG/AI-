package com.elephant.ai.service;

import com.elephant.ai.domain.AiChatMessage;
import com.elephant.ai.domain.ChatRecords;
import com.elephant.ai.domain.InterviewRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.elephant.ai.domain.MpRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
public interface IInterviewRecordsService extends IService<InterviewRecords> {

    List<AiChatMessage> loadPreChatMessageFromDB(String sessionId);

    boolean saveMessages(MpRequest mpRequest, List<AiChatMessage> aiChatMessages, String openId);

    List<ChatRecords> getChatRecordsByPage(String subject, String openId,int pageNum);
}
