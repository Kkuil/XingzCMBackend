package top.kkuily.xingbackend.config;

import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.model.DevChatRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 小K
 * @description 鱼聪明ChatGPT
 */

@Configuration
public class YuChatConfig {

    @Value("${yuapi.client.access-key}")
    private String accessKey;

    @Value("${yuapi.client.secret-key}")
    private String secretKey;

    @Bean
    public YuCongMingClient yuCongMingClient() {
        return new YuCongMingClient(accessKey, secretKey);
    }

    @Bean
    public DevChatRequest devChatRequest() {
        DevChatRequest devChatRequest = new DevChatRequest();
        return devChatRequest;
    }
}
