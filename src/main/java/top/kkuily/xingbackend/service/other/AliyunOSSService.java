package top.kkuily.xingbackend.service.other;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 小K
 * @description 阿里云OSS对象
 */
@Service
@Slf4j
public class AliyunOSSService {

    /**
     * 终端地址
     */
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    /**
     * 密钥ID
     */
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    /**
     * 密钥
     */
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 桶名
     */
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * @param path
     * @param file
     * @return String
     * @description 阿里云OSS SDK
     */
    public String upload(String path, MultipartFile file, Long maxFileSize) {

        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 判空
            if (file == null) {
                throw new IllegalArgumentException("至少上传一个文件");
            }
            // 判断文件大小
            double size = file.getSize() / (1024.0 * 1024.0);
            if (size > maxFileSize) {
                throw new IllegalArgumentException("图片大小不能超过" + maxFileSize);
            }
            // 上传文件到aliyunoss
            File fileObj = multipartFileToFile(file);
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, path, fileObj);

            // 设置该属性可以返回response。如果不设置，则返回的response为空。
            putObjectRequest.setProcess("true");

            // 上传文件
            PutObjectResult result = client.putObject(putObjectRequest);

            if (result == null) {
                return "";
            }

            // 拼接图片地址
            String url = "https://" + bucketName + "." + endpoint.split("//")[1] + "/" + path;

            log.info("url: {}", url);

            return url;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, " + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered " + "a serious internal problem while trying to communicate with OSS, " + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // 释放资源
            client.shutdown();
        }
        return "";
    }

    public static File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        InputStream inputStream = multipartFile.getInputStream();
        File tempFile = File.createTempFile("temp", null);
        FileOutputStream fos = new FileOutputStream(tempFile);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.flush();
        fos.close();
        File ossFile = new File(tempFile.getPath());
        return ossFile;
    }
}
