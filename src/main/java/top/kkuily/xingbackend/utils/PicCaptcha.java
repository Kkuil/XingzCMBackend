package top.kkuily.xingbackend.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author 小K
 * @description 生成图像验证码
 */
public class PicCaptcha {

    public static Map<String, String> generate() throws IOException {

        // 1. 生成UUID
        String uuid = UUID.randomUUID().toString();
        Map<String, String> capMap = new HashMap<>();
        // 2. 创建aptcha对象
        DefaultKaptcha kaptcha = new DefaultKaptcha();
        // 3. 设置验证码属性
        kaptcha.setConfig(new Config(new Properties()));
        // 3. 生成验证码文本和图片
        String code = kaptcha.createText();
        BufferedImage image = kaptcha.createImage(code);
        // 4. 将验证码图片转换为Base64编码的字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
        capMap.put("image", base64Image);
        capMap.put("code", code);
        capMap.put("uuid", uuid);
        return capMap;
    }
}
