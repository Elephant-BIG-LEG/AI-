package com.elephant.web.controller.ai_interview;

import com.elephant.common.config.AiInterviewConfig;
import com.elephant.common.core.domain.AjaxResult;
import com.elephant.framework.web.domain.server.Sys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Elephant-FZY
 * @Email: https://github.com/Elephant-BIG-LEG
 * @Date: 2025/02/27/15:52
 * @Description: 上传图片接口
 */
@RestController
@RequestMapping("/file")
public class fileController {

    SimpleDateFormat sdf = new SimpleDateFormat("/yyy/MM/dd");

    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file, HttpServletRequest req) {
        String format = sdf.format(new Date());
        String path = AiInterviewConfig.getProfile() + format;
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdirs();
        }
        //原始名称
        String oldName = file.getOriginalFilename();
        //生成新名称
        String newName= System.currentTimeMillis() + oldName.substring(oldName.lastIndexOf("."));
        try {
            file.transferTo(new File(folder,newName));
            String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/file/view/" + format + newName;
            return AjaxResult.success(url);
        } catch (IOException e) {
            return AjaxResult.error("上传失败");
        }
    }

    @GetMapping("/view/{year}/{month}/{day}/{name}")
    public void viewImage(HttpServletResponse resp,String year,String month,String day,String name) throws IOException{
        String path = AiInterviewConfig.getProfile() + "/" + year + month + day + name;
        File file = new File(path, name);
        if(!file.exists()){
            resp.setStatus(404);
            return;
        }
        byte[] bytes = new byte[(int) file.length()];
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            fileInputStream.read(bytes);
            resp.setContentLength((int) file.length());
            resp.setContentType("image/jpeg");
            resp.getOutputStream().write(bytes);
        }
    }
}
