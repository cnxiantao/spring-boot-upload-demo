package cn.xiantao.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.util.UUID;

@SpringBootApplication
@RestController
public class UploadApplication {

    @Value("${web.upload-file-path}")
    private String filePath;

    public static void main(String[] args) {
        SpringApplication.run(UploadApplication.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大
        factory.setMaxFileSize("201400KB"); // KB, MB
        // 设置上传数据总大小
        factory.setMaxRequestSize("2014000KB");
        return factory.createMultipartConfig();
    }

    @RequestMapping("/upload")
    public String upload(MultipartFile file) {
        try {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            System.out.println("上传的文件名为：" + fileName);

            // 获取文件的后缀，比如图片的jpeg,png
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            System.out.println("上传文件的后缀名为：" + suffixName);

            // 文件上传后的路径
            String newFileName = UUID.randomUUID() + suffixName;
            System.out.println("转换后的文件名：" + newFileName);

            File rootPath = new File(filePath);
            if (!rootPath.exists()) {
                rootPath.mkdirs();
            }
            File dest = new File(filePath + newFileName);
            file.transferTo(dest);
            return newFileName;
        } catch (Exception ex) {
            return "异常：" + ex.getMessage();
        }
    }

}
