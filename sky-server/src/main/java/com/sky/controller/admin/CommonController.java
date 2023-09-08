package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("upload")
    public Result<String> upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        assert filename != null;

        String suffix = filename.substring(filename.lastIndexOf('.'));
        String uuid = UUID.randomUUID().toString();
        try {
            String uploadPath = aliOssUtil.upload(file.getBytes(), uuid + suffix);
            return Result.success(uploadPath);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }

}
