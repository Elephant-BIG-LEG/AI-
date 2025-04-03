package com.elephant.ai.service.impl;

import com.elephant.ai.domain.WxLogin;
import com.elephant.ai.mapper.WxLoginMapper;
import com.elephant.ai.service.IWxLoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.elephant.common.config.MpProperties;
import com.elephant.common.constant.CacheConstants;
import com.elephant.common.core.redis.RedisCache;
import com.elephant.common.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
@Service
@EnableConfigurationProperties(MpProperties.class)
public class WxLoginServiceImpl extends ServiceImpl<WxLoginMapper, WxLogin> implements IWxLoginService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MpProperties mpProperties;

    private static final Logger logger = LoggerFactory.getLogger(WxLoginServiceImpl.class);

    @Autowired
    RedisCache redisCache;

    public WxLogin login(String code){
        HashMap<String, Object> params = new HashMap<>();
        params.put("js_code", code);
        params.put("appid",mpProperties.getAppid());
        params.put("secret",mpProperties.getSecret());
        WxLogin wxLogin = restTemplate.getForObject(mpProperties.getToken_url(), WxLogin.class, params);
        if(ObjectUtils.isEmpty(wxLogin.getErrmsg()) && StringUtils.isNotEmpty(wxLogin.getOpenid())){
            //成功之后，将OpenId存入数据库和Redis
            save(wxLogin);
            String MP_TOKEN = String.valueOf(UUID.randomUUID());
            wxLogin.setSessionKey(MP_TOKEN);
            // 保存到 Redis
            save2Redis(wxLogin);

        }
        return wxLogin;
    }

    /**
     * 将 OpenId 保存 Redis
     * 六十分钟过期
     * @param wxLogin
     */
    private void save2Redis(WxLogin wxLogin) {
        //TODO
        System.out.println(CacheConstants.WX_LOGIN_PREFIX + wxLogin.getSessionKey());
        redisCache.setCacheObject(CacheConstants.WX_LOGIN_PREFIX + wxLogin.getSessionKey(),wxLogin,
                60, TimeUnit.MINUTES);
    }
}
