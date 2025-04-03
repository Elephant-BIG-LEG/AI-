package com.elephant.framework.security.filter;

import com.elephant.ai.domain.WxLogin;
import com.elephant.common.constant.CacheConstants;
import com.elephant.common.core.redis.RedisCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/01/20/16:39
 * @Description: 微信小程序认证拦截
 */
@Component
public class MpAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if(requestURI.contains("/mp") && !requestURI.contains("/mp/login")){
            String auToken = request.getHeader("mp_token");
            WxLogin cacheObject = redisCache.getCacheObject(CacheConstants.WX_LOGIN_PREFIX + auToken);
            if(ObjectUtils.isEmpty(cacheObject)){
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write("{\"code\":401,\"msg\":\"登录已过期请重新登录\"}");
            }else {
                filterChain.doFilter(request,response);
            }
        }else{
            filterChain.doFilter(request,response);
        }
    }


}
