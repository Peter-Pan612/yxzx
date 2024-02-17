package com.yxzx.controller;

import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 15:47
 */

@RestController
@RequestMapping("/admin/system")
public class FileUploadController {


    @Autowired
    private FileUploadService fileUploadService;

    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) {
        //1 获取上传的文件

        //2调用service的方法上传，返回minio路径
        String url = fileUploadService.upload(file);

        return Result.build(url, ResultCodeEnum.SUCCESS);
    }


}
