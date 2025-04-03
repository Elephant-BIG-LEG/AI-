package com.elephant.web.controller.ai_interview;


import com.elephant.ai.domain.*;
import com.elephant.ai.service.*;
import com.elephant.common.annotation.RateLimiter;
import com.elephant.common.constant.AiConstants;
import com.elephant.common.core.controller.BaseController;
import com.elephant.common.core.domain.AjaxResult;
import com.elephant.common.enums.LimitType;
import com.elephant.common.utils.StringUtils;
import com.elephant.system.service.IAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/01/19/10:54
 * @Description: 专门处理小程序的相关请求
 */
@RestController
@RequestMapping("/mp")
public class MpController extends BaseController {
    @Autowired
    IWxLoginService iWxLoginService;

    @Autowired
    IBannerService bannerService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    ICategoryItemService categoryItemService;

    @Autowired
    IAiService iAiService;

    @Autowired
    IModelsService iModelsService;

    @Autowired
    ICategoryItemService iCategoryItemService;

    @Autowired
    IInterviewRecordsService iInterviewRecordsService;

    @GetMapping("/banners")
    public List<Banner> getAllBanner(){
        return bannerService.getAllBanner();
    }

    @GetMapping("/categories")
    public List<Category> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestParam("code") String code){
        WxLogin wxLogin = iWxLoginService.login(code);
        if(wxLogin.getOpenid() != null && wxLogin.getOpenid().length() > 0){
            return success(wxLogin);
        }else{
            return AjaxResult.error("401","登录失败");
        }
    }

    @PostMapping("/chat")
    @RateLimiter(key = "mp-chat",time = 3,count = 1,limitType = LimitType.IP)
    public MpAnswer chat(@RequestBody MpRequest mpRequest,@RequestHeader("openId") String openId) throws IOException {
        if(Objects.isNull(mpRequest)){
            return new MpAnswer("0","请输入内容");
        }
        if(StringUtils.isEmpty(openId)){
            return new MpAnswer("401","请登录后使用~~");
        }
        return iAiService.chat(mpRequest, openId);
    }

    @GetMapping("/models")
    public List<Models> getAllModels(){
        return iModelsService.getAllModels();
    }

    @GetMapping("/categories/{category}")
    public List<CategoryItem> getCategoryItems(@PathVariable String category){
        return iCategoryItemService.getCategoryItems(category);
    }

    @GetMapping("/records")
    public List<ChatRecords> getChatRecordsByPage(@RequestParam String subject,
                                                  @RequestHeader("openId") String openId,
                                                  @RequestParam(defaultValue = "1") int pageNum){
        //默认查询所有
        return iInterviewRecordsService.getChatRecordsByPage(subject,openId, pageNum);
    }

}
