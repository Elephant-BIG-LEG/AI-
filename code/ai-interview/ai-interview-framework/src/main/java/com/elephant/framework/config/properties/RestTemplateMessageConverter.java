package com.elephant.framework.config.properties;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import java.util.ArrayList;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/01/19/12:43
 * @Description: 消息转换器 -- 微信
 *               使用这个消息转换器要求是响应头必须是 application/json 要手动修改
 */
public class RestTemplateMessageConverter extends MappingJackson2HttpMessageConverter {

    public RestTemplateMessageConverter(){
        ArrayList<MediaType> mediaTypeList = new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        mediaTypeList.add(MediaType.TEXT_PLAIN);
        setSupportedMediaTypes(mediaTypeList);
    }
}
