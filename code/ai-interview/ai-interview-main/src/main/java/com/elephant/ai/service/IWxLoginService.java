package com.elephant.ai.service;

import com.elephant.ai.domain.WxLogin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
public interface IWxLoginService extends IService<WxLogin> {

    WxLogin login(String code);

}
