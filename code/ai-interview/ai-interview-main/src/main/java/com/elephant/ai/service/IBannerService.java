package com.elephant.ai.service;

import com.elephant.ai.domain.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
public interface IBannerService extends IService<Banner> {

    /**
     * 查询所有 banner，按照排序
     */
    public List<Banner> getAllBanner();

    List<Banner> selectBanner(Banner banner);

    boolean checkTitleExists(Banner banner);

    boolean deleteBanner(Long[] bannerIds);
}
