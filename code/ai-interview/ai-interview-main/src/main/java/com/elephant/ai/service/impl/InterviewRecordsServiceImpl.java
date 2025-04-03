package com.elephant.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elephant.ai.domain.AiChatMessage;
import com.elephant.ai.domain.ChatRecords;
import com.elephant.ai.domain.InterviewRecords;
import com.elephant.ai.domain.MpRequest;
import com.elephant.ai.mapper.InterviewRecordsMapper;
import com.elephant.ai.service.IInterviewRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elephant.common.constant.AiConstants;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
@Service
public class InterviewRecordsServiceImpl extends ServiceImpl<InterviewRecordsMapper, InterviewRecords> implements IInterviewRecordsService {

    @Autowired
    InterviewRecordsMapper interviewRecordsMapper;
    @Override
    public List<AiChatMessage> loadPreChatMessageFromDB(String sessionId) {
        LambdaQueryWrapper<InterviewRecords> qw = new LambdaQueryWrapper<>();
        qw.eq(InterviewRecords::getDelFlag,1);
        qw.eq(InterviewRecords::getSessionId,sessionId);
        List<InterviewRecords> list = list(qw);
        return list.stream().map(ir -> {
            AiChatMessage aiChatMessage = new AiChatMessage();
            aiChatMessage.setContent(ir.getContent());
            aiChatMessage.setRole(ir.getRole());
            return aiChatMessage;
        }).collect(Collectors.toList());
    }

    /**
     * 将对话信息保存到数据库中
     * @param mpRequest
     * @param aiChatMessages
     * @param openId
     */
    @Override
    public boolean saveMessages(MpRequest mpRequest, List<AiChatMessage> aiChatMessages, String openId) {
        List<InterviewRecords> records = aiChatMessages.stream().map(m -> {
            InterviewRecords ir = new InterviewRecords();
            ir.setContent(m.getContent());
            ir.setSessionId(mpRequest.getSessionId());
            ir.setRole(m.getRole());
            ir.setOpenid(openId);
            ir.setCategory(mpRequest.getContent());
            ir.setSubject(mpRequest.getSubject());
            return ir;
        }).collect(Collectors.toList());
        return saveBatch(records);
    }


    @Override
    public List<ChatRecords> getChatRecordsByPage(String subject, String openId, int pageNum) {
        List<InterviewRecords> list = interviewRecordsMapper.getChatRecordsByPage(subject,openId,
                (pageNum - 1) * AiConstants.PAGE_SIZE, AiConstants.PAGE_SIZE);
        ArrayList<ChatRecords> chatRecords = new ArrayList<>();
        String lastSessionId = null;
        ChatRecords cr = null;
        for(InterviewRecords ir : list){
            if(ir.getSessionId().equals(lastSessionId)){
                //这次跟上次的会话是一致的，说明是答案
                cr.setAnswer(ir);
            }else{
                //这次是题目
                cr = new ChatRecords();
                cr.setQuestion(ir);
                chatRecords.add(cr);
            }
            lastSessionId = ir.getSessionId();
        }
        return chatRecords;
    }
}
