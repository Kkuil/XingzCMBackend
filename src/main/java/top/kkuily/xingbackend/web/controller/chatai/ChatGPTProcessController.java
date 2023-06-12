package top.kkuily.xingbackend.web.controller.chatai;

import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.kkuily.xingbackend.anotation.UserAuthToken;

/**
 * @author 小K
 * @description ChatGPT接口
 */
@RestController
@RequestMapping("/chatgpt")
public class ChatGPTProcessController {

    @Resource
    private YuCongMingClient client;

    @Resource
    private DevChatRequest devChatRequest;

    @GetMapping("/process")
    @UserAuthToken
    private void sendMsg(String message) {
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
        System.out.println(response);
    }
}
