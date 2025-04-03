package com.elephant.common.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/01/19/11:48
 * @Description: 微信登录配置文件
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "mp")
public class MpProperties {
    private String appid;

    private String secret;

    private String grant_type;

    private String token_url;
}
