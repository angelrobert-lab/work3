package com.controller;

import com.annotation.IgnoreAuth;
import com.common.enums.BusinessCodeEnum;
import com.domain.po.ConfigPo;
import com.service.IConfigService;
import com.utils.FileUtil;
import com.utils.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 文件上传下载控制器（增强版）
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private IConfigService configService;

    // 允许的图片格式
    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = new HashSet<String>() {{
        add("jpg");add("jpeg");add("png");
        add("gif");add("webp");
    }};

    // 日期格式化（用于分类存储）
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    @Operation(summary = "通用文件上传接口")
    @PostMapping("/upload")
    public ResponseResult<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String type) throws Exception {
        if (file.isEmpty()) {
            return ResponseResult.fail(BusinessCodeEnum.UPLOAD_FILE_EMPTY);
        }

        // 获取文件扩展名
        String originalFilename = file.getOriginalFilename();
        int extIndex = originalFilename.lastIndexOf(".");
        if (extIndex == -1) {
            return ResponseResult.fail(BusinessCodeEnum.FILE_FORMAT_ERROR);
        }
        String fileExt = originalFilename.substring(extIndex + 1);
        // 构建上传路径
        File path = new File(ResourceUtils.getURL("classpath:static").getPath());
        if (!path.exists()) {
            path = new File("");
        }
        File uploadDir = new File(path.getAbsolutePath(), "/upload/");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String fileName = new Date().getTime() + "." + fileExt;
        File destFile = new File(uploadDir.getAbsolutePath() + "/" + fileName);
        file.transferTo(destFile);

        // 如果是人脸文件类型，更新配置
        if (StringUtils.isNotBlank(type) && "1".equals(type)) {
            ConfigPo configPo = configService.lambdaQuery()
                    .eq(ConfigPo::getName, "faceFile")
                    .one();

            if (configPo == null) {
                configPo = new ConfigPo();
                configPo.setName("faceFile");
                configPo.setValue(fileName);
            } else {
                configPo.setValue(fileName);
            }
            configService.saveOrUpdate(configPo);
        }

        return ResponseResult.success(BusinessCodeEnum.UPLOAD_SUCCESS, fileName);
    }

    @Operation(summary = "图片专用上传接口")
    @PostMapping("/upload/image")
    public ResponseResult<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "common") String type) throws Exception {

        // 1. 基础校验
        if (file.isEmpty()) {
            return ResponseResult.fail(BusinessCodeEnum.UPLOAD_FILE_EMPTY);
        }

        // 2. 文件名与格式校验
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            return ResponseResult.fail(BusinessCodeEnum.FILE_FORMAT_ERROR);
        }
        String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(fileExt)) {
            return ResponseResult.fail(BusinessCodeEnum.FILE_FORMAT_ERROR);
        }

        // 3. 构建存储路径（按类型和日期分类）
        File path = new File(ResourceUtils.getURL("classpath:static").getPath());
        if (!path.exists()) {
            path = new File("");
        }
        // 格式：upload/type/yyyyMMdd/
        String dateDir = DATE_FORMAT.format(new Date());
        File uploadDir = new File(path.getAbsolutePath() + "/upload/" + type + "/" + dateDir);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 自动创建多级目录
        }

        // 4. 生成唯一文件名（时间戳+随机数+扩展名）
        String fileName = System.currentTimeMillis()
                + "_" + (int)(Math.random() * 1000)
                + "." + fileExt;
        File destFile = new File(uploadDir.getAbsolutePath() + "/" + fileName);

        // 5. 保存文件
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.fail(BusinessCodeEnum.FILE_UPLOAD_FAILED);
        }

        // 6. 返回完整文件名（包含路径信息，用于后续下载）
        String fullFileName = type + "/" + dateDir + "/" + fileName;
        return ResponseResult.success(BusinessCodeEnum.UPLOAD_SUCCESS, fullFileName);
    }
    @Operation(summary = "下载文件")
    @IgnoreAuth
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName) {
        try {
            // 构建完整文件路径
            File path = new File(ResourceUtils.getURL("classpath:static").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            File file = new File(path.getAbsolutePath() + "/upload/" + fileName);

            if (file.exists() && file.isFile()) {
                HttpHeaders headers = new HttpHeaders();
                // 处理中文文件名乱码
                String encodedFileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
                headers.setContentDispositionFormData("attachment", encodedFileName);
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}