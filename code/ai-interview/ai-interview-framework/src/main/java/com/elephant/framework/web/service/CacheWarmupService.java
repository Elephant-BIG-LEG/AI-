package com.elephant.framework.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.elephant.ai.domain.Models;
import com.elephant.ai.service.IModelsService;
import com.elephant.common.constant.AiConstants;
import com.elephant.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/16/22:07
 * @Description: 缓存预热
 */
@Component
public class CacheWarmupService implements ApplicationRunner {

    @Autowired
    RedisCache redisCache;
    @Autowired
    IModelsService iModelsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //从数据库中获取模型列表，存入缓存
        LambdaQueryWrapper<Models> qw = new LambdaQueryWrapper<>();
        qw.eq(Models::getDelFlag,1);
        List<Models> modelsList = iModelsService.list(qw);
        //借用 ApplicationRunner类 实现启动后执行缓存预热
        for (Models models : modelsList) {
            redisCache.setCacheMapValue(AiConstants.AI_MODEL_NAMES,models.getName(),models);
        }
    }
}
