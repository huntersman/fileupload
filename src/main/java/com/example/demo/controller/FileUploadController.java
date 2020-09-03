package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Map<String, Object> fileupload(MultipartFile file, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.endsWith(".pdf")) {
            result.put("status", "error");
            result.put("messgae", "文件类型不正确");
            return result;
        }
        String realPath = request.getServletContext().getRealPath("/");
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String newName = UUID.randomUUID() + ".pdf";
        try {
            file.transferTo(new File(folder, newName));
            String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/"+ newName;
            result.put("status", "success");
            result.put("message", "上传成功");
            result.put("url", url);
        } catch (IOException e) {
            result.put("status", "error");
            result.put("messgae", e.getMessage());
        }
        return result;
    }
}
