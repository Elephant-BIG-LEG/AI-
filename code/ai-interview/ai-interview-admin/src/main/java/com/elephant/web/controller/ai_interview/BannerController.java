package com.elephant.web.controller.ai_interview;

import com.elephant.ai.domain.Banner;
import com.elephant.ai.service.IBannerService;
import com.elephant.common.annotation.Log;
import com.elephant.common.core.controller.BaseController;
import com.elephant.common.core.domain.AjaxResult;
import com.elephant.common.core.page.TableDataInfo;
import com.elephant.common.enums.BusinessType;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Elephant
 * @since 2025-01-18
 */
@RestController
@RequestMapping("/banner")
@Api(tags = "Banner相关接口")
public class BannerController extends BaseController {

    @Autowired
    IBannerService iBannerService;

    /**
     * 查 Banner
     * @param banner
     * @return
     */
    @PreAuthorize("@ss.hasPermi('ai:banner:search')")
    @GetMapping("/list")
    public TableDataInfo list(Banner banner){
        //会去获取 PageNum 和 PageSize 的参数
        startPage();
        List<Banner> list = iBannerService.selectBanner(banner);
        return getDataTable(list);
    }

    /**
     * 新增 Banner
     */
    @PreAuthorize("@ss.hasPermi('ai:banner:add')")
    @Log(title = "Banner管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Banner banner) {
        if (iBannerService.checkTitleExists(banner)) {
            return error("新增Banner'" + banner.getTitle() + "'失败，Title名称已存在");
        }
        return toAjax(iBannerService.save(banner));
    }

    /**
     * 删除 Banner
     */
    @PreAuthorize("@ss.hasPermi('ai:banner:remove')")
    @Log(title = "Banner管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{bannerIds}")
    public AjaxResult remove(@PathVariable Long[] bannerIds) {
        return toAjax(iBannerService.deleteBanner(bannerIds));
    }

    /**
     * 修改保存 Banner
     */
    @PreAuthorize("@ss.hasPermi('ai:banner:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Banner banner) {
        if (iBannerService.checkTitleExists(banner)){
            return new AjaxResult(1003,"该名称已被使用过");
        }
        return toAjax(iBannerService.updateById(banner));
    }
}
