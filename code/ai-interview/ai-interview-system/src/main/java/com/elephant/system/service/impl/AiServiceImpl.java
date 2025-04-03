package com.elephant.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elephant.ai.domain.*;
import com.elephant.ai.service.IInterviewRecordsService;
import com.elephant.ai.service.IModelsService;
import com.elephant.common.config.AiProperties;
import com.elephant.common.constant.AiConstants;
import com.elephant.common.core.redis.RedisCache;
import com.elephant.system.service.IAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import okhttp3.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/14/22:32
 * @Description: TODO
 */
@Service
@EnableConfigurationProperties(AiProperties.class)
public class AiServiceImpl implements IAiService {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Autowired
    RedisCache redisCache;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AiProperties  aiProperties;

    @Autowired
    IModelsService iModelsService;

    @Autowired
    IInterviewRecordsService iInterviewRecordsService;

    @Autowired
    RedisTemplate redisTemplate;


    /**
     * 服务端是支持多轮对话的，所以需要一个 openId 来标识一次对话
     * @param mpRequest
     * @param openId
     * @return
     */
//    @Override
//    public MpAnswer chat(MpRequest mpRequest, String openId) throws IOException{
//        //1.先获取 access_token
//        String accessToken = getAccessToken();
//        //通过 access_token 和 sessionId 发送请求
//        //2.根据小程序模型传递的模型名称，去查询这个模型的信息
//        Models models = getModels(mpRequest.getModelName());
//        //3.读取当前会话之前的聊天记录 -- 之前的聊天记录是会保存在数据库中
//        //为了安全，添加 sessionId 来保证聊天记录的安全
//        List<AiChatMessage> messages = loadPreChatMessageFromRedis(mpRequest.getSessionId());
//        //4.构建请求参数
//        AiChatMessage message = new AiChatMessage();
//        message.setRole(AiConstants.MESSAGE_ROLE_USER);
//        if(mpRequest.getType().equals(AiConstants.MP_REQUEST_TYPE_Q)){
//            message.setContent(String.format(models.getQuestionPrompt(),mpRequest.getContent()));
//        } else if (mpRequest.getType().equals(AiConstants.MP_REQUEST_TYPE_A)) {
//            message.setContent(String.format(models.getQuestionPrompt(),mpRequest.getContent()));
//        }else{
//            throw new RuntimeException("请求类型错误，只能是 提问 或者 回答~~");
//        }
//        messages.add(message);
//        AiRequestBodyERNIE_Speed_128K aiRequestBodyERNIESpeed128K = new AiRequestBodyERNIE_Speed_128K();
//        aiRequestBodyERNIESpeed128K.setMessages(messages);
//        aiRequestBodyERNIESpeed128K.setSystem(models.getRole());
//        //TODO 为什么要这个 OpenId
//        aiRequestBodyERNIESpeed128K.setUser_id(openId);
//        //将 url 进行拼接
//        String url = models.getUrl() + "?access_token=" + accessToken;
//
//        // 设置请求头
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // 创建HttpEntity，包含请求体和请求头
//        HttpEntity<AiRequestBodyERNIE_Speed_128K> requestEntity =
//            new HttpEntity<>(aiRequestBodyERNIESpeed128K, headers);
//
//        System.out.println("Request URL: " + url);
//        System.out.println("Request Body: " + aiRequestBodyERNIESpeed128K);
//
//        // 使用exchange方法替代postForObject
//        ResponseEntity<AiResponseERNIE_Speed_128K> responseEntity = restTemplate.exchange(
//            url,
//            HttpMethod.POST,
//            requestEntity,
//            AiResponseERNIE_Speed_128K.class
//        );
//
//        AiResponseERNIE_Speed_128K responseBody = responseEntity.getBody();
//
//        // 打印响应信息用于调试
//        System.out.println("Response Status: " + responseEntity.getStatusCode());
//        System.out.println("Response Body: " + responseBody);
//
//        AiChatMessage aiChatMessage = new AiChatMessage();
//        aiChatMessage.setRole(AiConstants.MESSAGE_ROLE_ASSISTANT);
//        aiChatMessage.setContent(responseBody.getResult());
//        messages.add(aiChatMessage);
//        cacheMessage(messages,mpRequest,openId);
//        //6.向前端响应
//        MpAnswer mpAnswer = new MpAnswer();
//        mpAnswer.setType(AiConstants.MP_QUESTION_TYPE_CHOICE);
//        mpAnswer.setContent(responseBody.getResult());
//        return mpAnswer;
//    }

    //TODO 缓存历史问题 数据库保存错误
    @Override
    public MpAnswer chat(MpRequest mpRequest, String openId) throws IOException {
        String accessToken = getAccessToken();
        Models models = getModels(mpRequest.getModelName());
        List<AiChatMessage> messages = loadPreChatMessageFromRedis(mpRequest.getSessionId());

        if (mpRequest.getType().equals(AiConstants.MP_REQUEST_TYPE_Q)) {
            // 处理获取问题的请求
            AiChatMessage message = new AiChatMessage();
            message.setRole(AiConstants.MESSAGE_ROLE_USER);
            message.setContent(String.format(models.getQuestionPrompt(), mpRequest.getContent()));
            messages.add(message);

            // 调用AI获取问题
            String question = callAiApi(accessToken, messages, models, openId);

            //保存到 Redis
            saveLastQuestionToRedis(mpRequest.getSessionId(),question);
            // 保存AI的回答到会话历史
            AiChatMessage aiResponse = new AiChatMessage();
            aiResponse.setRole(AiConstants.MESSAGE_ROLE_ASSISTANT);
            aiResponse.setContent(question);
            messages.add(aiResponse);

            MpAnswer mpAnswer = new MpAnswer();
            mpAnswer.setType(AiConstants.MP_QUESTION_TYPE_CHOICE);
            mpAnswer.setContent(question);
            return mpAnswer;

        } else if (mpRequest.getType().equals(AiConstants.MP_REQUEST_TYPE_A)) {
            // 从Redis获取最后一个问题
            String lastQuestion = getLastQuestion(mpRequest.getSessionId());
            if (lastQuestion == null) {
                throw new RuntimeException("未找到相关问题，请重新开始");
            }

            // 添加用户的答案，包含问题内容
            AiChatMessage answerMessage = new AiChatMessage();
            answerMessage.setRole(AiConstants.MESSAGE_ROLE_USER);
            answerMessage.setContent(String.format(
                    "问题是：%s\n我的答案是：%s\n这个答案对吗？请解释一下。",
                    lastQuestion,
                    mpRequest.getContent()
            ));
            messages.add(answerMessage);

            // 调用AI获取答案解析
            String analysis = callAiApi(accessToken, messages, models, openId);

            // 保存AI的解析到会话历史
            AiChatMessage aiResponse = new AiChatMessage();
            aiResponse.setRole(AiConstants.MESSAGE_ROLE_ASSISTANT);
            aiResponse.setContent(analysis);
            messages.add(aiResponse);

            //统一将 问题 和 答案进行保存
            cacheMessage(messages,mpRequest,openId);

            MpAnswer mpAnswer = new MpAnswer();
            mpAnswer.setType(AiConstants.MP_ANSWER_TYPE_ANALYSIS);
            mpAnswer.setContent(analysis);
            return mpAnswer;
        }

        throw new RuntimeException("请求类型错误，只能是提问或者回答");
    }

    // Redis相关的辅助方法
    private void saveLastQuestionToRedis(String sessionId, String question) {
        String key = "chat:last_question:" + sessionId;
        redisTemplate.opsForValue().set(key, question, 30, TimeUnit.MINUTES);
    }

    private String getLastQuestion(String sessionId) {
        String key = "chat:last_question:" + sessionId;
        return (String) redisTemplate.opsForValue().get(key);
    }


    // 调用AI API的辅助方法
    private String callAiApi(String accessToken, List<AiChatMessage> messages, Models models, String openId) {
        AiRequestBodyERNIE_Speed_128K requestBody = new AiRequestBodyERNIE_Speed_128K();
        requestBody.setMessages(messages);
        requestBody.setSystem(models.getRole());
        requestBody.setUser_id(openId);

        String url = models.getUrl() + "?access_token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequestBodyERNIE_Speed_128K> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<AiResponseERNIE_Speed_128K> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                AiResponseERNIE_Speed_128K.class
        );

        return responseEntity.getBody().getResult();
    }
    // Redis相关的辅助方法
    private void saveChatAnswerToRedis(List<AiChatMessage> messages, String sessionId) {
        String key = "chat:answer:" + sessionId;
        redisTemplate.opsForValue().set(key, messages, 30, TimeUnit.MINUTES);
    }



    /**
     * 将对话信息保存到 Redis 和 数据库 中
     * @param messages
     */
    private void cacheMessage(List<AiChatMessage> messages,MpRequest mpRequest,String openId) {
        redisCache.setCacheList(AiConstants.AI_CHAT_CONTENT + mpRequest.getSessionId(),messages);
        //保存到数据库中 只需要最后两条
        iInterviewRecordsService.saveMessages(mpRequest,messages.subList(messages.size() - 2,messages.size()),openId);
    }

    /**
     * 从数据库中获取会话之前的聊天记录
     * @param sessionId
     * @return
     */
    private List<AiChatMessage> loadPreChatMessageFromRedis(String sessionId) {
        List<AiChatMessage> cacheList = redisCache.getCacheList(AiConstants.AI_CHAT_CONTENT + sessionId);
        if(cacheList == null || cacheList.size() == 0){
            cacheList = iInterviewRecordsService.loadPreChatMessageFromDB(sessionId);
            if (cacheList == null){
                cacheList = new ArrayList<>();
            }
        }
        return cacheList;
    }


    /**
     * 获取模型信息
     * @param modelName
     * @return
     */
    private Models getModels(String modelName){
        if(modelName == null || "".equals(modelName)){
            modelName = AiConstants.DEFAULT_AI_MODEL;
        }
        //先从 Redis 中获取模型的数据
        //缓存预热
        Models models = redisCache.getCacheMapValue(AiConstants.AI_MODEL_NAMES, modelName);
        //缓存没有，去数据库查找
        if(modelName == null){
            //从数据库中获取模型列表，存入缓存
            LambdaQueryWrapper<Models> qw = new LambdaQueryWrapper<>();
            qw.eq(Models::getDelFlag,1);
            qw.eq(Models::getName,models);
            models = iModelsService.getOne(qw);
            if(models == null){
                throw new RuntimeException("模型不存在");
            }
            redisCache.setCacheMapValue(AiConstants.AI_MODEL_NAMES,modelName,models);
        }
        return models;
    }

    /**
     * 通过API Key和Secret Key。获取 access_token
     * @return
     */
    private String getAccessToken() throws IOException{
        //从 Redis 中获取
        String accessToken = redisCache.getCacheObject(AiConstants.AI_ERNIE_Speed_ACCESS_TOKEN_KEY);
        if(StringUtils.isEmpty(accessToken) && accessToken != null){
            return accessToken;
        }
//        //向平台索要 access_token
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=&client_secret=&grant_type=client_credentials")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        return response.body().string();
        LinkedMultiValueMap params = new LinkedMultiValueMap();
        params.add("grant_type",aiProperties.getGrant_type());
        params.add("client_id",aiProperties.getClient_id());
        params.add("client_secret",aiProperties.getClient_secret());
        System.out.println(aiProperties);
        Map<String,Object> map =
                restTemplate.postForObject(aiProperties.getAccessTokenUrl(), params, Map.class);
        //响应结果有访问凭证【access_token】
        accessToken = (String) map.get("access_token");
        //保存在 Redis 中
        redisCache.setCacheObject(AiConstants.AI_ERNIE_Speed_ACCESS_TOKEN_KEY,accessToken,3,
                TimeUnit.DAYS);
        return accessToken;
    }
}
