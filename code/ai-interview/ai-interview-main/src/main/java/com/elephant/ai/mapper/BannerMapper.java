package com.elephant.ai.mapper;

import com.elephant.ai.domain.Banner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
public interface BannerMapper extends BaseMapper<Banner> {

    List<Banner> selectBannerList(Banner banner);
}

