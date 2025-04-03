package com.elephant.common.config;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/15/23:04
 * @Description: 百度 AI 大模型 配置类 --ERNIE-Speed-128K
 */
@ConfigurationProperties(prefix = "ai")
@Setter
@Getter
public class AiProperties {
    private String grant_type;

    private String client_id;

    private String client_secret;

    private String accessTokenUrl;

}
