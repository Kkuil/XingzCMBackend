package top.kkuily.xingbackend.utils;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static top.kkuily.xingbackend.constant.commons.Api.PHONE_REG;

/**
 * @author 小K
 * @description 发送短信验证码
 */
public class SmsCaptcha {

    @Value("${aliyun.sms.accessKeyId}")
    private static String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private static String accessKeySecret;

    public static Integer send(String phone) throws ExecutionException, InterruptedException {

        Pattern reg = Pattern.compile(PHONE_REG);
        Matcher matcher = reg.matcher(phone);
        if (!matcher.matches()) {
            return 0;
        }
        System.out.println(accessKeyId);
        // Configure Credentials authentication information, including ak, secret, token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());

        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                )
                .build();

        // 生成随机数
        int sms = RandomUtil.randomInt(1000, 9999);
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("温伟强个人博客")
                .templateCode("SMS_271360644")
                .phoneNumbers(phone)
                .templateParam("{code:" + sms + "}")
                .build();

        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = response.get();
        System.out.println(new Gson().toJson(resp));

        client.close();
        return sms;
    }
}
