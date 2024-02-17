package com.yxzx.service.impl;


import cn.hutool.core.date.DateUtil;
import com.yxzx.exception.yxzxException;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.properties.MinioProperties;
import com.yxzx.service.FileUploadService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;


/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 15:48
 */

@Service
public class FileUploadServiceImple implements FileUploadService {

    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) {
        try {
            // 创建一个Minio的客户端对象
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioProperties.getEndpointUrl())
                    .credentials(minioProperties.getAccessKey(),
                            minioProperties.getSecreKey())
                    .build();

            // 判断桶是否存在
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
            if (!found) {       // 如果不存在，那么此时就创建一个新的桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
            } else {  // 如果存在打印信息
                System.out.println("Bucket 'yxzx-bucket' already exists.");
            }

            //获取上传文件名称
            //1 每个上传文件名称唯一 UUID

            //2 根据当前日期对上传文件进行分组
            String dateDir = DateUtil.format(new Date(), "yyyyMMdd");
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = dateDir + "/" + uuid + file.getOriginalFilename();

            //文件上传
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucketName())
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
            //获取上传文件在minio路径
            String url = minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw new yxzxException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
}
