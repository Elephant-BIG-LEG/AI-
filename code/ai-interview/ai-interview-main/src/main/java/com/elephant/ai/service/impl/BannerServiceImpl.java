package com.elephant.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.elephant.ai.domain.Banner;
import com.elephant.ai.mapper.BannerMapper;
import com.elephant.ai.service.IBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Elephant
 * @since 2025-01-19
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Autowired
    BannerMapper bannerMapper;

    public List<Banner> getAllBanner(){
        LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Banner::getDelFlag,1);
        queryWrapper.orderByAsc(Banner::getOrderNum);
        queryWrapper.select(Banner::getTitle,Banner::getImage,Banner::getUrl);
        return list(queryWrapper);
    }

    @Override
    public List<Banner> selectBanner(Banner banner) {
        return bannerMapper.selectBannerList(banner);
    }

    /**
     * 校验 title 是否重名
     * @param banner
     * @return
     */
    @Override
    public boolean checkTitleExists(Banner banner) {
        LambdaQueryWrapper<Banner> bqw = new LambdaQueryWrapper<>();
        bqw.eq(Banner::getTitle,banner.getTitle());
        bqw.eq(Banner::getDelFlag,1);
        //进行更新状态
        if (null != banner.getId()){
            bqw.ne(Banner::getId,banner.getId());
        }
        return exists(bqw);
    }

    @Override
    public boolean deleteBanner(Long[] bannerIds) {
        LambdaUpdateWrapper<Banner> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.in(Banner::getId,bannerIds);
        queryWrapper.set(Banner::getDelFlag,0);
        return update(queryWrapper);
    }

}
