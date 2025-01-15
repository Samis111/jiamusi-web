package com.web.controller.img;


import com.web.domain.MultimediaCourseware;
import com.web.domain.common.Result;
import com.web.domain.common.ResultCodeEnum;
import com.web.service.MultimediaCoursewareService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 上传文件映射表
 */
@RestController
@RequestMapping("file")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FileController {
    //	file/upload

//    /file/Adminupload

    @Autowired
    private MultimediaCoursewareService coursewareService;

    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping("/Adminupload")
    public Result Adminupload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return Result.build(null, ResultCodeEnum.FILE_TRANSFER);
        }

        String userDir = System.getProperty("user.dir");

        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String type = file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf('.'));
        String value = "\\src\\main\\resources\\image\\";
        String valueClass = "\\target\\classes\\image\\";

        InputStream in = file.getInputStream();
        FileOutputStream out = new FileOutputStream(userDir + value + name + type);
        for (int c = 0; (c = in.read()) != -1; ) {
            out.write(c);
        }

        FileOutputStream out2 = new FileOutputStream(userDir + valueClass + name + type);
        InputStream in2 = file.getInputStream();

        try {
            for (int c = 0; (c = in2.read()) != -1; ) {
                out2.write(c);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String str = "http://localhost:8082/" + "image\\" + name + type;
        return Result.ok(str);
    }


    /**
     * 上传文件
     *
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return Result.build(null, ResultCodeEnum.FILE_TRANSFER);
        }

        String userDir = System.getProperty("user.dir");

        String name = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String type = file.getOriginalFilename().
                substring(file.getOriginalFilename().lastIndexOf('.'));
        String value = "\\src\\main\\resources\\image\\";
        String valueClass = "\\target\\classes\\image\\";

        InputStream in = file.getInputStream();
        FileOutputStream out = new FileOutputStream(userDir + value + name + type);
        for (int c = 0; (c = in.read()) != -1; ) {
            out.write(c);
        }

        FileOutputStream out2 = new FileOutputStream(userDir + valueClass + name + type);
        InputStream in2 = file.getInputStream();

        try {
            for (int c = 0; (c = in2.read()) != -1; ) {
                out2.write(c);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String str = "http://localhost:8082/" + "image\\" + name + type;
        return Result.ok(str);
    }


    @RequestMapping(value = "multimedia/{id}", method = RequestMethod.GET)
    public void getDownload(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {

        MultimediaCourseware byId = coursewareService.getById(id);

        String image = byId.getCoursewarePath().split("image")[1];

        String userDir = System.getProperty("user.dir");
        String value = "\\src\\main\\resources\\image";
        String fullPath = userDir + value + image;
        File downloadFile = new File(fullPath);
        ServletContext context = request.getServletContext();
        String mimeType = context.getMimeType(fullPath);

        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setContentLength((int) downloadFile.length());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadFile.getName()));

        try (InputStream myStream = new FileInputStream(fullPath)) {
            IOUtils.copy(myStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
